package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.vyroxes.minersprosperity.objects.guis.BackpackGui;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.BackpackInventory;

public class BackpackContainer extends Container
{
    private final BackpackInventory backpackInventory;
    private final ItemStack backpackItemStack;

    public BackpackContainer(InventoryPlayer playerInventory, ItemStack backpackItemStack)
    {
        this.backpackItemStack = backpackItemStack;
        this.backpackInventory = Backpack.getBackpackInventory(backpackItemStack);

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new Slot(backpackInventory, col + row * 9, 8 + col * 18, 17 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            final int index = i;
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142)
            {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn)
                {
                    ItemStack stackInSlot = playerInventory.getStackInSlot(index);
                    if (!stackInSlot.isEmpty() && stackInSlot.getItem() == backpackItemStack.getItem())
                    {
                        NBTTagCompound tag = stackInSlot.getTagCompound();
                        NBTTagCompound guiNBT = backpackItemStack.getTagCompound();

                        if (tag != null && guiNBT != null && tag.equals(guiNBT))
                        {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (index < 27)
            {
                if (!this.mergeItemStack(itemstack1, 27, 63, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 27 && index < 63)
            {
                if (!this.mergeItemStack(itemstack1, 0, 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.getCount() == 0)
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

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        Backpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        BackpackGui.playBackpackOpenSound();
    }
}