package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class InsightHandler
{
    @SubscribeEvent
    public static void onMobKilled(LivingExperienceDropEvent event)
    {
        if (!(event.getAttackingPlayer() instanceof EntityPlayer))
            return;

        EntityPlayer player = event.getAttackingPlayer();
        ItemStack held = player.getHeldItemMainhand();
        if (held.isEmpty())
            return;

        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held);
        if (level > 0)
        {
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, boots) > 0)
                return;

            float multiplier = 1.0F + (0.2F + 0.1F * level);
            int bonusXP = Math.max(1, Math.round(event.getOriginalExperience() * (multiplier - 1)));
            event.setDroppedExperience(event.getDroppedExperience() + bonusXP);
        }
    }

    @SubscribeEvent
    public static void onBlockMined(BlockEvent.HarvestDropsEvent event)
    {
        if (!(event.getHarvester() instanceof EntityPlayer))
            return;

        EntityPlayer player = event.getHarvester();
        ItemStack held = player.getHeldItemMainhand();

        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held);
        if (level > 0)
        {
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.QUICK_MINING, held) > 0)
                return;

            World world = player.world;
            BlockPos pos = event.getPos();

            int xp = event.getState().getBlock().getExpDrop(event.getState(), world, pos, event.getFortuneLevel());

            if (xp > 0)
            {
                float multiplier = 1.0F + (0.2F + 0.1F * level);
                int bonusXP = Math.max(1, Math.round(xp * (multiplier - 1)));
                event.getHarvester().addExperience(bonusXP);
            }
        }
    }

    @SubscribeEvent
    public static void onFish(ItemFishedEvent event)
    {
        if (!(event.getEntityPlayer() instanceof EntityPlayer))
            return;

        EntityPlayer player = event.getEntityPlayer();
        ItemStack held = player.getHeldItemMainhand();
        if (held.isEmpty())
            return;

        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held);
        if (level > 0)
        {
            float multiplier = 1.0F + (0.2F + 0.1F * level);
            int xp = event.getDrops().stream().mapToInt(ItemStack::getCount).sum();
            int bonusXP = Math.max(1, Math.round(xp * (multiplier - 1)));
            player.addExperience(bonusXP);
        }
    }
}