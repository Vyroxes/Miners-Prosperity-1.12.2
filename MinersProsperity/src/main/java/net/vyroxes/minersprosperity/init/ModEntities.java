package net.vyroxes.minersprosperity.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.entity.projectile.EntityRelocatorProjectile;

public final class ModEntities {
    private static int entityId = 0;

    private ModEntities() {}

    public static void register()
    {
        if (ModConfig.ITEMS.enableEntityRelocator) EntityRegistry.registerModEntity(new ResourceLocation(Tags.MODID, "entity_relocator_projectile"), EntityRelocatorProjectile.class, "entity_relocator_projectile", entityId++, MinersProsperity.INSTANCE, 64, 10, true);
    }
}