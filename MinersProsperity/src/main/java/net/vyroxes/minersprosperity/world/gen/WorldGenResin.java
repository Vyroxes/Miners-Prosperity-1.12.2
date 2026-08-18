package net.vyroxes.minersprosperity.world.gen;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.block.BlockResin;
import net.vyroxes.minersprosperity.config.ModConfig;
import java.util.*;

public class WorldGenResin implements IWorldGenerator
{
    private static final int OVERWORLD_DIMENSION = 0;
    private static final int MIN_TREE_HEIGHT = 4;
    private static final int MIN_LARGE_TREE_HEIGHT = 6;
    private static final int MAX_LOG_SCAN_DOWN = 64;
    private static final int MAX_TRUNK_SCAN_DOWN = 64;
    private static final int MAX_TRUNK_SCAN_UP = 96;
    private static final int LARGE_TRUNK_VALIDATION_HEIGHT = 4;
    private static final int MIN_LARGE_TREE_RESIN = 2;
    private static final int MAX_LARGE_TREE_RESIN = 5;
    private static final int LARGE_TREE_HEIGHT_PER_RESIN = 6;
    private static final int MIN_RESIN_VERTICAL_DISTANCE = 2;
    private static final int MAX_TRIES_PER_CHUNK = 200;
    private static final EnumFacing[] HORIZONTAL_SIDES = {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() != OVERWORLD_DIMENSION) return;
        if (!ModConfig.WORLD_GENERATION.generateResinOnTreesWG) return;

        int tries = MathHelper.clamp(ModConfig.WORLD_GENERATION.resinTriesPerChunkWG, 0, MAX_TRIES_PER_CHUNK);
        int chancePercent = MathHelper.clamp(ModConfig.WORLD_GENERATION.resinChancePercentWG, 0, 100);

        if (tries <= 0 || chancePercent <= 0) return;

        Chunk chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);

        if (chunk == null) chunk = world.getChunk(chunkX, chunkZ);

        int chunkStartX = chunkX << 4;
        int chunkStartZ = chunkZ << 4;
        int maxWorldY = world.getActualHeight() - 1;

        Set<Long> checkedTrees = new HashSet<>();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int attempt = 0; attempt < tries; attempt++)
        {
            int localX = 1 + rand.nextInt(14);
            int localZ = 1 + rand.nextInt(14);
            int worldX = chunkStartX + localX;
            int worldZ = chunkStartZ + localZ;
            int topY = Math.min(maxWorldY, chunk.getHeightValue(localX, localZ));

            BlockPos anySpruceLog = findAnySpruceLogDown(chunk, worldX, topY, worldZ, cursor);

            if (anySpruceLog == null) continue;

            TrunkShape trunk = findTrunkShape(chunk, anySpruceLog, chunkStartX, chunkStartZ, maxWorldY, cursor);

            if (trunk.getHeight() < MIN_TREE_HEIGHT) continue;
            if (!checkedTrees.add(trunk.getTreeKey())) continue;
            if (rand.nextInt(100) >= chancePercent) continue;
            if (!hasSpruceLeavesNearby(chunk, trunk, chunkStartX, chunkStartZ, maxWorldY, cursor)) continue;

            int targetResinCount;

            if (trunk.large())
            {
                targetResinCount = MathHelper.clamp(trunk.getHeight() / LARGE_TREE_HEIGHT_PER_RESIN, MIN_LARGE_TREE_RESIN, MAX_LARGE_TREE_RESIN);
            }
            else
            {
                targetResinCount = 1;
            }

            ExistingResinData existingResin = findExistingResin(chunk, trunk, chunkStartX, chunkStartZ, cursor);

            if (existingResin.count() >= targetResinCount) continue;

            List<ResinCandidate> candidates = collectResinCandidates(world, chunk, trunk, chunkStartX, chunkStartZ, cursor);

            if (candidates.isEmpty()) continue;

            placeMissingResin(world, candidates, existingResin, targetResinCount, rand);
        }
    }

    private static BlockPos findAnySpruceLogDown(Chunk chunk, int x, int startY, int z, BlockPos.MutableBlockPos cursor)
    {
        int minY = Math.max(2, startY - MAX_LOG_SCAN_DOWN);

        for (int y = startY; y >= minY; y--)
        {
            cursor.setPos(x, y, z);

            if (isSpruceLog(chunk.getBlockState(cursor))) return new BlockPos(x, y, z);
        }

        return null;
    }

    private static TrunkShape findTrunkShape(Chunk chunk, BlockPos anyLog, int chunkStartX, int chunkStartZ, int maxWorldY, BlockPos.MutableBlockPos cursor)
    {
        int baseY = findTrunkBaseY(chunk, anyLog.getX(), anyLog.getY(), anyLog.getZ(), cursor);

        for (int offsetX = -1; offsetX <= 0; offsetX++)
        {
            for (int offsetZ = -1; offsetZ <= 0; offsetZ++)
            {
                int startX = anyLog.getX() + offsetX;
                int startZ = anyLog.getZ() + offsetZ;

                if (!isTwoByTwoSpruceTrunk(chunk, startX, baseY, startZ, chunkStartX, chunkStartZ, maxWorldY, cursor)) continue;

                BlockPos[] columns = {
                        new BlockPos(startX, baseY, startZ),
                        new BlockPos(startX + 1, baseY, startZ),
                        new BlockPos(startX, baseY, startZ + 1),
                        new BlockPos(startX + 1, baseY, startZ + 1)
                };

                int[] columnTopY = new int[columns.length];
                int highestTopY = baseY;
                boolean validLargeTree = true;

                for (int i = 0; i < columns.length; i++)
                {
                    BlockPos column = columns[i];

                    columnTopY[i] = findTrunkTopY(chunk, column.getX(), baseY, column.getZ(), maxWorldY, cursor);

                    int columnHeight = columnTopY[i] - baseY + 1;

                    if (columnHeight < MIN_LARGE_TREE_HEIGHT)
                    {
                        validLargeTree = false;
                        break;
                    }

                    highestTopY = Math.max(highestTopY, columnTopY[i]);
                }

                if (validLargeTree) return new TrunkShape(columns, columnTopY, baseY, highestTopY, true, startX, startZ);
            }
        }

        int topY = findTrunkTopY(chunk, anyLog.getX(), baseY, anyLog.getZ(), maxWorldY, cursor);
        BlockPos[] columns = {new BlockPos(anyLog.getX(), baseY, anyLog.getZ())};
        int[] columnTopY = {topY};

        return new TrunkShape(columns, columnTopY, baseY, topY, false, anyLog.getX(), anyLog.getZ());
    }

    private static int findTrunkBaseY(Chunk chunk, int x, int startY, int z, BlockPos.MutableBlockPos cursor)
    {
        int baseY = startY;

        for (int i = 0; i < MAX_TRUNK_SCAN_DOWN && baseY > 1; i++)
        {
            cursor.setPos(x, baseY - 1, z);

            if (!isSpruceLog(chunk.getBlockState(cursor))) break;

            baseY--;
        }

        return baseY;
    }

    private static int findTrunkTopY(Chunk chunk, int x, int startY, int z, int maxWorldY, BlockPos.MutableBlockPos cursor)
    {
        int topY = startY;

        for (int i = 0; i < MAX_TRUNK_SCAN_UP && topY < maxWorldY; i++)
        {
            cursor.setPos(x, topY + 1, z);

            if (!isSpruceLog(chunk.getBlockState(cursor))) break;

            topY++;
        }

        return topY;
    }

    private static boolean isTwoByTwoSpruceTrunk(Chunk chunk, int startX, int baseY, int startZ, int chunkStartX, int chunkStartZ, int maxWorldY, BlockPos.MutableBlockPos cursor)
    {
        if (!isInsideChunk(startX, startZ, chunkStartX, chunkStartZ)) return false;
        if (!isInsideChunk(startX + 1, startZ + 1, chunkStartX, chunkStartZ)) return false;
        if (baseY + LARGE_TRUNK_VALIDATION_HEIGHT - 1 > maxWorldY) return false;

        for (int dy = 0; dy < LARGE_TRUNK_VALIDATION_HEIGHT; dy++)
        {
            int y = baseY + dy;

            for (int dx = 0; dx <= 1; dx++)
            {
                for (int dz = 0; dz <= 1; dz++)
                {
                    cursor.setPos(startX + dx, y, startZ + dz);

                    if (!isSpruceLog(chunk.getBlockState(cursor))) return false;
                }
            }
        }

        return true;
    }

    private static boolean hasSpruceLeavesNearby(Chunk chunk, TrunkShape trunk, int chunkStartX, int chunkStartZ, int maxWorldY, BlockPos.MutableBlockPos cursor)
    {
        int requiredLeaves = 3;
        int foundLeaves = 0;
        int minX = Math.max(chunkStartX, trunk.minX() - 2);
        int maxX = Math.min(chunkStartX + 15, trunk.getMaxX() + 2);
        int minZ = Math.max(chunkStartZ, trunk.minZ() - 2);
        int maxZ = Math.min(chunkStartZ + 15, trunk.getMaxZ() + 2);
        int minY = Math.max(1, trunk.highestTopY() - 2);
        int maxY = Math.min(maxWorldY, trunk.highestTopY() + 4);

        for (int y = minY; y <= maxY; y++)
        {
            for (int x = minX; x <= maxX; x++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    cursor.setPos(x, y, z);

                    if (isSpruceLeaves(chunk.getBlockState(cursor)))
                    {
                        foundLeaves++;

                        if (foundLeaves >= requiredLeaves) return true;
                    }
                }
            }
        }

        return false;
    }

    private static ExistingResinData findExistingResin(Chunk chunk, TrunkShape trunk, int chunkStartX, int chunkStartZ, BlockPos.MutableBlockPos cursor)
    {
        Set<Long> resinPositions = new HashSet<>();
        Set<Integer> occupiedYLevels = new HashSet<>();

        for (int columnIndex = 0; columnIndex < trunk.getColumnCount(); columnIndex++)
        {
            BlockPos column = trunk.getColumn(columnIndex);
            int columnTopY = trunk.getColumnTopY(columnIndex);

            for (int y = trunk.baseY() + 1; y <= columnTopY; y++)
            {
                cursor.setPos(column.getX(), y, column.getZ());

                if (!isSpruceLog(chunk.getBlockState(cursor))) continue;

                for (EnumFacing side : HORIZONTAL_SIDES)
                {
                    int resinX = column.getX() + side.getXOffset();
                    int resinZ = column.getZ() + side.getZOffset();

                    if (!isInsideChunk(resinX, resinZ, chunkStartX, chunkStartZ)) continue;

                    cursor.setPos(resinX, y, resinZ);

                    if (chunk.getBlockState(cursor).getBlock() == ModBlocks.RESIN_BLOCK)
                    {
                        if (resinPositions.add(cursor.toLong())) occupiedYLevels.add(y);
                    }
                }
            }
        }

        return new ExistingResinData(resinPositions.size(), occupiedYLevels);
    }

    private static List<ResinCandidate> collectResinCandidates(World world, Chunk chunk, TrunkShape trunk, int chunkStartX, int chunkStartZ, BlockPos.MutableBlockPos cursor)
    {
        List<ResinCandidate> candidates = new ArrayList<>();
        Set<Long> addedPositions = new HashSet<>();

        for (int columnIndex = 0; columnIndex < trunk.getColumnCount(); columnIndex++)
        {
            BlockPos column = trunk.getColumn(columnIndex);
            int columnTopY = trunk.getColumnTopY(columnIndex);
            int columnHeight = columnTopY - trunk.baseY() + 1;
            int placementHeight = Math.max(1, (int) (columnHeight * 0.7D));
            int placementTopY = Math.min(columnTopY, trunk.baseY() + placementHeight);

            for (int y = trunk.baseY() + 1; y <= placementTopY; y++)
            {
                cursor.setPos(column.getX(), y, column.getZ());

                if (!isSpruceLog(chunk.getBlockState(cursor))) continue;

                for (EnumFacing side : HORIZONTAL_SIDES)
                {
                    int resinX = column.getX() + side.getXOffset();
                    int resinZ = column.getZ() + side.getZOffset();

                    if (!isInsideChunk(resinX, resinZ, chunkStartX, chunkStartZ)) continue;

                    BlockPos resinPos = new BlockPos(resinX, y, resinZ);

                    if (!world.isAirBlock(resinPos)) continue;
                    if (!addedPositions.add(resinPos.toLong())) continue;

                    candidates.add(new ResinCandidate(resinPos, side));
                }
            }
        }

        return candidates;
    }

    private static void placeMissingResin(World world, List<ResinCandidate> candidates, ExistingResinData existingResin, int targetResinCount, Random rand)
    {
        Collections.shuffle(candidates, rand);

        int currentCount = existingResin.count();
        Set<Integer> occupiedYLevels = new HashSet<>(existingResin.occupiedYLevels());

        for (ResinCandidate candidate : candidates)
        {
            if (currentCount >= targetResinCount) return;
            if (!hasRequiredVerticalDistance(candidate.getY(), occupiedYLevels)) continue;

            if (placeResin(world, candidate, rand))
            {
                currentCount++;
                occupiedYLevels.add(candidate.getY());
            }
        }

        if (currentCount < targetResinCount)
        {
            for (ResinCandidate candidate : candidates)
            {
                if (currentCount >= targetResinCount) return;

                if (placeResin(world, candidate, rand)) currentCount++;
            }
        }
    }

    private static boolean hasRequiredVerticalDistance(int candidateY, Set<Integer> occupiedYLevels)
    {
        for (Integer occupiedY : occupiedYLevels)
        {
            if (Math.abs(candidateY - occupiedY) < MIN_RESIN_VERTICAL_DISTANCE) return false;
        }

        return true;
    }

    private static boolean placeResin(World world, ResinCandidate candidate, Random rand)
    {
        BlockPos resinPos = candidate.position();

        if (!world.isAirBlock(resinPos)) return false;

        int variant = rand.nextInt(4);
        IBlockState resinState = ModBlocks.RESIN_BLOCK.getDefaultState().withProperty(BlockResin.FACING, candidate.facing()).withProperty(BlockResin.VARIANT, variant);

        return world.setBlockState(resinPos, resinState, 2);
    }

    private static boolean isSpruceLog(IBlockState state)
    {
        return state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.SPRUCE;
    }

    private static boolean isSpruceLeaves(IBlockState state)
    {
        return state.getBlock() == Blocks.LEAVES && state.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.SPRUCE;
    }

    private static boolean isInsideChunk(int x, int z, int chunkStartX, int chunkStartZ)
    {
        return x >= chunkStartX && x <= chunkStartX + 15 && z >= chunkStartZ && z <= chunkStartZ + 15;
    }

    @Desugar
    private record TrunkShape(BlockPos[] columns, int[] columnTopY, int baseY, int highestTopY, boolean large, int minX, int minZ)
    {
        private int getHeight() { return highestTopY - baseY + 1; }

        private int getColumnCount() { return columns.length; }

        private BlockPos getColumn(int index) { return columns[index]; }

        private int getColumnTopY(int index) { return columnTopY[index]; }

        private int getMaxX() { return large ? minX + 1 : minX; }

        private int getMaxZ() { return large ? minZ + 1 : minZ; }

        private long getTreeKey() { return new BlockPos(minX, baseY, minZ).toLong(); }
    }

    @Desugar
    private record ExistingResinData(int count, Set<Integer> occupiedYLevels) {}

    @Desugar
    private record ResinCandidate(BlockPos position, EnumFacing facing)
    {
        private int getY() { return position.getY(); }
    }
}