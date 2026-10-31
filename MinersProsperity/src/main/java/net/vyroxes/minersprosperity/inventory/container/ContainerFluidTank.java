package net.vyroxes.minersprosperity.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityFluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ContainerFluidTank extends Container
{
    private final TileEntityFluidTank tileEntity;

    public ContainerFluidTank(InventoryPlayer inventory, TileEntityFluidTank tileEntity)
    {
        this.tileEntity = tileEntity;
        for (int i = 0; i < 2; i++)
        {
            this.addSlotToContainer(new SlotItemHandler(tileEntity.getCustomItemStackHandler(), tileEntity.getInputSlotId(i), 51 + i * 58, 26));
        }
        for (int i = 0; i < 2; i++)
        {
            this.addSlotToContainer(new SlotItemHandler(tileEntity.getCustomItemStackHandler(), tileEntity.getOutputSlotId(i), 51 + i * 58, 46)
            {
                @Override
                public boolean isItemValid(@NotNull ItemStack stack) { return false; }
            });
        }
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++) this.addSlotToContainer(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        }
        for (int col = 0; col < 9; col++) this.addSlotToContainer(new Slot(inventory, col, 8 + col * 18, 142));
    }

    @Override
    public void addListener(@NotNull IContainerListener listener)
    {
        super.addListener(listener);
        if (listener instanceof net.minecraft.entity.player.EntityPlayerMP)
        {
            ((net.minecraft.entity.player.EntityPlayerMP) listener).connection.sendPacket(Objects.requireNonNull(this.tileEntity.getUpdatePacket()));
        }
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) { return this.tileEntity.isUsableByPlayer(player); }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int index)
    {
        if (index < 0 || index >= this.inventorySlots.size()) return ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (!slot.getHasStack()) return ItemStack.EMPTY;
        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();
        if (index < 4)
        {
            if (!this.mergeItemStack(stack, 4, 40, true)) return ItemStack.EMPTY;
        }
        else if (this.tileEntity.isContainerValid(0, stack))
        {
            if (!this.mergeItemStack(stack, 0, 1, false)) return ItemStack.EMPTY;
        }
        else if (this.tileEntity.isContainerValid(1, stack))
        {
            if (!this.mergeItemStack(stack, 1, 2, false)) return ItemStack.EMPTY;
        }
        else if (index < 31)
        {
            if (!this.mergeItemStack(stack, 31, 40, false)) return ItemStack.EMPTY;
        }
        else if (!this.mergeItemStack(stack, 4, 31, false)) return ItemStack.EMPTY;

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        slot.onTake(player, stack);
        return original;
    }
}