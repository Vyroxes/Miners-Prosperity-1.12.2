package net.vyroxes.minersprosperity.world.gen;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class WorldGeneration {
    private WorldGeneration() {}

    public static void register() {
        GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenResin(), 0);
        GameRegistry.registerWorldGenerator(new WorldGenCrudeOil(), 1);
    }
}