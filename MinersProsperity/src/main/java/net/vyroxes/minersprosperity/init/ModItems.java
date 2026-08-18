package net.vyroxes.minersprosperity.init;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import net.minecraft.item.Item;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.item.*;
import net.vyroxes.minersprosperity.item.backpack.BackpackTier;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpack;
import net.vyroxes.minersprosperity.item.battery.BatteryTier;
import net.vyroxes.minersprosperity.block.chest.ChestTier;
import net.vyroxes.minersprosperity.item.battery.ItemBattery;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<>();

    private static final Map<Item, BooleanSupplier> ENABLE_CONDITIONS = new IdentityHashMap<>();

	//ITEMS
	public static final Item WRENCH = new ItemBase("wrench", 1, 192, "ingotSteel");
	public static final Item WOODEN_TREETAP = new ItemBase("wooden_treetap", 1, 64, "plankWood");
	public static final Item IRON_TREETAP = new ItemBase("iron_treetap", 1, 128, "plankWood");
	public static final Item REFINED_OBSIDIAN_TREETAP = new ItemBase("refined_obsidian_treetap", 1, 384, "plankWood");
    public static final Item FIRE_EXTINGUISHER = configurable(new ItemFireExtinguisher("fire_extinguisher"), () -> ModConfig.ITEMS.enableFireExtinguisher);
    public static final Item PORTABLE_CRAFTING_TABLE = configurable(new ItemPortableCraftingTable("portable_crafting_table"), () -> ModConfig.ITEMS.enablePortableCraftingTable);
    public static final Item ENTITY_RELOCATOR = configurable(new ItemEntityRelocator("entity_relocator"), () -> ModConfig.ITEMS.enableEntityRelocator);

	//MATERIALS
	public static final Item RESIN = new ItemBase("resin");
	public static final Item RUBBER = new ItemBase("rubber");
	public static final Item SILICON = new ItemBase("silicon");

	//DIES
	public static final Item IRON_GEAR_DIE = new ItemBase("iron_gear_die", 1, 128);
	public static final Item IRON_PLATE_DIE = new ItemBase("iron_plate_die", 1, 128);
	public static final Item IRON_ROD_DIE = new ItemBase("iron_rod_die", 1, 128);
	public static final Item REFINED_OBSIDIAN_GEAR_DIE = new ItemBase("refined_obsidian_gear_die", 1, 384);
	public static final Item REFINED_OBSIDIAN_PLATE_DIE = new ItemBase("refined_obsidian_plate_die", 1, 384);
	public static final Item REFINED_OBSIDIAN_ROD_DIE = new ItemBase("refined_obsidian_rod_die", 1, 384);

	//CIRCUIT BOARDS
	public static final Item BASIC_CIRCUIT_BOARD = new ItemBase("basic_circuit_board");
	public static final Item IMPROVED_CIRCUIT_BOARD = new ItemBase("improved_circuit_board");
	public static final Item ADVANCED_CIRCUIT_BOARD = new ItemBase("advanced_circuit_board");
	public static final Item ELITE_CIRCUIT_BOARD = new ItemBase("elite_circuit_board");
	public static final Item ULTIMATE_CIRCUIT_BOARD = new ItemBase("ultimate_circuit_board");
	public static final Item SUPREME_CIRCUIT_BOARD = new ItemBase("supreme_circuit_board");

	//BATTERIES
	public static final Item BASIC_BATTERY = configurable(new ItemBattery("basic_battery", BatteryTier.BASIC), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item IMPROVED_BATTERY = configurable(new ItemBattery("improved_battery", BatteryTier.IMPROVED), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item ADVANCED_BATTERY = configurable(new ItemBattery("advanced_battery", BatteryTier.ADVANCED), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item ELITE_BATTERY = configurable(new ItemBattery("elite_battery", BatteryTier.ELITE), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item ULTIMATE_BATTERY = configurable(new ItemBattery("ultimate_battery", BatteryTier.ULTIMATE), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item SUPREME_BATTERY = configurable(new ItemBattery("supreme_battery", BatteryTier.SUPREME), () -> ModConfig.ITEMS.enableBatteries);
	public static final Item CREATIVE_BATTERY = configurable(new ItemBattery("creative_battery", BatteryTier.CREATIVE), () -> ModConfig.ITEMS.enableBatteries);

	//CHEST UPGRADES
	public static final Item UPGRADE_LEAD_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_lead_chest", ChestTier.VANILLA), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_IRON_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_iron_chest", ChestTier.LEAD), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_INCONEL_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_inconel_chest", ChestTier.IRON), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_GOLD_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_gold_chest", ChestTier.INCONEL), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_ELECTRUM_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_electrum_chest", ChestTier.GOLD), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_DIAMOND_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_diamond_chest", ChestTier.ELECTRUM), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_OBSIDIAN_CLUSTER_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_obsidian_cluster_chest", ChestTier.DIAMOND), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_EMERALD_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_emerald_chest", ChestTier.OBSIDIAN_CLUSTER), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_COBALTSTEEL_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_cobaltsteel_chest", ChestTier.EMERALD), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_DARK_STEEL_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_dark_steel_chest", ChestTier.COBALTSTEEL), () -> ModConfig.BLOCKS.enableCustomChests);
	public static final Item UPGRADE_BLACK_OPAL_CHEST = configurable(new ItemUpgradeCustomChest("upgrade_black_opal_chest", ChestTier.DARK_STEEL), () -> ModConfig.BLOCKS.enableCustomChests);

	//MACHINE UPGRADES
	public static final Item MACHINE_UPGRADE = new ItemBase("machine_upgrade");
	public static final Item SPEED_UPGRADE = new ItemBase("speed_upgrade");
	public static final Item ENERGY_UPGRADE = new ItemBase("energy_upgrade");
	public static final Item XP_UPGRADE = new ItemBase("xp_upgrade");

	//BACKPACKS
	public static final Item BACKPACK = configurable(new ItemBackpack("backpack", BackpackTier.BASIC), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item LEAD_BACKPACK = configurable(new ItemBackpack("lead_backpack", BackpackTier.LEAD), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item IRON_BACKPACK = configurable(new ItemBackpack("iron_backpack", BackpackTier.IRON), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item INCONEL_BACKPACK = configurable(new ItemBackpack("inconel_backpack", BackpackTier.INCONEL), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item GOLDEN_BACKPACK = configurable(new ItemBackpack("golden_backpack", BackpackTier.GOLDEN), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item ELECTRUM_BACKPACK = configurable(new ItemBackpack("electrum_backpack", BackpackTier.ELECTRUM), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item DIAMOND_BACKPACK = configurable(new ItemBackpack("diamond_backpack", BackpackTier.DIAMOND), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item OBSIDIAN_CLUSTER_BACKPACK = configurable(new ItemBackpack("obsidian_cluster_backpack", BackpackTier.OBSIDIAN_CLUSTER), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item EMERALD_BACKPACK = configurable(new ItemBackpack("emerald_backpack", BackpackTier.EMERALD), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item COBALTSTEEL_BACKPACK = configurable(new ItemBackpack("cobaltsteel_backpack", BackpackTier.COBALTSTEEL), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item DARK_STEEL_BACKPACK = configurable(new ItemBackpack("dark_steel_backpack", BackpackTier.DARK_STEEL), () -> ModConfig.ITEMS.enableBackpacks);
	public static final Item BLACK_OPAL_BACKPACK = configurable(new ItemBackpack("black_opal_backpack", BackpackTier.BLACK_OPAL), () -> ModConfig.ITEMS.enableBackpacks);

	//GEMS
	public static final Item GRAPHITE = new ItemBase("graphite");
	public static final Item COKE = new ItemBase("coke");
	public static final Item RUBY = new ItemBase("ruby");
	public static final Item SAPPHIRE = new ItemBase("sapphire");
	public static final Item AMETHYST = new ItemBase("amethyst");
	public static final Item TOPAZ = new ItemBase("topaz");
	public static final Item PERIDOT = new ItemBase("peridot");
	public static final Item ONYX = new ItemBase("onyx");
	public static final Item BLACK_OPAL = new ItemBase("black_opal");
	public static final Item VOIDIUM = new ItemBase("voidium");

	//SHARDS
	public static final Item GLASS_SHARD = new ItemBase("glass_shard");
	public static final Item GLOWSTONE_SHARD = new ItemBase("glowstone_shard");
	public static final Item REDSTONE_SHARD = new ItemBase("redstone_shard");
	public static final Item AZURE_SHARD = new ItemBase("azure_shard");
	public static final Item ROSALITE_SHARD = new ItemBase("rosalite_shard");
	public static final Item VERDITE_SHARD = new ItemBase("verdite_shard");
	public static final Item LUMINOSITE_SHARD = new ItemBase("luminosite_shard");

	//CLUSTERS
	public static final Item OBSIDIAN_CLUSTER = new ItemBase("obsidian_cluster");

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
	public static final Item QUARTZ_NUGGET = new ItemBase("quartz_nugget");
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
	public static final Item GLOWSTONE_NUGGET = new ItemBase("glowstone_nugget");
	public static final Item INCONEL_NUGGET = new ItemBase("inconel_nugget");
	public static final Item URANIUM_NUGGET = new ItemBase("uranium_nugget");
	public static final Item TUNGSTEN_NUGGET = new ItemBase("tungsten_nugget");
	public static final Item PLATINUM_NUGGET = new ItemBase("platinum_nugget");
	public static final Item WHITE_GOLD_NUGGET = new ItemBase("white_gold_nugget");
	public static final Item ELECTRUM_NUGGET = new ItemBase("electrum_nugget");
	public static final Item IRIDIUM_NUGGET = new ItemBase("iridium_nugget");
	public static final Item TITANIUM_NUGGET = new ItemBase("titanium_nugget");
	public static final Item LAPIS_NUGGET = new ItemBase("lapis_nugget");
	public static final Item REDSTONE_NUGGET = new ItemBase("redstone_nugget");
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
	public static final Item AZURE_NUGGET = new ItemBase("azure_nugget");
	public static final Item ROSALITE_NUGGET = new ItemBase("rosalite_nugget");
	public static final Item VERDITE_NUGGET = new ItemBase("verdite_nugget");
	public static final Item VOIDIUM_NUGGET = new ItemBase("voidium_nugget");
	public static final Item LUMINOSITE_NUGGET = new ItemBase("luminosite_nugget");

	//DUSTS
	public static final Item ASHES = new ItemBase("ashes");
	public static final Item SALT = new ItemBase("salt");
	public static final Item SULFUR = new ItemBase("sulfur");
	public static final Item CLAY_DUST = new ItemBase("clay_dust");
	public static final Item WOOD_DUST = new ItemBase("wood_dust");
	public static final Item STONE_DUST = new ItemBase("stone_dust");
	public static final Item FLINT_DUST = new ItemBase("flint_dust");
	public static final Item ENDER_DUST = new ItemBase("ender_dust");
	public static final Item END_STONE_DUST = new ItemBase("end_stone_dust");
	public static final Item NETHERRACK_DUST = new ItemBase("netherrack_dust");
	public static final Item COAL_DUST = new ItemBase("coal_dust");
	public static final Item CHARCOAL_DUST = new ItemBase("charcoal_dust");
	public static final Item GRAPHITE_DUST = new ItemBase("graphite_dust");
	public static final Item CARBON_DUST = new ItemBase("carbon_dust");
	public static final Item COKE_DUST = new ItemBase("coke_dust");
	public static final Item IRON_DUST = new ItemBase("iron_dust");
	public static final Item QUARTZ_DUST = new ItemBase("quartz_dust");
	public static final Item GOLD_DUST = new ItemBase("gold_dust");
	public static final Item LAPIS_DUST = new ItemBase("lapis_dust");
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
	public static final Item AZURE_DUST = new ItemBase("azure_dust");
	public static final Item ROSALITE_DUST = new ItemBase("rosalite_dust");
	public static final Item VERDITE_DUST = new ItemBase("verdite_dust");
	public static final Item VOIDIUM_DUST = new ItemBase("voidium_dust");
	public static final Item LUMINOSITE_DUST = new ItemBase("luminosite_dust");

	//TINY DUSTS
	public static final Item SALT_TINY_DUST = new ItemBase("salt_tiny_dust");
	public static final Item SULFUR_TINY_DUST = new ItemBase("sulfur_tiny_dust");
	public static final Item CLAY_TINY_DUST = new ItemBase("clay_tiny_dust");
	public static final Item WOOD_TINY_DUST = new ItemBase("wood_tiny_dust");
	public static final Item STONE_TINY_DUST = new ItemBase("stone_tiny_dust");
	public static final Item FLINT_TINY_DUST = new ItemBase("flint_tiny_dust");
	public static final Item ENDER_TINY_DUST = new ItemBase("ender_tiny_dust");
	public static final Item END_STONE_TINY_DUST = new ItemBase("end_stone_tiny_dust");
	public static final Item NETHERRACK_TINY_DUST = new ItemBase("netherrack_tiny_dust");
	public static final Item COAL_TINY_DUST = new ItemBase("coal_tiny_dust");
	public static final Item CHARCOAL_TINY_DUST = new ItemBase("charcoal_tiny_dust");
	public static final Item GRAPHITE_TINY_DUST = new ItemBase("graphite_tiny_dust");
	public static final Item CARBON_TINY_DUST = new ItemBase("carbon_tiny_dust");
	public static final Item COKE_TINY_DUST = new ItemBase("coke_tiny_dust");
	public static final Item IRON_TINY_DUST = new ItemBase("iron_tiny_dust");
	public static final Item GLOWSTONE_TINY_DUST = new ItemBase("glowstone_tiny_dust");
	public static final Item QUARTZ_TINY_DUST = new ItemBase("quartz_tiny_dust");
	public static final Item GOLD_TINY_DUST = new ItemBase("gold_tiny_dust");
	public static final Item LAPIS_TINY_DUST = new ItemBase("lapis_tiny_dust");
	public static final Item REDSTONE_TINY_DUST = new ItemBase("redstone_tiny_dust");
	public static final Item DIAMOND_TINY_DUST = new ItemBase("diamond_tiny_dust");
	public static final Item EMERALD_TINY_DUST = new ItemBase("emerald_tiny_dust");
	public static final Item COPPER_TINY_DUST = new ItemBase("copper_tiny_dust");
	public static final Item TIN_TINY_DUST = new ItemBase("tin_tiny_dust");
	public static final Item BRONZE_TINY_DUST = new ItemBase("bronze_tiny_dust");
	public static final Item SILVER_TINY_DUST = new ItemBase("silver_tiny_dust");
	public static final Item LEAD_TINY_DUST = new ItemBase("lead_tiny_dust");
	public static final Item ZINC_TINY_DUST = new ItemBase("zinc_tiny_dust");
	public static final Item BRASS_TINY_DUST = new ItemBase("brass_tiny_dust");
	public static final Item ALUMINUM_TINY_DUST = new ItemBase("aluminum_tiny_dust");
	public static final Item DURALUMINUM_TINY_DUST = new ItemBase("duraluminum_tiny_dust");
	public static final Item REFINED_IRON_TINY_DUST = new ItemBase("refined_iron_tiny_dust");
	public static final Item STEEL_TINY_DUST = new ItemBase("steel_tiny_dust");
	public static final Item NICKEL_TINY_DUST = new ItemBase("nickel_tiny_dust");
	public static final Item CHROMIUM_TINY_DUST = new ItemBase("chromium_tiny_dust");
	public static final Item INCONEL_TINY_DUST = new ItemBase("inconel_tiny_dust");
	public static final Item URANIUM_TINY_DUST = new ItemBase("uranium_tiny_dust");
	public static final Item TUNGSTEN_TINY_DUST = new ItemBase("tungsten_tiny_dust");
	public static final Item PLATINUM_TINY_DUST = new ItemBase("platinum_tiny_dust");
	public static final Item WHITE_GOLD_TINY_DUST = new ItemBase("white_gold_tiny_dust");
	public static final Item ELECTRUM_TINY_DUST = new ItemBase("electrum_tiny_dust");
	public static final Item IRIDIUM_TINY_DUST = new ItemBase("iridium_tiny_dust");
	public static final Item TITANIUM_TINY_DUST = new ItemBase("titanium_tiny_dust");
	public static final Item RUBY_TINY_DUST = new ItemBase("ruby_tiny_dust");
	public static final Item SAPPHIRE_TINY_DUST = new ItemBase("sapphire_tiny_dust");
	public static final Item AMETHYST_TINY_DUST = new ItemBase("amethyst_tiny_dust");
	public static final Item TOPAZ_TINY_DUST = new ItemBase("topaz_tiny_dust");
	public static final Item PERIDOT_TINY_DUST = new ItemBase("peridot_tiny_dust");
	public static final Item REFINED_OBSIDIAN_TINY_DUST = new ItemBase("refined_obsidian_tiny_dust");
	public static final Item COBALT_TINY_DUST = new ItemBase("cobalt_tiny_dust");
	public static final Item COBALTSTEEL_TINY_DUST = new ItemBase("cobaltsteel_tiny_dust");
	public static final Item ONYX_TINY_DUST = new ItemBase("onyx_tiny_dust");
	public static final Item DARK_STEEL_TINY_DUST = new ItemBase("dark_steel_tiny_dust");
	public static final Item BLACK_OPAL_TINY_DUST = new ItemBase("black_opal_tiny_dust");
	public static final Item AZURE_TINY_DUST = new ItemBase("azure_tiny_dust");
	public static final Item ROSALITE_TINY_DUST = new ItemBase("rosalite_tiny_dust");
	public static final Item VERDITE_TINY_DUST = new ItemBase("verdite_tiny_dust");
	public static final Item VOIDIUM_TINY_DUST = new ItemBase("voidium_tiny_dust");
	public static final Item LUMINOSITE_TINY_DUST = new ItemBase("luminosite_tiny_dust");

	//CHUNKS
	public static final Item COAL_CHUNK = new ItemBase("coal_chunk");
	public static final Item IRON_CHUNK = new ItemBase("iron_chunk");
	public static final Item QUARTZ_CHUNK = new ItemBase("quartz_chunk");
	public static final Item LAPIS_CHUNK = new ItemBase("lapis_chunk");
	public static final Item REDSTONE_CHUNK = new ItemBase("redstone_chunk");
	public static final Item GOLD_CHUNK = new ItemBase("gold_chunk");
	public static final Item DIAMOND_CHUNK = new ItemBase("diamond_chunk");
	public static final Item EMERALD_CHUNK = new ItemBase("emerald_chunk");
	public static final Item COPPER_CHUNK = new ItemBase("copper_chunk");
	public static final Item TIN_CHUNK = new ItemBase("tin_chunk");
	public static final Item SILVER_CHUNK = new ItemBase("silver_chunk");
	public static final Item LEAD_CHUNK = new ItemBase("lead_chunk");
	public static final Item ZINC_CHUNK = new ItemBase("zinc_chunk");
	public static final Item ALUMINUM_CHUNK = new ItemBase("aluminum_chunk");
	public static final Item NICKEL_CHUNK = new ItemBase("nickel_chunk");
	public static final Item CHROMIUM_CHUNK = new ItemBase("chromium_chunk");
	public static final Item URANIUM_CHUNK = new ItemBase("uranium_chunk");
	public static final Item TUNGSTEN_CHUNK = new ItemBase("tungsten_chunk");
	public static final Item PLATINUM_CHUNK = new ItemBase("platinum_chunk");
	public static final Item IRIDIUM_CHUNK = new ItemBase("iridium_chunk");
	public static final Item TITANIUM_CHUNK = new ItemBase("titanium_chunk");
	public static final Item RUBY_CHUNK = new ItemBase("ruby_chunk");
	public static final Item SAPPHIRE_CHUNK = new ItemBase("sapphire_chunk");
	public static final Item AMETHYST_CHUNK = new ItemBase("amethyst_chunk");
	public static final Item TOPAZ_CHUNK = new ItemBase("topaz_chunk");
	public static final Item PERIDOT_CHUNK = new ItemBase("peridot_chunk");
	public static final Item COBALT_CHUNK = new ItemBase("cobalt_chunk");
	public static final Item ONYX_CHUNK = new ItemBase("onyx_chunk");
	public static final Item BLACK_OPAL_CHUNK = new ItemBase("black_opal_chunk");
	public static final Item AZURE_CHUNK = new ItemBase("azure_chunk");
	public static final Item ROSALITE_CHUNK = new ItemBase("rosalite_chunk");
	public static final Item VERDITE_CHUNK = new ItemBase("verdite_chunk");
	public static final Item VOIDIUM_CHUNK = new ItemBase("voidium_chunk");
	public static final Item LUMINOSITE_CHUNK = new ItemBase("luminosite_chunk");

	//GEARS
	public static final Item WOOD_GEAR = new ItemBase("wood_gear");
	public static final Item STONE_GEAR = new ItemBase("stone_gear");
	public static final Item COAL_GEAR = new ItemBase("coal_gear");
	public static final Item CHARCOAL_GEAR = new ItemBase("charcoal_gear");
	public static final Item IRON_GEAR = new ItemBase("iron_gear");
	public static final Item QUARTZ_GEAR = new ItemBase("quartz_gear");
	public static final Item GOLD_GEAR = new ItemBase("gold_gear");
	public static final Item DIAMOND_GEAR = new ItemBase("diamond_gear");
	public static final Item EMERALD_GEAR = new ItemBase("emerald_gear");
	public static final Item REDSTONE_GEAR = new ItemBase("redstone_gear");
	public static final Item LAPIS_GEAR = new ItemBase("lapis_gear");
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
	public static final Item GLOWSTONE_GEAR = new ItemBase("glowstone_gear");
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
	public static final Item AZURE_GEAR = new ItemBase("azure_gear");
	public static final Item ROSALITE_GEAR = new ItemBase("rosalite_gear");
	public static final Item VERDITE_GEAR = new ItemBase("verdite_gear");
	public static final Item VOIDIUM_GEAR = new ItemBase("voidium_gear");
	public static final Item LUMINOSITE_GEAR = new ItemBase("luminosite_gear");

	//PLATES
	public static final Item WOOD_PLATE = new ItemBase("wood_plate");
	public static final Item STONE_PLATE = new ItemBase("stone_plate");
	public static final Item COAL_PLATE = new ItemBase("coal_plate");
	public static final Item CHARCOAL_PLATE = new ItemBase("charcoal_plate");
	public static final Item IRON_PLATE = new ItemBase("iron_plate");
	public static final Item QUARTZ_PLATE = new ItemBase("quartz_plate");
	public static final Item GOLD_PLATE = new ItemBase("gold_plate");
	public static final Item DIAMOND_PLATE = new ItemBase("diamond_plate");
	public static final Item EMERALD_PLATE = new ItemBase("emerald_plate");
	public static final Item REDSTONE_PLATE = new ItemBase("redstone_plate");
	public static final Item LAPIS_PLATE = new ItemBase("lapis_plate");
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
	public static final Item GLOWSTONE_PLATE = new ItemBase("glowstone_plate");
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
	public static final Item AZURE_PLATE = new ItemBase("azure_plate");
	public static final Item ROSALITE_PLATE = new ItemBase("rosalite_plate");
	public static final Item VERDITE_PLATE = new ItemBase("verdite_plate");
	public static final Item VOIDIUM_PLATE = new ItemBase("voidium_plate");
	public static final Item LUMINOSITE_PLATE = new ItemBase("luminosite_plate");

	//RODS
	public static final Item STONE_ROD = new ItemBase("stone_rod");
	public static final Item COAL_ROD = new ItemBase("coal_rod");
	public static final Item CHARCOAL_ROD = new ItemBase("charcoal_rod");
	public static final Item IRON_ROD = new ItemBase("iron_rod");
	public static final Item QUARTZ_ROD = new ItemBase("quartz_rod");
	public static final Item GOLD_ROD = new ItemBase("gold_rod");
	public static final Item DIAMOND_ROD = new ItemBase("diamond_rod");
	public static final Item EMERALD_ROD = new ItemBase("emerald_rod");
	public static final Item REDSTONE_ROD = new ItemBase("redstone_rod");
	public static final Item LAPIS_ROD = new ItemBase("lapis_rod");
	public static final Item COPPER_ROD = new ItemBase("copper_rod");
	public static final Item TIN_ROD = new ItemBase("tin_rod");
	public static final Item BRONZE_ROD = new ItemBase("bronze_rod");
	public static final Item SILVER_ROD = new ItemBase("silver_rod");
	public static final Item LEAD_ROD = new ItemBase("lead_rod");
	public static final Item ZINC_ROD = new ItemBase("zinc_rod");
	public static final Item BRASS_ROD = new ItemBase("brass_rod");
	public static final Item ALUMINUM_ROD = new ItemBase("aluminum_rod");
	public static final Item DURALUMINUM_ROD = new ItemBase("duraluminum_rod");
	public static final Item REFINED_IRON_ROD = new ItemBase("refined_iron_rod");
	public static final Item STEEL_ROD = new ItemBase("steel_rod");
	public static final Item NICKEL_ROD = new ItemBase("nickel_rod");
	public static final Item CHROMIUM_ROD = new ItemBase("chromium_rod");
	public static final Item GLOWSTONE_ROD = new ItemBase("glowstone_rod");
	public static final Item INCONEL_ROD = new ItemBase("inconel_rod");
	public static final Item URANIUM_ROD = new ItemBase("uranium_rod");
	public static final Item TUNGSTEN_ROD = new ItemBase("tungsten_rod");
	public static final Item PLATINUM_ROD = new ItemBase("platinum_rod");
	public static final Item WHITE_GOLD_ROD = new ItemBase("white_gold_rod");
	public static final Item ELECTRUM_ROD = new ItemBase("electrum_rod");
	public static final Item IRIDIUM_ROD = new ItemBase("iridium_rod");
	public static final Item TITANIUM_ROD = new ItemBase("titanium_rod");
	public static final Item RUBY_ROD = new ItemBase("ruby_rod");
	public static final Item SAPPHIRE_ROD = new ItemBase("sapphire_rod");
	public static final Item AMETHYST_ROD = new ItemBase("amethyst_rod");
	public static final Item TOPAZ_ROD = new ItemBase("topaz_rod");
	public static final Item PERIDOT_ROD = new ItemBase("peridot_rod");
	public static final Item REFINED_OBSIDIAN_ROD = new ItemBase("refined_obsidian_rod");
	public static final Item OBSIDIAN_CLUSTER_ROD = new ItemBase("obsidian_cluster_rod");
	public static final Item COBALT_ROD = new ItemBase("cobalt_rod");
	public static final Item COBALTSTEEL_ROD = new ItemBase("cobaltsteel_rod");
	public static final Item ONYX_ROD = new ItemBase("onyx_rod");
	public static final Item DARK_STEEL_ROD = new ItemBase("dark_steel_rod");
	public static final Item BLACK_OPAL_ROD = new ItemBase("black_opal_rod");
	public static final Item AZURE_ROD = new ItemBase("azure_rod");
	public static final Item ROSALITE_ROD = new ItemBase("rosalite_rod");
	public static final Item VERDITE_ROD = new ItemBase("verdite_rod");
	public static final Item VOIDIUM_ROD = new ItemBase("voidium_rod");
	public static final Item LUMINOSITE_ROD = new ItemBase("luminosite_rod");

    private static <T extends Item> T configurable(T item, BooleanSupplier condition)
    {
        ENABLE_CONDITIONS.put(item, condition);
        return item;
    }

    public static boolean isEnabled(Item item)
    {
        BooleanSupplier condition = ENABLE_CONDITIONS.get(item);
        return condition == null || condition.getAsBoolean();
    }
}