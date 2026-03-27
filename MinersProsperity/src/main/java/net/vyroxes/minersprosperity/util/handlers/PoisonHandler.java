package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

@Mod.EventBusSubscriber
public class PoisonHandler
{
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer player))
            return;

        ItemStack mainHand = player.getHeldItemMainhand();

        if (mainHand.getItem() instanceof ItemBow && !(event.getSource().getImmediateSource() instanceof EntityArrow))
            return;

        int lvl = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.POISON, mainHand);

        if (lvl > 0 && event.getEntityLiving() instanceof EntityLivingBase)
        {
            EntityLivingBase target = event.getEntityLiving();

            int duration = lvl == 1 ? 80 : 160;
            int amplifier = lvl - 1;

            target.addPotionEffect(new PotionEffect(MobEffects.POISON, duration, amplifier));
        }
    }
}