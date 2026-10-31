package net.vyroxes.minersprosperity.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityEnergyStorage;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class ContainerEnergyStorage extends Container
{
    private final TileEntityEnergyStorage tileEntity;
    private final int[] lastValues = new int[4];
    private final int[] clientValues = new int[4];

    public ContainerEnergyStorage(InventoryPlayer inventory, TileEntityEnergyStorage tileEntity)
    {
        this.tileEntity = tileEntity;
        Arrays.fill(this.lastValues, -1);
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getCustomItemStackHandler(), tileEntity.getEnergySlotId(0), 51, 36));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getCustomItemStackHandler(), tileEntity.getEnergySlotId(1), 109, 36));
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
        Arrays.fill(this.lastValues, -1);
        this.detectAndSendChanges();
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int[] values = {this.tileEntity.getMaxEnergyStored(), this.tileEntity.getEnergyStored(), this.tileEntity.getMaxReceive(), this.tileEntity.getMaxExtract()};
        for (int i = 0; i < values.length; i++)
        {
            if (values[i] == this.lastValues[i]) continue;
            for (IContainerListener listener : this.listeners)
            {
                listener.sendWindowProperty(this, i * 2, values[i] & 0xFFFF);
                listener.sendWindowProperty(this, i * 2 + 1, values[i] >>> 16);
            }
            this.lastValues[i] = values[i];
        }
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        if (id < 0 || id >= this.clientValues.length * 2) return;
        int field = id / 2;
        if (id % 2 == 0) this.clientValues[field] = (this.clientValues[field] & 0xFFFF0000) | (data & 0xFFFF);
        else
        {
            this.clientValues[field] = (this.clientValues[field] & 0xFFFF) | ((data & 0xFFFF) << 16);
            this.tileEntity.setField(field == 0 ? 3 : field == 1 ? 2 : field + 2, this.clientValues[field]);
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
        if (index < 2)
        {
            if (!this.mergeItemStack(stack, 2, 38, true)) return ItemStack.EMPTY;
        }
        else
        {
            boolean moved = false;
            net.minecraftforge.energy.IEnergyStorage energy = stack.getCapability(net.minecraftforge.energy.CapabilityEnergy.ENERGY, null);
            int firstSlot = energy != null && energy.getEnergyStored() == 0 ? 1 : 0;
            for (int i = 0; i < 2; i++)
            {
                int target = (firstSlot + i) % 2;
                if (!this.tileEntity.isEnergyItemValid(target, stack)) continue;
                if (this.mergeItemStack(stack, target, target + 1, false)) { moved = true; break; }
            }
            if (!moved)
            {
                if (index < 29)
                {
                    if (!this.mergeItemStack(stack, 29, 38, false)) return ItemStack.EMPTY;
                }
                else if (!this.mergeItemStack(stack, 2, 29, false)) return ItemStack.EMPTY;
            }
        }
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();
        slot.onTake(player, stack);
        return original;
    }
}