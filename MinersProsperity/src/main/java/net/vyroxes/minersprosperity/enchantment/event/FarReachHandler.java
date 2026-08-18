package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import net.vyroxes.minersprosperity.config.ModConfig;

@Mod.EventBusSubscriber
public class FarReachHandler
{
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP player)) return;

        if (event.getSlot() != EntityEquipmentSlot.LEGS) return;

        ItemStack leggings = event.getTo();
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FAR_REACH, leggings);

        double reach = (level > 0) ? ModConfig.ENCHANTMENTS.farReachDistance : 5.0;

        player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(reach);
    }
}