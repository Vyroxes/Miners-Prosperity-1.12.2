package net.vyroxes.minersprosperity.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.block.chest.BlockCustomChest;
import net.vyroxes.minersprosperity.block.chest.ChestTier;
import net.vyroxes.minersprosperity.tile.TileEntityCustomChest;
import org.jetbrains.annotations.NotNull;

public class ItemUpgradeCustomChest extends ItemBase {
    private final ChestTier fromTier;

    public ItemUpgradeCustomChest(String name, ChestTier fromTier) {
        super(name);
        this.fromTier = fromTier;
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (worldIn.isRemote) {
            player.swingArm(hand);
            return EnumActionResult.SUCCESS;
        }

        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() instanceof BlockCustomChest chest) {
            if (chest.getTier() != this.fromTier) return EnumActionResult.PASS;

            return chest.tryUpgrade(worldIn, pos, stack);
        }

        if (state.getBlock() instanceof BlockChest) {
            if (this.fromTier != ChestTier.VANILLA) return EnumActionResult.PASS;

            TileEntity te = worldIn.getTileEntity(pos);

            if (!(te instanceof TileEntityChest chest)) return EnumActionResult.PASS;

            if (chest.numPlayersUsing > 0) return EnumActionResult.FAIL;

            NonNullList<ItemStack> items = NonNullList.withSize(chest.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < chest.getSizeInventory(); i++) items.set(i, chest.getStackInSlot(i).copy());

            chest.clear();

            EnumFacing facingOld = EnumFacing.NORTH;
            if (state.getProperties().containsKey(BlockHorizontal.FACING)) facingOld = state.getValue(BlockHorizontal.FACING);

            Block newBlock = ModBlocks.LEAD_CHEST;
            IBlockState newState = newBlock.getDefaultState();

            if (newState.getProperties().containsKey(BlockCustomChest.FACING)) newState = newState.withProperty(BlockCustomChest.FACING, facingOld);

            worldIn.setBlockState(pos, newState, 3);

            TileEntity teUpgraded = worldIn.getTileEntity(pos);

            if (teUpgraded instanceof TileEntityCustomChest upgraded) {

                for (int i = 0; i < items.size(); i++) {
                    if (i < upgraded.getHandler().getSlots())
                    {
                        upgraded.getHandler().setStackInSlot(i, items.get(i));
                    } else {
                        Block.spawnAsEntity(worldIn, pos, items.get(i));
                    }
                }
                teUpgraded.markDirty();

                worldIn.notifyBlockUpdate(pos, state, newState, 3);

                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                worldIn.setBlockState(pos, state, 3);
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.PASS;
    }
}