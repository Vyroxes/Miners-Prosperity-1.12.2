package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import org.jetbrains.annotations.NotNull;

public class ContainerUpgrades extends Container
{
    private final TileEntityMachine tileEntity;

    public ContainerUpgrades(InventoryPlayer playerInventory, TileEntityMachine tileEntity)
    {
        this.tileEntity = tileEntity;
        IItemHandler handler = this.tileEntity.getCustomItemStackHandler();

        this.addSlotToContainer(new SlotItemHandler(handler, 4, 50, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 5, 70, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 6, 90, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 7, 110, 35));

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
    public boolean canInteractWith(@NotNull EntityPlayer playerIn)
    {
        return this.tileEntity.isUsableByPlayer(playerIn);
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

            if (index <= 3)
            {
                if (!this.mergeItemStack(itemstack1, 4, 40, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index <= 39 && this.tileEntity.getCustomItemStackHandler().isUpgradeItemValid(itemstack1) != -1)
            {
                int id = this.tileEntity.getCustomItemStackHandler().isUpgradeItemValid(itemstack1);

                if (!this.mergeItemStack(itemstack1, id, id+1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index <= 30)
            {
                if (!this.mergeItemStack(itemstack1, 31, 40, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index <= 39)
            {
                if (!this.mergeItemStack(itemstack1, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
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