package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.objects.fluids.CustomFluidTank;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidedIngredientHandler implements IItemHandler, IFluidHandler
{
    private final TileEntityMachine tileEntity;
    private final CustomItemStackHandler customItemStackHandler;
    private final CustomFluidTank tank;
    private final EnumFacing facing;

    private final SlotState[] slotStates;

    public SidedIngredientHandler(TileEntityMachine tileEntity, CustomItemStackHandler customItemStackHandler, CustomFluidTank tank, int inputs, int energy, int outputs, int upgrades, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.customItemStackHandler = customItemStackHandler;
        this.tank = tank;
        this.facing = facing;
        if (this.tank != null) this.slotStates = new SlotState[inputs + energy + outputs + upgrades + 1];
        else this.slotStates = new SlotState[inputs + energy + outputs + upgrades];

        for (int i = 0; i < inputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.INPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs; i < inputs + energy; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.ENERGY, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs + energy; i < inputs + energy + outputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.OUTPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs + energy + outputs; i < inputs + energy + outputs + upgrades; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.UPGRADE, SlotState.IngredientType.ITEM, SlotState.SlotMode.DISABLED, SlotState.SlotOutputMode.DEFAULT);
        }

        if (this.tank != null)
        {
            for (int i = inputs + energy + outputs + upgrades; i < inputs + energy + outputs + upgrades + 1; i++)
            {
                this.slotStates[i] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.FLUID, SlotState.SlotMode.OUTPUT, SlotState.SlotOutputMode.DEFAULT);
            }
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.tank.fill(resource, doFill);
    }

    @Override
    public @Nullable FluidStack drain(FluidStack resource, boolean doDrain) {
        if (getIngredientType(this.getSlots()-1) == SlotState.IngredientType.FLUID)
        {
            this.tank.setCanDrain(this.getSlotMode(this.getSlots() - 1) == SlotState.SlotMode.OUTPUT || this.getSlotMode(this.getSlots() - 1) == SlotState.SlotMode.AUTO_OUTPUT);
        }
        return this.tank.drain(resource, doDrain);
    }

    @Override
    public @Nullable FluidStack drain(int maxDrain, boolean doDrain) {
        if (getIngredientType(this.getSlots()-1) == SlotState.IngredientType.FLUID)
        {
            this.tank.setCanDrain(this.getSlotMode(this.getSlots() - 1) == SlotState.SlotMode.OUTPUT || this.getSlotMode(this.getSlots() - 1) == SlotState.SlotMode.AUTO_OUTPUT);
        }
        return this.tank.drain(maxDrain, doDrain);
    }

    public static class Builder
    {
        private int inputs = 0;
        private int energySlots = 0;
        private int outputs = 0;
        private int upgradeSlots = 0;

        public Builder setInputs(int inputs)
        {
            this.inputs = inputs;
            return this;
        }

        public Builder setEnergySlots(int energySlots)
        {
            this.energySlots = energySlots;
            return this;
        }

        public Builder setOutputs(int outputs)
        {
            this.outputs = outputs;
            return this;
        }

        public Builder setUpgradeSlots(int upgradeSlots)
        {
            this.upgradeSlots = upgradeSlots;
            return this;
        }

        public SidedIngredientHandler[] build(TileEntityMachine machine)
        {
            if (inputs + energySlots + outputs + upgradeSlots <= 0)
            {
                return null;
            }

            SidedIngredientHandler[] handlers = new SidedIngredientHandler[EnumFacing.values().length];
            for (EnumFacing facing : EnumFacing.values())
            {
                handlers[facing.ordinal()] = new SidedIngredientHandler(machine, machine.getCustomItemStackHandler(), machine.getFluidTank(), inputs, energySlots, outputs, upgradeSlots, facing);
            }
            return handlers;
        }
    }

    public boolean isSlotEnergy(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType().equals(SlotState.SlotType.ENERGY);
        }

        return false;
    }

    public boolean isSlotUpgrade(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType().equals(SlotState.SlotType.UPGRADE);
        }

        return false;
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
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id];
        }

        return null;
    }

    public void setSlotState(int id, SlotState slotState)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id] = slotState;

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, slotState, this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotType getSlotType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotType();
        }

        return null;
    }

    public void setSlotType(int id, SlotState.SlotType slotType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotType(slotType);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.IngredientType getIngredientType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getIngredientType();
        }

        return null;
    }

    public void setIngredientType(int id, SlotState.IngredientType ingredientType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setIngredientType(ingredientType);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotMode getSlotMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotMode();
        }

        return null;
    }

    public void setSlotMode(int id, SlotState.SlotMode slotMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotMode(slotMode);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotOutputMode getSlotOutputMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotOutputMode();
        }

        return null;
    }

    public void setSlotOutputMode(int id, SlotState.SlotOutputMode slotOutputMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotOutputMode(slotOutputMode);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    @Override
    public int getSlots()
    {
        if (this.tank != null) return this.customItemStackHandler.getSlots() + 1;
        else return this.customItemStackHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        return this.customItemStackHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (getSlotState(slot).getSlotMode() != SlotState.SlotMode.INPUT && getSlotState(slot).getSlotMode() != SlotState.SlotMode.AUTO_INPUT)
        {
            return stack;
        }
        else if (customItemStackHandler.isItemValid(slot, stack))
        {
            return customItemStackHandler.insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (getSlotState(slot).getSlotMode() != SlotState.SlotMode.OUTPUT && getSlotState(slot).getSlotMode() != SlotState.SlotMode.AUTO_OUTPUT)
        {
            return ItemStack.EMPTY;
        }

        return customItemStackHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.customItemStackHandler.getSlotLimit(slot);
    }

    public void writeToNBT(@NotNull NBTTagCompound tag)
    {
        NBTTagList slotsTagList = new NBTTagList();

        for (SlotState slotState : this.slotStates)
        {
            NBTTagCompound slotTag = new NBTTagCompound();
            if (slotState != null)
            {
                slotState.writeToNBT(slotTag);
            }
            slotsTagList.appendTag(slotTag);
        }

        tag.setTag(this.facing.toString() + "SlotStates", slotsTagList);
    }

    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        if (tag.hasKey(this.facing.toString() + "SlotStates", 9))
        {
            NBTTagList slotsTagList = tag.getTagList(this.facing.toString() + "SlotStates", 10);
            for (int i = 0; i < slotsTagList.tagCount(); i++)
            {
                NBTTagCompound slotTag = slotsTagList.getCompoundTagAt(i);
                if (i < this.slotStates.length)
                {
                    this.slotStates[i].readFromNBT(slotTag);
                }
            }
        }
    }
}