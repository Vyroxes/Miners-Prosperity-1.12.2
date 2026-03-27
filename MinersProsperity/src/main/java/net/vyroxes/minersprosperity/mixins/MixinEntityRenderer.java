package net.vyroxes.minersprosperity.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer
{
    @Inject(method = "getMouseOver", at = @At("HEAD"), cancellable = true)
    private void onGetMouseOver(float partialTicks, CallbackInfo ci)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        if (player == null)
            return;

        ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.FAR_REACH, leggings);

        if (level <= 0)
            return;

        double reach = ModConfig.ENCHANTMENTS.farReachDistance;
        Vec3d eyePosition = player.getPositionEyes(partialTicks);
        Vec3d lookVector = player.getLook(partialTicks);
        Vec3d reachVector = eyePosition.add(lookVector.scale(reach));

        Entity pointedEntity = null;
        Vec3d hitVec = null;
        double minDistance = reach;

        List<Entity> entities = mc.world.getEntitiesWithinAABBExcludingEntity(
                player,
                player.getEntityBoundingBox().expand(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach).grow(1.0D, 1.0D, 1.0D)
        );

        for (Entity entity : entities)
        {
            if (!entity.canBeCollidedWith() || entity instanceof EntityItem)
                continue;

            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            RayTraceResult intercept = aabb.calculateIntercept(eyePosition, reachVector);

            if (aabb.contains(eyePosition))
            {
                if (0.0D < minDistance || minDistance == 0.0D)
                {
                    pointedEntity = entity;
                    hitVec = eyePosition;
                    minDistance = 0.0D;
                }
            }
            else if (intercept != null)
            {
                double distanceTo = eyePosition.distanceTo(intercept.hitVec);

                if (distanceTo < minDistance || minDistance == 0.0D)
                {
                    pointedEntity = entity;
                    hitVec = intercept.hitVec;
                    minDistance = distanceTo;
                }
            }
        }

        RayTraceResult blockTrace = player.rayTrace(reach, partialTicks);
        if (blockTrace != null && (pointedEntity == null || eyePosition.distanceTo(blockTrace.hitVec) < minDistance)) {
            mc.objectMouseOver = blockTrace;
            mc.pointedEntity = null;
            ci.cancel();
            return;
        }

        if (pointedEntity != null && hitVec != null)
        {
            mc.objectMouseOver = new RayTraceResult(pointedEntity, hitVec);
            mc.pointedEntity = pointedEntity;
            ci.cancel();
        }
    }
}