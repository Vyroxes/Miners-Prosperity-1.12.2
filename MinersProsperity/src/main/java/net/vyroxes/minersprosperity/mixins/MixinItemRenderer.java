package net.vyroxes.minersprosperity.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer
{
    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void cancelFireOverlay(CallbackInfo ci)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
        {
            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (!chest.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.FLAMEPROOF, chest) > 0)
            {
                ci.cancel();
            }
        }
    }
}