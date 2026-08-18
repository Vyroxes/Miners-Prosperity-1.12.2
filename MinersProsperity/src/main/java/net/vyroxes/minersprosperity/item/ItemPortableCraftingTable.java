package net.vyroxes.minersprosperity.item;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemPortableCraftingTable extends ItemBase
{
    public ItemPortableCraftingTable(String name) {
        super(name);
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack)
    {
        return 1;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            playerIn.displayGui(new BlockWorkbench.InterfaceCraftingTable(worldIn, new BlockPos(playerIn)));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}