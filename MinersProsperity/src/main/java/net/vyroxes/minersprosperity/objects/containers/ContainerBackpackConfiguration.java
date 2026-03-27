package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerBackpackConfiguration extends Container
{
    protected final ItemStackHandler configurationInventory;
    protected final ItemStack backpackItemStack;
    protected final boolean fromBaubles;
    protected final int configurationRows;
    protected final int configurationColumns;
    protected final int configurationStartY;
    protected final int inventoryStartY;
    protected final int hotbarStartY;

    public ContainerBackpackConfiguration(InventoryPlayer playerInventory, ItemStack backpackItemStack, boolean fromBaubles)
    {
        this.backpackItemStack = backpackItemStack;
        this.configurationInventory = ItemBackpackBase.getConfigurationInventory(backpackItemStack);
        this.fromBaubles = fromBaubles;

        ItemBackpackBase item = (ItemBackpackBase) backpackItemStack.getItem();
        int totalSlots = item.getInventorySize();

        this.configurationColumns = item.getInventoryColumns();
        this.configurationRows = totalSlots / configurationColumns;

        int slotSize = 18;
        int columnOffset = Math.max(0, (configurationColumns - 9) / 2) * slotSize;
        configurationStartY = 23 + 17;
        int slotIndex = 0;

        for (int row = 0; row < configurationRows; row++)
        {
            int columnsThisRow;

            if (row < 4)
                columnsThisRow = configurationColumns - 3;
            else
                columnsThisRow = configurationColumns;

            for (int col = 0; col < columnsThisRow; col++)
            {
                this.addSlotToContainer(new SlotFilter(configurationInventory, slotIndex, 8 + col * slotSize, configurationStartY + row * slotSize));
                slotIndex++;
            }
        }

        inventoryStartY = 23 + 26 + configurationRows * slotSize + 4;
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

    public boolean getState(String state)
    {
        NBTTagCompound compound = backpackItemStack.getTagCompound();
        assert compound != null;
        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
        return backpackData.getBoolean(state);
    }

    public void setState(String state)
    {
        NBTTagCompound compound = backpackItemStack.getTagCompound();
        assert compound != null;
        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
        boolean value = backpackData.getBoolean(state);
        boolean newValue = !value;
        backpackData.setBoolean(state, newValue);
        compound.setTag("BackpackData", backpackData);
        NetworkHandler.sendBackpackStateUpdate(newValue, state, fromBaubles);
    }

    public int getRows()
    {
        return this.configurationRows;
    }

    public int getColumns() { return this.configurationColumns; }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot clickedSlot = this.inventorySlots.get(index);

        int configurationSlotCount = configurationInventory.getSlots();
        int inventorySlotEnd = configurationSlotCount + 27 + 9;

        if (clickedSlot != null && clickedSlot.getHasStack())
        {
            ItemStack itemstack1 = clickedSlot.getStack();
            itemstack = itemstack1.copy();

            if (index >= configurationSlotCount && index < inventorySlotEnd)
            {
                ItemStack ghostCopy = itemstack.copy();
                ghostCopy.setCount(1);

                for (int i = 0; i < configurationSlotCount; i++)
                {
                    Slot slot = this.inventorySlots.get(i);
                    if (slot instanceof SlotFilter)
                    {
                        ItemStack existing = slot.getStack();

                        if (existing.isEmpty() || ItemStack.areItemsEqual(existing, ghostCopy))
                        {
                            slot.putStack(ghostCopy);
                            return ItemStack.EMPTY;
                        }
                    }
                }

                return ItemStack.EMPTY;
            }

            if (index < configurationSlotCount)
            {
                return ItemStack.EMPTY;
            }

            if (!this.mergeItemStack(itemstack1, 0, configurationSlotCount, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                clickedSlot.putStack(ItemStack.EMPTY);
            }
            else
            {
                clickedSlot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            clickedSlot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull EntityPlayer player)
    {
        if (slotId >= 0 && slotId < this.inventorySlots.size())
        {
            Slot slot = this.inventorySlots.get(slotId);

            if (slot instanceof SlotFilter)
            {
                ItemStack cursor = player.inventory.getItemStack();

                if (!cursor.isEmpty())
                {
                    ItemStack copy = cursor.copy();
                    copy.setCount(1);

                    for (Slot s : this.inventorySlots)
                    {
                        if (s instanceof SlotFilter && s != slot)
                        {
                            ItemStack existing = s.getStack();
                            if (!existing.isEmpty() && ItemStack.areItemStacksEqual(existing, copy))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                    }

                    slot.putStack(copy);
                }
                else
                {
                    slot.putStack(ItemStack.EMPTY);
                }

                return ItemStack.EMPTY;
            }
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player);
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
        }
    }
}