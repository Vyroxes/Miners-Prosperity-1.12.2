package net.vyroxes.minersprosperity.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;

public class ContainerUpgrades extends Container
{
    private final TileEntityMachine tileEntity;

    public ContainerUpgrades(InventoryPlayer playerInventory, TileEntityMachine tileEntity)
    {
        this.tileEntity = tileEntity;
        IItemHandler handler = this.tileEntity.getCustomItemStackHandler();
        int upgrades = this.tileEntity.getUpgradeSlotCount();
        int startX = 50 + (4 - upgrades) * 10;

        for (int i = 0; i < upgrades; i++) this.addSlotToContainer(new SlotItemHandler(handler, this.tileEntity.getUpgradeSlotId(i), startX + i * 20, 35));

        for (int i = 0; i < 3; ++i) for(int j = 0; j < 9; ++j) this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (int k = 0; k < 9; ++k) this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn)
    {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index)
    {
        if (index < 0 || index >= this.inventorySlots.size()) return ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);
        if (!slot.getHasStack()) return ItemStack.EMPTY;

        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();

        int upgrades = this.tileEntity.getUpgradeSlotCount();
        int hotbarStart = upgrades + 27;
        int inventoryEnd = hotbarStart + 9;

        if (index < upgrades)
        {
            if (!this.mergeItemStack(stack, upgrades, inventoryEnd, false))
            {
                return ItemStack.EMPTY;
            }
        }
        else
        {
            int upgradeIndex = this.tileEntity.getCustomItemStackHandler().isUpgradeItemValid(stack);

            if (upgradeIndex >= 0)
            {
                if (!this.mergeItemStack(stack, upgradeIndex, upgradeIndex + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < hotbarStart)
            {
                if (!this.mergeItemStack(stack, hotbarStart, inventoryEnd, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(stack, upgrades, hotbarStart, false))
            {
                return ItemStack.EMPTY;
            }
        }

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;

        if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();

        slot.onTake(playerIn, stack);
        return original;
    }
}