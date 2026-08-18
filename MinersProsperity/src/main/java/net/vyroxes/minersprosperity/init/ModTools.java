package net.vyroxes.minersprosperity.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.item.tool.*;
import net.vyroxes.minersprosperity.item.BowTier;
import net.vyroxes.minersprosperity.item.ShearsTier;
import net.vyroxes.minersprosperity.item.ShieldTier;
import net.vyroxes.minersprosperity.item.tool.ToolTier;
import net.vyroxes.minersprosperity.item.ItemBowCustom;
import net.vyroxes.minersprosperity.item.ItemShearsCustom;
import net.vyroxes.minersprosperity.item.ItemShieldCustom;

import java.util.ArrayList;
import java.util.List;

public class ModTools
{
	public static final List<Item> TOOLS = new ArrayList<>();
	
	//TOOL MATERIALS
	public static final ToolMaterial LEAD_TOOL = ToolTier.LEAD.material;
	public static final ToolMaterial INCONEL_TOOL = ToolTier.INCONEL.material;
	public static final ToolMaterial ELECTRUM_TOOL = ToolTier.ELECTRUM.material;
	public static final ToolMaterial OBSIDIAN_CLUSTER_TOOL = ToolTier.OBSIDIAN_CLUSTER.material;
	public static final ToolMaterial EMERALD_TOOL = ToolTier.EMERALD.material;
	public static final ToolMaterial COBALTSTEEL_TOOL = ToolTier.COBALTSTEEL.material;
	public static final ToolMaterial DARK_STEEL_TOOL = ToolTier.DARK_STEEL.material;
	public static final ToolMaterial BLACK_OPAL_TOOL = ToolTier.BLACK_OPAL.material;

	static
	{
		LEAD_TOOL.setRepairItem(new ItemStack(ModItems.LEAD_INGOT));
		INCONEL_TOOL.setRepairItem(new ItemStack(ModItems.INCONEL_INGOT));
		ELECTRUM_TOOL.setRepairItem(new ItemStack(ModItems.ELECTRUM_INGOT));
		OBSIDIAN_CLUSTER_TOOL.setRepairItem(new ItemStack(ModItems.OBSIDIAN_CLUSTER));
		EMERALD_TOOL.setRepairItem(new ItemStack(Items.EMERALD));
		COBALTSTEEL_TOOL.setRepairItem(new ItemStack(ModItems.COBALTSTEEL_INGOT));
		DARK_STEEL_TOOL.setRepairItem(new ItemStack(ModItems.DARK_STEEL_INGOT));
		BLACK_OPAL_TOOL.setRepairItem(new ItemStack(ModItems.BLACK_OPAL));
	}

	//TOOLS
	//SHEARS
	public static final Item WOODEN_SHEARS = new ItemShearsCustom(ShearsTier.WOODEN);
	public static final Item STONE_SHEARS = new ItemShearsCustom(ShearsTier.STONE);
	public static final Item LEAD_SHEARS = new ItemShearsCustom(ShearsTier.LEAD);
	public static final Item INCONEL_SHEARS = new ItemShearsCustom(ShearsTier.INCONEL);
	public static final Item GOLDEN_SHEARS = new ItemShearsCustom(ShearsTier.GOLDEN);
	public static final Item ELECTRUM_SHEARS = new ItemShearsCustom(ShearsTier.ELECTRUM);
	public static final Item DIAMOND_SHEARS = new ItemShearsCustom(ShearsTier.DIAMOND);
	public static final Item OBSIDIAN_CLUSTER_SHEARS = new ItemShearsCustom(ShearsTier.OBSIDIAN_CLUSTER);
	public static final Item EMERALD_SHEARS = new ItemShearsCustom(ShearsTier.EMERALD);
	public static final Item COBALTSTEEL_SHEARS = new ItemShearsCustom(ShearsTier.COBALTSTEEL);
	public static final Item DARK_STEEL_SHEARS = new ItemShearsCustom(ShearsTier.DARK_STEEL);
	public static final Item BLACK_OPAL_SHEARS = new ItemShearsCustom(ShearsTier.BLACK_OPAL);

	//SHIELDS
	public static final Item STONE_SHIELD = new ItemShieldCustom(ShieldTier.STONE);
	public static final Item LEAD_SHIELD = new ItemShieldCustom(ShieldTier.LEAD);
	public static final Item IRON_SHIELD = new ItemShieldCustom(ShieldTier.IRON);
	public static final Item INCONEL_SHIELD = new ItemShieldCustom(ShieldTier.INCONEL);
	public static final Item GOLDEN_SHIELD = new ItemShieldCustom(ShieldTier.GOLDEN);
	public static final Item ELECTRUM_SHIELD = new ItemShieldCustom(ShieldTier.ELECTRUM);
	public static final Item DIAMOND_SHIELD = new ItemShieldCustom(ShieldTier.DIAMOND);
	public static final Item OBSIDIAN_CLUSTER_SHIELD = new ItemShieldCustom(ShieldTier.OBSIDIAN_CLUSTER);
	public static final Item EMERALD_SHIELD = new ItemShieldCustom(ShieldTier.EMERALD);
	public static final Item COBALTSTEEL_SHIELD = new ItemShieldCustom(ShieldTier.COBALTSTEEL);
	public static final Item DARK_STEEL_SHIELD = new ItemShieldCustom(ShieldTier.DARK_STEEL);
	public static final Item BLACK_OPAL_SHIELD = new ItemShieldCustom(ShieldTier.BLACK_OPAL);

	//BOWS
	public static final Item STONE_BOW = new ItemBowCustom(BowTier.STONE);
	public static final Item LEAD_BOW = new ItemBowCustom(BowTier.LEAD);
	public static final Item IRON_BOW = new ItemBowCustom(BowTier.IRON);
	public static final Item INCONEL_BOW = new ItemBowCustom(BowTier.INCONEL);
	public static final Item GOLDEN_BOW = new ItemBowCustom(BowTier.GOLDEN);
	public static final Item ELECTRUM_BOW = new ItemBowCustom(BowTier.ELECTRUM);
	public static final Item DIAMOND_BOW = new ItemBowCustom(BowTier.DIAMOND);
	public static final Item OBSIDIAN_CLUSTER_BOW = new ItemBowCustom(BowTier.OBSIDIAN_CLUSTER);
	public static final Item EMERALD_BOW = new ItemBowCustom(BowTier.EMERALD);
	public static final Item COBALTSTEEL_BOW = new ItemBowCustom(BowTier.COBALTSTEEL);
	public static final Item DARK_STEEL_BOW = new ItemBowCustom(BowTier.DARK_STEEL);
	public static final Item BLACK_OPAL_BOW = new ItemBowCustom(BowTier.BLACK_OPAL);

	//TOOLS
	public static final Item LEAD_SWORD = new ToolSwordBase("lead_sword", ToolTier.LEAD);
	public static final Item LEAD_PICKAXE = new ToolPickaxeBase("lead_pickaxe", ToolTier.LEAD);
	public static final Item LEAD_AXE = new ToolAxeBase("lead_axe", ToolTier.LEAD, 8.0F, -3.1F);
	public static final Item LEAD_SHOVEL = new ToolShovelBase("lead_shovel", ToolTier.LEAD);
	public static final Item LEAD_HOE = new ToolHoeBase("lead_hoe", ToolTier.LEAD);

	public static final Item INCONEL_SWORD = new ToolSwordBase("inconel_sword", ToolTier.INCONEL);
	public static final Item INCONEL_PICKAXE = new ToolPickaxeBase("inconel_pickaxe", ToolTier.INCONEL);
	public static final Item INCONEL_AXE = new ToolAxeBase("inconel_axe", ToolTier.INCONEL, 8.0F, -3.1F);
	public static final Item INCONEL_SHOVEL = new ToolShovelBase("inconel_shovel", ToolTier.INCONEL);
	public static final Item INCONEL_HOE = new ToolHoeBase("inconel_hoe", ToolTier.INCONEL);

	public static final Item ELECTRUM_SWORD = new ToolSwordBase("electrum_sword", ToolTier.ELECTRUM);
	public static final Item ELECTRUM_PICKAXE = new ToolPickaxeBase("electrum_pickaxe", ToolTier.ELECTRUM);
	public static final Item ELECTRUM_AXE = new ToolAxeBase("electrum_axe", ToolTier.ELECTRUM, 8.0F, -3.1F);
	public static final Item ELECTRUM_SHOVEL = new ToolShovelBase("electrum_shovel", ToolTier.ELECTRUM);
	public static final Item ELECTRUM_HOE = new ToolHoeBase("electrum_hoe", ToolTier.ELECTRUM);

	public static final Item OBSIDIAN_CLUSTER_SWORD = new ToolSwordBase("obsidian_cluster_sword", ToolTier.OBSIDIAN_CLUSTER);
	public static final Item OBSIDIAN_CLUSTER_PICKAXE = new ToolPickaxeBase("obsidian_cluster_pickaxe", ToolTier.OBSIDIAN_CLUSTER);
	public static final Item OBSIDIAN_CLUSTER_AXE = new ToolAxeBase("obsidian_cluster_axe", ToolTier.OBSIDIAN_CLUSTER, 9.0F, -2.9F);
	public static final Item OBSIDIAN_CLUSTER_SHOVEL = new ToolShovelBase("obsidian_cluster_shovel", ToolTier.OBSIDIAN_CLUSTER);
	public static final Item OBSIDIAN_CLUSTER_HOE = new ToolHoeBase("obsidian_cluster_hoe", ToolTier.OBSIDIAN_CLUSTER);

	public static final Item EMERALD_SWORD = new ToolSwordBase("emerald_sword", ToolTier.EMERALD);
	public static final Item EMERALD_PICKAXE = new ToolPickaxeBase("emerald_pickaxe", ToolTier.EMERALD);
	public static final Item EMERALD_AXE = new ToolAxeBase("emerald_axe", ToolTier.EMERALD, 10.0F, -2.8F);
	public static final Item EMERALD_SHOVEL = new ToolShovelBase("emerald_shovel", ToolTier.EMERALD);
	public static final Item EMERALD_HOE = new ToolHoeBase("emerald_hoe", ToolTier.EMERALD);

	public static final Item COBALTSTEEL_SWORD = new ToolSwordBase("cobaltsteel_sword", ToolTier.COBALTSTEEL);
	public static final Item COBALTSTEEL_PICKAXE = new ToolPickaxeBase("cobaltsteel_pickaxe", ToolTier.COBALTSTEEL);
	public static final Item COBALTSTEEL_AXE = new ToolAxeBase("cobaltsteel_axe", ToolTier.COBALTSTEEL, 11.0F, -2.7F);
	public static final Item COBALTSTEEL_SHOVEL = new ToolShovelBase("cobaltsteel_shovel", ToolTier.COBALTSTEEL);
	public static final Item COBALTSTEEL_HOE = new ToolHoeBase("cobaltsteel_hoe", ToolTier.COBALTSTEEL);

	public static final Item DARK_STEEL_SWORD = new ToolSwordBase("dark_steel_sword", ToolTier.DARK_STEEL);
	public static final Item DARK_STEEL_PICKAXE = new ToolPickaxeBase("dark_steel_pickaxe", ToolTier.DARK_STEEL);
	public static final Item DARK_STEEL_AXE = new ToolAxeBase("dark_steel_axe", ToolTier.DARK_STEEL, 12.0F, -2.6F);
	public static final Item DARK_STEEL_SHOVEL = new ToolShovelBase("dark_steel_shovel", ToolTier.DARK_STEEL);
	public static final Item DARK_STEEL_HOE = new ToolHoeBase("dark_steel_hoe", ToolTier.DARK_STEEL);

	public static final Item BLACK_OPAL_SWORD = new ToolSwordBase("black_opal_sword", ToolTier.BLACK_OPAL);
	public static final Item BLACK_OPAL_PICKAXE = new ToolPickaxeBase("black_opal_pickaxe", ToolTier.BLACK_OPAL);
	public static final Item BLACK_OPAL_AXE = new ToolAxeBase("black_opal_axe", ToolTier.BLACK_OPAL, 13.0F, -2.5F);
	public static final Item BLACK_OPAL_SHOVEL = new ToolShovelBase("black_opal_shovel", ToolTier.BLACK_OPAL);
	public static final Item BLACK_OPAL_HOE = new ToolHoeBase("black_opal_hoe", ToolTier.BLACK_OPAL);

	//PAXELS
	public static final Item WOODEN_PAXEL = new ToolPaxelBase("wooden_paxel", ToolTier.WOODEN, 5.0F, -2.7F);
	public static final Item STONE_PAXEL = new ToolPaxelBase("stone_paxel", ToolTier.STONE, 7.0F, -2.7F);
	public static final Item LEAD_PAXEL = new ToolPaxelBase("lead_paxel", ToolTier.LEAD, 6.5F, -2.7F);
	public static final Item IRON_PAXEL = new ToolPaxelBase("iron_paxel", ToolTier.IRON, 6.0F, -2.7F);
	public static final Item INCONEL_PAXEL = new ToolPaxelBase("inconel_paxel", ToolTier.INCONEL, 6.0F, -2.7F);
	public static final Item GOLDEN_PAXEL = new ToolPaxelBase("golden_paxel", ToolTier.GOLDEN, 6.0F, -2.7F);
	public static final Item ELECTRUM_PAXEL = new ToolPaxelBase("electrum_paxel", ToolTier.ELECTRUM, 5.5F, -2.7F);
	public static final Item DIAMOND_PAXEL = new ToolPaxelBase("diamond_paxel", ToolTier.DIAMOND, 5.0F, -2.7F);
	public static final Item OBSIDIAN_CLUSTER_PAXEL = new ToolPaxelBase("obsidian_cluster_paxel", ToolTier.OBSIDIAN_CLUSTER, 5.0F, -2.7F);
	public static final Item EMERALD_PAXEL = new ToolPaxelBase("emerald_paxel", ToolTier.EMERALD, 5.0F, -2.7F);
	public static final Item COBALTSTEEL_PAXEL = new ToolPaxelBase("cobaltsteel_paxel", ToolTier.COBALTSTEEL, 5.0F, -2.7F);
	public static final Item DARK_STEEL_PAXEL = new ToolPaxelBase("dark_steel_paxel", ToolTier.DARK_STEEL, 5.0F, -2.7F);
	public static final Item BLACK_OPAL_PAXEL = new ToolPaxelBase("black_opal_paxel", ToolTier.BLACK_OPAL, 5.0F, -2.7F);
}