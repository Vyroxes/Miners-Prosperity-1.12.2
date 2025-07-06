package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class GlowHandler
{
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingUpdateEvent event)
    {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof net.minecraft.entity.player.EntityPlayer player)
        {
            tickCounter++;
            if (tickCounter % 10 != 0) return;

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

            if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0)
            {
                BlockPos pos = player.getPosition();

                if (player.world.isAirBlock(pos))
                {
                    player.world.setBlockState(pos, BlockInit.INVISIBLE_LIGHT.getDefaultState(), 3);
                    LightTracker.add(pos);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        World world = event.world;

        Iterator<BlockPos> it = LightTracker.getAll().iterator();
        while (it.hasNext())
        {
            BlockPos pos = it.next();

            if (world.getBlockState(pos).getBlock() != BlockInit.INVISIBLE_LIGHT)
            {
                it.remove();
                continue;
            }

            boolean keep = world.playerEntities.stream().anyMatch(player -> {
                BlockPos p = player.getPosition();
                if (Math.abs(p.getX() - pos.getX()) > 1) return false;
                if (Math.abs(p.getY() - pos.getY()) > 1) return false;
                if (Math.abs(p.getZ() - pos.getZ()) > 1) return false;

                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                return EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0;
            });

            if (!keep)
            {
                world.setBlockToAir(pos);
                it.remove();
            }
        }
    }
}