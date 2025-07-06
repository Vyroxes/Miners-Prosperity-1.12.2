package net.vyroxes.minersprosperity.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.blocks.BlockCharcoal;
import net.vyroxes.minersprosperity.objects.blocks.BlockInvisibleLight;
import net.vyroxes.minersprosperity.objects.blocks.BlockOreBlocks;
import net.vyroxes.minersprosperity.objects.blocks.BlockOres;
import net.vyroxes.minersprosperity.objects.blocks.machines.*;

import java.util.ArrayList;
import java.util.List;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<>();

	//BLOCKS
	//MACHINES
	public static final Block ALLOY_FURNACE = new MachineAlloyFurnace("alloy_furnace");
	public static final Block BASIC_SOLAR_PANEL = new MachineBasicSolarPanel("basic_solar_panel");
	public static final Block IMPROVED_SOLAR_PANEL = new MachineImprovedSolarPanel("improved_solar_panel");
	public static final Block ADVANCED_SOLAR_PANEL = new MachineAdvancedSolarPanel("advanced_solar_panel");
	public static final Block ELITE_SOLAR_PANEL = new MachineEliteSolarPanel("elite_solar_panel");
	public static final Block ULTIMATE_SOLAR_PANEL = new MachineUltimateSolarPanel("ultimate_solar_panel");
	public static final Block SUPREME_SOLAR_PANEL = new MachineSupremeSolarPanel("supreme_solar_panel");
	public static final Block CREATIVE_SOLAR_PANEL = new MachineCreativeSolarPanel("creative_solar_panel");

	//ORES
	public static final Block COPPER_ORE = new BlockOres("copper_ore", 1, 5.0F, 10.0F, null);
	public static final Block TIN_ORE = new BlockOres("tin_ore", 1, 5.0F, 10.0F, null);
	public static final Block SILVER_ORE = new BlockOres("silver_ore", 1, 5.0F, 10.0F, null);
	public static final Block LEAD_ORE = new BlockOres("lead_ore", 1, 5.0F, 10.0F, null);
	public static final Block ZINC_ORE = new BlockOres("zinc_ore", 1, 5.0F, 10.0F, null);
	public static final Block ALUMINUM_ORE = new BlockOres("aluminum_ore", 1, 5.0F, 10.0F, null);
	public static final Block NICKEL_ORE = new BlockOres("nickel_ore", 1, 5.0F, 10.0F, null);
	public static final Block CHROMIUM_ORE = new BlockOres("chromium_ore", 1, 5.0F, 10.0F, null);
	public static final Block URANIUM_ORE = new BlockOres("uranium_ore", 1, 5.0F, 10.0F, null);
	public static final Block TUNGSTEN_ORE = new BlockOres("tungsten_ore", 1, 5.0F, 10.0F, null);
	public static final Block PLATINUM_ORE = new BlockOres("platinum_ore", 1, 5.0F, 10.0F, null);
	public static final Block IRIDIUM_ORE = new BlockOres("iridium_ore", 1, 5.0F, 10.0F, null);
	public static final Block TITANIUM_ORE = new BlockOres("titanium_ore", 1, 5.0F, 10.0F, null);
	public static final Block RUBY_ORE = new BlockOres("ruby_ore", 1, 5.0F, 10.0F, ItemInit.RUBY);
	public static final Block SAPPHIRE_ORE = new BlockOres("sapphire_ore", 1, 5.0F, 10.0F, ItemInit.SAPPHIRE);
	public static final Block AMETHYST_ORE = new BlockOres("amethyst_ore", 1, 5.0F, 10.0F, ItemInit.AMETHYST);
	public static final Block TOPAZ_ORE = new BlockOres("topaz_ore", 1, 5.0F, 10.0F, ItemInit.TOPAZ);
	public static final Block PERIDOT_ORE = new BlockOres("peridot_ore", 1, 5.0F, 10.0F, ItemInit.PERIDOT);
	public static final Block COBALT_ORE = new BlockOres("cobalt_ore", 1, 5.0F, 10.0F, null);
	public static final Block ONYX_ORE = new BlockOres("onyx_ore", 1, 5.0F, 10.0F, ItemInit.ONYX);
	public static final Block BLACK_OPAL_ORE = new BlockOres("black_opal_ore", 1, 5.0F, 10.0F, ItemInit.BLACK_OPAL);

	//OREBLOCKS
	public static final Block CHARCOAL_BLOCK = new BlockCharcoal("charcoal_block");
	public static final Block COPPER_BLOCK = new BlockOreBlocks("copper_block", MapColor.ORANGE_STAINED_HARDENED_CLAY, 1, 5.0F, 10.0F);
	public static final Block TIN_BLOCK = new BlockOreBlocks("tin_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block BRONZE_BLOCK = new BlockOreBlocks("bronze_block", MapColor.BROWN, 1, 5.0F, 10.0F);
	public static final Block SILVER_BLOCK = new BlockOreBlocks("silver_block", MapColor.SILVER, 1, 5.0F, 10.0F);
	public static final Block LEAD_BLOCK = new BlockOreBlocks("lead_block", MapColor.LIGHT_BLUE, 1, 5.0F, 10.0F);
	public static final Block ZINC_BLOCK = new BlockOreBlocks("zinc_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block BRASS_BLOCK = new BlockOreBlocks("brass_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block ALUMINUM_BLOCK = new BlockOreBlocks("aluminum_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block DURALUMINUM_BLOCK = new BlockOreBlocks("duraluminum_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block REFINED_IRON_BLOCK = new BlockOreBlocks("refined_iron_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block STEEL_BLOCK = new BlockOreBlocks("steel_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block NICKEL_BLOCK = new BlockOreBlocks("nickel_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block CHROMIUM_BLOCK = new BlockOreBlocks("chromium_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block INCONEL_BLOCK = new BlockOreBlocks("inconel_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block URANIUM_BLOCK = new BlockOreBlocks("uranium_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block TUNGSTEN_BLOCK = new BlockOreBlocks("tungsten_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block PLATINUM_BLOCK = new BlockOreBlocks("platinum_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block WHITE_GOLD_BLOCK = new BlockOreBlocks("white_gold_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block ELECTRUM_BLOCK = new BlockOreBlocks("electrum_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block IRIDIUM_BLOCK = new BlockOreBlocks("iridium_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block TITANIUM_BLOCK = new BlockOreBlocks("titanium_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block RUBY_BLOCK = new BlockOreBlocks("ruby_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block SAPPHIRE_BLOCK = new BlockOreBlocks("sapphire_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block AMETHYST_BLOCK = new BlockOreBlocks("amethyst_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block TOPAZ_BLOCK = new BlockOreBlocks("topaz_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block PERIDOT_BLOCK = new BlockOreBlocks("peridot_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block REFINED_OBSIDIAN_BLOCK = new BlockOreBlocks("refined_obsidian_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block COBALT_BLOCK = new BlockOreBlocks("cobalt_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block COBALTSTEEL_BLOCK = new BlockOreBlocks("cobaltsteel_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block ONYX_BLOCK = new BlockOreBlocks("onyx_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block DARK_STEEL_BLOCK = new BlockOreBlocks("dark_steel_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block BLACK_OPAL_BLOCK = new BlockOreBlocks("black_opal_block", MapColor.IRON, 1, 5.0F, 10.0F);

	//FLUIDBLOCKS
	public static final BlockFluidClassic LIQUID_EXPERIENCE_BLOCK = (BlockFluidClassic) new BlockFluidClassic(FluidInit.LIQUID_EXPERIENCE, Material.WATER).setRegistryName("liquid_experience").setCreativeTab(MinersProsperity.blocks_tab);

	//OTHERBLOCKS
	public static final Block INVISIBLE_LIGHT = new BlockInvisibleLight("invisible_light", Material.AIR, MapColor.AIR);
}