package net.vyroxes.minersprosperity.client.event;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.init.ModItems;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class GlassShardHandler
{
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();

        if (world.isRemote || player == null || player.isCreative())
            return;

        IBlockState state = world.getBlockState(pos);
        Block brokenBlock = state.getBlock();
        ItemStack blockStack = new ItemStack(brokenBlock, 1, brokenBlock.getMetaFromState(state));

        if (blockStack.isEmpty())
            return;

        int[] oreIDs = OreDictionary.getOreIDs(blockStack);
        boolean isGlass = false;
        boolean isGlassPane = false;

        for (int id : oreIDs)
        {
            String name = OreDictionary.getOreName(id);
            if (name.equals("blockGlass"))
            {
                isGlass = true;
                break;
            } else if (name.equals("paneGlass")) {
                isGlassPane = true;
                break;
            }
        }

        if (!isGlass && !isGlassPane)
            return;

        ItemStack held = player.getHeldItemMainhand();

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, held) > 0)
            return;

        int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, held);

        int amount = isGlass ? Math.max(1, fortuneLevel) + rand.nextInt(2) : 1;

        ItemStack drop = new ItemStack(ModItems.GLASS_SHARD, amount);
        Block.spawnAsEntity(world, pos, drop);
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        World world = event.getWorld();
        List<BlockPos> affectedBlocks = event.getAffectedBlocks();

        for (BlockPos pos : affectedBlocks)
        {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));

            if (stack.isEmpty())
                return;

            int[] oreIDs = OreDictionary.getOreIDs(stack);

            for (int id : oreIDs)
            {
                String name = OreDictionary.getOreName(id);
                if (name.equals("blockGlass") || name.equals("paneGlass"))
                {
                    if (rand.nextFloat() < 0.5f)
                    {
                        ItemStack drop = new ItemStack(ModItems.GLASS_SHARD);
                        Block.spawnAsEntity(world, pos, drop);
                    }
                    break;
                }
            }
        }
    }
}