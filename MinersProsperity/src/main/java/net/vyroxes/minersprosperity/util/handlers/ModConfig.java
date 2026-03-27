package net.vyroxes.minersprosperity.util.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.vyroxes.minersprosperity.Tags;

@Config(modid = Tags.MODID)
public class ModConfig
{
    @Config.Name("Ores")
    @Comment("Ores generation settings")
    public static Ores ORES = new Ores();
    @Config.Name("Machines")
    @Comment("Machine settings")
    public static Machines MACHINES = new Machines();
    @Config.Name("Backpacks")
    @Comment("Backpack settings")
    public static Backpacks BACKPACKS = new Backpacks();
    @Config.Name("Others")
    @Comment("Other settings")
    public static Other OTHER = new Other();
    @Config.Name("GUI")
    @Comment("GUI overlay settings")
    public static GUI GUI = new GUI();
    @Config.Name("Enchantments")
    @Comment("Enchantment settings")
    public static Enchantments ENCHANTMENTS = new Enchantments();
    @Config.Name("TheOneProbe")
    @Comment("TheOneProbe settings")
    public static TOP TOP = new TOP();
    @Config.Name("Tooltips")
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
                " - Nether ores need NETHERRACK",
                " - End ores need END_STONE"
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
                "Example: 1 = End (to disable ore generation there)"
        })
        public Integer[] dimensionsBlacklist = new Integer[]{};
        @Config.Name("Generate Copper Ore")
        public boolean daGenerateCopperOre = true;
        @Config.Name("Minimum Height Copper Ore")
        @RangeInt(min = 0, max = 255)
        public int eaMinHeightCopperOre = 0;
        @Config.Name("Maximum Height Copper Ore")
        @RangeInt(min = 0, max = 255)
        public int faMaxHeightCopperOre = 64;
        @Config.Name("Cluster Count Copper Ore")
        @RangeInt(min = 1, max = 100)
        public int gaClusterCountCopperOre = 16;
        @Config.Name("Cluster Size Copper Ore")
        @RangeInt(min = 3, max = 100)
        public int haClusterSizeCopperOre = 9;

        @Config.Name("Generate Tin Ore")
        public boolean iaGenerateTinOre = true;
        @Config.Name("Minimum Height Tin Ore")
        @RangeInt(min = 0, max = 255)
        public int jaMinHeightTinOre = 0;
        @Config.Name("Maximum Height Tin Ore")
        @RangeInt(min = 0, max = 255)
        public int kaMaxHeightTinOre = 64;
        @Config.Name("Cluster Count Tin Ore")
        @RangeInt(min = 1, max = 100)
        public int laClusterCountTinOre = 14;
        @Config.Name("Cluster Size Tin Ore")
        @RangeInt(min = 3, max = 100)
        public int maClusterSizeTinOre = 8;

        @Config.Name("Generate Silver Ore")
        public boolean naGenerateSilverOre = true;
        @Config.Name("Minimum Height Silver Ore")
        @RangeInt(min = 0, max = 255)
        public int oaMinHeightSilverOre = 0;
        @Config.Name("Maximum Height Silver Ore")
        @RangeInt(min = 0, max = 255)
        public int paMaxHeightSilverOre = 48;
        @Config.Name("Cluster Count Silver Ore")
        @RangeInt(min = 1, max = 100)
        public int qaClusterCountSilverOre = 8;
        @Config.Name("Cluster Size Silver Ore")
        @RangeInt(min = 3, max = 100)
        public int raClusterSizeSilverOre = 7;

        @Config.Name("Generate Lead Ore")
        public boolean saGenerateLeadOre = true;
        @Config.Name("Minimum Height Lead Ore")
        @RangeInt(min = 0, max = 255)
        public int taMinHeightLeadOre = 0;
        @Config.Name("Maximum Height Lead Ore")
        @RangeInt(min = 0, max = 255)
        public int uaMaxHeightLeadOre = 48;
        @Config.Name("Cluster Count Lead Ore")
        @RangeInt(min = 1, max = 100)
        public int vaClusterCountLeadOre = 7;
        @Config.Name("Cluster Size Lead Ore")
        @RangeInt(min = 3, max = 100)
        public int waClusterSizeLeadOre = 7;

        @Config.Name("Generate Zinc Ore")
        public boolean xaGenerateZincOre = true;
        @Config.Name("Minimum Height Zinc Ore")
        @RangeInt(min = 0, max = 255)
        public int yaMinHeightZincOre = 0;
        @Config.Name("Maximum Height Zinc Ore")
        @RangeInt(min = 0, max = 255)
        public int zaMaxHeightZincOre = 64;
        @Config.Name("Cluster Count Zinc Ore")
        @RangeInt(min = 1, max = 100)
        public int abClusterCountZincOre = 13;
        @Config.Name("Cluster Size Zinc Ore")
        @RangeInt(min = 3, max = 100)
        public int bbClusterSizeZincOre = 7;

        @Config.Name("Generate Aluminum Ore")
        public boolean cbGenerateAluminumOre = true;
        @Config.Name("Minimum Height Aluminum Ore")
        @RangeInt(min = 0, max = 255)
        public int dbMinHeightAluminumOre = 0;
        @Config.Name("Maximum Height Aluminum Ore")
        @RangeInt(min = 0, max = 255)
        public int ebMaxHeightAluminumOre = 64;
        @Config.Name("Cluster Count Aluminum Ore")
        @RangeInt(min = 1, max = 100)
        public int fbClusterCountAluminumOre = 12;
        @Config.Name("Cluster Size Aluminum Ore")
        @RangeInt(min = 3, max = 100)
        public int gbClusterSizeAluminumOre = 7;

        @Config.Name("Generate Nickel Ore")
        public boolean hbGenerateNickelOre = true;
        @Config.Name("Minimum Height Nickel Ore")
        @RangeInt(min = 0, max = 255)
        public int ibMinHeightNickelOre = 0;
        @Config.Name("Maximum Height Nickel Ore")
        @RangeInt(min = 0, max = 255)
        public int jbMaxHeightNickelOre = 32;
        @Config.Name("Cluster Count Nickel Ore")
        @RangeInt(min = 1, max = 100)
        public int kbClusterCountNickelOre = 5;
        @Config.Name("Cluster Size Nickel Ore")
        @RangeInt(min = 3, max = 100)
        public int lbClusterSizeNickelOre = 6;

        @Config.Name("Generate Chromium Ore")
        public boolean mbGenerateChromiumOre = true;
        @Config.Name("Minimum Height Chromium Ore")
        @RangeInt(min = 0, max = 255)
        public int nbMinHeightChromiumOre = 0;
        @Config.Name("Maximum Height Chromium Ore")
        @RangeInt(min = 0, max = 255)
        public int obMaxHeightChromiumOre = 32;
        @Config.Name("Cluster Count Chromium Ore")
        @RangeInt(min = 1, max = 100)
        public int pbClusterCountChromiumOre = 5;
        @Config.Name("Cluster Size Chromium Ore")
        @RangeInt(min = 3, max = 100)
        public int qbClusterSizeChromiumOre = 6;

        @Config.Name("Generate Uranium Ore")
        public boolean rbGenerateUraniumOre = true;
        @Config.Name("Minimum Height Uranium Ore")
        @RangeInt(min = 0, max = 255)
        public int sbMinHeightUraniumOre = 0;
        @Config.Name("Maximum Height Uranium Ore")
        @RangeInt(min = 0, max = 255)
        public int tbMaxHeightUraniumOre = 24;
        @Config.Name("Cluster Count Uranium Ore")
        @RangeInt(min = 1, max = 100)
        public int ubClusterCountUraniumOre = 3;
        @Config.Name("Cluster Size Uranium Ore")
        @RangeInt(min = 3, max = 100)
        public int vbClusterSizeUraniumOre = 5;

        @Config.Name("Generate Tungsten Ore")
        public boolean wbGenerateTungstenOre = true;
        @Config.Name("Minimum Height Tungsten Ore")
        @RangeInt(min = 0, max = 255)
        public int xbMinHeightTungstenOre = 0;
        @Config.Name("Maximum Height Tungsten Ore")
        @RangeInt(min = 0, max = 255)
        public int ybMaxHeightTungstenOre = 24;
        @Config.Name("Cluster Count Tungsten Ore")
        @RangeInt(min = 1, max = 100)
        public int zbClusterCountTungstenOre = 3;
        @Config.Name("Cluster Size Tungsten Ore")
        @RangeInt(min = 3, max = 100)
        public int acClusterSizeTungstenOre = 5;

        @Config.Name("Generate Platinum Ore")
        public boolean bcGeneratePlatinumOre = true;
        @Config.Name("Minimum Height Platinum Ore")
        @RangeInt(min = 0, max = 255)
        public int ccMinHeightPlatinumOre = 0;
        @Config.Name("Maximum Height Platinum Ore")
        @RangeInt(min = 0, max = 255)
        public int dcMaxHeightPlatinumOre = 16;
        @Config.Name("Cluster Count Platinum Ore")
        @RangeInt(min = 1, max = 100)
        public int ecClusterCountPlatinumOre = 3;
        @Config.Name("Cluster Size Platinum Ore")
        @RangeInt(min = 3, max = 100)
        public int fcClusterSizePlatinumOre = 4;

        @Config.Name("Generate Iridium Ore")
        public boolean gcGenerateIridiumOre = true;
        @Config.Name("Minimum Height Iridium Ore")
        @RangeInt(min = 0, max = 255)
        public int hcMinHeightIridiumOre = 0;
        @Config.Name("Maximum Height Iridium Ore")
        @RangeInt(min = 0, max = 255)
        public int icMaxHeightIridiumOre = 16;
        @Config.Name("Cluster Count Iridium Ore")
        @RangeInt(min = 1, max = 100)
        public int jcClusterCountIridiumOre = 3;
        @Config.Name("Cluster Size Iridium Ore")
        @RangeInt(min = 3, max = 100)
        public int kcClusterSizeIridiumOre = 4;

        @Config.Name("Generate Titanium Ore")
        public boolean lcGenerateTitaniumOre = true;
        @Config.Name("Minimum Height Titanium Ore")
        @RangeInt(min = 0, max = 255)
        public int mcMinHeightTitaniumOre = 0;
        @Config.Name("Maximum Height Titanium Ore")
        @RangeInt(min = 0, max = 255)
        public int ncMaxHeightTitaniumOre = 16;
        @Config.Name("Cluster Count Titanium Ore")
        @RangeInt(min = 1, max = 100)
        public int ocClusterCountTitaniumOre = 3;
        @Config.Name("Cluster Size Titanium Ore")
        @RangeInt(min = 3, max = 100)
        public int pcClusterSizeTitaniumOre = 4;

        @Config.Name("Generate Ruby Ore")
        public boolean qcGenerateRubyOre = true;
        @Config.Name("Minimum Height Ruby Ore")
        @RangeInt(min = 0, max = 255)
        public int rcMinHeightRubyOre = 0;
        @Config.Name("Maximum Height Ruby Ore")
        @RangeInt(min = 0, max = 255)
        public int scMaxHeightRubyOre = 16;
        @Config.Name("Cluster Count Ruby Ore")
        @RangeInt(min = 1, max = 100)
        public int tcClusterCountRubyOre = 2;
        @Config.Name("Cluster Size Ruby Ore")
        @RangeInt(min = 3, max = 100)
        public int ucClusterSizeRubyOre = 4;

        @Config.Name("Generate Sapphire Ore")
        public boolean vcGenerateSapphireOre = true;
        @Config.Name("Minimum Height Sapphire Ore")
        @RangeInt(min = 0, max = 255)
        public int wcMinHeightSapphireOre = 0;
        @Config.Name("Maximum Height Sapphire Ore")
        @RangeInt(min = 0, max = 255)
        public int xcMaxHeightSapphireOre = 16;
        @Config.Name("Cluster Count Sapphire Ore")
        @RangeInt(min = 1, max = 100)
        public int ycClusterCountSapphireOre = 2;
        @Config.Name("Cluster Size Sapphire Ore")
        @RangeInt(min = 3, max = 100)
        public int zcClusterSizeSapphireOre = 4;

        @Config.Name("Generate Amethyst Ore")
        public boolean adGenerateAmethystOre = true;
        @Config.Name("Minimum Height Amethyst Ore")
        @RangeInt(min = 0, max = 255)
        public int bdMinHeightAmethystOre = 0;
        @Config.Name("Maximum Height Amethyst Ore")
        @RangeInt(min = 0, max = 255)
        public int cdMaxHeightAmethystOre = 16;
        @Config.Name("Cluster Count Amethyst Ore")
        @RangeInt(min = 1, max = 100)
        public int ddClusterCountAmethystOre = 2;
        @Config.Name("Cluster Size Amethyst Ore")
        @RangeInt(min = 3, max = 100)
        public int edClusterSizeAmethystOre = 4;

        @Config.Name("Generate Topaz Ore")
        public boolean fdGenerateTopazOre = true;
        @Config.Name("Minimum Height Topaz Ore")
        @RangeInt(min = 0, max = 255)
        public int gdMinHeightTopazOre = 0;
        @Config.Name("Maximum Height Topaz Ore")
        @RangeInt(min = 0, max = 255)
        public int hdMaxHeightTopazOre = 16;
        @Config.Name("Cluster Count Topaz Ore")
        @RangeInt(min = 1, max = 100)
        public int idClusterCountTopazOre = 2;
        @Config.Name("Cluster Size Topaz Ore")
        @RangeInt(min = 3, max = 100)
        public int jdClusterSizeTopazOre = 4;

        @Config.Name("Generate Peridot Ore")
        public boolean kdGeneratePeridotOre = true;
        @Config.Name("Minimum Height Peridot Ore")
        @RangeInt(min = 0, max = 255)
        public int ldMinHeightPeridotOre = 0;
        @Config.Name("Maximum Height Peridot Ore")
        @RangeInt(min = 0, max = 255)
        public int mdMaxHeightPeridotOre = 16;
        @Config.Name("Cluster Count Peridot Ore")
        @RangeInt(min = 1, max = 100)
        public int ndClusterCountPeridotOre = 2;
        @Config.Name("Cluster Size Peridot Ore")
        @RangeInt(min = 3, max = 100)
        public int odClusterSizePeridotOre = 4;

        @Config.Name("Generate Cobalt Ore")
        public boolean pdGenerateCobaltOre = true;
        @Config.Name("Minimum Height Cobalt Ore")
        @RangeInt(min = 0, max = 255)
        public int qdMinHeightCobaltOre = 0;
        @Config.Name("Maximum Height Cobalt Ore")
        @RangeInt(min = 0, max = 255)
        public int rdMaxHeightCobaltOre = 24;
        @Config.Name("Cluster Count Cobalt Ore")
        @RangeInt(min = 1, max = 100)
        public int sdClusterCountCobaltOre = 2;
        @Config.Name("Cluster Size Cobalt Ore")
        @RangeInt(min = 3, max = 100)
        public int tdClusterSizeCobaltOre = 3;

        @Config.Name("Generate Onyx Ore")
        public boolean udGenerateOnyxOre = true;
        @Config.Name("Minimum Height Onyx Ore")
        @RangeInt(min = 0, max = 255)
        public int vdMinHeightOnyxOre = 0;
        @Config.Name("Maximum Height Onyx Ore")
        @RangeInt(min = 0, max = 255)
        public int wdMaxHeightOnyxOre = 16;
        @Config.Name("Cluster Count Onyx Ore")
        @RangeInt(min = 1, max = 100)
        public int xdClusterCountOnyxOre = 1;
        @Config.Name("Cluster Size Onyx Ore")
        @RangeInt(min = 3, max = 100)
        public int ydClusterSizeOnyxOre = 4;

        @Config.Name("Generate Black Opal Ore")
        public boolean zdGenerateBlackOpalOre = true;
        @Config.Name("Minimum Height Black Opal Ore")
        @RangeInt(min = 0, max = 255)
        public int aeMinHeightBlackOpalOre = 0;
        @Config.Name("Maximum Height Black Opal Ore")
        @RangeInt(min = 0, max = 255)
        public int beMaxHeightBlackOpalOre = 16;
        @Config.Name("Cluster Count Black Opal Ore")
        @RangeInt(min = 1, max = 100)
        public int ceClusterCountBlackOpalOre = 1;
        @Config.Name("Cluster Size Black Opal Ore")
        @RangeInt(min = 3, max = 100)
        public int deClusterSizeBlackOpalOre = 3;

        @Config.Name("Generate Azure Ore")
        public boolean eeGenerateAzureOre = true;
        @Config.Name("Minimum Height Azure Ore")
        @RangeInt(min = 0, max = 255)
        public int feMinHeightAzureOre = 0;
        @Config.Name("Maximum Height Azure Ore")
        @RangeInt(min = 0, max = 255)
        public int geMaxHeightAzureOre = 64;
        @Config.Name("Cluster Count Azure Ore")
        @RangeInt(min = 1, max = 100)
        public int heClusterCountAzureOre = 3;
        @Config.Name("Cluster Size Azure Ore")
        @RangeInt(min = 3, max = 100)
        public int ieClusterSizeAzureOre = 5;

        @Config.Name("Generate Rosalite Ore")
        public boolean jeGenerateRosaliteOre = true;
        @Config.Name("Minimum Height Rosalite Ore")
        @RangeInt(min = 0, max = 255)
        public int keMinHeightRosaliteOre = 0;
        @Config.Name("Maximum Height Rosalite Ore")
        @RangeInt(min = 0, max = 255)
        public int leMaxHeightRosaliteOre = 64;
        @Config.Name("Cluster Count Rosalite Ore")
        @RangeInt(min = 1, max = 100)
        public int meClusterCountRosaliteOre = 3;
        @Config.Name("Cluster Size Rosalite Ore")
        @RangeInt(min = 3, max = 100)
        public int neClusterSizeRosaliteOre = 4;

        @Config.Name("Generate Verdite Ore")
        public boolean oeGenerateVerditeOre = true;
        @Config.Name("Minimum Height Verdite Ore")
        @RangeInt(min = 0, max = 255)
        public int peMinHeightVerditeOre = 0;
        @Config.Name("Maximum Height Verdite Ore")
        @RangeInt(min = 0, max = 255)
        public int qeMaxHeightVerditeOre = 64;
        @Config.Name("Cluster Count Verdite Ore")
        @RangeInt(min = 1, max = 100)
        public int reClusterCountVerditeOre = 2;
        @Config.Name("Cluster Size Verdite Ore")
        @RangeInt(min = 3, max = 100)
        public int seClusterSizeVerditeOre = 4;

        @Config.Name("Generate Voidium Ore")
        public boolean teGenerateVoidiumOre = true;
        @Config.Name("Minimum Height Voidium Ore")
        @RangeInt(min = 0, max = 255)
        public int ueMinHeightVoidiumOre = 0;
        @Config.Name("Maximum Height Voidium Ore")
        @RangeInt(min = 0, max = 255)
        public int veMaxHeightVoidiumOre = 64;
        @Config.Name("Cluster Count Voidium Ore")
        @RangeInt(min = 1, max = 100)
        public int weClusterCountVoidiumOre = 3;
        @Config.Name("Cluster Size Voidium Ore")
        @RangeInt(min = 3, max = 100)
        public int xeClusterSizeVoidiumOre = 4;

        @Config.Name("Generate Luminosite Ore")
        public boolean yeGenerateLuminositeOre = true;
        @Config.Name("Minimum Height Luminosite Ore")
        @RangeInt(min = 0, max = 255)
        public int zeMinHeightLuminositeOre = 0;
        @Config.Name("Maximum Height Luminosite Ore")
        @RangeInt(min = 0, max = 255)
        public int afMaxHeightLuminositeOre = 64;
        @Config.Name("Cluster Count Luminosite Ore")
        @RangeInt(min = 1, max = 100)
        public int bfClusterCountLuminositeOre = 2;
        @Config.Name("Cluster Size Luminosite Ore")
        @RangeInt(min = 3, max = 100)
        public int cfClusterSizeLuminositeOre = 4;
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
        @Config.Name("Electric Furnace Base Energy Usage")
        @RangeInt(min = 1, max = 1000)
        public int electricFurnaceBaseEnergyUsage = 40;
        @Config.Name("Electric Furnace Base Cook Time")
        @RangeInt(min = 1, max = 1000)
        public int electricFurnaceBaseCookTime = 200;
    }

    public static class Backpacks
    {
        @Config.Name("Allow Backpack Looping")
        public boolean allowBackpackLooping = false;
        @Config.Name("Backpack Item Blacklist")
        @Comment({
                "List of blacklisted items in backpacks",
                "Supported formats:",
                "  - modid:item        (all variants of this item)",
                "  - modid:item/meta   (only specific metadata)"
        })
        public String[] backpackItemBlacklist = new String[]{
                "modid:example_item/0",
                "mod:item",
                "mod:item/2"
        };
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
        @Comment("Colors for armor/toughness 21-40")
        public Color color21to40 = new Color(1.0f, 1.0f, 0.0f);
        @Config.Name("Armor/Toughness Color 41-60")
        @Comment("Colors for armor/toughness 41-60")
        public Color color41to60 = new Color(0.1f, 0.7f, 1.0f);
        @Config.Name("Armor/Toughness Color 61-80")
        @Comment("Colors for armor/toughness 61-80")
        public Color color61to80 = new Color(0.0f, 0.8f, 0.3f);
        @Config.Name("Armor/Toughness Color 81-100")
        @Comment("Colors for armor/toughness 81-100")
        public Color color81to100 = new Color(0.6f, 0.0f, 0.8f);
        @Config.Name("Armor/Toughness Color 101-120")
        @Comment("Colors for armor/toughness 101-120")
        public Color color101to120 = new Color(1.0f, 0.0f, 0.0f);
        @Config.Name("Armor/Toughness Color 121-140")
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
                "  - modid:block        (all variants of this crop)",
                "  - modid:block/meta   (only specific metadata)",
                "  - modid:*            (ALL crops from that mod are blacklisted)"
        })
        public String[] farmersBlessingCropBlacklist = new String[]{
                "modid:example_block/0",
                "mod:block",
                "mod:*"
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

    public static class TOP
    {
        @Config.Name("TheOneProbe Compat")
        public boolean theOneProbeCompat = true;
        @Config.Name("Vanilla Tile Entities")
        public boolean vanillaTileEntities = true;
        @Config.Name("Items Per Row")
        @RangeInt(min = 0, max = 100)
        public int itemsPerRow = 8;
        @Config.Name("Max Rows")
        @RangeInt(min = 1, max = 100)
        public int maxRows = 10;
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
    }
}