package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class VoidGuardHandler
{
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        EntityPlayer player = event.player;
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        if (helmet.isEmpty())
            return;

        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VOID_GUARD, helmet);

        if (level <= 0)
            return;

        if (player.posY < -2)
        {
            BlockPos safePos = findSafeBlock(player);

            if (safePos == null)
            {
                safePos = player.world.getSpawnPoint();
            }

            player.setPositionAndUpdate(safePos.getX() + 0.5, safePos.getY() + 1, safePos.getZ() + 0.5);
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }

    private static BlockPos findSafeBlock(EntityPlayer player)
    {
        int[] radius = {5, 20, 50, 100};
        int minY = 0;
        int maxY = player.world.provider.getActualHeight();

        for (int rad : radius)
        {
            BlockPos safePos;
            if (rad == 5 || rad == 20)
            {
                safePos = searchArea(player, rad, minY, maxY);
            }
            else
            {
                safePos = searchSpiral(player, rad, minY, maxY);
            }
            if (safePos != null)
                return safePos;
        }
        return null;
    }

    private static BlockPos searchArea(EntityPlayer player, int radius, int minY, int maxY)
    {
        BlockPos playerPos = player.getPosition();
        BlockPos bestPos = null;
        int bestY = Integer.MAX_VALUE;
        double bestDistance = Double.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++)
        {
            for (int dz = -radius; dz <= radius; dz++)
            {
                int x = playerPos.getX() + dx;
                int z = playerPos.getZ() + dz;

                for (int y = minY; y <= maxY - 2; y++)
                {
                    BlockPos base = new BlockPos(x, y, z);
                    if (!player.world.getBlockState(base).getMaterial().isSolid())
                        continue;

                    if (isSafe(player.world, base))
                    {
                        double distance = playerPos.distanceSq(x, y, z);

                        if (y < bestY || (y == bestY && distance < bestDistance))
                        {
                            bestY = y;
                            bestDistance = distance;
                            bestPos = base;
                        }
                    }
                }
            }
        }
        return bestPos;
    }

    private static BlockPos searchSpiral(EntityPlayer player, int radius, int minY, int maxY)
    {
        BlockPos playerPos = player.getPosition();
        int x = 0;
        int z = 0;
        int dx = 0;
        int dz = -1;
        int maxSteps = (radius * 2 + 1) * (radius * 2 + 1);

        for (int step = 0; step < maxSteps; step++)
        {
            if (-radius <= x && x <= radius && -radius <= z && z <= radius)
            {
                int worldX = playerPos.getX() + x;
                int worldZ = playerPos.getZ() + z;

                for (int y = minY; y <= maxY - 2; y++)
                {
                    BlockPos base = new BlockPos(worldX, y, worldZ);
                    IBlockState state = player.world.getBlockState(base);

                    if (!state.getMaterial().isSolid())
                        continue;

                    if (isSafe(player.world, base))
                    {
                        return base;
                    }
                }
            }

            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z))
            {
                int tmp = dx;
                dx = -dz;
                dz = tmp;
            }
            x += dx;
            z += dz;
        }

        return null;
    }

    private static boolean isSafe(World world, BlockPos base)
    {
        if (!world.getBlockState(base).getMaterial().isSolid())
            return false;

        BlockPos above = base.up();
        BlockPos above2 = above.up();

        return world.isAirBlock(above) && world.isAirBlock(above2);
    }
}