package net.vyroxes.minersprosperity.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.block.*;
import net.vyroxes.minersprosperity.block.chest.BlockCustomChest;
import net.vyroxes.minersprosperity.block.chest.ItemBlockCustomChest;
import net.vyroxes.minersprosperity.block.machine.*;
import net.vyroxes.minersprosperity.block.chest.ChestTier;
import net.vyroxes.minersprosperity.machine.SolarPanelTier;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<>();

    private static final Map<Block, BooleanSupplier> ENABLE_CONDITIONS = new IdentityHashMap<>();

	//BLOCKS
	//BLOCKS
	public static final Block RESIN_BLOCK = new BlockResin("resin_block");
	public static final Block RESIN_BLOCK_EMPTY = new BlockResinEmpty("resin_block_empty");

	//CHESTS
	public static final Block LEAD_CHEST = new BlockCustomChest("lead_chest", ChestTier.LEAD);
	public static final Block IRON_CHEST = new BlockCustomChest("iron_chest", ChestTier.IRON);
	public static final Block INCONEL_CHEST = new BlockCustomChest("inconel_chest", ChestTier.INCONEL);
	public static final Block GOLD_CHEST = new BlockCustomChest("gold_chest", ChestTier.GOLD);
	public static final Block ELECTRUM_CHEST = new BlockCustomChest("electrum_chest", ChestTier.ELECTRUM);
	public static final Block DIAMOND_CHEST = new BlockCustomChest("diamond_chest", ChestTier.DIAMOND);
	public static final Block OBSIDIAN_CLUSTER_CHEST = new BlockCustomChest("obsidian_cluster_chest", ChestTier.OBSIDIAN_CLUSTER);
	public static final Block EMERALD_CHEST = new BlockCustomChest("emerald_chest", ChestTier.EMERALD);
	public static final Block COBALTSTEEL_CHEST = new BlockCustomChest("cobaltsteel_chest", ChestTier.COBALTSTEEL);
	public static final Block DARK_STEEL_CHEST = new BlockCustomChest("dark_steel_chest", ChestTier.DARK_STEEL);
	public static final Block BLACK_OPAL_CHEST = new BlockCustomChest("black_opal_chest", ChestTier.BLACK_OPAL);

	static {
		ModItems.ITEMS.add(new ItemBlockCustomChest(LEAD_CHEST, ChestTier.LEAD));
		ModItems.ITEMS.add(new ItemBlockCustomChest(IRON_CHEST, ChestTier.IRON));
		ModItems.ITEMS.add(new ItemBlockCustomChest(INCONEL_CHEST, ChestTier.INCONEL));
		ModItems.ITEMS.add(new ItemBlockCustomChest(GOLD_CHEST, ChestTier.GOLD));
		ModItems.ITEMS.add(new ItemBlockCustomChest(ELECTRUM_CHEST, ChestTier.ELECTRUM));
		ModItems.ITEMS.add(new ItemBlockCustomChest(DIAMOND_CHEST, ChestTier.DIAMOND));
		ModItems.ITEMS.add(new ItemBlockCustomChest(OBSIDIAN_CLUSTER_CHEST, ChestTier.OBSIDIAN_CLUSTER));
		ModItems.ITEMS.add(new ItemBlockCustomChest(EMERALD_CHEST, ChestTier.EMERALD));
		ModItems.ITEMS.add(new ItemBlockCustomChest(COBALTSTEEL_CHEST, ChestTier.COBALTSTEEL));
		ModItems.ITEMS.add(new ItemBlockCustomChest(DARK_STEEL_CHEST, ChestTier.DARK_STEEL));
		ModItems.ITEMS.add(new ItemBlockCustomChest(BLACK_OPAL_CHEST, ChestTier.BLACK_OPAL));
	}

	//MACHINE FRAMES
	public static final Block BASIC_MACHINE_FRAME = new BlockBase("basic_machine_frame", Material.IRON, MapColor.STONE, true);
	public static final Block IMPROVED_MACHINE_FRAME = new BlockBase("improved_machine_frame", Material.IRON, MapColor.STONE, true);
	public static final Block ADVANCED_MACHINE_FRAME = new BlockBase("advanced_machine_frame", Material.IRON, MapColor.STONE, true);
	public static final Block ELITE_MACHINE_FRAME = new BlockBase("elite_machine_frame", Material.IRON, MapColor.STONE, true);
	public static final Block ULTIMATE_MACHINE_FRAME = new BlockBase("ultimate_machine_frame", Material.IRON, MapColor.STONE, true);
	public static final Block SUPREME_MACHINE_FRAME = new BlockBase("supreme_machine_frame", Material.IRON, MapColor.STONE, true);

	//MACHINES
	public static final Block ALLOY_FURNACE = new BlockMachineAlloyFurnace("alloy_furnace");
	public static final Block PULVERIZER = new BlockMachinePulverizer("pulverizer");
	public static final Block METAL_PRESS = new BlockMachineMetalPress("metal_press");
	public static final Block ELECTRIC_FURNACE = new BlockMachineElectricFurnace("electric_furnace");

	//SOLAR PANELS
	public static final Block BASIC_SOLAR_PANEL = new BlockMachineSolarPanel("basic_solar_panel", SolarPanelTier.BASIC);
	public static final Block IMPROVED_SOLAR_PANEL = new BlockMachineSolarPanel("improved_solar_panel", SolarPanelTier.IMPROVED);
	public static final Block ADVANCED_SOLAR_PANEL = new BlockMachineSolarPanel("advanced_solar_panel", SolarPanelTier.ADVANCED);
	public static final Block ELITE_SOLAR_PANEL = new BlockMachineSolarPanel("elite_solar_panel", SolarPanelTier.ELITE);
	public static final Block ULTIMATE_SOLAR_PANEL = new BlockMachineSolarPanel("ultimate_solar_panel", SolarPanelTier.ULTIMATE);
	public static final Block SUPREME_SOLAR_PANEL = new BlockMachineSolarPanel("supreme_solar_panel", SolarPanelTier.SUPREME);
	public static final Block CREATIVE_SOLAR_PANEL = new BlockMachineSolarPanel("creative_solar_panel", SolarPanelTier.CREATIVE);

	//ORES
	public static final Block HALITE_ORE = new BlockOres("halite_ore", 1, 2.0F, 3.0F, ModItems.SALT, false, false);
	public static final Block SULFUR_ORE = new BlockOres("sulfur_ore", 1, 2.0F, 5.0F, ModItems.SULFUR, false, false);
	public static final Block GRAPHITE_ORE = new BlockOres("graphite_ore", 1, 2.5F, 4.0F, ModItems.GRAPHITE, false, false);
	public static final Block COPPER_ORE = new BlockOres("copper_ore", 1, 2.5F, 3.0F, null, false, false);
	public static final Block TIN_ORE = new BlockOres("tin_ore", 1, 3.0F, 6.0F, null, false, false);
	public static final Block SILVER_ORE = new BlockOres("silver_ore", 1, 3.0F, 9.0F, null, false, false);
	public static final Block LEAD_ORE = new BlockOres("lead_ore", 1, 3.5F, 5.0F, null, false, false);
	public static final Block ZINC_ORE = new BlockOres("zinc_ore", 1, 3.0F, 7.0F, null, false, false);
	public static final Block ALUMINUM_ORE = new BlockOres("aluminum_ore", 1, 3.0F, 8.0F, null, false, false);
	public static final Block NICKEL_ORE = new BlockOres("nickel_ore", 1, 3.5F, 6.0F, null, false, false);
	public static final Block CHROMIUM_ORE = new BlockOres("chromium_ore", 1, 3.5F, 7.0F, null, false, false);
	public static final Block URANIUM_ORE = new BlockOres("uranium_ore", 2, 4.0F, 15.0F, null, false, false);
	public static final Block TUNGSTEN_ORE = new BlockOres("tungsten_ore", 2, 4.5F, 14.0F, null, false, false);
	public static final Block PLATINUM_ORE = new BlockOres("platinum_ore", 2, 4.5F, 16.0F, null, false, false);
	public static final Block IRIDIUM_ORE = new BlockOres("iridium_ore", 3, 5.5F, 20.0F, null, false, false);
	public static final Block TITANIUM_ORE = new BlockOres("titanium_ore", 2, 4.5F, 17.0F, null, false, false);
	public static final Block RUBY_ORE = new BlockOres("ruby_ore", 2, 4.0F, 11.0F, ModItems.RUBY, false, false);
	public static final Block SAPPHIRE_ORE = new BlockOres("sapphire_ore", 2, 4.0F, 19.0F, ModItems.SAPPHIRE, false, false);
	public static final Block AMETHYST_ORE = new BlockOres("amethyst_ore", 2, 4.0F, 14.0F, ModItems.AMETHYST, false, false);
	public static final Block TOPAZ_ORE = new BlockOres("topaz_ore", 2, 4.0F, 15.0F, ModItems.TOPAZ, false, false);
	public static final Block PERIDOT_ORE = new BlockOres("peridot_ore", 2, 4.0F, 13.0F, ModItems.PERIDOT, false, false);
	public static final Block COBALT_ORE = new BlockOres("cobalt_ore", 3, 4.5F, 16.0F, null, false, false);
	public static final Block ONYX_ORE = new BlockOres("onyx_ore", 3, 5.5F, 18.0F, ModItems.ONYX, false, false);
	public static final Block BLACK_OPAL_ORE = new BlockOres("black_opal_ore", 3, 6.5F, 20.0F, ModItems.BLACK_OPAL, false, false);

	//NETHER ORES
	public static final Block AZURE_ORE = new BlockOres("azure_ore", 2, 5.0F, 14.0F, ModItems.AZURE_DUST, true, false);
	public static final Block ROSALITE_ORE = new BlockOres("rosalite_ore", 3, 6.0F, 10.0F, ModItems.ROSALITE_DUST, true, false);
	public static final Block VERDITE_ORE = new BlockOres("verdite_ore", 3, 7.0F, 18.0F, ModItems.VERDITE_DUST, true, false);

	//END ORES
	public static final Block VOIDIUM_ORE = new BlockOres("voidium_ore", 3, 6.0F, 8.0F, ModItems.VOIDIUM, false, true);
	public static final Block LUMINOSITE_ORE = new BlockOres("luminosite_ore", 3, 7.0F, 15.0F, ModItems.LUMINOSITE_SHARD, false, true);

	//OREBLOCKS
	public static final Block CHARCOAL_BLOCK = new BlockCharcoal("charcoal_block");
	public static final Block COPPER_BLOCK = new BlockOreBlocks("copper_block", MapColor.ORANGE_STAINED_HARDENED_CLAY, 2, 5.0F, 6.0F);
	public static final Block TIN_BLOCK = new BlockOreBlocks("tin_block", MapColor.IRON, 2, 6.0F, 12.0F);
	public static final Block BRONZE_BLOCK = new BlockOreBlocks("bronze_block", MapColor.ORANGE_STAINED_HARDENED_CLAY, 2, 5.5F, 12.0F);
	public static final Block SILVER_BLOCK = new BlockOreBlocks("silver_block", MapColor.SILVER, 2, 6.0F, 18.0F);
	public static final Block LEAD_BLOCK = new BlockOreBlocks("lead_block", MapColor.LIGHT_BLUE, 2, 7.0F, 10.0F);
	public static final Block ZINC_BLOCK = new BlockOreBlocks("zinc_block", MapColor.IRON, 2, 6.0F, 14.0F);
	public static final Block BRASS_BLOCK = new BlockOreBlocks("brass_block", MapColor.YELLOW, 2, 5.5F, 13.0F);
	public static final Block ALUMINUM_BLOCK = new BlockOreBlocks("aluminum_block", MapColor.IRON, 2, 6.0F, 16.0F);
	public static final Block DURALUMINUM_BLOCK = new BlockOreBlocks("duraluminum_block", MapColor.IRON, 2, 6.5F, 14.0F);
	public static final Block REFINED_IRON_BLOCK = new BlockOreBlocks("refined_iron_block", MapColor.IRON, 2, 6.0F, 12.0F);
	public static final Block STEEL_BLOCK = new BlockOreBlocks("steel_block", MapColor.STONE, 2, 7.0F, 16.0F);
	public static final Block NICKEL_BLOCK = new BlockOreBlocks("nickel_block", MapColor.IRON, 2, 7.0F, 12.0F);
	public static final Block CHROMIUM_BLOCK = new BlockOreBlocks("chromium_block", MapColor.IRON, 2, 7.0F, 14.0F);
	public static final Block INCONEL_BLOCK = new BlockOreBlocks("inconel_block", MapColor.IRON, 2, 7.5F, 22.0F);
	public static final Block URANIUM_BLOCK = new BlockOreBlocks("uranium_block", MapColor.LIME, 2, 8.0F, 30.0F);
	public static final Block TUNGSTEN_BLOCK = new BlockOreBlocks("tungsten_block", MapColor.GREEN, 2, 9.0F, 28.0F);
	public static final Block PLATINUM_BLOCK = new BlockOreBlocks("platinum_block", MapColor.LIGHT_BLUE, 2, 9.0F, 32.0F);
	public static final Block WHITE_GOLD_BLOCK = new BlockOreBlocks("white_gold_block", MapColor.IRON, 2, 9.5F, 26.0F);
	public static final Block ELECTRUM_BLOCK = new BlockOreBlocks("electrum_block", MapColor.YELLOW, 2, 9.5F, 30.0F);
	public static final Block IRIDIUM_BLOCK = new BlockOreBlocks("iridium_block", MapColor.BROWN, 3, 11.0F, 40.0F);
	public static final Block TITANIUM_BLOCK = new BlockOreBlocks("titanium_block", MapColor.GRAY, 2, 9.0F, 34.0F);
	public static final Block RUBY_BLOCK = new BlockOreBlocks("ruby_block", MapColor.RED, 2, 8.0F, 22.0F);
	public static final Block SAPPHIRE_BLOCK = new BlockOreBlocks("sapphire_block", MapColor.BLUE, 2, 8.0F, 38.0F);
	public static final Block AMETHYST_BLOCK = new BlockOreBlocks("amethyst_block", MapColor.PURPLE, 2, 8.0F, 28.0F);
	public static final Block TOPAZ_BLOCK = new BlockOreBlocks("topaz_block", MapColor.YELLOW, 2, 8.0F, 30.0F);
	public static final Block PERIDOT_BLOCK = new BlockOreBlocks("peridot_block", MapColor.LIME, 2, 8.0F, 26.0F);
	public static final Block REFINED_OBSIDIAN_BLOCK = new BlockOreBlocks("refined_obsidian_block", MapColor.PURPLE, 2, 70.0F, 1700.0F);
	public static final Block COBALT_BLOCK = new BlockOreBlocks("cobalt_block", MapColor.BLUE, 3, 9.0F, 32.0F);
	public static final Block COBALTSTEEL_BLOCK = new BlockOreBlocks("cobaltsteel_block", MapColor.LIGHT_BLUE, 3, 8.0F, 28.0F);
	public static final Block ONYX_BLOCK = new BlockOreBlocks("onyx_block", MapColor.BLACK, 3, 11.0F, 36.0F);
	public static final Block DARK_STEEL_BLOCK = new BlockOreBlocks("dark_steel_block", MapColor.GRAY, 3, 10.0F, 32.0F);
	public static final Block BLACK_OPAL_BLOCK = new BlockOreBlocks("black_opal_block", MapColor.BLACK, 3, 13.0F, 40.0F);
	public static final Block AZURE_BLOCK = new BlockOreBlocks("azure_block", MapColor.BLUE, 2, 10.0F, 28.0F);
	public static final Block ROSALITE_BLOCK = new BlockOreBlocks("rosalite_block", MapColor.PINK, 3, 12.0F, 20.0F);
	public static final Block VERDITE_BLOCK = new BlockOreBlocks("verdite_block", MapColor.GREEN, 3, 14.0F, 36.0F);
	public static final Block VOIDIUM_BLOCK = new BlockOreBlocks("voidium_block", MapColor.PURPLE, 3, 12.0F, 16.0F);
	public static final Block LUMINOSITE_BLOCK = new BlockOreBlocks("luminosite_block", MapColor.WHITE_STAINED_HARDENED_CLAY, 3, 14.0F, 30.0F);

	//FLUIDBLOCKS
    public static final BlockFluidClassic LIQUID_EXPERIENCE_BLOCK = createFluidBlock("liquid_experience", ModFluids.LIQUID_EXPERIENCE);
    public static final BlockFluidClassic CRUDE_OIL_BLOCK = createFluidBlock("crude_oil", ModFluids.CRUDE_OIL);

    private static BlockFluidClassic createFluidBlock(String name, Fluid fluid)
    {
        if (fluid == null) throw new IllegalStateException("Fluid '" + name + "' was used before ModFluids.register()");

        return (BlockFluidClassic) new BlockFluidClassic(fluid, Material.WATER).setRegistryName(new ResourceLocation(Tags.MODID, name)).setTranslationKey(name).setCreativeTab(MinersProsperity.blocks_tab);
    }

    private static <T extends Block> T configurable(T block, BooleanSupplier condition)
    {
        ENABLE_CONDITIONS.put(block, condition);
        return block;
    }

    public static boolean isEnabled(Block block)
    {
        BooleanSupplier condition = ENABLE_CONDITIONS.get(block);
        return condition == null || condition.getAsBoolean();
    }
}