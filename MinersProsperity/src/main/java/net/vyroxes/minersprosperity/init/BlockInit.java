package net.vyroxes.minersprosperity.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.blocks.BlockBase;
import net.vyroxes.minersprosperity.objects.blocks.BlockCharcoal;
import net.vyroxes.minersprosperity.objects.blocks.BlockOreBlocks;
import net.vyroxes.minersprosperity.objects.blocks.BlockOres;
import net.vyroxes.minersprosperity.objects.blocks.machines.*;
import net.vyroxes.minersprosperity.objects.enums.ESolarPanelTier;
import java.util.ArrayList;
import java.util.List;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<>();

	//BLOCKS
	//MACHINE FRAMES
	public static final Block BASIC_MACHINE_FRAME = new BlockBase("basic_machine_frame", Material.IRON, MapColor.STONE);
	public static final Block IMPROVED_MACHINE_FRAME = new BlockBase("improved_machine_frame", Material.IRON, MapColor.STONE);
	public static final Block ADVANCED_MACHINE_FRAME = new BlockBase("advanced_machine_frame", Material.IRON, MapColor.STONE);
	public static final Block ELITE_MACHINE_FRAME = new BlockBase("elite_machine_frame", Material.IRON, MapColor.STONE);
	public static final Block ULTIMATE_MACHINE_FRAME = new BlockBase("ultimate_machine_frame", Material.IRON, MapColor.STONE);
	public static final Block SUPREME_MACHINE_FRAME = new BlockBase("supreme_machine_frame", Material.IRON, MapColor.STONE);

	//MACHINES
	public static final Block ALLOY_FURNACE = new MachineAlloyFurnace("alloy_furnace");
	public static final Block METAL_PRESS = new MachineMetalPress("metal_press");
	public static final Block ELECTRIC_FURNACE = new MachineElectricFurnace("electric_furnace");

	//SOLAR PANELS
	public static final Block BASIC_SOLAR_PANEL = new MachineSolarPanel("basic_solar_panel", ESolarPanelTier.BASIC);
	public static final Block IMPROVED_SOLAR_PANEL = new MachineSolarPanel("improved_solar_panel", ESolarPanelTier.IMPROVED);
	public static final Block ADVANCED_SOLAR_PANEL = new MachineSolarPanel("advanced_solar_panel", ESolarPanelTier.ADVANCED);
	public static final Block ELITE_SOLAR_PANEL = new MachineSolarPanel("elite_solar_panel", ESolarPanelTier.ELITE);
	public static final Block ULTIMATE_SOLAR_PANEL = new MachineSolarPanel("ultimate_solar_panel", ESolarPanelTier.ULTIMATE);
	public static final Block SUPREME_SOLAR_PANEL = new MachineSolarPanel("supreme_solar_panel", ESolarPanelTier.SUPREME);
	public static final Block CREATIVE_SOLAR_PANEL = new MachineSolarPanel("creative_solar_panel", ESolarPanelTier.CREATIVE);

	//ORES
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
	public static final Block RUBY_ORE = new BlockOres("ruby_ore", 2, 4.0F, 11.0F, ItemInit.RUBY, false, false);
	public static final Block SAPPHIRE_ORE = new BlockOres("sapphire_ore", 2, 4.0F, 19.0F, ItemInit.SAPPHIRE, false, false);
	public static final Block AMETHYST_ORE = new BlockOres("amethyst_ore", 2, 4.0F, 14.0F, ItemInit.AMETHYST, false, false);
	public static final Block TOPAZ_ORE = new BlockOres("topaz_ore", 2, 4.0F, 15.0F, ItemInit.TOPAZ, false, false);
	public static final Block PERIDOT_ORE = new BlockOres("peridot_ore", 2, 4.0F, 13.0F, ItemInit.PERIDOT, false, false);
	public static final Block COBALT_ORE = new BlockOres("cobalt_ore", 3, 4.5F, 16.0F, null, false, false);
	public static final Block ONYX_ORE = new BlockOres("onyx_ore", 3, 5.5F, 18.0F, ItemInit.ONYX, false, false);
	public static final Block BLACK_OPAL_ORE = new BlockOres("black_opal_ore", 3, 6.5F, 20.0F, ItemInit.BLACK_OPAL, false, false);

	//NETHER ORES
	public static final Block AZURE_ORE = new BlockOres("azure_ore", 2, 5.0F, 14.0F, ItemInit.AZURE_DUST, true, false);
	public static final Block ROSALITE_ORE = new BlockOres("rosalite_ore", 3, 6.0F, 10.0F, ItemInit.ROSALITE_DUST, true, false);
	public static final Block VERDITE_ORE = new BlockOres("verdite_ore", 3, 7.0F, 18.0F, ItemInit.VERDITE_DUST, true, false);

	//END ORES
	public static final Block VOIDIUM_ORE = new BlockOres("voidium_ore", 3, 6.0F, 8.0F, ItemInit.VOIDIUM, false, true);
	public static final Block LUMINOSITE_ORE = new BlockOres("luminosite_ore", 3, 7.0F, 15.0F, ItemInit.LUMINOSITE_SHARD, false, true);

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
	public static final BlockFluidClassic LIQUID_EXPERIENCE_BLOCK = (BlockFluidClassic) new BlockFluidClassic(FluidInit.LIQUID_EXPERIENCE, Material.WATER).setRegistryName("liquid_experience").setCreativeTab(MinersProsperity.blocks_tab);

	//OTHERS
//	public static final BlockOmniPipe BASIC_OMNIPIPE = new BlockOmniPipe("basic_omnipipe", Material.CIRCUITS, MapColor.BLACK);
}