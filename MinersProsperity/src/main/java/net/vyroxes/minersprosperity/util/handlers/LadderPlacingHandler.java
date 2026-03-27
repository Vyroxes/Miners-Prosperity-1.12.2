package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class LadderPlacingHandler
{
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        if (!ModConfig.OTHER.simpleLadderPlacing)
            return;

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EnumHand hand = event.getHand();
        ItemStack held = player.getHeldItem(hand);

        Block ladderBlock = Block.getBlockFromItem(held.getItem());
        if (held.isEmpty() || !(ladderBlock instanceof BlockLadder))
            return;

        Block clickedBlock = world.getBlockState(pos).getBlock();
        if (!(clickedBlock instanceof BlockLadder))
            return;

        EnumFacing facing = world.getBlockState(pos).getValue(BlockLadder.FACING);
        BlockPos down = pos.down();
        boolean placedAny = false;

        if (player.isSneaking())
        {
            BlockPos lastPlacedPos = null;

            while (down.getY() > 0 && !held.isEmpty())
            {
                if (world.isAirBlock(down) && ladderBlock.canPlaceBlockAt(world, down))
                {
                    world.setBlockState(down, ladderBlock.getDefaultState().withProperty(BlockLadder.FACING, facing));
                    lastPlacedPos = down;

                    if (!player.isCreative())
                        held.shrink(1);

                    placedAny = true;
                }

                if (world.getBlockState(down).getBlock() instanceof BlockLadder || world.isAirBlock(down))
                {
                    down = down.down();
                }
                else
                {
                    break;
                }
            }

            if (lastPlacedPos != null)
            {
                IBlockState placed = world.getBlockState(lastPlacedPos);
                SoundType sound = placed.getBlock().getSoundType(placed, world, lastPlacedPos, player);
                world.playSound(null, lastPlacedPos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            }
        }
        else
        {
            while (down.getY() > 0 && world.getBlockState(down).getBlock() instanceof BlockLadder)
            {
                down = down.down();
            }

            if (down.getY() > 0 && world.isAirBlock(down) && ladderBlock.canPlaceBlockAt(world, down))
            {
                world.setBlockState(down, ladderBlock.getDefaultState().withProperty(BlockLadder.FACING, facing));

                IBlockState placed = world.getBlockState(down);
                SoundType sound = placed.getBlock().getSoundType(placed, world, down, player);
                world.playSound(null, down, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);

                if (!player.isCreative())
                    held.shrink(1);

                placedAny = true;
            }
        }

        if (placedAny)
        {
            event.setCanceled(true);
            player.swingArm(hand);
        }
    }
}