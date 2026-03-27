package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import org.jetbrains.annotations.NotNull;

public class ContainerBackpack extends Container
{
    protected final ItemStackHandler backpackInventory;
    protected final ItemStack backpackItemStack;
    protected final boolean fromBaubles;
    protected final int backpackRows;
    protected final int backpackColumns;
    protected final int backpackStartY;
    protected final int inventoryStartY;
    protected final int hotbarStartY;

    public ContainerBackpack(InventoryPlayer playerInventory, ItemStack backpackItemStack, boolean fromBaubles)
    {
        this.backpackItemStack = backpackItemStack;
        this.backpackInventory = ItemBackpackBase.getBackpackInventory(backpackItemStack);
        this.fromBaubles = fromBaubles;

        ItemBackpackBase item = (ItemBackpackBase) backpackItemStack.getItem();
        int totalSlots = item.getInventorySize();

        this.backpackColumns = item.getInventoryColumns();
        this.backpackRows = totalSlots / backpackColumns;

        int slotSize = 18;
        int columnOffset = Math.max(0, (backpackColumns - 9) / 2) * slotSize;
        backpackStartY = 23 + 17;

        for (int row = 0; row < backpackRows; row++)
        {
            for (int col = 0; col < backpackColumns; col++)
            {
                this.addSlotToContainer(new SlotBackpack(backpackInventory, col + row * backpackColumns, 8 + col * slotSize, backpackStartY + row * slotSize));
            }
        }

        inventoryStartY = 23 + 26 + backpackRows * slotSize + 4;
        hotbarStartY = inventoryStartY + 58;

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * slotSize + columnOffset, inventoryStartY + row * slotSize));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            final int index = i;
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * slotSize + columnOffset, hotbarStartY)
            {
                @Override
                public boolean canTakeStack(@NotNull EntityPlayer playerIn)
                {
                    ItemStack stackInSlot = playerInventory.getStackInSlot(index);
                    if (!stackInSlot.isEmpty() && stackInSlot.getItem() == backpackItemStack.getItem())
                    {
                        NBTTagCompound tag = stackInSlot.getTagCompound();
                        if (tag != null && tag.hasKey("BackpackData"))
                        {
                            NBTTagCompound backpackData = tag.getCompoundTag("BackpackData");
                            return !backpackData.getBoolean("IsOpen");
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

    public int getColumns() { return this.backpackColumns; }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        int backpackSlotCount = backpackInventory.getSlots();
        int inventorySlotEnd = backpackSlotCount + 27 + 9;

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < backpackSlotCount)
            {
                if (!this.mergeItemStack(itemstack1, backpackSlotCount, inventorySlotEnd, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < inventorySlotEnd)
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
    public boolean canInteractWith(@NotNull EntityPlayer playerIn)
    {
        return true;
    }

    @Override
    public void onContainerClosed(@NotNull EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        ItemStack held = playerIn.getHeldItemMainhand();
        if (held.getItem() instanceof ItemBackpackBase)
        {
            held.getOrCreateSubCompound("BackpackData").setBoolean("IsOpen", false);
            System.out.print("IsOpen: " + held.getOrCreateSubCompound("BackpackData").getBoolean("IsOpen"));
        }
    }
}