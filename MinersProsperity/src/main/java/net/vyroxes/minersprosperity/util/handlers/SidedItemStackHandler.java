package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SidedItemStackHandler implements IItemHandler
{
    private final TileEntityAlloyFurnace tileEntity;
    private final ItemStackHandler itemStackHandler;
    private final EnumFacing facing;

    public static class SlotState
    {
        public enum SlotType
        {
            INPUT,
            OUTPUT
        }

        public enum IngredientType
        {
            ITEM,
            FLUID
        }

        public enum SlotMode
        {
            DISABLED,
            INPUT,
            OUTPUT
        }

        public enum SlotAutoMode
        {
            DISABLED,
            AUTO_INPUT,
            AUTO_OUTPUT,
            BOTH
        }

        public enum SlotOutputMode
        {
            DEFAULT,
            VOID_EXCESS,
            VOID_ALL
        }

        private SlotType slotType;
        private IngredientType ingredientType;
        private SlotMode slotMode;
        private SlotAutoMode slotAutoMode;
        private SlotOutputMode slotOutputMode;

        public SlotState(SlotType slotType, IngredientType ingredientType, SlotMode slotMode, SlotAutoMode slotAutoMode, SlotOutputMode slotOutputMode)
        {
            this.slotType = slotType;
            this.ingredientType = ingredientType;
            this.slotMode = slotMode;
            this.slotAutoMode = slotAutoMode;
            this.slotOutputMode = slotOutputMode;
        }

        public SlotType getSlotType()
        {
            return this.slotType;
        }

        public void setSlotType(SlotType slotType)
        {
            this.slotType = slotType;
        }

        public IngredientType getIngredientType()
        {
            return this.ingredientType;
        }

        public void setIngredientType(IngredientType ingredientType)
        {
            this.ingredientType = ingredientType;
        }

        public SlotMode getSlotMode()
        {
            return this.slotMode;
        }

        public void setSlotMode(SlotMode slotMode)
        {
            this.slotMode = slotMode;
        }

        public SlotAutoMode getSlotAutoMode()
        {
            return this.slotAutoMode;
        }

        public void setSlotAutoMode(SlotAutoMode slotAutoMode)
        {
            this.slotAutoMode = slotAutoMode;
        }

        public SlotOutputMode getSlotOutputMode()
        {
            return this.slotOutputMode;
        }

        public void setSlotOutputMode(SlotOutputMode slotOutputMode)
        {
            this.slotOutputMode = slotOutputMode;
        }
    }

    private final SlotState[] slotStates;

    public SidedItemStackHandler(TileEntityAlloyFurnace tileEntity, ItemStackHandler itemStackHandler, int inputs, int outputs, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.itemStackHandler = itemStackHandler;
        this.facing = facing;
        this.slotStates = new SlotState[inputs + outputs];

        for (int i = 0; i < inputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.INPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotAutoMode.DISABLED, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs; i < inputs + outputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.OUTPUT, SlotState.SlotAutoMode.AUTO_OUTPUT, SlotState.SlotOutputMode.DEFAULT);
        }
    }

    public boolean isSlotOutput(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType().equals(SlotState.SlotType.OUTPUT);
        }

        return false;
    }

    public SlotState getSlotState(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id];
        }

        return null;
    }

    public void setSlotState(int id, SlotState slotState)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id] = slotState;
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    public SlotState.SlotType getSlotType(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType();
        }

        return null;
    }

    public void setSlotType(int id, SlotState.SlotType slotType)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id].setSlotType(slotType);
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    public SlotState.IngredientType getIngredientType(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getIngredientType();
        }

        return null;
    }

    public void setIngredientType(int id, SlotState.IngredientType ingredientType)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id].setIngredientType(ingredientType);
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    public SlotState.SlotMode getSlotMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotMode();
        }

        return null;
    }

    public void setSlotMode(int id, SlotState.SlotMode slotMode)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id].setSlotMode(slotMode);
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    public SlotState.SlotAutoMode getSlotAutoMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotAutoMode();
        }

        return null;
    }

    public void setSlotAutoMode(int id, SlotState.SlotAutoMode slotAutoMode)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id].setSlotAutoMode(slotAutoMode);
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    public SlotState.SlotOutputMode getSlotOutputMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotOutputMode();
        }

        return null;
    }

    public void setSlotOutputMode(int id, SlotState.SlotOutputMode slotOutputMode)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            this.slotStates[id].setSlotOutputMode(slotOutputMode);
        }

        if (this.tileEntity.getWorld().isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
        }

        this.tileEntity.markDirty();
    }

    @Override
    public int getSlots()
    {
        return this.itemStackHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        return this.itemStackHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (getSlotState(slot).slotMode != SlotState.SlotMode.INPUT)
        {
            return stack;
        }
        else if (isItemValid(slot, stack))
        {
            return itemStackHandler.insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (getSlotState(slot).slotMode != SlotState.SlotMode.OUTPUT)
        {
            return ItemStack.EMPTY;
        }

        return itemStackHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.itemStackHandler.getSlotLimit(slot);
    }
}