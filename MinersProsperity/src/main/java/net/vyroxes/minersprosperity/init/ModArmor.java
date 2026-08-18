package net.vyroxes.minersprosperity.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.item.armor.ArmorBase;
import net.vyroxes.minersprosperity.item.armor.ArmorTier;
import net.vyroxes.minersprosperity.item.HorseArmorTier;
import net.vyroxes.minersprosperity.item.ItemHorseArmorCustom;

import java.util.ArrayList;
import java.util.List;

public class ModArmor
{
	public static final List<Item> ARMORS = new ArrayList<>();

	//ARMOR MATERIALS
	public static final ArmorMaterial WOODEN_ARMOR = ArmorTier.WOODEN.material;
	public static final ArmorMaterial STONE_ARMOR = ArmorTier.STONE.material;
	public static final ArmorMaterial LEAD_ARMOR = ArmorTier.LEAD.material;
	public static final ArmorMaterial INCONEL_ARMOR = ArmorTier.INCONEL.material;
	public static final ArmorMaterial ELECTRUM_ARMOR = ArmorTier.ELECTRUM.material;
	public static final ArmorMaterial OBSIDIAN_CLUSTER_ARMOR = ArmorTier.OBSIDIAN_CLUSTER.material;
	public static final ArmorMaterial EMERALD_ARMOR = ArmorTier.EMERALD.material;
	public static final ArmorMaterial COBALTSTEEL_ARMOR = ArmorTier.COBALTSTEEL.material;
	public static final ArmorMaterial DARK_STEEL_ARMOR = ArmorTier.DARK_STEEL.material;
	public static final ArmorMaterial BLACK_OPAL_ARMOR = ArmorTier.BLACK_OPAL.material;

	static
	{
		WOODEN_ARMOR.setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS)));
		STONE_ARMOR.setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.STONE)));
		LEAD_ARMOR.setRepairItem(new ItemStack(ModItems.LEAD_INGOT));
		INCONEL_ARMOR.setRepairItem(new ItemStack(ModItems.INCONEL_INGOT));
		ELECTRUM_ARMOR.setRepairItem(new ItemStack(ModItems.ELECTRUM_INGOT));
		OBSIDIAN_CLUSTER_ARMOR.setRepairItem(new ItemStack(ModItems.OBSIDIAN_CLUSTER));
		EMERALD_ARMOR.setRepairItem(new ItemStack(Items.EMERALD));
		COBALTSTEEL_ARMOR.setRepairItem(new ItemStack(ModItems.COBALTSTEEL_INGOT));
		DARK_STEEL_ARMOR.setRepairItem(new ItemStack(ModItems.DARK_STEEL_INGOT));
		BLACK_OPAL_ARMOR.setRepairItem(new ItemStack(ModItems.BLACK_OPAL));
	}

	//ARMOR
	public static final Item LEAD_HELMET = new ArmorBase("lead_helmet", ArmorTier.LEAD, 1, EntityEquipmentSlot.HEAD);
	public static final Item LEAD_CHESTPLATE = new ArmorBase("lead_chestplate", ArmorTier.LEAD, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEAD_LEGGINGS = new ArmorBase("lead_leggings", ArmorTier.LEAD, 2, EntityEquipmentSlot.LEGS);
	public static final Item LEAD_BOOTS = new ArmorBase("lead_boots", ArmorTier.LEAD, 1, EntityEquipmentSlot.FEET);

	public static final Item INCONEL_HELMET = new ArmorBase("inconel_helmet", ArmorTier.INCONEL, 1, EntityEquipmentSlot.HEAD);
	public static final Item INCONEL_CHESTPLATE = new ArmorBase("inconel_chestplate", ArmorTier.INCONEL, 1, EntityEquipmentSlot.CHEST);
	public static final Item INCONEL_LEGGINGS = new ArmorBase("inconel_leggings", ArmorTier.INCONEL, 2, EntityEquipmentSlot.LEGS);
	public static final Item INCONEL_BOOTS = new ArmorBase("inconel_boots", ArmorTier.INCONEL, 1, EntityEquipmentSlot.FEET);

	public static final Item ELECTRUM_HELMET = new ArmorBase("electrum_helmet", ArmorTier.ELECTRUM, 1, EntityEquipmentSlot.HEAD);
	public static final Item ELECTRUM_CHESTPLATE = new ArmorBase("electrum_chestplate", ArmorTier.ELECTRUM, 1, EntityEquipmentSlot.CHEST);
	public static final Item ELECTRUM_LEGGINGS = new ArmorBase("electrum_leggings", ArmorTier.ELECTRUM, 2, EntityEquipmentSlot.LEGS);
	public static final Item ELECTRUM_BOOTS = new ArmorBase("electrum_boots", ArmorTier.ELECTRUM, 1, EntityEquipmentSlot.FEET);

	public static final Item WOODEN_HELMET = new ArmorBase("wooden_helmet", ArmorTier.WOODEN, 1, EntityEquipmentSlot.HEAD);
	public static final Item WOODEN_CHESTPLATE = new ArmorBase("wooden_chestplate", ArmorTier.WOODEN, 1, EntityEquipmentSlot.CHEST);
	public static final Item WOODEN_LEGGINGS = new ArmorBase("wooden_leggings", ArmorTier.WOODEN, 2, EntityEquipmentSlot.LEGS);
	public static final Item WOODEN_BOOTS = new ArmorBase("wooden_boots", ArmorTier.WOODEN, 1, EntityEquipmentSlot.FEET);

	public static final Item STONE_HELMET = new ArmorBase("stone_helmet", ArmorTier.STONE, 1, EntityEquipmentSlot.HEAD);
	public static final Item STONE_CHESTPLATE = new ArmorBase("stone_chestplate", ArmorTier.STONE, 1, EntityEquipmentSlot.CHEST);
	public static final Item STONE_LEGGINGS = new ArmorBase("stone_leggings", ArmorTier.STONE, 2, EntityEquipmentSlot.LEGS);
	public static final Item STONE_BOOTS = new ArmorBase("stone_boots", ArmorTier.STONE, 1, EntityEquipmentSlot.FEET);

	public static final Item OBSIDIAN_CLUSTER_HELMET = new ArmorBase("obsidian_cluster_helmet", ArmorTier.OBSIDIAN_CLUSTER, 1, EntityEquipmentSlot.HEAD);
	public static final Item OBSIDIAN_CLUSTER_CHESTPLATE = new ArmorBase("obsidian_cluster_chestplate", ArmorTier.OBSIDIAN_CLUSTER, 1, EntityEquipmentSlot.CHEST);
	public static final Item OBSIDIAN_CLUSTER_LEGGINGS = new ArmorBase("obsidian_cluster_leggings", ArmorTier.OBSIDIAN_CLUSTER, 2, EntityEquipmentSlot.LEGS);
	public static final Item OBSIDIAN_CLUSTER_BOOTS = new ArmorBase("obsidian_cluster_boots", ArmorTier.OBSIDIAN_CLUSTER, 1, EntityEquipmentSlot.FEET);

	public static final Item EMERALD_HELMET = new ArmorBase("emerald_helmet", ArmorTier.EMERALD, 1, EntityEquipmentSlot.HEAD);
	public static final Item EMERALD_CHESTPLATE = new ArmorBase("emerald_chestplate", ArmorTier.EMERALD, 1, EntityEquipmentSlot.CHEST);
	public static final Item EMERALD_LEGGINGS = new ArmorBase("emerald_leggings", ArmorTier.EMERALD, 2, EntityEquipmentSlot.LEGS);
	public static final Item EMERALD_BOOTS = new ArmorBase("emerald_boots", ArmorTier.EMERALD, 1, EntityEquipmentSlot.FEET);

	public static final Item COBALTSTEEL_HELMET = new ArmorBase("cobaltsteel_helmet", ArmorTier.COBALTSTEEL, 1, EntityEquipmentSlot.HEAD);
	public static final Item COBALTSTEEL_CHESTPLATE = new ArmorBase("cobaltsteel_chestplate", ArmorTier.COBALTSTEEL, 1, EntityEquipmentSlot.CHEST);
	public static final Item COBALTSTEEL_LEGGINGS = new ArmorBase("cobaltsteel_leggings", ArmorTier.COBALTSTEEL, 2, EntityEquipmentSlot.LEGS);
	public static final Item COBALTSTEEL_BOOTS = new ArmorBase("cobaltsteel_boots", ArmorTier.COBALTSTEEL, 1, EntityEquipmentSlot.FEET);

	public static final Item DARK_STEEL_HELMET = new ArmorBase("dark_steel_helmet", ArmorTier.DARK_STEEL, 1, EntityEquipmentSlot.HEAD);
	public static final Item DARK_STEEL_CHESTPLATE = new ArmorBase("dark_steel_chestplate", ArmorTier.DARK_STEEL, 1, EntityEquipmentSlot.CHEST);
	public static final Item DARK_STEEL_LEGGINGS = new ArmorBase("dark_steel_leggings", ArmorTier.DARK_STEEL, 2, EntityEquipmentSlot.LEGS);
	public static final Item DARK_STEEL_BOOTS = new ArmorBase("dark_steel_boots", ArmorTier.DARK_STEEL, 1, EntityEquipmentSlot.FEET);

	public static final Item BLACK_OPAL_HELMET = new ArmorBase("black_opal_helmet", ArmorTier.BLACK_OPAL, 1, EntityEquipmentSlot.HEAD);
	public static final Item BLACK_OPAL_CHESTPLATE = new ArmorBase("black_opal_chestplate", ArmorTier.BLACK_OPAL, 1, EntityEquipmentSlot.CHEST);
	public static final Item BLACK_OPAL_LEGGINGS = new ArmorBase("black_opal_leggings", ArmorTier.BLACK_OPAL, 2, EntityEquipmentSlot.LEGS);
	public static final Item BLACK_OPAL_BOOTS = new ArmorBase("black_opal_boots", ArmorTier.BLACK_OPAL, 1, EntityEquipmentSlot.FEET);

	//HORSE ARMOR
	public static final Item LEATHER_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.LEATHER);
	public static final Item WOODEN_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.WOODEN);
	public static final Item STONE_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.STONE);
	public static final Item LEAD_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.LEAD);
	public static final Item INCONEL_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.INCONEL);
	public static final Item ELECTRUM_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.ELECTRUM);
	public static final Item OBSIDIAN_CLUSTER_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.OBSIDIAN_CLUSTER);
	public static final Item EMERALD_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.EMERALD);
	public static final Item COBALTSTEEL_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.COBALTSTEEL);
	public static final Item DARK_STEEL_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.DARK_STEEL);
	public static final Item BLACK_OPAL_HORSE_ARMOR = new ItemHorseArmorCustom(HorseArmorTier.BLACK_OPAL);
}