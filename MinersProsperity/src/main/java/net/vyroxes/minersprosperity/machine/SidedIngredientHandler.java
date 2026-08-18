package net.vyroxes.minersprosperity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.inventory.CustomItemStackHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.fluid.FluidTankDefinition;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SidedIngredientHandler implements IItemHandler, IFluidHandler, IEnergyStorage
{
    private final TileEntityMachine tileEntity;
    private final CustomItemStackHandler customItemStackHandler;
    private final EnumFacing facing;
    private final MachineSlotLayout slotLayout;
    private final SlotState[] slotStates;
    private final SlotState.SlotMode defaultEnergyMode;
    private final CustomFluidTank[] tanks;
    private final int[] fluidStateIds;
    private final FluidTankDefinition[] fluidTankDefinitions;
    private final int energyStateId;

    public SidedIngredientHandler(TileEntityMachine tileEntity, CustomItemStackHandler customItemStackHandler, CustomFluidTank[] tanks, FluidTankDefinition[] fluidTankDefinitions, MachineSlotLayout slotLayout, boolean energyHandler, SlotState.SlotMode defaultEnergyMode, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.customItemStackHandler = customItemStackHandler;
        this.tanks = tanks != null ? tanks : new CustomFluidTank[0];
        this.fluidTankDefinitions = fluidTankDefinitions != null ? fluidTankDefinitions : new FluidTankDefinition[0];
        this.slotLayout = slotLayout;
        this.facing = facing;

        if (this.tanks.length != this.fluidTankDefinitions.length) throw new IllegalArgumentException("Fluid tanks and fluid tank definitions must have the same length");

        int itemStates = this.slotLayout.getTotalSlots();

        this.defaultEnergyMode = defaultEnergyMode;

        int fluidStates = this.tanks.length;

        this.fluidStateIds = new int[fluidStates];

        for (int i = 0; i < fluidStates; i++) this.fluidStateIds[i] = itemStates + i;

        this.energyStateId = energyHandler ? itemStates + fluidStates : -1;

        int stateCount = itemStates + fluidStates + (energyHandler ? 1 : 0);

        this.slotStates = new SlotState[stateCount];

        // ITEM INPUT
        for (int slot : this.slotLayout.getInputSlots()) this.slotStates[slot] = new SlotState(SlotState.SlotType.INPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);

        // ITEM ENERGY
        for (int slot : this.slotLayout.getEnergySlots()) this.slotStates[slot] = new SlotState(SlotState.SlotType.ENERGY, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);

        // ITEM OUTPUT
        for (int slot : this.slotLayout.getOutputSlots()) this.slotStates[slot] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.OUTPUT, SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);

        // ITEM UPGRADE
        for (int slot : this.slotLayout.getUpgradeSlots()) this.slotStates[slot] = new SlotState(SlotState.SlotType.UPGRADE, SlotState.IngredientType.ITEM, SlotState.SlotMode.DISABLED, SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);

        // FLUIDS
        for (int i = 0; i < fluidStates; i++)
        {
            FluidTankDefinition definition = this.fluidTankDefinitions[i];
            this.slotStates[this.fluidStateIds[i]] = new SlotState(definition.getSlotType(), SlotState.IngredientType.FLUID, definition.getDefaultMode(), SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);
        }

        // ENERGY CAPABILITY
        if (this.energyStateId >= 0) this.slotStates[this.energyStateId] = new SlotState(SlotState.SlotType.ENERGY, SlotState.IngredientType.ENERGY, defaultEnergyMode, SlotState.SlotOutputMode.DEFAULT, SlotState.SlotCondition.DEFAULT);
    }

    public SlotState.SlotMode getDefaultMode(int id)
    {
        SlotState state = getSlotState(id);

        if (state == null) return SlotState.SlotMode.DISABLED;

        if (state.getIngredientType() == SlotState.IngredientType.ENERGY) return sanitizeEnergyMode(this.defaultEnergyMode);

        if (state.getIngredientType() == SlotState.IngredientType.FLUID)
        {
            int tankIndex = getFluidTankIndexFromStateId(id);

            if (tankIndex >= 0) return this.fluidTankDefinitions[tankIndex].getDefaultMode();

            return SlotState.SlotMode.DISABLED;
        }

        if (state.getSlotType() == SlotState.SlotType.OUTPUT) return SlotState.SlotMode.OUTPUT;

        if (state.getSlotType() == SlotState.SlotType.UPGRADE) return SlotState.SlotMode.DISABLED;

        return SlotState.SlotMode.INPUT;
    }

    private boolean matchesEnergyCondition(int slot, ItemStack stack)
    {
        if (!isSlotEnergy(slot)) return true;

        if (stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return false;

        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);

        if (energy == null) return false;

        SlotState.SlotCondition condition = getSlotCondition(slot);

        if (condition == SlotState.SlotCondition.WHEN_EMPTY)
        {
            return energy.getEnergyStored() <= 0;
        }

        if (condition == SlotState.SlotCondition.WHEN_FULL)
        {
            return energy.getMaxEnergyStored() > 0 &&
                    energy.getEnergyStored() >= energy.getMaxEnergyStored();
        }

        return true;
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        List<IFluidTankProperties> properties = new ArrayList<>();

        for (CustomFluidTank tank : tanks) Collections.addAll(properties, tank.getTankProperties());

        return properties.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null || resource.amount <= 0) return 0;

        int remaining = resource.amount;
        int filledTotal = 0;

        for (int i = 0; i < tanks.length && remaining > 0; i++)
        {
            SlotState state = getSlotState(fluidStateIds[i]);

            if (state == null) continue;

            SlotState.SlotMode mode = state.getSlotMode();

            if (mode != SlotState.SlotMode.INPUT && mode != SlotState.SlotMode.AUTO_INPUT && mode != SlotState.SlotMode.INPUT_OUTPUT) continue;

            FluidStack toFill = resource.copy();
            toFill.amount = remaining;

            int filled = tanks[i].fill(toFill, doFill);

            if (filled > 0)
            {
                filledTotal += filled;
                remaining -= filled;
            }
        }

        return filledTotal;
    }

    public int getFluidStateId(int tankIndex)
    {
        if (tankIndex < 0 || tankIndex >= this.fluidStateIds.length) return -1;
        return this.fluidStateIds[tankIndex];
    }

    public int[] getFluidStateIds() { return this.fluidStateIds.clone(); }

    private int getFluidTankIndexFromStateId(int stateId)
    {
        for (int i = 0; i < this.fluidStateIds.length; i++) if (this.fluidStateIds[i] == stateId) return i;
        return -1;
    }

    @Override
    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (resource == null || resource.amount <= 0) return null;

        FluidStack drainedTotal = null;
        int remaining = resource.amount;

        for (int i = 0; i < this.tanks.length; i++)
        {
            if (remaining <= 0) break;

            SlotState state = getSlotState(this.fluidStateIds[i]);

            if (state == null) continue;

            SlotState.SlotMode mode = state.getSlotMode();

            if (mode != SlotState.SlotMode.OUTPUT && mode != SlotState.SlotMode.AUTO_OUTPUT && mode != SlotState.SlotMode.INPUT_OUTPUT) continue;

            FluidStack request = resource.copy();
            request.amount = remaining;

            FluidStack drained = this.tanks[i].drain(request, doDrain);

            if (drained == null || drained.amount <= 0) continue;

            if (drainedTotal == null) drainedTotal = drained.copy();
            else drainedTotal.amount += drained.amount;

            remaining -= drained.amount;
        }

        return drainedTotal;
    }

    @Override
    @Nullable
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (maxDrain <= 0) return null;

        FluidStack drainedTotal = null;
        int remaining = maxDrain;

        for (int i = 0; i < this.tanks.length; i++)
        {
            if (remaining <= 0) break;

            SlotState state = getSlotState(this.fluidStateIds[i]);

            if (state == null) continue;

            SlotState.SlotMode mode = state.getSlotMode();

            if (mode != SlotState.SlotMode.OUTPUT && mode != SlotState.SlotMode.AUTO_OUTPUT && mode != SlotState.SlotMode.INPUT_OUTPUT) continue;

            FluidStack drained;

            if (drainedTotal == null) drained = this.tanks[i].drain(remaining, doDrain);
            else
            {
                FluidStack request = drainedTotal.copy();
                request.amount = remaining;

                drained = this.tanks[i].drain(request, doDrain);
            }

            if (drained == null || drained.amount <= 0) continue;

            if (drainedTotal == null) drainedTotal = drained.copy();
            else drainedTotal.amount += drained.amount;

            remaining -= drained.amount;
        }

        return drainedTotal;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        SlotState state = getEnergyState();
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        if (state == null || storage == null) return 0;

        if (state.getSlotMode() != SlotState.SlotMode.INPUT && state.getSlotMode() != SlotState.SlotMode.INPUT_OUTPUT) return 0;

        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        SlotState state = getEnergyState();
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        if (state == null || storage == null) return 0;

        if (state.getSlotMode() != SlotState.SlotMode.OUTPUT && state.getSlotMode() != SlotState.SlotMode.INPUT_OUTPUT) return 0;

        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        return storage != null ? storage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        return storage != null ? storage.getMaxEnergyStored() : 0;
    }

    @Override
    public boolean canExtract() {
        SlotState state = getEnergyState();
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        if (state == null || storage == null) return false;

        return (state.getSlotMode() == SlotState.SlotMode.OUTPUT || state.getSlotMode() == SlotState.SlotMode.INPUT_OUTPUT) && storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        SlotState state = getEnergyState();
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        if (state == null || storage == null) return false;

        return (state.getSlotMode() == SlotState.SlotMode.INPUT || state.getSlotMode() == SlotState.SlotMode.INPUT_OUTPUT) && storage.canReceive();
    }

    public static class Builder
    {
        private boolean energyHandler = false;

        private SlotState.SlotMode defaultEnergyMode = SlotState.SlotMode.DISABLED;

        public Builder setEnergyHandler(SlotState.SlotMode defaultMode)
        {
            this.energyHandler = true;
            this.defaultEnergyMode = defaultMode;

            return this;
        }

        /*
         * Zostawione tymczasowo dla kompatybilności
         * ze starszymi TileEntity.
         *
         * Liczby slotów są teraz pobierane
         * automatycznie z TileEntityMachine.
         */

        @Deprecated
        public Builder setInputs(int ignored)
        {
            return this;
        }

        @Deprecated
        public Builder setEnergySlots(int ignored)
        {
            return this;
        }

        @Deprecated
        public Builder setOutputs(int ignored)
        {
            return this;
        }

        @Deprecated
        public Builder setUpgradeSlots(int ignored)
        {
            return this;
        }

        public SidedIngredientHandler[] build(TileEntityMachine machine)
        {
            CustomFluidTank[] tanks = machine.getFluidTanks();

            FluidTankDefinition[] definitions = machine.getFluidTankDefinitions();

            MachineSlotLayout slotLayout = machine.getSlotLayout();

            if (slotLayout.getTotalSlots() <= 0 && tanks.length == 0 && !energyHandler) return null;

            SidedIngredientHandler[] handlers = new SidedIngredientHandler[EnumFacing.values().length];

            for (EnumFacing facing : EnumFacing.values()) handlers[facing.ordinal()] = new SidedIngredientHandler(machine, machine.getCustomItemStackHandler(), tanks, definitions, slotLayout, energyHandler, defaultEnergyMode, facing);

            return handlers;
        }
    }

    public boolean isSlotEnergy(int id)
    {
        return id >= 0 && id < this.slotStates.length && this.slotStates[id] != null && this.slotStates[id].getSlotType() == SlotState.SlotType.ENERGY;
    }

    public boolean isSlotOutput(int id)
    {
        return id >= 0 && id < this.slotStates.length && this.slotStates[id] != null && this.slotStates[id].getSlotType() == SlotState.SlotType.OUTPUT;
    }

    public boolean isSlotUpgrade(int id)
    {
        return id >= 0 && id < this.slotStates.length && this.slotStates[id] != null && this.slotStates[id].getSlotType() == SlotState.SlotType.UPGRADE;
    }

    public SlotState getSlotState(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id];

        return null;
    }

    public void setSlotState(int id, SlotState slotState)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id] = slotState;

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, slotState, this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotType getSlotType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id].getSlotType();

        return null;
    }

    public void setSlotType(int id, SlotState.SlotType slotType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotType(slotType);

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    public SlotState.IngredientType getIngredientType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id].getIngredientType();

        return null;
    }

    public void setIngredientType(int id, SlotState.IngredientType ingredientType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setIngredientType(ingredientType);

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotMode getSlotMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id].getSlotMode();

        return null;
    }

    public void setSlotMode(int id, SlotState.SlotMode slotMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            if (this.slotStates[id].getIngredientType() == SlotState.IngredientType.ENERGY) slotMode = sanitizeEnergyMode(slotMode);

            this.slotStates[id].setSlotMode(slotMode);

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotOutputMode getSlotOutputMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id].getSlotOutputMode();

        return null;
    }

    public void setSlotOutputMode(int id, SlotState.SlotOutputMode slotOutputMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotOutputMode(slotOutputMode);

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotCondition getSlotCondition(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null) return this.slotStates[id].getSlotCondition();

        return null;
    }

    public void setSlotCondition(int id, SlotState.SlotCondition slotCondition)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotCondition(slotCondition);

            if (this.tileEntity.getWorld().isRemote) NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());

            this.tileEntity.markDirty();
        }
    }

    private SlotState getEnergyState()
    {
        if (this.energyStateId < 0 || this.energyStateId >= this.slotStates.length) return null;

        return this.slotStates[this.energyStateId];
    }

    public int getEnergyStateId()
    {
        return this.energyStateId;
    }

    public boolean hasEnergyHandler()
    {
        return this.energyStateId >= 0;
    }

    @Override
    public int getSlots() { return this.customItemStackHandler != null ? this.customItemStackHandler.getSlots() : 0; }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;
        return this.customItemStackHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (this.customItemStackHandler == null) return stack;

        if (slot < 0 || slot >= this.customItemStackHandler.getSlots()) return stack;

        SlotState state = getSlotState(slot);

        if (state == null) return stack;

        if (state.getSlotMode() != SlotState.SlotMode.INPUT && state.getSlotMode() != SlotState.SlotMode.AUTO_INPUT && state.getSlotMode() != SlotState.SlotMode.INPUT_OUTPUT) return stack;

        if (!matchesEnergyCondition(slot, stack)) return stack;

        if (customItemStackHandler.isItemValid(slot, stack)) return customItemStackHandler.insertItem(slot, stack, simulate);

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;

        if (slot < 0 || slot >= this.customItemStackHandler.getSlots()) return ItemStack.EMPTY;

        SlotState state = getSlotState(slot);

        if (state == null) return ItemStack.EMPTY;

        if (state.getSlotMode() != SlotState.SlotMode.OUTPUT && state.getSlotMode() != SlotState.SlotMode.AUTO_OUTPUT && state.getSlotMode() != SlotState.SlotMode.INPUT_OUTPUT) return ItemStack.EMPTY;

        ItemStack stack = customItemStackHandler.getStackInSlot(slot);

        if (!matchesEnergyCondition(slot, stack)) return ItemStack.EMPTY;

        return customItemStackHandler.extractItem(slot, amount, simulate);
    }

    public void autoInput()
    {
        if (this.customItemStackHandler == null || this.tileEntity == null || this.facing == null || this.slotLayout == null) return;

        EnumFacing offsetFacing = this.tileEntity.getWorldSide(this.tileEntity.getMachineFacing(), this.facing);
        if (offsetFacing == null) return;

        TileEntity neighborTile = this.tileEntity.getWorld().getTileEntity(this.tileEntity.getPos().offset(offsetFacing));
        if (neighborTile == null) return;

        EnumFacing neighborSide = offsetFacing.getOpposite();

        if (!neighborTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, neighborSide)) return;

        IItemHandler itemHandler = neighborTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, neighborSide);
        if (itemHandler == null) return;

        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            ItemStack extracted = itemHandler.extractItem(i, Integer.MAX_VALUE, true);
            if (extracted.isEmpty()) continue;

            for (int j = 0; j < this.slotLayout.getTotalSlots(); j++)
            {
                SlotState state = this.getSlotState(j);

                if (state == null) continue;
                if (state.getIngredientType() != SlotState.IngredientType.ITEM) continue;
                if (state.getSlotMode() != SlotState.SlotMode.AUTO_INPUT) continue;

                ItemStack remaining = this.insertItem(j, extracted, true);

                int accepted = extracted.getCount() - remaining.getCount();
                if (accepted <= 0) continue;

                ItemStack actuallyExtracted = itemHandler.extractItem(i, accepted, false);
                if (actuallyExtracted.isEmpty()) break;

                ItemStack leftover = this.insertItem(j, actuallyExtracted, false);

                if (!leftover.isEmpty())
                {
                    for (int k = 0; k < itemHandler.getSlots(); k++)
                    {
                        leftover = itemHandler.insertItem(k, leftover, false);
                        if (leftover.isEmpty()) break;
                    }
                }

                break;
            }
        }
    }

    public void autoInputFluid()
    {
        if (this.facing == null || this.tileEntity == null) return;

        EnumFacing offsetFacing = this.tileEntity.getWorldSide(this.tileEntity.getMachineFacing(), this.facing);
        TileEntity neighborTile = this.tileEntity.getWorld().getTileEntity(this.tileEntity.getPos().offset(offsetFacing));

        if (neighborTile == null || !neighborTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, offsetFacing.getOpposite())) return;

        IFluidHandler fluidHandler = neighborTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, offsetFacing.getOpposite());

        if (fluidHandler == null) return;

        IFluidTankProperties[] properties = fluidHandler.getTankProperties();

        for (int i = 0; i < this.tanks.length; i++)
        {
            SlotState state = getSlotState(this.fluidStateIds[i]);

            if (state == null || state.getSlotMode() != SlotState.SlotMode.AUTO_INPUT) continue;

            for (IFluidTankProperties property : properties)
            {
                FluidStack available = property.getContents();

                if (available == null || available.amount <= 0) continue;

                FluidStack candidate = available.copy();

                int canFill = this.tanks[i].fill(candidate, false);

                if (canFill <= 0) continue;

                candidate.amount = Math.min(canFill, available.amount);

                FluidStack simulatedDrain = fluidHandler.drain(candidate, false);

                if (simulatedDrain == null || simulatedDrain.amount <= 0) continue;

                int accepted = this.tanks[i].fill(simulatedDrain, false);

                if (accepted <= 0) continue;

                FluidStack request = simulatedDrain.copy();
                request.amount = accepted;

                FluidStack actuallyDrained = fluidHandler.drain(request, true);

                if (actuallyDrained != null && actuallyDrained.amount > 0) this.tanks[i].fill(actuallyDrained, true);

                break;
            }
        }
    }

    public void autoOutput()
    {
        if (this.customItemStackHandler == null) return;

        if (this.facing == null || this.tileEntity == null) return;

        EnumFacing offsetFacing = this.tileEntity.getWorldSide(this.tileEntity.getMachineFacing(), this.facing);
        TileEntity neighborTile = this.tileEntity.getWorld().getTileEntity(this.tileEntity.getPos().offset(offsetFacing));

        if (neighborTile == null || !neighborTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, offsetFacing.getOpposite())) return;

        IItemHandler itemHandler = neighborTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, offsetFacing.getOpposite());

        if (itemHandler == null) return;

        for (int i = 0; i < this.slotLayout.getTotalSlots(); i++)
        {
            SlotState state = this.getSlotState(i);

            if (state == null) continue;

            if (state.getIngredientType() != SlotState.IngredientType.ITEM) continue;

            if (state.getSlotMode() != SlotState.SlotMode.AUTO_OUTPUT) continue;

            ItemStack simulatedExtract = this.extractItem(i, Integer.MAX_VALUE, true);

            if (simulatedExtract.isEmpty()) continue;

            ItemStack remaining = simulatedExtract.copy();

            for (int j = 0; j < itemHandler.getSlots(); j++)
            {
                remaining = itemHandler.insertItem(j, remaining, false);
                if (remaining.isEmpty()) break;
            }

            int inserted = simulatedExtract.getCount() - remaining.getCount();

            if (inserted > 0) this.extractItem(i, inserted, false);
        }
    }

    public void autoOutputFluid()
    {
        if (this.facing == null || this.tileEntity == null) return;

        EnumFacing offsetFacing = this.tileEntity.getWorldSide(this.tileEntity.getMachineFacing(), this.facing);
        TileEntity neighborTile = this.tileEntity.getWorld().getTileEntity(this.tileEntity.getPos().offset(offsetFacing));

        if (neighborTile == null || !neighborTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, offsetFacing.getOpposite())) return;

        IFluidHandler fluidHandler = neighborTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, offsetFacing.getOpposite());

        if (fluidHandler == null) return;

        for (int i = 0; i < tanks.length; i++)
        {
            SlotState state = getSlotState(fluidStateIds[i]);

            if (state == null || state.getSlotMode() != SlotState.SlotMode.AUTO_OUTPUT) continue;

            FluidStack fluid = tanks[i].getFluid();

            if (fluid == null || fluid.amount <= 0) continue;

            FluidStack simulated = fluid.copy();

            int accepted = fluidHandler.fill(simulated, false);

            if (accepted <= 0) continue;

            FluidStack toSend = fluid.copy();
            toSend.amount = Math.min(accepted, fluid.amount);

            int actuallyAccepted = fluidHandler.fill(toSend, true);

            if (actuallyAccepted > 0) tanks[i].drainInternal(actuallyAccepted, true);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (this.customItemStackHandler == null) return 0;
        return this.customItemStackHandler.getSlotLimit(slot);
    }

    public int getStateCount() { return this.slotStates.length; }

    public boolean supportsEnergyInput()
    {
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        return storage != null && storage.canReceive();
    }

    public boolean supportsEnergyOutput()
    {
        IEnergyStorage storage = this.tileEntity.getEnergyStorage();

        return storage != null && storage.canExtract();
    }

    private SlotState.SlotMode sanitizeEnergyMode(SlotState.SlotMode mode)
    {
        boolean canInput = supportsEnergyInput();
        boolean canOutput = supportsEnergyOutput();

        return switch (mode)
        {
            case DISABLED -> SlotState.SlotMode.DISABLED;
            case INPUT, AUTO_INPUT -> canInput ? SlotState.SlotMode.INPUT : SlotState.SlotMode.DISABLED;
            case OUTPUT, AUTO_OUTPUT -> canOutput ? SlotState.SlotMode.OUTPUT : SlotState.SlotMode.DISABLED;
            case INPUT_OUTPUT ->
            {
                if (canInput && canOutput) yield SlotState.SlotMode.INPUT_OUTPUT;
                if (canInput) yield SlotState.SlotMode.INPUT;
                if (canOutput) yield SlotState.SlotMode.OUTPUT;

                yield SlotState.SlotMode.DISABLED;
            }
        };
    }

    public SlotState.SlotMode getNextEnergyMode(SlotState.SlotMode currentMode, boolean reverse)
    {
        boolean canInput = supportsEnergyInput();
        boolean canOutput = supportsEnergyOutput();

        currentMode = sanitizeEnergyMode(currentMode);

        if (canInput && canOutput)
        {
            if (!reverse)
            {
                return switch (currentMode)
                {
                    case DISABLED -> SlotState.SlotMode.INPUT;
                    case INPUT -> SlotState.SlotMode.OUTPUT;
                    case OUTPUT -> SlotState.SlotMode.INPUT_OUTPUT;
                    case INPUT_OUTPUT -> SlotState.SlotMode.DISABLED;
                    case AUTO_INPUT, AUTO_OUTPUT -> SlotState.SlotMode.DISABLED;
                };
            }

            return switch (currentMode)
            {
                case DISABLED -> SlotState.SlotMode.INPUT_OUTPUT;
                case INPUT -> SlotState.SlotMode.DISABLED;
                case OUTPUT -> SlotState.SlotMode.INPUT;
                case INPUT_OUTPUT -> SlotState.SlotMode.OUTPUT;
                case AUTO_INPUT, AUTO_OUTPUT -> SlotState.SlotMode.DISABLED;
            };
        }

        if (canInput) return currentMode == SlotState.SlotMode.INPUT ? SlotState.SlotMode.DISABLED : SlotState.SlotMode.INPUT;

        if (canOutput) return currentMode == SlotState.SlotMode.OUTPUT ? SlotState.SlotMode.DISABLED : SlotState.SlotMode.OUTPUT;

        return SlotState.SlotMode.DISABLED;
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
        if (!tag.hasKey(this.facing.toString() + "SlotStates", 9)) return;

        NBTTagList slotsTagList = tag.getTagList(this.facing.toString() + "SlotStates", 10);
        int count = Math.min(slotsTagList.tagCount(), this.slotStates.length);

        for (int i = 0; i < count; i++)
        {
            if (this.slotStates[i] == null) continue;

            NBTTagCompound slotTag = slotsTagList.getCompoundTagAt(i);

            this.slotStates[i].readFromNBT(slotTag);

            if (this.slotStates[i].getIngredientType() == SlotState.IngredientType.ENERGY) this.slotStates[i].setSlotMode(sanitizeEnergyMode(this.slotStates[i].getSlotMode()));
        }
    }
}