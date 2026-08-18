package net.vyroxes.minersprosperity.enchantment.event;

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
import net.vyroxes.minersprosperity.init.ModEnchantments;
import java.util.Random;

@Mod.EventBusSubscriber
public class BloodthirstHandler
{
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer player))
            return;

        ItemStack mainHand = player.getHeldItemMainhand();

        if (mainHand.getItem() instanceof ItemBow && !(event.getSource().getImmediateSource() instanceof EntityArrow))
            return;

        int lvl = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLOODTHIRST, mainHand);

        if (lvl > 0 && event.getEntityLiving() instanceof EntityLivingBase)
        {
            float chance = 0.2F * lvl;
            if (random.nextFloat() <= chance)
            {
                if (player.getHealth() < player.getMaxHealth())
                {
                    float healAmount = 1.0F * lvl;
                    player.heal(healAmount);
                }
                else
                {
                    int amplifier = lvl - 1;
                    int duration = 20 * lvl;

                    if (duration > 0)
                    {
                        player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, duration, amplifier));
                    }
                }
            }
        }
    }
}