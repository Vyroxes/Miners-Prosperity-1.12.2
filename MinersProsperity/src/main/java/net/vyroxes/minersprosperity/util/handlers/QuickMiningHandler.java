package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber
public class QuickMiningHandler
{
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        BlockPos origin = event.getPos();

        if (world.isRemote || player == null || player.isSneaking())
            return;

        ItemStack held = player.getHeldItemMainhand();

        if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.QUICK_MINING, held) > 0)
        {
            IBlockState targetState = world.getBlockState(origin);
            Block targetBlock = targetState.getBlock();

            if (!isOreBlock(targetBlock))
                return;

            Set<BlockPos> visited = new HashSet<>();
            Queue<BlockPos> toCheck = new LinkedList<>();
            toCheck.add(origin);

            int limit = 64;

            while (!toCheck.isEmpty() && visited.size() < limit)
            {
                BlockPos pos = toCheck.poll();
                if (!visited.add(pos))
                    continue;

                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (!isOreBlock(block))
                    continue;

                if (player.canHarvestBlock(state))
                {
                    state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), held);
                    world.setBlockToAir(pos);
                    held.damageItem(1, player);
                }

                for (EnumFacing face : EnumFacing.values())
                {
                    BlockPos neighbor = pos.offset(face);
                    if (!visited.contains(neighbor))
                        toCheck.add(neighbor);
                }
            }
        }
    }

    private static final Pattern ORE_PATTERN = Pattern.compile("(^|[^a-z])ore(\\d*|[^a-z]|$)");

    private static boolean isOreBlock(Block block)
    {
        String registryName = block.getRegistryName().getPath().toLowerCase();

        Matcher matcher = ORE_PATTERN.matcher(registryName);
        return matcher.find();
    }
}