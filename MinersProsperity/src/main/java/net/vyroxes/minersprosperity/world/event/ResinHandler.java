package net.vyroxes.minersprosperity.world.event;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.block.BlockResin;
import java.util.*;

@Mod.EventBusSubscriber
public class ResinHandler
{
    private static final int OVERWORLD_DIMENSION = 0;
    private static final int GENERATION_DELAY_TICKS = 2;
    private static final int MIN_TREE_HEIGHT = 4;
    private static final int MIN_LARGE_TREE_HEIGHT = 6;
    private static final int LARGE_TRUNK_VALIDATION_HEIGHT = 4;
    private static final int MAX_TRUNK_SCAN_DOWN = 64;
    private static final int MAX_TRUNK_SCAN_UP = 96;
    private static final int MIN_LARGE_TREE_RESIN = 2;
    private static final int MAX_LARGE_TREE_RESIN = 5;
    private static final int LARGE_TREE_HEIGHT_PER_RESIN = 6;
    private static final int MIN_VERTICAL_DISTANCE = 2;
    private static final int PLACEMENT_ATTEMPTS = 48;

    private static final EnumFacing[] HORIZONTAL_SIDES = {
            EnumFacing.NORTH,
            EnumFacing.SOUTH,
            EnumFacing.WEST,
            EnumFacing.EAST
    };

    private static final Map<World, List<PendingSapling>> PENDING = new WeakHashMap<>();

    @Desugar
    private record PendingSapling(BlockPos pos, long executeAtWorldTime, long seed) {}

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if (event.phase != TickEvent.Phase.END) return;
        if (world.isRemote) return;

        List<PendingSapling> pendingList = PENDING.get(world);

        if (pendingList == null || pendingList.isEmpty()) return;

        long currentTime = world.getTotalWorldTime();
        Iterator<PendingSapling> iterator = pendingList.iterator();

        while (iterator.hasNext())
        {
            PendingSapling pending = iterator.next();

            if (currentTime < pending.executeAtWorldTime()) continue;

            iterator.remove();

            if (!world.isBlockLoaded(pending.pos())) continue;

            Random random = new Random(pending.seed());

            tryGenerateResinAfterTreeGrows(world, pending.pos(), random);
        }

        if (pendingList.isEmpty()) PENDING.remove(world);
    }

    @SubscribeEvent
    public static void onSaplingGrow(SaplingGrowTreeEvent event)
    {
        World world = event.getWorld();

        if (world == null || world.isRemote) return;
        if (world.provider.getDimension() != OVERWORLD_DIMENSION) return;
        if (!ModConfig.WORLD_GENERATION.generateResinOnTreesS) return;

        BlockPos saplingPos = event.getPos();

        if (saplingPos == null) return;

        IBlockState saplingState = world.getBlockState(saplingPos);

        if (saplingState.getBlock() != Blocks.SAPLING) return;

        BlockPlanks.EnumType saplingType = saplingState.getValue(BlockSapling.TYPE);

        if (saplingType != BlockPlanks.EnumType.SPRUCE) return;

        long currentTime = world.getTotalWorldTime();
        long seed = createSeed(world.getSeed(), saplingPos, currentTime);
        List<PendingSapling> pendingList = PENDING.computeIfAbsent(world, key -> new ArrayList<>());

        for (PendingSapling pending : pendingList)
        {
            if (pending.pos().equals(saplingPos)) return;
        }

        pendingList.add(new PendingSapling(saplingPos.toImmutable(), currentTime + GENERATION_DELAY_TICKS, seed));
    }

    private static void tryGenerateResinAfterTreeGrows(World world, BlockPos saplingPos, Random random)
    {
        if (!ModConfig.WORLD_GENERATION.generateResinOnTreesS) return;
        if (!isSpruceLog(world, saplingPos)) return;

        int chancePercent = MathHelper.clamp(ModConfig.WORLD_GENERATION.resinChancePercentS, 0, 100);

        if (chancePercent <= 0) return;
        if (random.nextInt(100) >= chancePercent) return;

        TrunkShape trunk = findTrunkShape(world, saplingPos);

        if (trunk == null || trunk.getHeight() < MIN_TREE_HEIGHT) return;

        int targetResinCount;

        if (trunk.large())
        {
            targetResinCount = MathHelper.clamp(trunk.getHeight() / LARGE_TREE_HEIGHT_PER_RESIN, MIN_LARGE_TREE_RESIN, MAX_LARGE_TREE_RESIN);
        }
        else
        {
            targetResinCount = 1;
        }

        ExistingResin existingResin = findExistingResin(world, trunk);
        int missingResin = targetResinCount - existingResin.count();

        if (missingResin <= 0) return;

        Set<Integer> occupiedYLevels = new HashSet<>(existingResin.occupiedYLevels());

        for (int i = 0; i < missingResin; i++)
        {
            boolean placed = tryPlaceOneResin(world, trunk, random, occupiedYLevels, true);

            if (!placed) placed = tryPlaceOneResin(world, trunk, random, occupiedYLevels, false);
            if (!placed) break;
        }
    }

    private static TrunkShape findTrunkShape(World world, BlockPos startLog)
    {
        if (!isSpruceLog(world, startLog)) return null;

        int baseY = findTrunkBaseY(world, startLog.getX(), startLog.getY(), startLog.getZ());

        for (int offsetX = -1; offsetX <= 0; offsetX++)
        {
            for (int offsetZ = -1; offsetZ <= 0; offsetZ++)
            {
                int startX = startLog.getX() + offsetX;
                int startZ = startLog.getZ() + offsetZ;

                if (!isTwoByTwoSpruceTrunk(world, startX, baseY, startZ)) continue;

                BlockPos[] columns = {
                        new BlockPos(startX, baseY, startZ),
                        new BlockPos(startX + 1, baseY, startZ),
                        new BlockPos(startX, baseY, startZ + 1),
                        new BlockPos(startX + 1, baseY, startZ + 1)
                };

                int commonTopY = Integer.MAX_VALUE;

                for (BlockPos column : columns)
                {
                    int columnTopY = findTrunkTopY(world, column.getX(), baseY, column.getZ());
                    commonTopY = Math.min(commonTopY, columnTopY);
                }

                int height = commonTopY - baseY + 1;

                if (height >= MIN_LARGE_TREE_HEIGHT) return new TrunkShape(columns, baseY, commonTopY, true);
            }
        }

        int topY = findTrunkTopY(world, startLog.getX(), baseY, startLog.getZ());

        return new TrunkShape(new BlockPos[]{new BlockPos(startLog.getX(), baseY, startLog.getZ())}, baseY, topY, false);
    }

    private static boolean isTwoByTwoSpruceTrunk(World world, int startX, int baseY, int startZ)
    {
        int maxWorldY = world.getActualHeight() - 1;

        if (baseY + LARGE_TRUNK_VALIDATION_HEIGHT - 1 > maxWorldY) return false;

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dy = 0; dy < LARGE_TRUNK_VALIDATION_HEIGHT; dy++)
        {
            int y = baseY + dy;

            for (int dx = 0; dx <= 1; dx++)
            {
                for (int dz = 0; dz <= 1; dz++)
                {
                    cursor.setPos(startX + dx, y, startZ + dz);

                    if (!isSpruceLog(world, cursor)) return false;
                }
            }
        }

        return true;
    }

    private static int findTrunkBaseY(World world, int x, int startY, int z)
    {
        int baseY = startY;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int i = 0; i < MAX_TRUNK_SCAN_DOWN && baseY > 1; i++)
        {
            cursor.setPos(x, baseY - 1, z);

            if (!isSpruceLog(world, cursor)) break;

            baseY--;
        }

        return baseY;
    }

    private static int findTrunkTopY(World world, int x, int startY, int z)
    {
        int topY = startY;
        int maxWorldY = world.getActualHeight() - 1;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int i = 0; i < MAX_TRUNK_SCAN_UP && topY < maxWorldY; i++)
        {
            cursor.setPos(x, topY + 1, z);

            if (!isSpruceLog(world, cursor)) break;

            topY++;
        }

        return topY;
    }

    private static ExistingResin findExistingResin(World world, TrunkShape trunk)
    {
        Set<Long> resinPositions = new HashSet<>();
        Set<Integer> occupiedYLevels = new HashSet<>();

        for (BlockPos column : trunk.columns())
        {
            for (int y = trunk.baseY() + 1; y <= trunk.topY(); y++)
            {
                BlockPos logPos = new BlockPos(column.getX(), y, column.getZ());

                if (!isSpruceLog(world, logPos)) continue;

                for (EnumFacing side : HORIZONTAL_SIDES)
                {
                    BlockPos resinPos = logPos.offset(side);

                    if (world.getBlockState(resinPos).getBlock() != ModBlocks.RESIN_BLOCK) continue;

                    if (resinPositions.add(resinPos.toLong())) occupiedYLevels.add(y);
                }
            }
        }

        return new ExistingResin(resinPositions.size(), occupiedYLevels);
    }

    private static boolean tryPlaceOneResin(World world, TrunkShape trunk, Random random, Set<Integer> occupiedYLevels, boolean requireVerticalDistance)
    {
        int height = trunk.getHeight();
        int minY = trunk.baseY() + 1;
        int maxY = Math.min(trunk.topY(), trunk.baseY() + Math.max(1, (int) (height * 0.7D)));

        if (minY > maxY) return false;

        for (int attempt = 0; attempt < PLACEMENT_ATTEMPTS; attempt++)
        {
            int y = minY + random.nextInt(maxY - minY + 1);

            if (requireVerticalDistance && !hasRequiredVerticalDistance(y, occupiedYLevels)) continue;

            BlockPos column = trunk.columns()[random.nextInt(trunk.columns().length)];
            BlockPos logPos = new BlockPos(column.getX(), y, column.getZ());

            if (!isSpruceLog(world, logPos)) continue;

            int firstSide = random.nextInt(HORIZONTAL_SIDES.length);

            for (int sideIndex = 0; sideIndex < HORIZONTAL_SIDES.length; sideIndex++)
            {
                EnumFacing side = HORIZONTAL_SIDES[(firstSide + sideIndex) % HORIZONTAL_SIDES.length];
                BlockPos resinPos = logPos.offset(side);

                if (!world.isAirBlock(resinPos)) continue;

                IBlockState resinState = ModBlocks.RESIN_BLOCK.getDefaultState().withProperty(BlockResin.FACING, side).withProperty(BlockResin.VARIANT, random.nextInt(4));

                if (world.setBlockState(resinPos, resinState, 2))
                {
                    occupiedYLevels.add(y);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasRequiredVerticalDistance(int candidateY, Set<Integer> occupiedYLevels)
    {
        for (Integer occupiedY : occupiedYLevels)
        {
            if (Math.abs(candidateY - occupiedY) < MIN_VERTICAL_DISTANCE) return false;
        }

        return true;
    }

    private static boolean isSpruceLog(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        return state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.SPRUCE;
    }

    private static long createSeed(long worldSeed, BlockPos position, long worldTime)
    {
        long seed = worldSeed ^ position.toLong() ^ worldTime * 0x9E3779B97F4A7C15L;

        seed ^= seed >>> 30;
        seed *= 0xBF58476D1CE4E5B9L;
        seed ^= seed >>> 27;
        seed *= 0x94D049BB133111EBL;
        seed ^= seed >>> 31;

        return seed;
    }

    @Desugar
    private record TrunkShape(BlockPos[] columns, int baseY, int topY, boolean large)
    {
        private int getHeight()
        {
            return topY - baseY + 1;
        }
    }

    @Desugar
    private record ExistingResin(int count, Set<Integer> occupiedYLevels) {}
}