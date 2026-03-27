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
		GameRegistry.registerTileEntity(TileEntityMetalPress.class, new ResourceLocation(Tags.MODID + ":metal_press"));
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, new ResourceLocation(Tags.MODID + ":electric_furnace"));
		GameRegistry.registerTileEntity(TileEntitySolarPanel.class, new ResourceLocation(Tags.MODID + ":solar_panel"));
	}
}