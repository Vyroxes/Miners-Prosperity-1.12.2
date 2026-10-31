package net.vyroxes.minersprosperity.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.vyroxes.minersprosperity.block.BlockOres;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModBlocks;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class WorldGenCrudeOil implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        ModConfig.WorldGeneration config = ModConfig.WORLD_GENERATION;
        if (!config.generateCrudeOil || world.getWorldType() == WorldType.FLAT) return;

        boolean allowedDimension = false;
        for (int dimension : config.crudeOilDimensions) if (dimension == world.provider.getDimension()) allowedDimension = true;
        if (!allowedDimension) return;

        if (config.generateSurfaceCrudeOil && random.nextInt(Math.max(1, config.surfaceCrudeOilRarity)) == 0)
        {
            int radius = randomRadius(random, config.surfaceCrudeOilMinRadius, config.surfaceCrudeOilMaxRadius);
            int x = (chunkX << 4) + radius + 1 + random.nextInt(14 - radius * 2);
            int z = (chunkZ << 4) + radius + 1 + random.nextInt(14 - radius * 2);
            BlockPos center = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
            generateReservoir(random, world, center, radius, MathHelper.clamp(config.surfaceCrudeOilDepth, 1, 6), true);
        }

        if (config.generateUndergroundCrudeOil && random.nextInt(Math.max(1, config.undergroundCrudeOilRarity)) == 0)
        {
            int radius = randomRadius(random, config.undergroundCrudeOilMinRadius, config.undergroundCrudeOilMaxRadius);
            int verticalRadius = MathHelper.clamp(config.undergroundCrudeOilVerticalRadius, 1, 6);
            int minY = Math.max(verticalRadius + 1, Math.min(config.undergroundCrudeOilMinHeight, config.undergroundCrudeOilMaxHeight));
            int maxY = Math.min(world.getActualHeight() - verticalRadius - 2, Math.max(config.undergroundCrudeOilMinHeight, config.undergroundCrudeOilMaxHeight));
            if (minY > maxY) return;
            int x = (chunkX << 4) + radius + 1 + random.nextInt(14 - radius * 2);
            int z = (chunkZ << 4) + radius + 1 + random.nextInt(14 - radius * 2);
            int y = minY + random.nextInt(maxY - minY + 1);
            generateReservoir(random, world, new BlockPos(x, y, z), radius, verticalRadius, false);
        }
    }

    private int randomRadius(Random random, int minimum, int maximum)
    {
        int min = MathHelper.clamp(Math.min(minimum, maximum), 1, 6);
        int max = MathHelper.clamp(Math.max(minimum, maximum), 1, 6);
        return min + random.nextInt(max - min + 1);
    }

    private void generateReservoir(Random random, World world, BlockPos center, int radius, int verticalRadius, boolean surface)
    {
        if (center.getY() - verticalRadius < 1 || center.getY() + (surface ? 1 : verticalRadius + 1) >= world.getActualHeight()) return;

        Set<BlockPos> positions = new LinkedHashSet<>();
        int parts = 3 + random.nextInt(3);
        for (int part = 0; part < parts; part++)
        {
            double offsetX = (random.nextDouble() - 0.5D) * radius * 0.7D;
            double offsetZ = (random.nextDouble() - 0.5D) * radius * 0.7D;
            double sizeX = (radius + 0.5D - Math.abs(offsetX)) * (0.8D + random.nextDouble() * 0.2D);
            double sizeZ = (radius + 0.5D - Math.abs(offsetZ)) * (0.8D + random.nextDouble() * 0.2D);
            double sizeY = (surface ? verticalRadius : verticalRadius + 0.5D) * (0.7D + random.nextDouble() * 0.3D);
            for (int x = -radius; x <= radius; x++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    for (int y = surface ? 1 - verticalRadius : -verticalRadius; y <= (surface ? 0 : verticalRadius); y++)
                    {
                        double dx = (x - offsetX) / sizeX;
                        double dz = (z - offsetZ) / sizeZ;
                        if (dx * dx + dz * dz + y * y / (sizeY * sizeY) <= 1.0D) positions.add(center.add(x, y, z));
                    }
                }
            }
        }

        for (BlockPos pos : positions)
        {
            if (!isTerrain(world.getBlockState(pos)) || world.getTileEntity(pos) != null) return;
            for (EnumFacing facing : EnumFacing.values())
            {
                BlockPos neighbor = pos.offset(facing);
                if (positions.contains(neighbor)) continue;
                IBlockState state = world.getBlockState(neighbor);
                if (surface && facing == EnumFacing.UP && pos.getY() == center.getY())
                {
                    Material material = state.getMaterial();
                    if (material != Material.AIR && material != Material.PLANTS && material != Material.VINE && material != Material.SNOW) return;
                }
                else if (!isTerrain(state) || world.getTileEntity(neighbor) != null) return;
            }
        }

        IBlockState oil = ModBlocks.CRUDE_OIL_BLOCK.getDefaultState();
        for (BlockPos pos : positions)
        {
            world.setBlockState(pos, oil, 2);
            if (surface && pos.getY() == center.getY() && !world.isAirBlock(pos.up())) world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }
    }

    private boolean isTerrain(IBlockState state)
    {
        Block block = state.getBlock();
        return block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.MYCELIUM
                || block == Blocks.SAND || block == Blocks.GRAVEL || block == Blocks.CLAY || block == Blocks.SANDSTONE
                || block == Blocks.RED_SANDSTONE || block == Blocks.HARDENED_CLAY || block == Blocks.STAINED_HARDENED_CLAY
                || block instanceof BlockOre || block instanceof BlockOres;
    }
}