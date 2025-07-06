package net.vyroxes.minersprosperity.util.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;

public class ConfigHandler
{
	public static Configuration config;

	public static boolean generateCopperOre = true;
	public static int minHeightCopperOre = 0;
	public static int maxHeightCopperOre = 64;
	public static int clusterCountCopperOre = 8;
	public static int clusterSizeCopperOre = 8;

	public static boolean generateTinOre = true;
	public static int minHeightTinOre = 0;
	public static int maxHeightTinOre = 64;
	public static int clusterCountTinOre = 7;
	public static int clusterSizeTinOre = 8;

	public static boolean generateSilverOre = true;
	public static int minHeightSilverOre = 0;
	public static int maxHeightSilverOre = 64;
	public static int clusterCountSilverOre = 7;
	public static int clusterSizeSilverOre = 7;

	public static boolean generateLeadOre = true;
	public static int minHeightLeadOre = 0;
	public static int maxHeightLeadOre = 64;
	public static int clusterCountLeadOre = 6;
	public static int clusterSizeLeadOre = 7;

	public static boolean allowBackpackLooping = false;
	public static List<String> backpackItemBlacklist = new ArrayList<>();

	public static boolean armorOverlay = true;
	public static boolean toughnessOverlay = true;
	public static boolean armorOverlayTexture = true;
	public static boolean toughnessOverlayTexture = true;
	public static int armorX = -91;
	public static int armorY = -49;
	public static int toughnessX = 10;
	public static int toughnessY = -49;
	public static float colorRed21to40 = 1.0f;
	public static float colorGreen21to40 = 1.0f;
	public static float colorBlue21to40 = 0.0f;
	public static float colorRed41to60 = 0.1f;
	public static float colorGreen41to60 = 0.7f;
	public static float colorBlue41to60 = 1.0f;
	public static float colorRed61to80 = 0.0f;
	public static float colorGreen61to80 = 0.8f;
	public static float colorBlue61to80 = 0.3f;
	public static float colorRed81to100 = 0.6f;
	public static float colorGreen81to100 = 0.0f;
	public static float colorBlue81to100 = 0.8f;
	public static float colorRed101to120 = 1.0f;
	public static float colorGreen101to120 = 0.0f;
	public static float colorBlue101to120 = 0.0f;
	public static float colorRed121to140 = 0.4f;
	public static float colorGreen121to140 = 0.4f;
	public static float colorBlue121to140 = 0.4f;

	public static boolean quickMining = true;
	public static boolean glow = true;
	public static boolean unrestricted = true;
	public static boolean autoCollect = true;
	public static boolean farReach = true;

    public static int oreDictInTooltips = 2;
	public static boolean customTooltips = true;
    
    public static void init(File file)
    {
    	config = new Configuration(file);

		String categoryOres = "Ores";
		config.addCustomCategoryComment(categoryOres, "Ores configuration");
		generateCopperOre = config.getBoolean("Generate Copper Ore", categoryOres, true, "Set whether you want to generate Copper Ore");
		minHeightCopperOre = config.getInt("Copper Ore Min Height", categoryOres, 0, 0, 255, "Set min height for generating Copper Ore");
		maxHeightCopperOre = config.getInt("Copper Ore Max Height", categoryOres, 64, 0, 255, "Set max height for generating Copper Ore");
		clusterCountCopperOre = config.getInt("Copper Ore Cluster Count", categoryOres, 8, 1, 100, "Set cluster count for generating Copper Ore");
		clusterSizeCopperOre = config.getInt("Copper Ore Cluster Size", categoryOres, 8, 1, 100, "Set cluster size for generating Copper Ore");

		generateTinOre = config.getBoolean("Generate Tin Ore", categoryOres, true, "Set whether you want to generate Tin Ore");
		minHeightTinOre = config.getInt("Tin Ore Min Height", categoryOres, 0, 0, 255, "Set min height for generating Tin Ore");
		maxHeightTinOre = config.getInt("Tin Ore Max Height", categoryOres, 64, 0, 255, "Set max height for generating Tin Ore");
		clusterCountTinOre = config.getInt("Tin Ore Cluster Count", categoryOres, 7, 1, 100, "Set cluster count for generating Tin Ore");
		clusterSizeTinOre = config.getInt("Tin Ore Cluster Size", categoryOres, 8, 1, 100, "Set cluster size for generating Tin Ore");

		generateSilverOre = config.getBoolean("Generate Silver Ore", categoryOres, true, "Set whether you want to generate Silver Ore");
		minHeightSilverOre = config.getInt("Silver Ore Min Height", categoryOres, 0, 0, 255, "Set min height for generating Silver Ore");
		maxHeightSilverOre = config.getInt("Silver Ore Max Height", categoryOres, 64, 0, 255, "Set max height for generating Silver Ore");
		clusterCountSilverOre = config.getInt("Silver Ore Cluster Count", categoryOres, 7, 1, 100, "Set cluster count for generating Silver Ore");
		clusterSizeSilverOre = config.getInt("Silver Ore Cluster Size", categoryOres, 7, 1, 100, "Set cluster size for generating Silver Ore");

		generateLeadOre = config.getBoolean("Generate Lead Ore", categoryOres, true, "Set whether you want to generate Lead Ore");
		minHeightLeadOre = config.getInt("Lead Ore Min Height", categoryOres, 0, 0, 255, "Set min height for generating Lead Ore");
		maxHeightLeadOre = config.getInt("Lead Ore Max Height", categoryOres, 64, 0, 255, "Set max height for generating Lead Ore");
		clusterCountLeadOre = config.getInt("Lead Ore Cluster Count", categoryOres, 6, 1, 100, "Set cluster count for generating Lead Ore");
		clusterSizeLeadOre = config.getInt("Lead Ore Cluster Size", categoryOres, 7, 1, 100, "Set cluster size for generating Lead Ore");

		String categoryBackpacks = "Backpacks";
		config.addCustomCategoryComment(categoryBackpacks, "Backpacks configuration");
		allowBackpackLooping = config.getBoolean("Allow Backpack Looping", categoryBackpacks, false, "If true, backpacks can be placed inside other backpacks (looping allowed)");
		String[] blacklistArray  = config.getStringList("Backpack Item Blacklist", categoryBackpacks, new String[] {
				"modid:example_item/metadata",
				"mod:item",
				"mod:item/2"
		}, "List of item registry names that are blacklisted from being placed in backpacks");
		backpackItemBlacklist.clear();
        Collections.addAll(backpackItemBlacklist, blacklistArray);

		String categoryGui = "GUI";
		config.addCustomCategoryComment(categoryGui, "GUI configuration");
		String categoryArmorAndArmorToughnessOverlay = categoryGui + ".Armor/armor toughness overlay";
		config.addCustomCategoryComment(categoryArmorAndArmorToughnessOverlay, "Enable or disable the custom armor and armor toughness overlay");
		armorOverlay = config.getBoolean("Armor Overlay", categoryArmorAndArmorToughnessOverlay, true, "Enable or disable the custom armor overlay");
		toughnessOverlay = config.getBoolean("Armor Toughness Overlay", categoryArmorAndArmorToughnessOverlay, true, "Enable or disable the custom armor toughness overlay");
		armorOverlayTexture = config.getBoolean("Armor Overlay Texture", categoryArmorAndArmorToughnessOverlay, true, "Use custom texture for the armor overlay instead of recoloring icons");
		toughnessOverlayTexture = config.getBoolean("Armor Toughness Overlay Texture", categoryArmorAndArmorToughnessOverlay, true, "Use custom texture for the armor toughness overlay instead of recoloring icons");

		String categoryToughnessOverlayPosition = categoryGui + ".Armor/armor toughness overlay position";
		config.addCustomCategoryComment(categoryToughnessOverlayPosition, "Armor/armor toughness overlay x and y position");
		armorX = config.getInt("Armor Overlay X Position", categoryToughnessOverlayPosition, -91, -10000, 10000, "X position of armor overlay");
		armorY = config.getInt("Armor Overlay Y Position", categoryToughnessOverlayPosition, -49, -10000, 10000, "Y position of armor overlay");
		toughnessX = config.getInt("Armor toughness Overlay X Position", categoryToughnessOverlayPosition, 10, -10000, 10000, "X position of armor toughness overlay (10 - above food overlay, -91 - above armor overlay)");
		toughnessY = config.getInt("Armor toughness Overlay Y Position", categoryToughnessOverlayPosition, -49, -10000, 10000, "Y position of armor toughness overlay (-49 - above food overlay, -59 - above armor overlay)");

		String category21to40 = categoryGui + ".Armor/armor toughness values 21-40";
		config.addCustomCategoryComment(category21to40, "Armor/armor toughness overlay colors for armor/armor toughness values 21-40");
		colorRed21to40 = config.getFloat("Color Red", category21to40, 1.0f, 0.0f, 1.0f, "Red component (21-40)");
		colorGreen21to40 = config.getFloat("Color Green", category21to40, 1.0f, 0.0f, 1.0f, "Green component (21-40)");
		colorBlue21to40 = config.getFloat("Color Blue", category21to40, 0.0f, 0.0f, 1.0f, "Blue component (21-40)");

		String category41to60 = categoryGui + ".Armor/armor toughness values 41-60";
		config.addCustomCategoryComment(category41to60, "Armor/armor toughness overlay colors for armor/armor toughness values 41-60");
		colorRed41to60 = config.getFloat("Color Red", category41to60, 0.1f, 0.0f, 1.0f, "Red component (41-60)");
		colorGreen41to60 = config.getFloat("Color Green", category41to60, 0.7f, 0.0f, 1.0f, "Green component (41-60)");
		colorBlue41to60 = config.getFloat("Color Blue", category41to60, 1.0f, 0.0f, 1.0f, "Blue component (41-60)");

		String category61to80 = categoryGui + ".Armor/armor toughness values 61-80";
		config.addCustomCategoryComment(category61to80, "Armor/armor toughness overlay colors for armor/armor toughness values 61-80");
		colorRed61to80 = config.getFloat("Color Red", category61to80, 0.0f, 0.0f, 1.0f, "Red component (61-80)");
		colorGreen61to80 = config.getFloat("Color Green", category61to80, 0.8f, 0.0f, 1.0f, "Green component (61-80)");
		colorBlue61to80 = config.getFloat("Color Blue", category61to80, 0.3f, 0.0f, 1.0f, "Blue component (61-80)");

		String category81to100 = categoryGui + ".Armor/armor toughness values 81-100";
		config.addCustomCategoryComment(category81to100, "Armor/armor toughness overlay colors for armor/armor toughness values 81-100");
		colorRed81to100 = config.getFloat("Color Red", category81to100, 0.6f, 0.0f, 1.0f, "Red component (81-100)");
		colorGreen81to100 = config.getFloat("Color Green", category81to100, 0.0f, 0.0f, 1.0f, "Green component (81-100)");
		colorBlue81to100 = config.getFloat("Color Blue", category81to100, 0.8f, 0.0f, 1.0f, "Blue component (81-100)");

		String category101to120 = categoryGui + ".Armor/armor toughness values 101-120";
		config.addCustomCategoryComment(category101to120, "Armor/armor toughness overlay colors for armor/armor toughness values 101-120");
		colorRed101to120 = config.getFloat("Color Red", category101to120, 1.0f, 0.0f, 1.0f, "Red component (101-120)");
		colorGreen101to120 = config.getFloat("Color Green", category101to120, 0.0f, 0.0f, 1.0f, "Green component (101-120)");
		colorBlue101to120 = config.getFloat("Color Blue", category101to120, 0.0f, 0.0f, 1.0f, "Blue component (101-120)");

		String category121to140 = categoryGui + ".Armor/armor toughness values 121-140";
		config.addCustomCategoryComment(category121to140, "Armor/armor toughness overlay colors for armor/armor toughness values 121-140");
		colorRed121to140 = config.getFloat("Color Red", category121to140, 0.4f, 0.0f, 1.0f, "Red component (121-140)");
		colorGreen121to140 = config.getFloat("Color Green", category121to140, 0.4f, 0.0f, 1.0f, "Green component (121-140)");
		colorBlue121to140 = config.getFloat("Color Blue", category121to140, 0.4f, 0.0f, 1.0f, "Blue component (121-140)");

		String categoryEnchantments = "Enchantments";
		config.addCustomCategoryComment(categoryEnchantments, "Enchantments configuration");
		quickMining = config.getBoolean("Quick Mining Enchantment", categoryEnchantments, true, "Enable or disable Quick Mining enchantment");
		glow = config.getBoolean("Glow Enchantment", categoryEnchantments, true, "Enable or disable Glow enchantment");
		unrestricted = config.getBoolean("Unrestricted Enchantment", categoryEnchantments, true, "Enable or disable Unrestricted enchantment");
		autoCollect = config.getBoolean("Auto-Collect Enchantment", categoryEnchantments, true, "Enable or disable Auto-Collect enchantment");
		farReach = config.getBoolean("Far Reach Enchantment", categoryEnchantments, true, "Enable or disable Far Reach enchantment");

		String categoryTooltips = "Tooltips";
    	config.addCustomCategoryComment(categoryTooltips, "Tooltips configuration");
    	oreDictInTooltips = config.getInt("Show OreDict In Tooltips", categoryTooltips, 2, 0, 2, "Set whether you want to show OreDict in tooltips: 0 - off, 1 - on, 2 - only in advanced tooltips mode (F3+H)");
		customTooltips = config.getBoolean("Show Custom Tooltips", categoryTooltips, true, "Enable or disable tools' custom tooltips");

    	config.save();
    }
    
    public static void registerConfig(FMLPreInitializationEvent event)
    {
    	MinersProsperity.config = new File(event.getModConfigurationDirectory() + "/" + Tags.MODID);
    	init(new File(MinersProsperity.config.getPath(), Tags.MODID + ".cfg"));
    }
}