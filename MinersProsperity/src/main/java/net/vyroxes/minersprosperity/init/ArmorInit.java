package net.vyroxes.minersprosperity.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.armor.ArmorBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArmorInit
{
	public static final List<Item> ARMORS = new ArrayList<>();

	//ARMOR MATERIALS
	public static final ArmorMaterial WOODEN_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("wooden armor", Tags.MODID + ":wooden", 5, new int[]{1, 2, 3, 1}, 22, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F)).setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS)));
	public static final ArmorMaterial STONE_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("stone armor", Tags.MODID + ":stone", 8, new int[]{2, 3, 4, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F)).setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.STONE)));
	public static final ArmorMaterial LEAD_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("lead armor", Tags.MODID + ":lead", 12, new int[]{2, 4, 5, 2}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F)).setRepairItem(new ItemStack(ItemInit.LEAD_INGOT));
	public static final ArmorMaterial INCONEL_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("inconel armor", Tags.MODID + ":inconel", 21, new int[]{2, 5, 6, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F)).setRepairItem(new ItemStack(ItemInit.INCONEL_INGOT));
	public static final ArmorMaterial ELECTRUM_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("electrum armor", Tags.MODID + ":electrum", 27, new int[]{3, 5, 6, 3}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F)).setRepairItem(new ItemStack(ItemInit.ELECTRUM_INGOT));
	public static final ArmorMaterial OBSIDIAN_CLUSTER_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("obsidian cluster armor", Tags.MODID + ":obsidian_cluster", 40, new int[]{4, 7, 9, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F)).setRepairItem(new ItemStack(ItemInit.OBSIDIAN_CLUSTER));
	public static final ArmorMaterial EMERALD_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("emerald armor", Tags.MODID + ":emerald", 47, new int[]{5, 8, 10, 5}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F)).setRepairItem(new ItemStack(Items.EMERALD));
	public static final ArmorMaterial COBALTSTEEL_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("cobaltsteel armor", Tags.MODID + ":cobaltsteel", 54, new int[]{5, 10, 12, 5}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 5.0F)).setRepairItem(new ItemStack(ItemInit.COBALTSTEEL_INGOT));
	public static final ArmorMaterial DARK_STEEL_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("dark steel armor", Tags.MODID + ":dark_steel", 61, new int[]{6, 10, 12, 6}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 6.0F)).setRepairItem(new ItemStack(ItemInit.DARK_STEEL_INGOT));
	public static final ArmorMaterial BLACK_OPAL_ARMOR = Objects.requireNonNull(EnumHelper.addArmorMaterial("black opal armor", Tags.MODID + ":black_opal", 68, new int[]{7, 12, 14, 7}, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 7.0F)).setRepairItem(new ItemStack(ItemInit.BLACK_OPAL));;;

	//ARMOR
	public static final Item LEAD_HELMET = new ArmorBase("lead_helmet", LEAD_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item LEAD_CHESTPLATE = new ArmorBase("lead_chestplate", LEAD_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item LEAD_LEGGINGS = new ArmorBase("lead_leggings", LEAD_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item LEAD_BOOTS = new ArmorBase("lead_boots", LEAD_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item INCONEL_HELMET = new ArmorBase("inconel_helmet", INCONEL_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item INCONEL_CHESTPLATE = new ArmorBase("inconel_chestplate", INCONEL_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item INCONEL_LEGGINGS = new ArmorBase("inconel_leggings", INCONEL_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item INCONEL_BOOTS = new ArmorBase("inconel_boots", INCONEL_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item ELECTRUM_HELMET = new ArmorBase("electrum_helmet", ELECTRUM_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item ELECTRUM_CHESTPLATE = new ArmorBase("electrum_chestplate", ELECTRUM_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item ELECTRUM_LEGGINGS = new ArmorBase("electrum_leggings", ELECTRUM_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item ELECTRUM_BOOTS = new ArmorBase("electrum_boots", ELECTRUM_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item WOODEN_HELMET = new ArmorBase("wooden_helmet", WOODEN_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item WOODEN_CHESTPLATE = new ArmorBase("wooden_chestplate", WOODEN_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item WOODEN_LEGGINGS = new ArmorBase("wooden_leggings", WOODEN_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item WOODEN_BOOTS = new ArmorBase("wooden_boots", WOODEN_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item STONE_HELMET = new ArmorBase("stone_helmet", STONE_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item STONE_CHESTPLATE = new ArmorBase("stone_chestplate", STONE_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item STONE_LEGGINGS = new ArmorBase("stone_leggings", STONE_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item STONE_BOOTS = new ArmorBase("stone_boots", STONE_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item OBSIDIAN_CLUSTER_HELMET = new ArmorBase("obsidian_cluster_helmet", OBSIDIAN_CLUSTER_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item OBSIDIAN_CLUSTER_CHESTPLATE = new ArmorBase("obsidian_cluster_chestplate", OBSIDIAN_CLUSTER_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item OBSIDIAN_CLUSTER_LEGGINGS = new ArmorBase("obsidian_cluster_leggings", OBSIDIAN_CLUSTER_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item OBSIDIAN_CLUSTER_BOOTS = new ArmorBase("obsidian_cluster_boots", OBSIDIAN_CLUSTER_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item EMERALD_HELMET = new ArmorBase("emerald_helmet", EMERALD_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item EMERALD_CHESTPLATE = new ArmorBase("emerald_chestplate", EMERALD_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item EMERALD_LEGGINGS = new ArmorBase("emerald_leggings", EMERALD_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item EMERALD_BOOTS = new ArmorBase("emerald_boots", EMERALD_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item COBALTSTEEL_HELMET = new ArmorBase("cobaltsteel_helmet", COBALTSTEEL_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item COBALTSTEEL_CHESTPLATE = new ArmorBase("cobaltsteel_chestplate", COBALTSTEEL_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item COBALTSTEEL_LEGGINGS = new ArmorBase("cobaltsteel_leggings", COBALTSTEEL_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item COBALTSTEEL_BOOTS = new ArmorBase("cobaltsteel_boots", COBALTSTEEL_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item DARK_STEEL_HELMET = new ArmorBase("dark_steel_helmet", DARK_STEEL_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item DARK_STEEL_CHESTPLATE = new ArmorBase("dark_steel_chestplate", DARK_STEEL_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item DARK_STEEL_LEGGINGS = new ArmorBase("dark_steel_leggings", DARK_STEEL_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item DARK_STEEL_BOOTS = new ArmorBase("dark_steel_boots", DARK_STEEL_ARMOR, 1, EntityEquipmentSlot.FEET);
	public static final Item BLACK_OPAL_HELMET = new ArmorBase("black_opal_helmet", BLACK_OPAL_ARMOR, 1, EntityEquipmentSlot.HEAD);
	public static final Item BLACK_OPAL_CHESTPLATE = new ArmorBase("black_opal_chestplate", BLACK_OPAL_ARMOR, 1, EntityEquipmentSlot.CHEST);
	public static final Item BLACK_OPAL_LEGGINGS = new ArmorBase("black_opal_leggings", BLACK_OPAL_ARMOR, 2, EntityEquipmentSlot.LEGS);
	public static final Item BLACK_OPAL_BOOTS = new ArmorBase("black_opal_boots", BLACK_OPAL_ARMOR, 1, EntityEquipmentSlot.FEET);
}