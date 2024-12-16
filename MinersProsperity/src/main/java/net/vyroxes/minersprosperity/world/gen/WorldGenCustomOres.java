package net.vyroxes.minersprosperity.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.util.handlers.ConfigHandler;

import java.util.Random;

public class WorldGenCustomOres implements IWorldGenerator
{
    private WorldGenerator copper_ore, tin_ore, lead_ore, silver_ore;

    public WorldGenCustomOres()
    {
        copper_ore = new WorldGenMinable(BlockInit.COPPER_ORE.getDefaultState(), ConfigHandler.clusterSizeCopperOre, BlockMatcher.forBlock(Blocks.STONE));
        tin_ore = new WorldGenMinable(BlockInit.TIN_ORE.getDefaultState(), ConfigHandler.clusterSizeTinOre, BlockMatcher.forBlock(Blocks.STONE));
        lead_ore = new WorldGenMinable(BlockInit.LEAD_ORE.getDefaultState(), ConfigHandler.clusterSizeLeadOre, BlockMatcher.forBlock(Blocks.STONE));
        silver_ore = new WorldGenMinable(BlockInit.SILVER_ORE.getDefaultState(), ConfigHandler.clusterSizeSilverOre, BlockMatcher.forBlock(Blocks.STONE));
    }

    private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int clusterCount, int minHeight, int maxHeight)
    {
        if (minHeight > maxHeight || minHeight < 0 || maxHeight > 255) throw new IllegalArgumentException("Ore generated out of bounds");

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < clusterCount; i++)
        {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(x, y, z));
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        switch(world.provider.getDimension())
        {
            case -1:
                //runGenerator(tin_ore, world, random, chunkX, chunkZ, 50, 0, 100);
                break;
            case 0:
                if (ConfigHandler.generateCopperOre)
                {
                    runGenerator(copper_ore, world, random, chunkX, chunkZ, ConfigHandler.clusterCountCopperOre, ConfigHandler.minHeightCopperOre, ConfigHandler.maxHeightCopperOre);
                }
                if (ConfigHandler.generateTinOre)
                {
                    runGenerator(tin_ore, world, random, chunkX, chunkZ, ConfigHandler.clusterCountTinOre, ConfigHandler.minHeightTinOre, ConfigHandler.maxHeightTinOre);
                }
                if (ConfigHandler.generateLeadOre)
                {
                    runGenerator(lead_ore, world, random, chunkX, chunkZ, ConfigHandler.clusterCountLeadOre, ConfigHandler.minHeightLeadOre, ConfigHandler.maxHeightLeadOre);
                }
                if (ConfigHandler.generateSilverOre)
                {
                    runGenerator(silver_ore, world, random, chunkX, chunkZ, ConfigHandler.clusterCountSilverOre, ConfigHandler.minHeightSilverOre, ConfigHandler.maxHeightSilverOre);
                }
                break;
            case 1:
                //runGenerator(tin_ore, world, random, chunkX, chunkZ, 50, 0, 100);
        }
    }
}