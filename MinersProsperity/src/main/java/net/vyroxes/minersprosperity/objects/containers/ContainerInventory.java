package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import org.jetbrains.annotations.NotNull;

public class ContainerInventory extends Container
{
    private final TileEntityAlloyFurnace tileEntityAlloyFurnace;

    public ContainerInventory(InventoryPlayer playerInventory, TileEntityAlloyFurnace tileEntityAlloyFurnace)
    {
        this.tileEntityAlloyFurnace = tileEntityAlloyFurnace;
        
        for (int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn)
    {
        return this.tileEntityAlloyFurnace.isUsableByPlayer(playerIn);
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index <= 26)
            {
                if (!this.mergeItemStack(itemstack1, 27, 36, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index <= 35)
            {
                if (!this.mergeItemStack(itemstack1, 0, 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 36, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }
}