package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;

public class TileEntityHandler
{
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, new ResourceLocation(Reference.MODID + ":alloy_furnace"));
	}
}