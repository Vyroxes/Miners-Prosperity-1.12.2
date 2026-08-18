package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class ImmovableHandler
{
    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityPlayer player))
            return;

        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        boolean hasImmovable = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.IMMOVABLE, chestplate) > 0;

        if (!hasImmovable) return;

        event.setStrength(0.0F);
    }
}