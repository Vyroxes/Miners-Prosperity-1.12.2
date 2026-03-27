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
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.mixins.ChunkGeneratorFlatAccessor;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenCustomOres implements IWorldGenerator
{
    private final WorldGenerator COPPER_ORE = new WorldGenMinable(BlockInit.COPPER_ORE.getDefaultState(), ModConfig.ORES.haClusterSizeCopperOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TIN_ORE = new WorldGenMinable(BlockInit.TIN_ORE.getDefaultState(), ModConfig.ORES.maClusterSizeTinOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator SILVER_ORE = new WorldGenMinable(BlockInit.SILVER_ORE.getDefaultState(), ModConfig.ORES.raClusterSizeSilverOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator LEAD_ORE = new WorldGenMinable(BlockInit.LEAD_ORE.getDefaultState(), ModConfig.ORES.waClusterSizeLeadOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ZINC_ORE = new WorldGenMinable(BlockInit.ZINC_ORE.getDefaultState(), ModConfig.ORES.bbClusterSizeZincOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ALUMINUM_ORE = new WorldGenMinable(BlockInit.ALUMINUM_ORE.getDefaultState(), ModConfig.ORES.gbClusterSizeAluminumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator NICKEL_ORE = new WorldGenMinable(BlockInit.NICKEL_ORE.getDefaultState(), ModConfig.ORES.lbClusterSizeNickelOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator CHROMIUM_ORE = new WorldGenMinable(BlockInit.CHROMIUM_ORE.getDefaultState(), ModConfig.ORES.qbClusterSizeChromiumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator URANIUM_ORE = new WorldGenMinable(BlockInit.URANIUM_ORE.getDefaultState(), ModConfig.ORES.vbClusterSizeUraniumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TUNGSTEN_ORE = new WorldGenMinable(BlockInit.TUNGSTEN_ORE.getDefaultState(), ModConfig.ORES.acClusterSizeTungstenOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator PLATINUM_ORE = new WorldGenMinable(BlockInit.PLATINUM_ORE.getDefaultState(), ModConfig.ORES.fcClusterSizePlatinumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator IRIDIUM_ORE = new WorldGenMinable(BlockInit.IRIDIUM_ORE.getDefaultState(), ModConfig.ORES.kcClusterSizeIridiumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TITANIUM_ORE = new WorldGenMinable(BlockInit.TITANIUM_ORE.getDefaultState(), ModConfig.ORES.pcClusterSizeTitaniumOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator RUBY_ORE = new WorldGenMinable(BlockInit.RUBY_ORE.getDefaultState(), ModConfig.ORES.ucClusterSizeRubyOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator SAPPHIRE_ORE = new WorldGenMinable(BlockInit.SAPPHIRE_ORE.getDefaultState(), ModConfig.ORES.zcClusterSizeSapphireOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator AMETHYST_ORE = new WorldGenMinable(BlockInit.AMETHYST_ORE.getDefaultState(), ModConfig.ORES.edClusterSizeAmethystOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator TOPAZ_ORE = new WorldGenMinable(BlockInit.TOPAZ_ORE.getDefaultState(), ModConfig.ORES.jdClusterSizeTopazOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator PERIDOT_ORE = new WorldGenMinable(BlockInit.PERIDOT_ORE.getDefaultState(), ModConfig.ORES.odClusterSizePeridotOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator COBALT_ORE = new WorldGenMinable(BlockInit.COBALT_ORE.getDefaultState(), ModConfig.ORES.tdClusterSizeCobaltOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator ONYX_ORE = new WorldGenMinable(BlockInit.ONYX_ORE.getDefaultState(), ModConfig.ORES.ydClusterSizeOnyxOre, BlockMatcher.forBlock(Blocks.STONE));
    private final WorldGenerator BLACK_OPAL_ORE = new WorldGenMinable(BlockInit.BLACK_OPAL_ORE.getDefaultState(), ModConfig.ORES.deClusterSizeBlackOpalOre, BlockMatcher.forBlock(Blocks.STONE));

    private final WorldGenerator AZURE_ORE = new WorldGenMinable(BlockInit.AZURE_ORE.getDefaultState(), ModConfig.ORES.ieClusterSizeAzureOre, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenerator ROSALITE_ORE = new WorldGenMinable(BlockInit.ROSALITE_ORE.getDefaultState(), ModConfig.ORES.neClusterSizeRosaliteOre, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenerator VERDITE_ORE = new WorldGenMinable(BlockInit.VERDITE_ORE.getDefaultState(), ModConfig.ORES.seClusterSizeVerditeOre, BlockMatcher.forBlock(Blocks.NETHERRACK));

    private final WorldGenerator VOIDIUM_ORE = new WorldGenMinable(BlockInit.VOIDIUM_ORE.getDefaultState(), ModConfig.ORES.xeClusterSizeVoidiumOre, BlockMatcher.forBlock(Blocks.END_STONE));
    private final WorldGenerator LUMINOSITE_ORE = new WorldGenMinable(BlockInit.LUMINOSITE_ORE.getDefaultState(), ModConfig.ORES.cfClusterSizeLuminositeOre, BlockMatcher.forBlock(Blocks.END_STONE));


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

        if (!ModConfig.ORES.enableOreGen || !dimensionsWhitelist.contains(dimensionId) || dimensionsBlacklist.contains(dimensionId))
            return;

        boolean isSuperflat = world.getWorldType() == WorldType.FLAT;

        if (isSuperflat && chunkGenerator instanceof ChunkGeneratorFlat)
        {
            ChunkGeneratorFlatAccessor accessor = (ChunkGeneratorFlatAccessor) chunkGenerator;

            if (!accessor.getHasDecoration())
                return;
        }

        switch(world.provider.getDimension())
        {
            case -1:
                if (ModConfig.ORES.eeGenerateAzureOre)
                    runGenerator(AZURE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.heClusterCountAzureOre, ModConfig.ORES.feMinHeightAzureOre, ModConfig.ORES.geMaxHeightAzureOre);
                if (ModConfig.ORES.jeGenerateRosaliteOre)
                    runGenerator(ROSALITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.meClusterCountRosaliteOre, ModConfig.ORES.keMinHeightRosaliteOre, ModConfig.ORES.leMaxHeightRosaliteOre);
                if (ModConfig.ORES.oeGenerateVerditeOre)
                    runGenerator(VERDITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.reClusterCountVerditeOre, ModConfig.ORES.peMinHeightVerditeOre, ModConfig.ORES.qeMaxHeightVerditeOre);
                break;
            case 1:
                if (ModConfig.ORES.teGenerateVoidiumOre)
                    runGenerator(VOIDIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.weClusterCountVoidiumOre, ModConfig.ORES.ueMinHeightVoidiumOre, ModConfig.ORES.veMaxHeightVoidiumOre);
                if (ModConfig.ORES.yeGenerateLuminositeOre)
                    runGenerator(LUMINOSITE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.bfClusterCountLuminositeOre, ModConfig.ORES.zeMinHeightLuminositeOre, ModConfig.ORES.afMaxHeightLuminositeOre);
                break;
            case 0:
            default:
                if (ModConfig.ORES.daGenerateCopperOre)
                    runGenerator(COPPER_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.gaClusterCountCopperOre, ModConfig.ORES.eaMinHeightCopperOre, ModConfig.ORES.faMaxHeightCopperOre);
                if (ModConfig.ORES.iaGenerateTinOre)
                    runGenerator(TIN_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.laClusterCountTinOre, ModConfig.ORES.jaMinHeightTinOre, ModConfig.ORES.kaMaxHeightTinOre);
                if (ModConfig.ORES.naGenerateSilverOre)
                    runGenerator(SILVER_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.qaClusterCountSilverOre, ModConfig.ORES.oaMinHeightSilverOre, ModConfig.ORES.paMaxHeightSilverOre);
                if (ModConfig.ORES.saGenerateLeadOre)
                    runGenerator(LEAD_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.vaClusterCountLeadOre, ModConfig.ORES.taMinHeightLeadOre, ModConfig.ORES.uaMaxHeightLeadOre);
                if (ModConfig.ORES.xaGenerateZincOre)
                    runGenerator(ZINC_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.abClusterCountZincOre, ModConfig.ORES.yaMinHeightZincOre, ModConfig.ORES.zaMaxHeightZincOre);
                if (ModConfig.ORES.cbGenerateAluminumOre)
                    runGenerator(ALUMINUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.fbClusterCountAluminumOre, ModConfig.ORES.dbMinHeightAluminumOre, ModConfig.ORES.ebMaxHeightAluminumOre);
                if (ModConfig.ORES.hbGenerateNickelOre)
                    runGenerator(NICKEL_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.kbClusterCountNickelOre, ModConfig.ORES.ibMinHeightNickelOre, ModConfig.ORES.jbMaxHeightNickelOre);
                if (ModConfig.ORES.mbGenerateChromiumOre)
                    runGenerator(CHROMIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.pbClusterCountChromiumOre, ModConfig.ORES.nbMinHeightChromiumOre, ModConfig.ORES.obMaxHeightChromiumOre);
                if (ModConfig.ORES.rbGenerateUraniumOre)
                    runGenerator(URANIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ubClusterCountUraniumOre, ModConfig.ORES.sbMinHeightUraniumOre, ModConfig.ORES.tbMaxHeightUraniumOre);
                if (ModConfig.ORES.wbGenerateTungstenOre)
                    runGenerator(TUNGSTEN_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.zbClusterCountTungstenOre, ModConfig.ORES.xbMinHeightTungstenOre, ModConfig.ORES.ybMaxHeightTungstenOre);
                if (ModConfig.ORES.bcGeneratePlatinumOre)
                    runGenerator(PLATINUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ecClusterCountPlatinumOre, ModConfig.ORES.ccMinHeightPlatinumOre, ModConfig.ORES.dcMaxHeightPlatinumOre);
                if (ModConfig.ORES.gcGenerateIridiumOre)
                    runGenerator(IRIDIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.jcClusterCountIridiumOre, ModConfig.ORES.hcMinHeightIridiumOre, ModConfig.ORES.icMaxHeightIridiumOre);
                if (ModConfig.ORES.lcGenerateTitaniumOre)
                    runGenerator(TITANIUM_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ocClusterCountTitaniumOre, ModConfig.ORES.mcMinHeightTitaniumOre, ModConfig.ORES.ncMaxHeightTitaniumOre);
                if (ModConfig.ORES.qcGenerateRubyOre)
                    runGenerator(RUBY_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.tcClusterCountRubyOre, ModConfig.ORES.rcMinHeightRubyOre, ModConfig.ORES.scMaxHeightRubyOre);
                if (ModConfig.ORES.vcGenerateSapphireOre)
                    runGenerator(SAPPHIRE_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ycClusterCountSapphireOre, ModConfig.ORES.wcMinHeightSapphireOre, ModConfig.ORES.xcMaxHeightSapphireOre);
                if (ModConfig.ORES.adGenerateAmethystOre)
                    runGenerator(AMETHYST_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ddClusterCountAmethystOre, ModConfig.ORES.bdMinHeightAmethystOre, ModConfig.ORES.cdMaxHeightAmethystOre);
                if (ModConfig.ORES.fdGenerateTopazOre)
                    runGenerator(TOPAZ_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.idClusterCountTopazOre, ModConfig.ORES.gdMinHeightTopazOre, ModConfig.ORES.hdMaxHeightTopazOre);
                if (ModConfig.ORES.kdGeneratePeridotOre)
                    runGenerator(PERIDOT_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ndClusterCountPeridotOre, ModConfig.ORES.ldMinHeightPeridotOre, ModConfig.ORES.mdMaxHeightPeridotOre);
                if (ModConfig.ORES.pdGenerateCobaltOre)
                    runGenerator(COBALT_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.sdClusterCountCobaltOre, ModConfig.ORES.qdMinHeightCobaltOre, ModConfig.ORES.rdMaxHeightCobaltOre);
                if (ModConfig.ORES.udGenerateOnyxOre)
                    runGenerator(ONYX_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.xdClusterCountOnyxOre, ModConfig.ORES.vdMinHeightOnyxOre, ModConfig.ORES.wdMaxHeightOnyxOre);
                if (ModConfig.ORES.zdGenerateBlackOpalOre)
                    runGenerator(BLACK_OPAL_ORE, world, random, chunkX, chunkZ, ModConfig.ORES.ceClusterCountBlackOpalOre, ModConfig.ORES.aeMinHeightBlackOpalOre, ModConfig.ORES.beMaxHeightBlackOpalOre);
                break;
        }
    }
}