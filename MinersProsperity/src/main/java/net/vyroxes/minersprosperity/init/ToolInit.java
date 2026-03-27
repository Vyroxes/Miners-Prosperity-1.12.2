package net.vyroxes.minersprosperity.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.objects.tools.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolInit
{
	public static final List<Item> TOOLS = new ArrayList<>();
	
	//TOOL MATERIALS
	public static final ToolMaterial LEAD_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("lead tool", 1, 96, 6.0F, 1.5F, 14)).setRepairItem(new ItemStack(ItemInit.LEAD_INGOT));
	public static final ToolMaterial INCONEL_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("inconel tool", 2, 502, 6.5F, 2.0F, 12)).setRepairItem(new ItemStack(ItemInit.INCONEL_INGOT));
	public static final ToolMaterial ELECTRUM_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("electrum tool", 2, 1004, 7.0F, 2.5F, 10)).setRepairItem(new ItemStack(ItemInit.ELECTRUM_INGOT));
	public static final ToolMaterial OBSIDIAN_CLUSTER_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("obsidian cluster tool", 3, 1892, 10.0F, 4.0F, 12)).setRepairItem(new ItemStack(ItemInit.OBSIDIAN_CLUSTER));
	public static final ToolMaterial EMERALD_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("emerald tool", 4, 2223, 12.0F, 5.0F, 14)).setRepairItem(new ItemStack(Items.EMERALD));
	public static final ToolMaterial COBALTSTEEL_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("cobaltsteel tool", 4, 2611, 14.0F, 6.0F, 16)).setRepairItem(new ItemStack(ItemInit.COBALTSTEEL_INGOT));
	public static final ToolMaterial DARK_STEEL_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("dark steel tool", 5, 3067, 16.0F, 7.0F, 18)).setRepairItem(new ItemStack(ItemInit.DARK_STEEL_INGOT));
	public static final ToolMaterial BLACK_OPAL_TOOL = Objects.requireNonNull(EnumHelper.addToolMaterial("black opal tool", 5, 3603, 18.0F, 8.0F, 20)).setRepairItem(new ItemStack(ItemInit.BLACK_OPAL));

	//TOOLS
	public static final Item WOODEN_PAXEL = new ToolPaxelBase("wooden_paxel", ToolMaterial.WOOD, 5.0F, -2.2F);
	public static final Item STONE_PAXEL = new ToolPaxelBase("stone_paxel", ToolMaterial.STONE, 7.0F, -2.2F);
	public static final Item LEAD_PAXEL = new ToolPaxelBase("lead_paxel", LEAD_TOOL, 6.5F, -2.2F);
	public static final Item IRON_PAXEL = new ToolPaxelBase("iron_paxel", ToolMaterial.IRON, 6.0F, -2.2F);
	public static final Item INCONEL_PAXEL = new ToolPaxelBase("inconel_paxel", INCONEL_TOOL, 6.0F, -2.2F);
	public static final Item GOLDEN_PAXEL = new ToolPaxelBase("golden_paxel", ToolMaterial.GOLD, 6.0F, -2.2F);
	public static final Item ELECTRUM_PAXEL = new ToolPaxelBase("electrum_paxel", ELECTRUM_TOOL, 5.5F, -2.2F);
	public static final Item DIAMOND_PAXEL = new ToolPaxelBase("diamond_paxel", ToolMaterial.DIAMOND, 5.0F, -2.2F);
	public static final Item OBSIDIAN_CLUSTER_PAXEL = new ToolPaxelBase("obsidian_cluster_paxel", OBSIDIAN_CLUSTER_TOOL, 5.0F, -2.2F);
	public static final Item EMERALD_PAXEL = new ToolPaxelBase("emerald_paxel", EMERALD_TOOL, 5.0F, -2.2F);
	public static final Item COBALTSTEEL_PAXEL = new ToolPaxelBase("cobaltsteel_paxel", COBALTSTEEL_TOOL, 5.0F, -2.2F);
	public static final Item DARK_STEEL_PAXEL = new ToolPaxelBase("dark_steel_paxel", DARK_STEEL_TOOL, 5.0F, -2.2F);
	public static final Item BLACK_OPAL_PAXEL = new ToolPaxelBase("black_opal_paxel", BLACK_OPAL_TOOL, 5.0F, -2.2F);
	public static final Item LEAD_SWORD = new ToolSwordBase("lead_sword", LEAD_TOOL);
	public static final Item LEAD_PICKAXE = new ToolPickaxeBase("lead_pickaxe", LEAD_TOOL);
	public static final Item LEAD_AXE = new ToolAxeBase("lead_axe", LEAD_TOOL, 8.0F, -3.1F);
	public static final Item LEAD_SHOVEL = new ToolShovelBase("lead_shovel", LEAD_TOOL);
	public static final Item LEAD_HOE = new ToolHoeBase("lead_hoe", LEAD_TOOL);
	public static final Item INCONEL_SWORD = new ToolSwordBase("inconel_sword", INCONEL_TOOL);
	public static final Item INCONEL_PICKAXE = new ToolPickaxeBase("inconel_pickaxe", INCONEL_TOOL);
	public static final Item INCONEL_AXE = new ToolAxeBase("inconel_axe", INCONEL_TOOL, 8.0F, -3.1F);
	public static final Item INCONEL_SHOVEL = new ToolShovelBase("inconel_shovel", INCONEL_TOOL);
	public static final Item INCONEL_HOE = new ToolHoeBase("inconel_hoe", INCONEL_TOOL);
	public static final Item ELECTRUM_SWORD = new ToolSwordBase("electrum_sword", ELECTRUM_TOOL);
	public static final Item ELECTRUM_PICKAXE = new ToolPickaxeBase("electrum_pickaxe", ELECTRUM_TOOL);
	public static final Item ELECTRUM_AXE = new ToolAxeBase("electrum_axe", ELECTRUM_TOOL, 8.0F, -3.1F);
	public static final Item ELECTRUM_SHOVEL = new ToolShovelBase("electrum_shovel", ELECTRUM_TOOL);
	public static final Item ELECTRUM_HOE = new ToolHoeBase("electrum_hoe", ELECTRUM_TOOL);
	public static final Item OBSIDIAN_CLUSTER_SWORD = new ToolSwordBase("obsidian_cluster_sword", OBSIDIAN_CLUSTER_TOOL);
	public static final Item OBSIDIAN_CLUSTER_PICKAXE = new ToolPickaxeBase("obsidian_cluster_pickaxe", OBSIDIAN_CLUSTER_TOOL);
	public static final Item OBSIDIAN_CLUSTER_AXE = new ToolAxeBase("obsidian_cluster_axe", OBSIDIAN_CLUSTER_TOOL, 9.0F, -2.9F);
	public static final Item OBSIDIAN_CLUSTER_SHOVEL = new ToolShovelBase("obsidian_cluster_shovel", OBSIDIAN_CLUSTER_TOOL);
	public static final Item OBSIDIAN_CLUSTER_HOE = new ToolHoeBase("obsidian_cluster_hoe", OBSIDIAN_CLUSTER_TOOL);
	public static final Item EMERALD_SWORD = new ToolSwordBase("emerald_sword", EMERALD_TOOL);
	public static final Item EMERALD_PICKAXE = new ToolPickaxeBase("emerald_pickaxe", EMERALD_TOOL);
	public static final Item EMERALD_AXE = new ToolAxeBase("emerald_axe", EMERALD_TOOL, 10.0F, -2.8F);
	public static final Item EMERALD_SHOVEL = new ToolShovelBase("emerald_shovel", EMERALD_TOOL);
	public static final Item EMERALD_HOE = new ToolHoeBase("emerald_hoe", EMERALD_TOOL);
	public static final Item COBALTSTEEL_SWORD = new ToolSwordBase("cobaltsteel_sword", COBALTSTEEL_TOOL);
	public static final Item COBALTSTEEL_PICKAXE = new ToolPickaxeBase("cobaltsteel_pickaxe", COBALTSTEEL_TOOL);
	public static final Item COBALTSTEEL_AXE = new ToolAxeBase("cobaltsteel_axe", COBALTSTEEL_TOOL, 11.0F, -2.7F);
	public static final Item COBALTSTEEL_SHOVEL = new ToolShovelBase("cobaltsteel_shovel", COBALTSTEEL_TOOL);
	public static final Item COBALTSTEEL_HOE = new ToolHoeBase("cobaltsteel_hoe", COBALTSTEEL_TOOL);
	public static final Item DARK_STEEL_SWORD = new ToolSwordBase("dark_steel_sword", DARK_STEEL_TOOL);
	public static final Item DARK_STEEL_PICKAXE = new ToolPickaxeBase("dark_steel_pickaxe", DARK_STEEL_TOOL);
	public static final Item DARK_STEEL_AXE = new ToolAxeBase("dark_steel_axe", DARK_STEEL_TOOL, 12.0F, -2.6F);
	public static final Item DARK_STEEL_SHOVEL = new ToolShovelBase("dark_steel_shovel", DARK_STEEL_TOOL);
	public static final Item DARK_STEEL_HOE = new ToolHoeBase("dark_steel_hoe", DARK_STEEL_TOOL);
	public static final Item BLACK_OPAL_SWORD = new ToolSwordBase("black_opal_sword", BLACK_OPAL_TOOL);
	public static final Item BLACK_OPAL_PICKAXE = new ToolPickaxeBase("black_opal_pickaxe", BLACK_OPAL_TOOL);
	public static final Item BLACK_OPAL_AXE = new ToolAxeBase("black_opal_axe", BLACK_OPAL_TOOL, 13.0F, -2.5F);
	public static final Item BLACK_OPAL_SHOVEL = new ToolShovelBase("black_opal_shovel", BLACK_OPAL_TOOL);
	public static final Item BLACK_OPAL_HOE = new ToolHoeBase("black_opal_hoe", BLACK_OPAL_TOOL);
}