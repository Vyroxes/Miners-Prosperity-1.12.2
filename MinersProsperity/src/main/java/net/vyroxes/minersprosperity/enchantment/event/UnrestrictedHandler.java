package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class UnrestrictedHandler
{
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player == null) return;

        ItemStack chestplate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.UNRESTRICTED, chestplate) <= 0) return;

        float speed = event.getNewSpeed();

        if (!player.onGround) speed *= 5.0F;

        if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player)) speed *= 5.0F;

        event.setNewSpeed(speed);
    }
}