package net.vyroxes.minersprosperity.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.armour.ArmourEmerald;
import net.vyroxes.minersprosperity.objects.armour.ArmourStone;
import net.vyroxes.minersprosperity.objects.armour.ArmourWood;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;
import net.vyroxes.minersprosperity.objects.items.ItemBase;
import net.vyroxes.minersprosperity.objects.tools.AxeEmerald;
import net.vyroxes.minersprosperity.objects.tools.HoeEmerald;
import net.vyroxes.minersprosperity.objects.tools.PickaxeEmerald;
import net.vyroxes.minersprosperity.objects.tools.ShovelEmerald;
import net.vyroxes.minersprosperity.objects.tools.SwordEmerald;

public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<>();
	
	//TOOL MATERIALS
	public static final ToolMaterial EMERALD_TOOL = EnumHelper.addToolMaterial("emerald tool", 4, 1892, 10.0F, 4.0F, 8);

	//ARMOUR MATERIALS
	public static final ArmorMaterial WOOD_ARMOUR = EnumHelper.addArmorMaterial("wood armor", Tags.MODID + ":wood", 5, new int[]{1, 2, 3, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
	public static final ArmorMaterial STONE_ARMOUR = EnumHelper.addArmorMaterial("stone armor", Tags.MODID + ":stone", 8, new int[]{2, 3, 4, 2}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static final ArmorMaterial EMERALD_ARMOUR = EnumHelper.addArmorMaterial("emerald armor", Tags.MODID + ":emerald", 40, new int[]{4, 7, 9, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F);

	//TOOLS
	public static final Item EMERALD_SWORD = new SwordEmerald("emerald_sword", EMERALD_TOOL);
	public static final Item EMERALD_PICKAXE = new PickaxeEmerald("emerald_pickaxe", EMERALD_TOOL);
	public static final Item EMERALD_AXE = new AxeEmerald("emerald_axe", EMERALD_TOOL);
	public static final Item EMERALD_SHOVEL = new ShovelEmerald("emerald_shovel", EMERALD_TOOL);
	public static final Item EMERALD_HOE = new HoeEmerald("emerald_hoe", EMERALD_TOOL);
	
	//ARMOUR
	public static final Item WOOD_HELMET = new ArmourWood("wood_helmet", WOOD_ARMOUR, 1, EntityEquipmentSlot.HEAD);
	public static final Item WOOD_CHESTPLATE = new ArmourWood("wood_chestplate", WOOD_ARMOUR, 1, EntityEquipmentSlot.CHEST);
	public static final Item WOOD_LEGGINGS = new ArmourWood("wood_leggings", WOOD_ARMOUR, 2, EntityEquipmentSlot.LEGS);
	public static final Item WOOD_BOOTS = new ArmourWood("wood_boots", WOOD_ARMOUR, 1, EntityEquipmentSlot.FEET);
	public static final Item STONE_HELMET = new ArmourStone("stone_helmet", STONE_ARMOUR, 1, EntityEquipmentSlot.HEAD);
	public static final Item STONE_CHESTPLATE = new ArmourStone("stone_chestplate", STONE_ARMOUR, 1, EntityEquipmentSlot.CHEST);
	public static final Item STONE_LEGGINGS = new ArmourStone("stone_leggings", STONE_ARMOUR, 2, EntityEquipmentSlot.LEGS);
	public static final Item STONE_BOOTS = new ArmourStone("stone_boots", STONE_ARMOUR, 1, EntityEquipmentSlot.FEET);
	public static final Item EMERALD_HELMET = new ArmourEmerald("emerald_helmet", EMERALD_ARMOUR, 1, EntityEquipmentSlot.HEAD);
	public static final Item EMERALD_CHESTPLATE = new ArmourEmerald("emerald_chestplate", EMERALD_ARMOUR, 1, EntityEquipmentSlot.CHEST);
	public static final Item EMERALD_LEGGINGS = new ArmourEmerald("emerald_leggings", EMERALD_ARMOUR, 2, EntityEquipmentSlot.LEGS);
	public static final Item EMERALD_BOOTS = new ArmourEmerald("emerald_boots", EMERALD_ARMOUR, 1, EntityEquipmentSlot.FEET);

	//ITEMS
	public static final Item CHAIN = new ItemBase("chain");

	//UPGRADES
	public static final Item SPEED_UPGRADE = new ItemBase("speed_upgrade");
	public static final Item ENERGY_UPGRADE = new ItemBase("energy_upgrade");

	//BACKPACKS
	public static final Item BACKPACK = new Backpack("backpack");
	public static final Item IRON_BACKPACK = new IronBackpack("iron_backpack");

	//INGOTS
	public static final Item COPPER_INGOT = new ItemBase("copper_ingot");
	public static final Item TIN_INGOT = new ItemBase("tin_ingot");
	public static final Item LEAD_INGOT = new ItemBase("lead_ingot");
	public static final Item SILVER_INGOT = new ItemBase("silver_ingot");

	//NUGGETS
	public static final Item COPPER_NUGGET = new ItemBase("copper_nugget");
	public static final Item TIN_NUGGET = new ItemBase("tin_nugget");
	public static final Item LEAD_NUGGET = new ItemBase("lead_nugget");
	public static final Item SILVER_NUGGET = new ItemBase("silver_nugget");
	public static final Item DIAMOND_NUGGET = new ItemBase("diamond_nugget");
	public static final Item EMERALD_NUGGET = new ItemBase("emerald_nugget");

	//DUSTS
	public static final Item COAL_DUST = new ItemBase("coal_dust");
	public static final Item CHARCOAL_DUST = new ItemBase("charcoal_dust");
	public static final Item COPPER_DUST = new ItemBase("copper_dust");
	public static final Item TIN_DUST = new ItemBase("tin_dust");
	public static final Item LEAD_DUST = new ItemBase("lead_dust");
	public static final Item SILVER_DUST = new ItemBase("silver_dust");
	public static final Item IRON_DUST = new ItemBase("iron_dust");
	public static final Item GOLD_DUST = new ItemBase("gold_dust");
	public static final Item DIAMOND_DUST = new ItemBase("diamond_dust");
	public static final Item EMERALD_DUST = new ItemBase("emerald_dust");

	//GEARS
	public static final Item COAL_GEAR = new ItemBase("coal_gear");
	public static final Item CHARCOAL_GEAR = new ItemBase("charcoal_gear");
	public static final Item COPPER_GEAR = new ItemBase("copper_gear");
	public static final Item TIN_GEAR = new ItemBase("tin_gear");
	public static final Item LEAD_GEAR = new ItemBase("lead_gear");
	public static final Item SILVER_GEAR = new ItemBase("silver_gear");
	public static final Item IRON_GEAR = new ItemBase("iron_gear");
	public static final Item GOLD_GEAR = new ItemBase("gold_gear");
	public static final Item DIAMOND_GEAR = new ItemBase("diamond_gear");
	public static final Item EMERALD_GEAR = new ItemBase("emerald_gear");
	public static final Item REDSTONE_GEAR = new ItemBase("redstone_gear");
	public static final Item LAPIS_LAZULI_GEAR = new ItemBase("lapis_lazuli_gear");

	//PLATES
	public static final Item COAL_PLATE = new ItemBase("coal_plate");
	public static final Item CHARCOAL_PLATE = new ItemBase("charcoal_plate");
	public static final Item COPPER_PLATE = new ItemBase("copper_plate");
	public static final Item TIN_PLATE = new ItemBase("tin_plate");
	public static final Item LEAD_PLATE = new ItemBase("lead_plate");
	public static final Item SILVER_PLATE = new ItemBase("silver_plate");
	public static final Item IRON_PLATE = new ItemBase("iron_plate");
	public static final Item GOLD_PLATE = new ItemBase("gold_plate");
	public static final Item DIAMOND_PLATE = new ItemBase("diamond_plate");
	public static final Item EMERALD_PLATE = new ItemBase("emerald_plate");
	public static final Item REDSTONE_PLATE = new ItemBase("redstone_plate");
	public static final Item LAPIS_LAZULI_PLATE = new ItemBase("lapis_lazuli_plate");
}