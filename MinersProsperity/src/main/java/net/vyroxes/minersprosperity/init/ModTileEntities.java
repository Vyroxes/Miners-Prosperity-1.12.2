package net.vyroxes.minersprosperity.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.tile.*;

public final class ModTileEntities
{
    private ModTileEntities() {}

    public static void register() {
        if (ModConfig.MACHINES.enableAlloyFurnace) register(TileEntityAlloyFurnace.class, "alloy_furnace");
        if (ModConfig.MACHINES.enablePulverizer) register(TileEntityPulverizer.class, "pulverizer");
        if (ModConfig.MACHINES.enableMetalPress) register(TileEntityMetalPress.class, "metal_press");
        if (ModConfig.MACHINES.enableElectricFurnace) register(TileEntityElectricFurnace.class, "electric_furnace");
        if (ModConfig.MACHINES.enableSolarPanels) register(TileEntitySolarPanel.class, "solar_panel");
        if (ModConfig.BLOCKS.enableCustomChests) register(TileEntityCustomChest.class, "chest");
    }

    private static void register(Class<? extends net.minecraft.tileentity.TileEntity> type, String name) {
        GameRegistry.registerTileEntity(type, new ResourceLocation(Tags.MODID, name));
    }
}