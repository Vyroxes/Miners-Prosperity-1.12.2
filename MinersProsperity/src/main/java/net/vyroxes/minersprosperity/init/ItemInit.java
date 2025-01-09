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
	public static final Item XP_UPGRADE = new ItemBase("xp_upgrade");
	public static final Item MACHINE_UPGRADE = new ItemBase("machine_upgrade");

	//BACKPACKS
	public static final Item BACKPACK = new Backpack("backpack");
	public static final Item IRON_BACKPACK = new IronBackpack("iron_backpack");

	//GEMS
	public static final Item RUBY = new ItemBase("ruby");
	public static final Item SAPPHIRE = new ItemBase("sapphire");
	public static final Item AMETHYST = new ItemBase("amethyst");
	public static final Item TOPAZ = new ItemBase("topaz");
	public static final Item PERIDOT = new ItemBase("peridot");
	public static final Item ONYX = new ItemBase("onyx");
	public static final Item BLACK_OPAL = new ItemBase("black_opal");

	//INGOTS
	public static final Item COPPER_INGOT = new ItemBase("copper_ingot");
	public static final Item TIN_INGOT = new ItemBase("tin_ingot");
	public static final Item BRONZE_INGOT = new ItemBase("bronze_ingot");
	public static final Item SILVER_INGOT = new ItemBase("silver_ingot");
	public static final Item LEAD_INGOT = new ItemBase("lead_ingot");
	public static final Item ZINC_INGOT = new ItemBase("zinc_ingot");
	public static final Item BRASS_INGOT = new ItemBase("brass_ingot");
	public static final Item ALUMINUM_INGOT = new ItemBase("aluminum_ingot");
	public static final Item DURALUMINUM_INGOT = new ItemBase("duraluminum_ingot");
	public static final Item REFINED_IRON_INGOT = new ItemBase("refined_iron_ingot");
	public static final Item STEEL_INGOT = new ItemBase("steel_ingot");
	public static final Item NICKEL_INGOT = new ItemBase("nickel_ingot");
	public static final Item CHROMIUM_INGOT = new ItemBase("chromium_ingot");
	public static final Item INCONEL_INGOT = new ItemBase("inconel_ingot");
	public static final Item URANIUM_INGOT = new ItemBase("uranium_ingot");
	public static final Item TUNGSTEN_INGOT = new ItemBase("tungsten_ingot");
	public static final Item PLATINUM_INGOT = new ItemBase("platinum_ingot");
	public static final Item WHITE_GOLD_INGOT = new ItemBase("white_gold_ingot");
	public static final Item ELECTRUM_INGOT = new ItemBase("electrum_ingot");
	public static final Item IRIDIUM_INGOT = new ItemBase("iridium_ingot");
	public static final Item TITANIUM_INGOT = new ItemBase("titanium_ingot");
	public static final Item REFINED_OBSIDIAN_INGOT = new ItemBase("refined_obsidian_ingot");
	public static final Item COBALT_INGOT = new ItemBase("cobalt_ingot");
	public static final Item COBALTSTEEL_INGOT = new ItemBase("cobaltsteel_ingot");
	public static final Item DARK_STEEL_INGOT = new ItemBase("dark_steel_ingot");

	//NUGGETS
	public static final Item DIAMOND_NUGGET = new ItemBase("diamond_nugget");
	public static final Item EMERALD_NUGGET = new ItemBase("emerald_nugget");
	public static final Item COPPER_NUGGET = new ItemBase("copper_nugget");
	public static final Item TIN_NUGGET = new ItemBase("tin_nugget");
	public static final Item BRONZE_NUGGET = new ItemBase("bronze_nugget");
	public static final Item SILVER_NUGGET = new ItemBase("silver_nugget");
	public static final Item LEAD_NUGGET = new ItemBase("lead_nugget");
	public static final Item ZINC_NUGGET = new ItemBase("zinc_nugget");
	public static final Item BRASS_NUGGET = new ItemBase("brass_nugget");
	public static final Item ALUMINUM_NUGGET = new ItemBase("aluminum_nugget");
	public static final Item DURALUMINUM_NUGGET = new ItemBase("duraluminum_nugget");
	public static final Item REFINED_IRON_NUGGET = new ItemBase("refined_iron_nugget");
	public static final Item STEEL_NUGGET = new ItemBase("steel_nugget");
	public static final Item NICKEL_NUGGET = new ItemBase("nickel_nugget");
	public static final Item CHROMIUM_NUGGET = new ItemBase("chromium_nugget");
	public static final Item INCONEL_NUGGET = new ItemBase("inconel_nugget");
	public static final Item URANIUM_NUGGET = new ItemBase("uranium_nugget");
	public static final Item TUNGSTEN_NUGGET = new ItemBase("tungsten_nugget");
	public static final Item PLATINUM_NUGGET = new ItemBase("platinum_nugget");
	public static final Item WHITE_GOLD_NUGGET = new ItemBase("white_gold_nugget");
	public static final Item ELECTRUM_NUGGET = new ItemBase("electrum_nugget");
	public static final Item IRIDIUM_NUGGET = new ItemBase("iridium_nugget");
	public static final Item TITANIUM_NUGGET = new ItemBase("titanium_nugget");
	public static final Item RUBY_NUGGET = new ItemBase("ruby_nugget");
	public static final Item SAPPHIRE_NUGGET = new ItemBase("sapphire_nugget");
	public static final Item AMETHYST_NUGGET = new ItemBase("amethyst_nugget");
	public static final Item TOPAZ_NUGGET = new ItemBase("topaz_nugget");
	public static final Item PERIDOT_NUGGET = new ItemBase("peridot_nugget");
	public static final Item REFINED_OBSIDIAN_NUGGET = new ItemBase("refined_obsidian_nugget");
	public static final Item COBALT_NUGGET = new ItemBase("cobalt_nugget");
	public static final Item COBALTSTEEL_NUGGET = new ItemBase("cobaltsteel_nugget");
	public static final Item ONYX_NUGGET = new ItemBase("onyx_nugget");
	public static final Item DARK_STEEL_NUGGET = new ItemBase("dark_steel_nugget");
	public static final Item BLACK_OPAL_NUGGET = new ItemBase("black_opal_nugget");

	//DUSTS
	public static final Item COAL_DUST = new ItemBase("coal_dust");
	public static final Item CHARCOAL_DUST = new ItemBase("charcoal_dust");
	public static final Item IRON_DUST = new ItemBase("iron_dust");
	public static final Item GOLD_DUST = new ItemBase("gold_dust");
	public static final Item DIAMOND_DUST = new ItemBase("diamond_dust");
	public static final Item EMERALD_DUST = new ItemBase("emerald_dust");
	public static final Item COPPER_DUST = new ItemBase("copper_dust");
	public static final Item TIN_DUST = new ItemBase("tin_dust");
	public static final Item BRONZE_DUST = new ItemBase("bronze_dust");
	public static final Item SILVER_DUST = new ItemBase("silver_dust");
	public static final Item LEAD_DUST = new ItemBase("lead_dust");
	public static final Item ZINC_DUST = new ItemBase("zinc_dust");
	public static final Item BRASS_DUST = new ItemBase("brass_dust");
	public static final Item ALUMINUM_DUST = new ItemBase("aluminum_dust");
	public static final Item DURALUMINUM_DUST = new ItemBase("duraluminum_dust");
	public static final Item REFINED_IRON_DUST = new ItemBase("refined_iron_dust");
	public static final Item STEEL_DUST = new ItemBase("steel_dust");
	public static final Item NICKEL_DUST = new ItemBase("nickel_dust");
	public static final Item CHROMIUM_DUST = new ItemBase("chromium_dust");
	public static final Item INCONEL_DUST = new ItemBase("inconel_dust");
	public static final Item URANIUM_DUST = new ItemBase("uranium_dust");
	public static final Item TUNGSTEN_DUST = new ItemBase("tungsten_dust");
	public static final Item PLATINUM_DUST = new ItemBase("platinum_dust");
	public static final Item WHITE_GOLD_DUST = new ItemBase("white_gold_dust");
	public static final Item ELECTRUM_DUST = new ItemBase("electrum_dust");
	public static final Item IRIDIUM_DUST = new ItemBase("iridium_dust");
	public static final Item TITANIUM_DUST = new ItemBase("titanium_dust");
	public static final Item RUBY_DUST = new ItemBase("ruby_dust");
	public static final Item SAPPHIRE_DUST = new ItemBase("sapphire_dust");
	public static final Item AMETHYST_DUST = new ItemBase("amethyst_dust");
	public static final Item TOPAZ_DUST = new ItemBase("topaz_dust");
	public static final Item PERIDOT_DUST = new ItemBase("peridot_dust");
	public static final Item REFINED_OBSIDIAN_DUST = new ItemBase("refined_obsidian_dust");
	public static final Item COBALT_DUST = new ItemBase("cobalt_dust");
	public static final Item COBALTSTEEL_DUST = new ItemBase("cobaltsteel_dust");
	public static final Item ONYX_DUST = new ItemBase("onyx_dust");
	public static final Item DARK_STEEL_DUST = new ItemBase("dark_steel_dust");
	public static final Item BLACK_OPAL_DUST = new ItemBase("black_opal_dust");

	//GEARS
	public static final Item COAL_GEAR = new ItemBase("coal_gear");
	public static final Item CHARCOAL_GEAR = new ItemBase("charcoal_gear");
	public static final Item IRON_GEAR = new ItemBase("iron_gear");
	public static final Item GOLD_GEAR = new ItemBase("gold_gear");
	public static final Item DIAMOND_GEAR = new ItemBase("diamond_gear");
	public static final Item EMERALD_GEAR = new ItemBase("emerald_gear");
	public static final Item REDSTONE_GEAR = new ItemBase("redstone_gear");
	public static final Item LAPIS_LAZULI_GEAR = new ItemBase("lapis_lazuli_gear");
	public static final Item COPPER_GEAR = new ItemBase("copper_gear");
	public static final Item TIN_GEAR = new ItemBase("tin_gear");
	public static final Item BRONZE_GEAR = new ItemBase("bronze_gear");
	public static final Item SILVER_GEAR = new ItemBase("silver_gear");
	public static final Item LEAD_GEAR = new ItemBase("lead_gear");
	public static final Item ZINC_GEAR = new ItemBase("zinc_gear");
	public static final Item BRASS_GEAR = new ItemBase("brass_gear");
	public static final Item ALUMINUM_GEAR = new ItemBase("aluminum_gear");
	public static final Item DURALUMINUM_GEAR = new ItemBase("duraluminum_gear");
	public static final Item REFINED_IRON_GEAR = new ItemBase("refined_iron_gear");
	public static final Item STEEL_GEAR = new ItemBase("steel_gear");
	public static final Item NICKEL_GEAR = new ItemBase("nickel_gear");
	public static final Item CHROMIUM_GEAR = new ItemBase("chromium_gear");
	public static final Item INCONEL_GEAR = new ItemBase("inconel_gear");
	public static final Item URANIUM_GEAR = new ItemBase("uranium_gear");
	public static final Item TUNGSTEN_GEAR = new ItemBase("tungsten_gear");
	public static final Item PLATINUM_GEAR = new ItemBase("platinum_gear");
	public static final Item WHITE_GOLD_GEAR = new ItemBase("white_gold_gear");
	public static final Item ELECTRUM_GEAR = new ItemBase("electrum_gear");
	public static final Item IRIDIUM_GEAR = new ItemBase("iridium_gear");
	public static final Item TITANIUM_GEAR = new ItemBase("titanium_gear");
	public static final Item RUBY_GEAR = new ItemBase("ruby_gear");
	public static final Item SAPPHIRE_GEAR = new ItemBase("sapphire_gear");
	public static final Item AMETHYST_GEAR = new ItemBase("amethyst_gear");
	public static final Item TOPAZ_GEAR = new ItemBase("topaz_gear");
	public static final Item PERIDOT_GEAR = new ItemBase("peridot_gear");
	public static final Item REFINED_OBSIDIAN_GEAR = new ItemBase("refined_obsidian_gear");
	public static final Item COBALT_GEAR = new ItemBase("cobalt_gear");
	public static final Item COBALTSTEEL_GEAR = new ItemBase("cobaltsteel_gear");
	public static final Item ONYX_GEAR = new ItemBase("onyx_gear");
	public static final Item DARK_STEEL_GEAR = new ItemBase("dark_steel_gear");
	public static final Item BLACK_OPAL_GEAR = new ItemBase("black_opal_gear");

	//PLATES
	public static final Item COAL_PLATE = new ItemBase("coal_plate");
	public static final Item CHARCOAL_PLATE = new ItemBase("charcoal_plate");
	public static final Item IRON_PLATE = new ItemBase("iron_plate");
	public static final Item GOLD_PLATE = new ItemBase("gold_plate");
	public static final Item DIAMOND_PLATE = new ItemBase("diamond_plate");
	public static final Item EMERALD_PLATE = new ItemBase("emerald_plate");
	public static final Item REDSTONE_PLATE = new ItemBase("redstone_plate");
	public static final Item LAPIS_LAZULI_PLATE = new ItemBase("lapis_lazuli_plate");
	public static final Item COPPER_PLATE = new ItemBase("copper_plate");
	public static final Item TIN_PLATE = new ItemBase("tin_plate");
	public static final Item BRONZE_PLATE = new ItemBase("bronze_plate");
	public static final Item SILVER_PLATE = new ItemBase("silver_plate");
	public static final Item LEAD_PLATE = new ItemBase("lead_plate");
	public static final Item ZINC_PLATE = new ItemBase("zinc_plate");
	public static final Item BRASS_PLATE = new ItemBase("brass_plate");
	public static final Item ALUMINUM_PLATE = new ItemBase("aluminum_plate");
	public static final Item DURALUMINUM_PLATE = new ItemBase("duraluminum_plate");
	public static final Item REFINED_IRON_PLATE = new ItemBase("refined_iron_plate");
	public static final Item STEEL_PLATE = new ItemBase("steel_plate");
	public static final Item NICKEL_PLATE = new ItemBase("nickel_plate");
	public static final Item CHROMIUM_PLATE = new ItemBase("chromium_plate");
	public static final Item INCONEL_PLATE = new ItemBase("inconel_plate");
	public static final Item URANIUM_PLATE = new ItemBase("uranium_plate");
	public static final Item TUNGSTEN_PLATE = new ItemBase("tungsten_plate");
	public static final Item PLATINUM_PLATE = new ItemBase("platinum_plate");
	public static final Item WHITE_GOLD_PLATE = new ItemBase("white_gold_plate");
	public static final Item ELECTRUM_PLATE = new ItemBase("electrum_plate");
	public static final Item IRIDIUM_PLATE = new ItemBase("iridium_plate");
	public static final Item TITANIUM_PLATE = new ItemBase("titanium_plate");
	public static final Item RUBY_PLATE = new ItemBase("ruby_plate");
	public static final Item SAPPHIRE_PLATE = new ItemBase("sapphire_plate");
	public static final Item AMETHYST_PLATE = new ItemBase("amethyst_plate");
	public static final Item TOPAZ_PLATE = new ItemBase("topaz_plate");
	public static final Item PERIDOT_PLATE = new ItemBase("peridot_plate");
	public static final Item REFINED_OBSIDIAN_PLATE = new ItemBase("refined_obsidian_plate");
	public static final Item COBALT_PLATE = new ItemBase("cobalt_plate");
	public static final Item COBALTSTEEL_PLATE = new ItemBase("cobaltsteel_plate");
	public static final Item ONYX_PLATE = new ItemBase("onyx_plate");
	public static final Item DARK_STEEL_PLATE = new ItemBase("dark_steel_plate");
	public static final Item BLACK_OPAL_PLATE = new ItemBase("black_opal_plate");
}