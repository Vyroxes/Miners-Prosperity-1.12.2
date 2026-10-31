package net.vyroxes.minersprosperity.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.vyroxes.minersprosperity.Tags;

@Config(modid = Tags.MODID)
public class ModConfig
{
    @Config.Name("Ores")
    @Config.LangKey("config.minersprosperity.category.ores")
    @Comment("Ores generation settings")
    public static Ores ORES = new Ores();

    @Config.Name("World Generation")
    @Config.LangKey("config.minersprosperity.category.world_generation")
    @Comment("World generation settings")
    public static WorldGeneration WORLD_GENERATION = new WorldGeneration();

    @Config.Name("Machines")
    @Config.LangKey("config.minersprosperity.category.machines")
    @Comment("Machine settings")
    public static Machines MACHINES = new Machines();

    @Config.Name("Blocks")
    @Config.LangKey("config.minersprosperity.category.blocks")
    @Comment("Block settings")
    public static Blocks BLOCKS = new Blocks();

    @Config.Name("Items")
    @Config.LangKey("config.minersprosperity.category.items")
    @Comment("Item settings")
    public static Items ITEMS = new Items();

    @Config.Name("Others")
    @Config.LangKey("config.minersprosperity.category.others")
    @Comment("Other settings")
    public static Other OTHER = new Other();

    @Config.Name("GUI")
    @Config.LangKey("config.minersprosperity.category.gui")
    @Comment("GUI settings")
    public static GUI GUI = new GUI();

    @Config.Name("Enchantments")
    @Config.LangKey("config.minersprosperity.category.enchantments")
    @Comment("Enchantment settings")
    public static Enchantments ENCHANTMENTS = new Enchantments();

    @Config.Name("Integration")
    @Config.LangKey("config.minersprosperity.category.integration")
    @Comment("Integration settings")
    public static Integration INTEGRATION = new Integration();

    @Config.Name("Tooltips")
    @Config.LangKey("config.minersprosperity.category.tooltips")
    @Comment("Tooltip display settings")
    public static Tooltips TOOLTIPS = new Tooltips();

    public static class Ores
    {
        @Config.Name("Enable Ore Generation")
        @Comment("Whether to generate ores")
        public boolean enableOreGen = true;
        @Config.Name("Dimensions Whitelist")
        @Comment({
                "List of dimension IDs where ore generation is allowed",
                "If this list is not empty, ores will only generate in listed dimensions",
                "Example: -1 = Nether, 0 = Overworld, 1 = End",
                "Note: Ores require specific base blocks to generate:",
                " - Overworld ores need STONE",
                " - The Nether ores need NETHERRACK",
                " - The End ores need END_STONE"
        })
        public Integer[] dimensionsWhitelist = new Integer[]{
                -1,
                0,
                1
        };
        @Config.Name("Dimensions Blacklist")
        @Comment({
                "List of dimension IDs where ore generation is disallowed",
                "This is checked after the whitelist",
                "Examples:",
                " - -1 - The Nether (to disable ore generation there)",
                " - 0 - Overworld",
                " - 1 - The End"
        })
        public Integer[] dimensionsBlacklist = new Integer[]{};

        @Config.Name("Generate Halite Ore")
        public boolean generateHaliteOre = true;
        @Config.Name("Minimum Height Halite Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightHaliteOre = 0;
        @Config.Name("Maximum Height Halite Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightHaliteOre = 128;
        @Config.Name("Cluster Count Halite Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountHaliteOre = 5;
        @Config.Name("Cluster Size Halite Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeHaliteOre = 8;

        @Config.Name("Generate Sulfur Ore")
        public boolean generateSulfurOre = true;
        @Config.Name("Minimum Height Sulfur Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightSulfurOre = 0;
        @Config.Name("Maximum Height Sulfur Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightSulfurOre = 128;
        @Config.Name("Cluster Count Sulfur Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountSulfurOre = 3;
        @Config.Name("Cluster Size Sulfur Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeSulfurOre = 8;

        @Config.Name("Generate Graphite Ore")
        public boolean generateGraphiteOre = true;
        @Config.Name("Minimum Height Graphite Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightGraphiteOre = 0;
        @Config.Name("Maximum Height Graphite Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightGraphiteOre = 128;
        @Config.Name("Cluster Count Graphite Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountGraphiteOre = 1;
        @Config.Name("Cluster Size Graphite Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeGraphiteOre = 8;

        @Config.Name("Generate Copper Ore")
        public boolean generateCopperOre = true;
        @Config.Name("Minimum Height Copper Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightCopperOre = 0;
        @Config.Name("Maximum Height Copper Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightCopperOre = 64;
        @Config.Name("Cluster Count Copper Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountCopperOre = 16;
        @Config.Name("Cluster Size Copper Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeCopperOre = 9;

        @Config.Name("Generate Tin Ore")
        public boolean generateTinOre = true;
        @Config.Name("Minimum Height Tin Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightTinOre = 0;
        @Config.Name("Maximum Height Tin Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightTinOre = 64;
        @Config.Name("Cluster Count Tin Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountTinOre = 14;
        @Config.Name("Cluster Size Tin Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeTinOre = 8;

        @Config.Name("Generate Silver Ore")
        public boolean generateSilverOre = true;
        @Config.Name("Minimum Height Silver Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightSilverOre = 0;
        @Config.Name("Maximum Height Silver Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightSilverOre = 48;
        @Config.Name("Cluster Count Silver Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountSilverOre = 8;
        @Config.Name("Cluster Size Silver Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeSilverOre = 7;

        @Config.Name("Generate Lead Ore")
        public boolean generateLeadOre = true;
        @Config.Name("Minimum Height Lead Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightLeadOre = 0;
        @Config.Name("Maximum Height Lead Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightLeadOre = 48;
        @Config.Name("Cluster Count Lead Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountLeadOre = 7;
        @Config.Name("Cluster Size Lead Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeLeadOre = 7;

        @Config.Name("Generate Zinc Ore")
        public boolean generateZincOre = true;
        @Config.Name("Minimum Height Zinc Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightZincOre = 0;
        @Config.Name("Maximum Height Zinc Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightZincOre = 64;
        @Config.Name("Cluster Count Zinc Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountZincOre = 13;
        @Config.Name("Cluster Size Zinc Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeZincOre = 7;

        @Config.Name("Generate Aluminum Ore")
        public boolean generateAluminumOre = true;
        @Config.Name("Minimum Height Aluminum Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightAluminumOre = 0;
        @Config.Name("Maximum Height Aluminum Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightAluminumOre = 64;
        @Config.Name("Cluster Count Aluminum Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountAluminumOre = 12;
        @Config.Name("Cluster Size Aluminum Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeAluminumOre = 7;

        @Config.Name("Generate Nickel Ore")
        public boolean generateNickelOre = true;
        @Config.Name("Minimum Height Nickel Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightNickelOre = 0;
        @Config.Name("Maximum Height Nickel Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightNickelOre = 32;
        @Config.Name("Cluster Count Nickel Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountNickelOre = 5;
        @Config.Name("Cluster Size Nickel Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeNickelOre = 6;

        @Config.Name("Generate Chromium Ore")
        public boolean generateChromiumOre = true;
        @Config.Name("Minimum Height Chromium Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightChromiumOre = 0;
        @Config.Name("Maximum Height Chromium Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightChromiumOre = 32;
        @Config.Name("Cluster Count Chromium Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountChromiumOre = 5;
        @Config.Name("Cluster Size Chromium Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeChromiumOre = 6;

        @Config.Name("Generate Uranium Ore")
        public boolean generateUraniumOre = true;
        @Config.Name("Minimum Height Uranium Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightUraniumOre = 0;
        @Config.Name("Maximum Height Uranium Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightUraniumOre = 24;
        @Config.Name("Cluster Count Uranium Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountUraniumOre = 3;
        @Config.Name("Cluster Size Uranium Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeUraniumOre = 5;

        @Config.Name("Generate Tungsten Ore")
        public boolean generateTungstenOre = true;
        @Config.Name("Minimum Height Tungsten Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightTungstenOre = 0;
        @Config.Name("Maximum Height Tungsten Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightTungstenOre = 24;
        @Config.Name("Cluster Count Tungsten Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountTungstenOre = 3;
        @Config.Name("Cluster Size Tungsten Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeTungstenOre = 5;

        @Config.Name("Generate Platinum Ore")
        public boolean generatePlatinumOre = true;
        @Config.Name("Minimum Height Platinum Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightPlatinumOre = 0;
        @Config.Name("Maximum Height Platinum Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightPlatinumOre = 16;
        @Config.Name("Cluster Count Platinum Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountPlatinumOre = 3;
        @Config.Name("Cluster Size Platinum Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizePlatinumOre = 4;

        @Config.Name("Generate Iridium Ore")
        public boolean generateIridiumOre = true;
        @Config.Name("Minimum Height Iridium Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightIridiumOre = 0;
        @Config.Name("Maximum Height Iridium Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightIridiumOre = 16;
        @Config.Name("Cluster Count Iridium Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountIridiumOre = 3;
        @Config.Name("Cluster Size Iridium Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeIridiumOre = 4;

        @Config.Name("Generate Titanium Ore")
        public boolean generateTitaniumOre = true;
        @Config.Name("Minimum Height Titanium Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightTitaniumOre = 0;
        @Config.Name("Maximum Height Titanium Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightTitaniumOre = 16;
        @Config.Name("Cluster Count Titanium Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountTitaniumOre = 3;
        @Config.Name("Cluster Size Titanium Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeTitaniumOre = 4;

        @Config.Name("Generate Ruby Ore")
        public boolean generateRubyOre = true;
        @Config.Name("Minimum Height Ruby Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightRubyOre = 0;
        @Config.Name("Maximum Height Ruby Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightRubyOre = 16;
        @Config.Name("Cluster Count Ruby Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountRubyOre = 2;
        @Config.Name("Cluster Size Ruby Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeRubyOre = 4;

        @Config.Name("Generate Sapphire Ore")
        public boolean generateSapphireOre = true;
        @Config.Name("Minimum Height Sapphire Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightSapphireOre = 0;
        @Config.Name("Maximum Height Sapphire Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightSapphireOre = 16;
        @Config.Name("Cluster Count Sapphire Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountSapphireOre = 2;
        @Config.Name("Cluster Size Sapphire Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeSapphireOre = 4;

        @Config.Name("Generate Amethyst Ore")
        public boolean generateAmethystOre = true;
        @Config.Name("Minimum Height Amethyst Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightAmethystOre = 0;
        @Config.Name("Maximum Height Amethyst Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightAmethystOre = 16;
        @Config.Name("Cluster Count Amethyst Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountAmethystOre = 2;
        @Config.Name("Cluster Size Amethyst Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeAmethystOre = 4;

        @Config.Name("Generate Topaz Ore")
        public boolean generateTopazOre = true;
        @Config.Name("Minimum Height Topaz Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightTopazOre = 0;
        @Config.Name("Maximum Height Topaz Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightTopazOre = 16;
        @Config.Name("Cluster Count Topaz Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountTopazOre = 2;
        @Config.Name("Cluster Size Topaz Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeTopazOre = 4;

        @Config.Name("Generate Peridot Ore")
        public boolean generatePeridotOre = true;
        @Config.Name("Minimum Height Peridot Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightPeridotOre = 0;
        @Config.Name("Maximum Height Peridot Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightPeridotOre = 16;
        @Config.Name("Cluster Count Peridot Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountPeridotOre = 2;
        @Config.Name("Cluster Size Peridot Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizePeridotOre = 4;

        @Config.Name("Generate Cobalt Ore")
        public boolean generateCobaltOre = true;
        @Config.Name("Minimum Height Cobalt Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightCobaltOre = 0;
        @Config.Name("Maximum Height Cobalt Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightCobaltOre = 24;
        @Config.Name("Cluster Count Cobalt Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountCobaltOre = 2;
        @Config.Name("Cluster Size Cobalt Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeCobaltOre = 3;

        @Config.Name("Generate Onyx Ore")
        public boolean generateOnyxOre = true;
        @Config.Name("Minimum Height Onyx Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightOnyxOre = 0;
        @Config.Name("Maximum Height Onyx Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightOnyxOre = 16;
        @Config.Name("Cluster Count Onyx Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountOnyxOre = 1;
        @Config.Name("Cluster Size Onyx Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeOnyxOre = 4;

        @Config.Name("Generate Black Opal Ore")
        public boolean generateBlackOpalOre = true;
        @Config.Name("Minimum Height Black Opal Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightBlackOpalOre = 0;
        @Config.Name("Maximum Height Black Opal Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightBlackOpalOre = 16;
        @Config.Name("Cluster Count Black Opal Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountBlackOpalOre = 1;
        @Config.Name("Cluster Size Black Opal Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeBlackOpalOre = 3;

        @Config.Name("Generate Azure Ore")
        public boolean generateAzureOre = true;
        @Config.Name("Minimum Height Azure Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightAzureOre = 0;
        @Config.Name("Maximum Height Azure Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightAzureOre = 64;
        @Config.Name("Cluster Count Azure Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountAzureOre = 3;
        @Config.Name("Cluster Size Azure Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeAzureOre = 5;

        @Config.Name("Generate Rosalite Ore")
        public boolean generateRosaliteOre = true;
        @Config.Name("Minimum Height Rosalite Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightRosaliteOre = 0;
        @Config.Name("Maximum Height Rosalite Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightRosaliteOre = 64;
        @Config.Name("Cluster Count Rosalite Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountRosaliteOre = 3;
        @Config.Name("Cluster Size Rosalite Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeRosaliteOre = 4;

        @Config.Name("Generate Verdite Ore")
        public boolean generateVerditeOre = true;
        @Config.Name("Minimum Height Verdite Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightVerditeOre = 0;
        @Config.Name("Maximum Height Verdite Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightVerditeOre = 64;
        @Config.Name("Cluster Count Verdite Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountVerditeOre = 2;
        @Config.Name("Cluster Size Verdite Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeVerditeOre = 4;

        @Config.Name("Generate Voidium Ore")
        public boolean generateVoidiumOre = true;
        @Config.Name("Minimum Height Voidium Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightVoidiumOre = 0;
        @Config.Name("Maximum Height Voidium Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightVoidiumOre = 64;
        @Config.Name("Cluster Count Voidium Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountVoidiumOre = 3;
        @Config.Name("Cluster Size Voidium Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeVoidiumOre = 4;

        @Config.Name("Generate Luminosite Ore")
        public boolean generateLuminositeOre = true;
        @Config.Name("Minimum Height Luminosite Ore")
        @RangeInt(min = 0, max = 255)
        public int minHeightLuminositeOre = 0;
        @Config.Name("Maximum Height Luminosite Ore")
        @RangeInt(min = 0, max = 255)
        public int maxHeightLuminositeOre = 64;
        @Config.Name("Cluster Count Luminosite Ore")
        @RangeInt(min = 1, max = 100)
        public int clusterCountLuminositeOre = 2;
        @Config.Name("Cluster Size Luminosite Ore")
        @RangeInt(min = 3, max = 100)
        public int clusterSizeLuminositeOre = 4;
    }

    public static class WorldGeneration
    {
        @Config.Name("Generate Crude Oil")
        public boolean generateCrudeOil = true;
        @Config.Name("Crude Oil Dimensions")
        @Comment("Dimension IDs where natural crude oil generation is allowed. Only affects newly generated chunks.")
        public int[] crudeOilDimensions = {0};
        @Config.Name("Generate Surface Crude Oil")
        public boolean generateSurfaceCrudeOil = true;
        @Config.Name("Surface Crude Oil Rarity")
        @Comment("One generation attempt per this many chunks on average. 1 means every chunk. Unsuitable terrain is skipped.")
        @RangeInt(min = 1, max = 1000000)
        public int surfaceCrudeOilRarity = 96;
        @Config.Name("Surface Crude Oil Minimum Radius")
        @RangeInt(min = 1, max = 6)
        public int surfaceCrudeOilMinRadius = 2;
        @Config.Name("Surface Crude Oil Maximum Radius")
        @RangeInt(min = 1, max = 6)
        public int surfaceCrudeOilMaxRadius = 3;
        @Config.Name("Surface Crude Oil Depth")
        @Comment("Maximum depth in blocks, including the surface layer.")
        @RangeInt(min = 1, max = 6)
        public int surfaceCrudeOilDepth = 2;
        @Config.Name("Generate Underground Crude Oil")
        public boolean generateUndergroundCrudeOil = true;
        @Config.Name("Underground Crude Oil Rarity")
        @Comment("One generation attempt per this many chunks on average. 1 means every chunk. Open caves and unsuitable terrain are skipped.")
        @RangeInt(min = 1, max = 1000000)
        public int undergroundCrudeOilRarity = 24;
        @Config.Name("Underground Crude Oil Minimum Radius")
        @RangeInt(min = 1, max = 6)
        public int undergroundCrudeOilMinRadius = 3;
        @Config.Name("Underground Crude Oil Maximum Radius")
        @RangeInt(min = 1, max = 6)
        public int undergroundCrudeOilMaxRadius = 6;
        @Config.Name("Underground Crude Oil Vertical Radius")
        @RangeInt(min = 1, max = 6)
        public int undergroundCrudeOilVerticalRadius = 3;
        @Config.Name("Underground Crude Oil Minimum Height")
        @Comment("Minimum Y coordinate of the reservoir center.")
        @RangeInt(min = 1, max = 254)
        public int undergroundCrudeOilMinHeight = 12;
        @Config.Name("Underground Crude Oil Maximum Height")
        @Comment("Maximum Y coordinate of the reservoir center. Reservoirs must remain fully enclosed underground.")
        @RangeInt(min = 1, max = 254)
        public int undergroundCrudeOilMaxHeight = 48;

        @Config.Name("Generate Resin On Trees (WG)")
        @Comment("Whether to generate resin on spruce trees during world generation")
        public boolean generateResinOnTreesWG = true;
        @Config.Name("Resin Tries Per Chunk (WG)")
        @Comment("How many attempts per chunk to find a suitable spruce tree for resin generation during world generation")
        @RangeInt(min = 1, max = 200)
        public int resinTriesPerChunkWG = 50;
        @Config.Name("Resin Chance Per Found Log (WG)")
        @Comment("Chance (0-100%) to generate resin after a suitable spruce tree is found during world generation")
        @RangeInt(min = 1, max = 100)
        public int resinChancePercentWG = 50;
        @Config.Name("Generate Resin On Trees (S)")
        @Comment("Whether to generate resin on spruce trees when they grow from saplings")
        public boolean generateResinOnTreesS = true;
        @Config.Name("Resin Chance Per Tree Growth (S)")
        @Comment("Chance (0-100%) to actually place resin when a tree grows from a sapling and has a valid log candidate")
        @RangeInt(min = 1, max = 100)
        public int resinChancePercentS = 50;
        @Config.Name("Resin Regrowth")
        @Comment("Whether resin can regrow after being harvested")
        public boolean resinRegrowth = true;
        @Config.Name("Resin Regrowth Minimum Time")
        @Comment("Minimum resin regrowth time in ticks. 20 ticks = 1 second. Default: 12000 ticks = 10 minutes")
        @RangeInt(min = 20, max = 1728000)
        public int resinRegrowthMinTicks = 12000;
        @Config.Name("Resin Regrowth Maximum Time")
        @Comment("Maximum resin regrowth time in ticks. 20 ticks = 1 second. Default: 36000 ticks = 30 minutes")
        @RangeInt(min = 20, max = 1728000)
        public int resinRegrowthMaxTicks = 36000;
    }

    public static class Machines
    {
        @Config.Name("Experience Fluid")
        @Comment({
                "Fluid used for experience output in machines",
                "Format: modid:fluidname",
                "Examples:",
                " - minersprosperity:liquid_experience - ratio 1000 mB = 100 XP",
                " - cyclicmagic:xpjuice - ratio 1000 mB = 50 XP",
                " - industrialforegoing:essence - ratio 1000 mB = 100 XP",
                " - thermalfoundation:experience - ratio 1000 mB = 50 XP"
        })
        public String experienceFluid = "minersprosperity:liquid_experience";
        @Config.Name("MB to XP Ratio")
        @Comment("How much XP is given per 1000 mB of fluid")
        @RangeInt(min = 1, max = 100)
        public int mbToXpRatio = 100;
        @Config.Name("Enable Alloy Furnace")
        public boolean enableAlloyFurnace = true;
        @Config.Name("Enable Pulverizer")
        public boolean enablePulverizer = true;
        @Config.Name("Enable Metal Press")
        public boolean enableMetalPress = true;
        @Config.Name("Enable Electric Furnace")
        public boolean enableElectricFurnace = true;
        @Config.Name("Electric Furnace Base Energy Usage")
        @RangeInt(min = 1, max = 1000)
        public int electricFurnaceBaseEnergyUsage = 40;
        @Config.Name("Electric Furnace Base Cook Time")
        @RangeInt(min = 1, max = 1000)
        public int electricFurnaceBaseCookTime = 200;
        @Config.Name("Enable Solid Fuel Generator")
        public boolean enableSolidFuelGenerator = true;
        @Config.Name("Solid Fuel Generator Base Generation")
        @Comment("Energy generated per furnace fuel tick in FE/t, before Power upgrades")
        @RangeInt(min = 1, max = 1000)
        public int solidFuelGeneratorBaseGeneration = 40;
        @Config.Name("Enable Liquid Fuel Generator")
        public boolean enableLiquidFuelGenerator = true;
        @Config.Name("Enable Solar Panels")
        public boolean enableSolarPanels = true;
        @Config.Name("Enable Fluid Tanks")
        public boolean enableFluidTanks = true;
        @Config.Name("Enable Energy Storages")
        public boolean enableEnergyStorages = true;
    }

    public static class Blocks
    {
        @Config.Name("Enable Custom Chests")
        public boolean enableCustomChests = true;
        @Config.Name("Allow Opening Vanilla Chests")
        @Comment("If true, vanilla chests can be opened even if there is a block above it. If false, vanilla chests will not open if there is a block above it")
        public boolean allowOpeningVanillaChest = true;
        @Config.Name("Allow Opening Custom Chests")
        @Comment("If true, custom chests can be opened even if there is a block above it. If false, custom chests will not open if there is a block above it")
        public boolean allowOpeningCustomChest = true;
    }

    public static class Items
    {
        @Config.Name("Enable Backpacks")
        public boolean enableBackpacks = true;
        @Config.Name("Allow Backpack Looping")
        public boolean allowBackpackLooping = false;
        @Config.Name("Backpack Item Blacklist")
        @Comment({
                "List of blacklisted items in backpacks that should NOT be allowed to be stored in backpacks",
                "Supported formats:",
                "  - modid:*           (all items from the mod)",
                "  - modid:item        (only metadata 0 / default variant)",
                "  - modid:item/meta   (only specific metadata)",
                "  - modid:item/*      (all metadata variants of the item)",
                "  - oreDict           (all items registered under the exact OreDictionary entry)",
                "  - ore*              (all items registered under OreDictionary entries starting with the prefix)"
        })
        public String[] backpackItemBlacklist = new String[]{
                "mod:*",
                "mod:item",
                "mod:item/2",
                "mod:item/*",
                "ingotIron",
                "ingot*"
        };
        @Config.Name("Enable Portable Crafting Table")
        public boolean enablePortableCraftingTable = true;
        @Config.Name("Enable Fire Extinguisher")
        public boolean enableFireExtinguisher = true;
        @Config.Name("Fire Extinguisher Range")
        @Comment("The extinguishing radius in blocks. Default: 4 (covers a 9x9x9 area)")
        @RangeInt(min = 1, max = 20)
        public int fireExtinguisherRange = 4;
        @Config.Name("Enable Entity Relocator")
        public boolean enableEntityRelocator = true;
        @Config.Name("Entity Relocator Blacklist")
        @Comment({
                "List of entities that cannot be captured by the Entity Relocator",
                "Supported formats:",
                "  - modid:*           (all entities from the mod)",
                "  - modid:entity      (only specific entity)"
        })
        public String[] entityRelocatorBlacklist = new String[]{
                "mod:*",
                "mod:entity"
        };
        @Config.Name("Enable Machine Relocator")
        public boolean enableMachineRelocator = true;
        @Config.Name("Machine Relocator Blacklist")
        @Comment({
                "List of blocks and Tile Entity classes that cannot be moved by the Machine Relocator",
                "Multiblock controllers and multiblock parts should be blacklisted to prevent breaking their structures",
                "Supported formats:",
                "  - modid:*                                      (all blocks from a mod)",
                "  - modid:block                                  (specific block)",
                "  - modid:block/meta                             (only specific metadata)",
                "  - modid:block/*                                (all metadata variants of the block)",
                "  - class:fully.qualified.TileEntityClass        (specific Tile Entity class)",
                "  - class:fully.qualified.package.*              (all Tile Entity classes from a package)"
        })
        public String[] machineRelocatorBlacklist = new String[]{
                "mod:*",
                "mod:machine",
                "mod:machine/2",
                "mod:machine/*",
                "class:some.mod.tile.TileMultiblockController",
                "class:some.mod.multiblock.*"
        };
        @Config.Name("Machine Relocator Cross-Dimension")
        @Comment("Whether machines stored in a Machine Relocator can be placed in a different dimension. Disabled by default for compatibility with machines that store dimension-specific data")
        public boolean machineRelocatorCrossDimension = false;
        @Config.Name("Enable Batteries")
        public boolean enableBatteries = true;
    }

    public static class Other
    {
        @Config.Name("Jump Onto Fences")
        public boolean jumpOntoFences = true;
        @Config.Name("Right Click Harvest")
        public boolean rightClickHarvest = true;
        @Config.Name("Harvest XP")
        public boolean harvestXP = true;
        @Config.Name("Simple Ladder Placing")
        public boolean simpleLadderPlacing = true;
        @Config.Name("Faster Ladder Climbing")
        public boolean fasterLadderClimbing = true;
        @Config.Name("Faster Ladder Climbing Speed Modifier")
        @RangeInt(min = 1, max = 10)
        public int fasterLadderClimbingSpeedModifier = 5;
    }

    public static class GUI
    {
        @Config.Name("Show Config Button In Main Menu")
        @Config.Comment("Shows the MP config button in the Minecraft main menu")
        public boolean showConfigButtonInMainMenu = true;
        @Config.Name("Show Config Button In Pause Menu")
        @Config.Comment("Shows the MP config button in the in-game pause menu")
        public boolean showConfigButtonInPauseMenu = true;
        @Config.Name("Armor Overlay")
        public boolean armorOverlay = true;
        @Config.Name("Toughness Overlay")
        public boolean toughnessOverlay = true;
        @Config.Name("Armor Overlay Texture")
        public boolean armorOverlayTexture = true;
        @Config.Name("Toughness Overlay Texture")
        public boolean toughnessOverlayTexture = true;
        @Config.Name("Armor Overlay Absorption Shift")
        public boolean armorOverlayAbsorptionShift = true;
        @Config.Name("Armor Overlay More Than 20 Health Shift")
        public boolean armorOverlayHealthShift = true;
        @Config.Name("Armor Overlay X Position")
        public int armorX = -91;
        @Config.Name("Armor Overlay Y Position")
        public int armorY = -49;
        @Config.Name("Toughness Overlay X Position")
        public int toughnessX = 10;
        @Config.Name("Toughness Overlay Y Position")
        public int toughnessY = -49;
        @Config.Name("Armor/Toughness Color 21-40")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_21_40")
        @Comment("Colors for armor/toughness 21-40")
        public Color color21to40 = new Color(1.0f, 1.0f, 0.0f);
        @Config.Name("Armor/Toughness Color 41-60")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_41_60")
        @Comment("Colors for armor/toughness 41-60")
        public Color color41to60 = new Color(0.1f, 0.7f, 1.0f);
        @Config.Name("Armor/Toughness Color 61-80")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_61_80")
        @Comment("Colors for armor/toughness 61-80")
        public Color color61to80 = new Color(0.0f, 0.8f, 0.3f);
        @Config.Name("Armor/Toughness Color 81-100")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_81_100")
        @Comment("Colors for armor/toughness 81-100")
        public Color color81to100 = new Color(0.6f, 0.0f, 0.8f);
        @Config.Name("Armor/Toughness Color 101-120")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_101_120")
        @Comment("Colors for armor/toughness 101-120")
        public Color color101to120 = new Color(1.0f, 0.0f, 0.0f);
        @Config.Name("Armor/Toughness Color 121-140")
        @Config.LangKey("config.minersprosperity.gui.armor_toughness_color_121_140")
        @Comment("Colors for armor/toughness 121-140")
        public Color color121to140 = new Color(0.4f, 0.4f, 0.4f);

        public static class Color
        {
            @Config.Name("Red")
            @RangeDouble(min = 0.0, max = 1.0)
            public float red;
            @Config.Name("Green")
            @RangeDouble(min = 0.0, max = 1.0)
            public float green;
            @Config.Name("Blue")
            @RangeDouble(min = 0.0, max = 1.0)
            public float blue;

            public Color(float red, float green, float blue)
            {
                this.red = red;
                this.green = green;
                this.blue = blue;
            }
        }
    }

    public static class Enchantments
    {
        @Config.Name("Quick Mining")
        public boolean quickMining = true;
        @Config.Name("Quick Mining Block Limit")
        @RangeInt(min = 2, max = 1024)
        public int quickMiningBlockLimit = 64;
        @Config.Name("Glow")
        public boolean glow = true;
        @Config.Name("Glow Light Value")
        @RangeInt(min = 1, max = 15)
        public int glowLightValue = 15;
        @Config.Name("Unrestricted")
        public boolean unrestricted = true;
        @Config.Name("Vacuum")
        public boolean vacuum = true;
        @Config.Name("Far Reach")
        public boolean farReach = true;
        @Config.Name("Far Reach Distance")
        @RangeInt(min = 5, max = 32)
        public int farReachDistance = 16;
        @Config.Name("Flameproof")
        public boolean flameproof = true;
        @Config.Name("Poisonproof")
        public boolean poisonproof = true;
        @Config.Name("Witherproof")
        public boolean witherproof = true;
        @Config.Name("Lightningproof")
        public boolean lightningproof = true;
        @Config.Name("Insight")
        public boolean insight = true;
        @Config.Name("Void Guard")
        public boolean voidGuard = true;
        @Config.Name("Farmer's Blessing")
        public boolean farmersBlessing = true;
        @Config.Name("Farmer's Blessing Crop Blacklist")
        @Comment({
                "List of blacklisted crops that should NOT be affected by Farmer's Blessing enchantment",
                "Supported formats:",
                "  - modid:*           (all crops from the mod)",
                "  - modid:block       (only metadata 0 / default variant)",
                "  - modid:block/meta  (only specific metadata)",
                "  - modid:block/*     (all metadata variants of the crop)",
                "  - oreDict           (all crops registered under the exact OreDictionary entry)",
                "  - ore*              (all crops registered under OreDictionary entries starting with the prefix)"
        })
        public String[] farmersBlessingCropBlacklist = new String[]{
                "mod:*",
                "mod:block",
                "mod:block/2",
                "mod:*",
                "ingotIron",
                "ingot*"
        };
        @Config.Name("Immovable")
        public boolean immovable = true;
        @Config.Name("Poison")
        public boolean poison = true;
        @Config.Name("Withering")
        public boolean withering = true;
        @Config.Name("Lightning")
        public boolean lightning = true;
        @Config.Name("Bloodthirst")
        public boolean bloodthirst = true;
    }

    public static class Integration
    {
        @Config.Name("The One Probe Compat")
        public boolean theOneProbeCompat = true;
        @Config.Name("The One Probe Vanilla Tile Entities")
        public boolean theOneProbeVanillaTileEntities = true;
        @Config.Name("The One Probe Items Per Row")
        @RangeInt(min = 0, max = 100)
        public int theOneProbeItemsPerRow = 8;
        @Config.Name("The One Probe Max Rows")
        @RangeInt(min = 1, max = 100)
        public int theOneProbeMaxRows = 10;
    }

    public static class Tooltips
    {
        @Config.Name("OreDict In Tooltips")
        @RangeInt(min = 0, max = 2)
        @Comment({
                "Controls whether OreDict entries are displayed in item tooltips:",
                "0 - Disabled,",
                "1 - Always shown,",
                "2 - Shown only in advanced mode (F3+H)"
        })
        public int oreDictInTooltips = 2;
        @Config.Name("Custom Tooltips")
        public boolean customTooltips = true;
        @Config.Name("Hide Vanilla Attribute Tooltips")
        @Config.Comment("Removes vanilla armor and main-hand attribute tooltip lines when equivalent custom icon stats are enabled")
        public boolean hideVanillaAttributeTooltips = true;
        @Config.Name("Food Stats")
        public boolean foodStats = true;
        @Config.Name("Armor Stats")
        public boolean armorStats = true;
        @Config.Name("Combat Stats")
        public boolean combatStats = true;
        @Config.Name("Mining Stats")
        public boolean miningStats = true;
        @Config.Name("Durability Stats")
        public boolean durabilityStats = true;
        @Config.Name("Fuel Burn Time")
        public boolean fuelBurnTime = true;
        @Config.Name("Custom Tooltips Blacklist")
        @Comment({
                "List of blacklisted items which custom tooltips should NOT be displayed",
                "Supported formats:",
                "  - modid:*           (all items from the mod)",
                "  - modid:item        (only metadata 0 / default variant)",
                "  - modid:item/meta   (only specific metadata)",
                "  - modid:item/*      (all metadata variants of the item)",
                "  - oreDict           (all items registered under the exact OreDictionary entry)",
                "  - ore*              (all items registered under OreDictionary entries starting with the prefix)"
        })
        public String[] customTooltipsBlacklist = new String[]{
                "mod:*",
                "mod:item",
                "mod:item/2",
                "mod:item/*",
                "ingotIron",
                "ingot*"
        };
    }
}