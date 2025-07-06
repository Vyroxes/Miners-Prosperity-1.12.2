package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.guis.GuiBackpack;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.InventoryBackpack;
import org.jetbrains.annotations.NotNull;

public class ContainerBackpack extends Container
{
    protected final InventoryBackpack backpackInventory;
    protected final ItemStack backpackItemStack;
    protected final int backpackRows;
    protected final int inventoryStartY;
    protected final int hotbarStartY;

    public ContainerBackpack(InventoryPlayer playerInventory, ItemStack backpackItemStack, int backpackRows)
    {
        this.backpackItemStack = backpackItemStack;
        this.backpackInventory = Backpack.getBackpackInventory(backpackItemStack);
        this.backpackRows = backpackRows;

        int slotSize = 18;

        for (int row = 0; row < backpackRows; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new SlotBackpack(backpackInventory, col + row * 9, 8 + col * slotSize, 17 + row * slotSize));
            }
        }

        inventoryStartY = 26 + backpackRows * slotSize + 4;
        hotbarStartY = inventoryStartY + 58;

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * slotSize, inventoryStartY + row * slotSize));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            final int index = i;
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * slotSize, hotbarStartY)
            {
                @Override
                public boolean canTakeStack(EntityPlayer playerIn)
                {
                    ItemStack stackInSlot = playerInventory.getStackInSlot(index);
                    if (!stackInSlot.isEmpty() && stackInSlot.getItem() == backpackItemStack.getItem())
                    {
                        NBTTagCompound tag = stackInSlot.getTagCompound();
                        if (tag != null && tag.hasKey("BackpackData"))
                        {
                            NBTTagCompound backpackData = tag.getCompoundTag("BackpackData");
                            if (backpackData.getBoolean("IsOpen"))
                            {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }

    public int getRows()
    {
        return this.backpackRows;
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        int backpackSlotCount = backpackRows * 9;
        int inventorySlotStart = backpackSlotCount;
        int inventorySlotEnd = inventorySlotStart + 27 + 9;

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < backpackSlotCount)
            {
                if (!this.mergeItemStack(itemstack1, inventorySlotStart, inventorySlotEnd, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= inventorySlotStart && index < inventorySlotEnd)
            {
                if (!this.mergeItemStack(itemstack1, 0, backpackSlotCount, false))
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
        GuiBackpack.playBackpackOpenSound();
        ItemStack held = playerIn.getHeldItemMainhand();
        if (!ItemStack.areItemsEqual(held, backpackItemStack))
        {
            held = playerIn.getHeldItemOffhand();
        }

        if (held.getItem() instanceof Backpack)
        {
            held.getOrCreateSubCompound("BackpackData").setBoolean("IsOpen", false);
        }
    }
}