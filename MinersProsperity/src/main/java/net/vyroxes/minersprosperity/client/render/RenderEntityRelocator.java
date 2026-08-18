package net.vyroxes.minersprosperity.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.vyroxes.minersprosperity.entity.projectile.EntityRelocatorProjectile;
import net.vyroxes.minersprosperity.init.ModItems;

public class RenderEntityRelocator extends RenderSnowball<EntityRelocatorProjectile> {
    public RenderEntityRelocator(RenderManager renderManager)
    {
        super(renderManager, ModItems.ENTITY_RELOCATOR, Minecraft.getMinecraft().getRenderItem());
    }
}