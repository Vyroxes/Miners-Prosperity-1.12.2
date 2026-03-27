package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

@Mod.EventBusSubscriber
public class FlameproofHandler
{
    @SubscribeEvent
    public static void onPlayerAttacked(LivingAttackEvent event)
    {
        if (!(event.getEntityLiving() instanceof EntityPlayer player))
            return;

        if (!event.getSource().isFireDamage())
            return;

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (!chest.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.FLAMEPROOF, chest) > 0)
        {
            event.setCanceled(true);
            player.extinguish();
        }
    }
}