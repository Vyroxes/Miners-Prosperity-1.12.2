package net.vyroxes.minersprosperity.client.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.item.ItemShieldCustom;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ShieldHandler
{
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event)
    {
        if (event.getEntityPlayer().world.isRemote) return;
        if (!(event.getTarget() instanceof EntityPlayer defender)) return;

        net.minecraft.entity.player.EntityPlayer attacker = event.getEntityPlayer();

        if (!defender.isHandActive()) return;

        ItemStack shield = defender.getActiveItemStack();
        ItemStack weapon = attacker.getHeldItemMainhand();

        if (!(shield.getItem() instanceof ItemShieldCustom)) return;
        if (!(weapon.getItem() instanceof ItemAxe)) return;

        assert Enchantments.EFFICIENCY != null;
        int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, weapon);
        float chance = 0.25F + efficiency * 0.05F;

        if (attacker.getCooledAttackStrength(0.5F) > 0.9F)
        {
            chance += 0.75F;
        }

        if (attacker.getRNG().nextFloat() >= chance) return;

        defender.getCooldownTracker().setCooldown(shield.getItem(), 100);
        defender.resetActiveHand();
        defender.world.setEntityState(defender, (byte) 30);
    }
}