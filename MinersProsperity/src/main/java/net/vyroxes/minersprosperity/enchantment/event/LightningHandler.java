package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class LightningHandler
{
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer player))
            return;

        ItemStack mainHand = player.getHeldItemMainhand();

        if (mainHand.getItem() instanceof ItemBow && !(event.getSource().getImmediateSource() instanceof EntityArrow))
            return;

        int lvl = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LIGHTNING, mainHand);

        if (lvl > 0 && event.getEntityLiving() instanceof EntityLivingBase)
        {
            EntityLivingBase target = event.getEntityLiving();

            target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 2.0F * lvl);

            World world = target.getEntityWorld();
            if (!world.isRemote)
            {
                EntityLightningBolt bolt = new EntityLightningBolt(world, target.posX, target.posY, target.posZ, false);
                world.addWeatherEffect(bolt);
            }
        }
    }
}