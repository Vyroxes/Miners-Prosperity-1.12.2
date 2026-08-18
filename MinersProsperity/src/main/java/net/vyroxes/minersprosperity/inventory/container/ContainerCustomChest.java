package net.vyroxes.minersprosperity.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityCustomChest;
import org.jetbrains.annotations.NotNull;

public class ContainerCustomChest extends Container {
    protected final TileEntityCustomChest te;
    protected final ItemStackHandler chestInventory;
    protected final int chestRows;
    protected final int chestColumns;
    protected final int chestStartY;
    protected final int inventoryStartY;
    protected final int hotbarStartY;

    public ContainerCustomChest(InventoryPlayer playerInventory, TileEntityCustomChest te) {
        this.te = te;
        this.chestInventory = (ItemStackHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        this.chestColumns = te.getTier().inventoryColumns;
        this.chestRows = te.getTier().inventorySize / chestColumns;

        int slotSize = 18;
        int columnOffset = Math.max(0, (chestColumns - 9) / 2) * slotSize;
        chestStartY = 17;

        if (!playerInventory.player.world.isRemote) {
            te.openInventory(playerInventory.player);
            te.markDirty();
        }

        for (int row = 0; row < chestRows; row++) {
            for (int col = 0; col < chestColumns; col++) {
                this.addSlotToContainer(new SlotItemHandler(chestInventory, col + row * chestColumns, 8 + col * slotSize, chestStartY + row * slotSize));
            }
        }

        inventoryStartY = 26 + chestRows * slotSize + 4;
        hotbarStartY = inventoryStartY + 58;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * slotSize + columnOffset, inventoryStartY + row * slotSize));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * slotSize + columnOffset, hotbarStartY));
        }
    }

    public int getRows() {
        return this.chestRows;
    }

    public int getColumns() {
        return this.chestColumns;
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        int chestSlotCount = chestInventory.getSlots();
        int inventorySlotEnd = chestSlotCount + 27 + 9;

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < chestSlotCount)
            {
                if (!this.mergeItemStack(itemstack1, chestSlotCount, inventorySlotEnd, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < inventorySlotEnd)
            {
                if (!this.mergeItemStack(itemstack1, 0, chestSlotCount, false))
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
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(@NotNull EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!playerIn.world.isRemote) {
            te.closeInventory(playerIn);
            te.markDirty();
        }
    }
}