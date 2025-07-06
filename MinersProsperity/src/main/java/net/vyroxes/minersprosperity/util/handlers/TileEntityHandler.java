package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.tileentities.*;

public class TileEntityHandler
{
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, new ResourceLocation(Tags.MODID + ":alloy_furnace"));
		GameRegistry.registerTileEntity(TileEntityBasicSolarPanel.class, new ResourceLocation(Tags.MODID + ":basic_solar_panel"));
		GameRegistry.registerTileEntity(TileEntityImprovedSolarPanel.class, new ResourceLocation(Tags.MODID + ":improved_solar_panel"));
		GameRegistry.registerTileEntity(TileEntityAdvancedSolarPanel.class, new ResourceLocation(Tags.MODID + ":advanced_solar_panel"));
		GameRegistry.registerTileEntity(TileEntityEliteSolarPanel.class, new ResourceLocation(Tags.MODID + ":elite_solar_panel"));
		GameRegistry.registerTileEntity(TileEntityUltimateSolarPanel.class, new ResourceLocation(Tags.MODID + ":ultimate_solar_panel"));
		GameRegistry.registerTileEntity(TileEntitySupremeSolarPanel.class, new ResourceLocation(Tags.MODID + ":supreme_solar_panel"));
		GameRegistry.registerTileEntity(TileEntityCreativeSolarPanel.class, new ResourceLocation(Tags.MODID + ":creative_solar_panel"));
	}
}