package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class WitherproofHandler
{
    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityPlayer player))
            return;

        ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

        if (!leggings.isEmpty() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.WITHERPROOF, leggings) > 0)
        {
            if (player.isPotionActive(MobEffects.WITHER))
            {
                player.removeActivePotionEffect(MobEffects.WITHER);
            }
        }
    }
}