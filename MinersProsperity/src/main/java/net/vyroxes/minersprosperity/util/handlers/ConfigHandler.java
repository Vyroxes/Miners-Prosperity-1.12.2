package net.vyroxes.minersprosperity.util.handlers;

import java.io.File;

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
    
    public static int showOreDictInTooltips = 1;
	public static int showHarvestLevelInTooltips = 1;
    
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

    	String categoryOther = "Other";
    	config.addCustomCategoryComment(categoryOther, "Other configuration");
    	showOreDictInTooltips = config.getInt("Show OreDict In Tooltips", categoryOther, 1, 0, 2, "Set whether you want to show OreDict in tooltipsL 0 - off, 1 - on, 2 - only in advanced tooltips mode (F3+H)");
		showHarvestLevelInTooltips = config.getInt("Show Harvest Level In Tooltips", categoryOther, 1, 0, 2, "Set whether you want to show tools' harvest level in tooltips: 0 - off, 1 - on, 2 - only in advanced tooltips mode (F3+H)");

    	config.save();
    }
    
    public static void registerConfig(FMLPreInitializationEvent event)
    {
    	MinersProsperity.config = new File(event.getModConfigurationDirectory() + "/" + Tags.MODID);
    	MinersProsperity.config.mkdirs();
    	init(new File(MinersProsperity.config.getPath(), Tags.MODID + ".cfg"));
    }
}