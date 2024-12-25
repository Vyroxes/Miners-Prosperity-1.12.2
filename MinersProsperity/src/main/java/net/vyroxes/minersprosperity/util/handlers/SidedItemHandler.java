package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SidedItemHandler implements IItemHandler
{
    private final TileEntityAlloyFurnace tileEntity;
    private final ItemStackHandler machineSlots;
    private final EnumFacing facing;
    public enum SlotState
    {
        DISABLED,
        INPUT,
        OUTPUT
    }
    private final SlotState[] inputs;
    private final SlotState[] outputs;

    public SidedItemHandler(TileEntityAlloyFurnace tileEntity, ItemStackHandler machineSlots, int inputs, int outputs, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.machineSlots = machineSlots;
        this.facing = facing;

        this.inputs = new SlotState[inputs];
        this.outputs = new SlotState[outputs];

        Arrays.fill(this.inputs, SlotState.DISABLED);
        Arrays.fill(this.outputs, SlotState.DISABLED);
    }

    public SlotState[] getInputs()
    {
        return this.inputs;
    }

    public SlotState[] getOutputs()
    {
        return this.outputs;
    }

    public boolean isSlotOutput(int id)
    {
        return id >= this.inputs.length;
    }

    public SlotState getSlotState(int id)
    {
        if (id < inputs.length)
        {
            return this.inputs[id];
        }
        else
        {
            int outputIndex = id - this.inputs.length;
            if (outputIndex < this.outputs.length)
            {
                return this.outputs[outputIndex];
            }
        }
        return SlotState.DISABLED;
    }

    public void setSlotState(int id, SlotState slotsState)
    {
        if (id < this.inputs.length)
        {
            this.inputs[id] = slotsState;
        }
        else
        {
            int outputIndex = id - this.inputs.length;
            if (outputIndex < this.outputs.length)
            {
                this.outputs[outputIndex] = slotsState;
            }
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, slotsState, this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    @Override
    public int getSlots()
    {
        return this.machineSlots.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        return this.machineSlots.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (getSlotState(slot) != SlotState.INPUT)
        {
            return stack;
        }
        else if (isItemValid(slot, stack))
        {
            return machineSlots.insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (getSlotState(slot) != SlotState.OUTPUT)
        {
            return ItemStack.EMPTY;
        }

        return machineSlots.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.machineSlots.getSlotLimit(slot);
    }
}