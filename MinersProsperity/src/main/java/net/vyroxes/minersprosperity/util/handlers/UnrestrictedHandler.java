package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

@Mod.EventBusSubscriber
public class UnrestrictedHandler
{
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player == null) return;

        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        boolean hasUnrestricted = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.UNRESTRICTED, chestplate) > 0;

        if (!hasUnrestricted) return;

        if (!player.onGround)
        {
            float oldSpeed = event.getOriginalSpeed();
            float newSpeed = event.getNewSpeed();

            if (oldSpeed < newSpeed * 5F)
            {
                event.setNewSpeed(newSpeed * 5.0F);
            }
        }

        if (player.isInsideOfMaterial(Material.WATER))
        {
            event.setNewSpeed(event.getNewSpeed() * 5.0F);
        }
    }
}