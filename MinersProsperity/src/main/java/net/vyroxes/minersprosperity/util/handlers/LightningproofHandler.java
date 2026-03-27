package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

@Mod.EventBusSubscriber
public class LightningproofHandler
{
    @SubscribeEvent
    public static void onPlayerAttacked(LivingAttackEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityPlayer player))
            return;

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (!boots.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.LIGHTNINGPROOF, boots) > 0 && event.getSource().damageType.equals("lightningBolt"))
        {
            event.setCanceled(true);
            player.extinguish();
        }
    }
}