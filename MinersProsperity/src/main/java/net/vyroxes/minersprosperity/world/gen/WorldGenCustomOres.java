package net.vyroxes.minersprosperity.world.gen;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.mixin.accessor.ChunkGeneratorFlatAccessor;
import net.vyroxes.minersprosperity.config.ModConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenCustomOres implements IWorldGenerator
{
    private final WorldGenerator HALITE_ORE = new WorldGenMinable(ModBlocks.HALITE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeHaliteOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator SULFUR_ORE = new WorldGenMinable(ModBlocks.SULFUR_ORE.getDefaultState(), ModConfig.ORES.clusterSizeSulfurOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator GRAPHITE_ORE = new WorldGenMinable(ModBlocks.GRAPHITE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeGraphiteOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator COPPER_ORE = new WorldGenMinable(ModBlocks.COPPER_ORE.getDefaultState(), ModConfig.ORES.clusterSizeCopperOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TIN_ORE = new WorldGenMinable(ModBlocks.TIN_ORE.getDefaultState(), ModConfig.ORES.clusterSizeTinOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator SILVER_ORE = new WorldGenMinable(ModBlocks.SILVER_ORE.getDefaultState(), ModConfig.ORES.clusterSizeSilverOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator LEAD_ORE = new WorldGenMinable(ModBlocks.LEAD_ORE.getDefaultState(), ModConfig.ORES.clusterSizeLeadOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ZINC_ORE = new WorldGenMinable(ModBlocks.ZINC_ORE.getDefaultState(), ModConfig.ORES.clusterSizeZincOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ALUMINUM_ORE = new WorldGenMinable(ModBlocks.ALUMINUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeAluminumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator NICKEL_ORE = new WorldGenMinable(ModBlocks.NICKEL_ORE.getDefaultState(), ModConfig.ORES.clusterSizeNickelOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator CHROMIUM_ORE = new WorldGenMinable(ModBlocks.CHROMIUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeChromiumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator URANIUM_ORE = new WorldGenMinable(ModBlocks.URANIUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeUraniumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TUNGSTEN_ORE = new WorldGenMinable(ModBlocks.TUNGSTEN_ORE.getDefaultState(), ModConfig.ORES.clusterSizeTungstenOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator PLATINUM_ORE = new WorldGenMinable(ModBlocks.PLATINUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizePlatinumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator IRIDIUM_ORE = new WorldGenMinable(ModBlocks.IRIDIUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeIridiumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TITANIUM_ORE = new WorldGenMinable(ModBlocks.TITANIUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeTitaniumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator RUBY_ORE = new WorldGenMinable(ModBlocks.RUBY_ORE.getDefaultState(), ModConfig.ORES.clusterSizeRubyOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator SAPPHIRE_ORE = new WorldGenMinable(ModBlocks.SAPPHIRE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeSapphireOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator AMETHYST_ORE = new WorldGenMinable(ModBlocks.AMETHYST_ORE.getDefaultState(), ModConfig.ORES.clusterSizeAmethystOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TOPAZ_ORE = new WorldGenMinable(ModBlocks.TOPAZ_ORE.getDefaultState(), ModConfig.ORES.clusterSizeTopazOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator PERIDOT_ORE = new WorldGenMinable(ModBlocks.PERIDOT_ORE.getDefaultState(), ModConfig.ORES.clusterSizePeridotOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator COBALT_ORE = new WorldGenMinable(ModBlocks.COBALT_ORE.getDefaultState(), ModConfig.ORES.clusterSizeCobaltOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ONYX_ORE = new WorldGenMinable(ModBlocks.ONYX_ORE.getDefaultState(), ModConfig.ORES.clusterSizeOnyxOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator BLACK_OPAL_ORE = new WorldGenMinable(ModBlocks.BLACK_OPAL_ORE.getDefaultState(), ModConfig.ORES.clusterSizeBlackOpalOre, BlockMatcher.forBlock(Blocks.STONE));

    private final WorldGenerator AZURE_ORE = new WorldGenMinable(ModBlocks.AZURE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeAzureOre, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenerator ROSALITE_ORE = new WorldGenMinable(ModBlocks.ROSALITE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeRosaliteOre, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenerator VERDITE_ORE = new WorldGenMinable(ModBlocks.VERDITE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeVerditeOre, BlockMatcher.forBlock(Blocks.NETHERRACK));

    private final WorldGenerator VOIDIUM_ORE = new WorldGenMinable(ModBlocks.VOIDIUM_ORE.getDefaultState(), ModConfig.ORES.clusterSizeVoidiumOre, BlockMatcher.forBlock(Blocks.END_STONE));
    private final WorldGenerator LUMINOSITE_ORE = new WorldGenMinable(ModBlocks.LUMINOSITE_ORE.getDefaultState(), ModConfig.ORES.clusterSizeLuminositeOre, BlockMatcher.forBlock(Blocks.END_STONE));

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
        int dimensionId = world.provider.getDimension();
        List<Integer> dimensionsWhitelist = Arrays.asList(ModConfig.ORES.dimensionsWhitelist);
        List<Integer> dimensionsBlacklist = Arrays.asList(ModConfig.ORES.dimensionsBlacklist);

        if (!ModConfig.ORES.enableOreGen || !dimensionsWhitelist.contains(dimensionId) || dimensionsBlacklist.contains(dimensionId)) return;

        boolean isSuperflat = world.getWorldType() == WorldType.FLAT;

        if (isSuperflat && chunkGenerator instanceof ChunkGeneratorFlat)
        {
            ChunkGeneratorFlatAccessor accessor = (ChunkGeneratorFlatAccessor) chunkGenerator;
            if (!accessor.getHasDecoration()) return;
        }

        switch(world.provider.getDimension())
        {
            case -1:
                if (ModConfig.ORES.generateAzureOre) runGenerator(AZURE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountAzureOre, ModConfig.ORES.minHeightAzureOre, ModConfig.ORES.maxHeightAzureOre);
                if (ModConfig.ORES.generateRosaliteOre) runGenerator(ROSALITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountRosaliteOre, ModConfig.ORES.minHeightRosaliteOre, ModConfig.ORES.maxHeightRosaliteOre);
                if (ModConfig.ORES.generateVerditeOre) runGenerator(VERDITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountVerditeOre, ModConfig.ORES.minHeightVerditeOre, ModConfig.ORES.maxHeightVerditeOre);
                break;

            case 1:
                if (ModConfig.ORES.generateVoidiumOre) runGenerator(VOIDIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountVoidiumOre, ModConfig.ORES.minHeightVoidiumOre, ModConfig.ORES.maxHeightVoidiumOre);
                if (ModConfig.ORES.generateLuminositeOre) runGenerator(LUMINOSITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountLuminositeOre, ModConfig.ORES.minHeightLuminositeOre, ModConfig.ORES.maxHeightLuminositeOre);
                break;

            case 0:
            default:
                if (ModConfig.ORES.generateHaliteOre) runGenerator(HALITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountHaliteOre, ModConfig.ORES.minHeightHaliteOre, ModConfig.ORES.maxHeightHaliteOre);
                if (ModConfig.ORES.generateSulfurOre) runGenerator(SULFUR_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountSulfurOre, ModConfig.ORES.minHeightSulfurOre, ModConfig.ORES.maxHeightSulfurOre);
                if (ModConfig.ORES.generateGraphiteOre) runGenerator(GRAPHITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountGraphiteOre, ModConfig.ORES.minHeightGraphiteOre, ModConfig.ORES.maxHeightGraphiteOre);
                if (ModConfig.ORES.generateCopperOre) runGenerator(COPPER_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountCopperOre, ModConfig.ORES.minHeightCopperOre, ModConfig.ORES.maxHeightCopperOre);
                if (ModConfig.ORES.generateTinOre) runGenerator(TIN_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountTinOre, ModConfig.ORES.minHeightTinOre, ModConfig.ORES.maxHeightTinOre);
                if (ModConfig.ORES.generateSilverOre) runGenerator(SILVER_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountSilverOre, ModConfig.ORES.minHeightSilverOre, ModConfig.ORES.maxHeightSilverOre);
                if (ModConfig.ORES.generateLeadOre) runGenerator(LEAD_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountLeadOre, ModConfig.ORES.minHeightLeadOre, ModConfig.ORES.maxHeightLeadOre);
                if (ModConfig.ORES.generateZincOre) runGenerator(ZINC_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountZincOre, ModConfig.ORES.minHeightZincOre, ModConfig.ORES.maxHeightZincOre);
                if (ModConfig.ORES.generateAluminumOre) runGenerator(ALUMINUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountAluminumOre, ModConfig.ORES.minHeightAluminumOre, ModConfig.ORES.maxHeightAluminumOre);
                if (ModConfig.ORES.generateNickelOre) runGenerator(NICKEL_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountNickelOre, ModConfig.ORES.minHeightNickelOre, ModConfig.ORES.maxHeightNickelOre);
                if (ModConfig.ORES.generateChromiumOre) runGenerator(CHROMIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountChromiumOre, ModConfig.ORES.minHeightChromiumOre, ModConfig.ORES.maxHeightChromiumOre);
                if (ModConfig.ORES.generateUraniumOre) runGenerator(URANIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountUraniumOre, ModConfig.ORES.minHeightUraniumOre, ModConfig.ORES.maxHeightUraniumOre);
                if (ModConfig.ORES.generateTungstenOre) runGenerator(TUNGSTEN_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountTungstenOre, ModConfig.ORES.minHeightTungstenOre, ModConfig.ORES.maxHeightTungstenOre);
                if (ModConfig.ORES.generatePlatinumOre) runGenerator(PLATINUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountPlatinumOre, ModConfig.ORES.minHeightPlatinumOre, ModConfig.ORES.maxHeightPlatinumOre);
                if (ModConfig.ORES.generateIridiumOre) runGenerator(IRIDIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountIridiumOre, ModConfig.ORES.minHeightIridiumOre, ModConfig.ORES.maxHeightIridiumOre);
                if (ModConfig.ORES.generateTitaniumOre) runGenerator(TITANIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountTitaniumOre, ModConfig.ORES.minHeightTitaniumOre, ModConfig.ORES.maxHeightTitaniumOre);
                if (ModConfig.ORES.generateRubyOre) runGenerator(RUBY_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountRubyOre, ModConfig.ORES.minHeightRubyOre, ModConfig.ORES.maxHeightRubyOre);
                if (ModConfig.ORES.generateSapphireOre) runGenerator(SAPPHIRE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountSapphireOre, ModConfig.ORES.minHeightSapphireOre, ModConfig.ORES.maxHeightSapphireOre);
                if (ModConfig.ORES.generateAmethystOre) runGenerator(AMETHYST_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountAmethystOre, ModConfig.ORES.minHeightAmethystOre, ModConfig.ORES.maxHeightAmethystOre);
                if (ModConfig.ORES.generateTopazOre) runGenerator(TOPAZ_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountTopazOre, ModConfig.ORES.minHeightTopazOre, ModConfig.ORES.maxHeightTopazOre);
                if (ModConfig.ORES.generatePeridotOre) runGenerator(PERIDOT_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountPeridotOre, ModConfig.ORES.minHeightPeridotOre, ModConfig.ORES.maxHeightPeridotOre);
                if (ModConfig.ORES.generateCobaltOre) runGenerator(COBALT_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountCobaltOre, ModConfig.ORES.minHeightCobaltOre, ModConfig.ORES.maxHeightCobaltOre);
                if (ModConfig.ORES.generateOnyxOre) runGenerator(ONYX_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountOnyxOre, ModConfig.ORES.minHeightOnyxOre, ModConfig.ORES.maxHeightOnyxOre);
                if (ModConfig.ORES.generateBlackOpalOre) runGenerator(BLACK_OPAL_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.clusterCountBlackOpalOre, ModConfig.ORES.minHeightBlackOpalOre, ModConfig.ORES.maxHeightBlackOpalOre);
                break;
        }
    }
}