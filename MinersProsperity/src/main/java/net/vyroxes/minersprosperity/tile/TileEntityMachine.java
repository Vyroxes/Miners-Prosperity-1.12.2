package net.vyroxes.minersprosperity.tile;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModFluids;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.inventory.CustomItemStackHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.machine.MachineSlotLayout;
import net.vyroxes.minersprosperity.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.fluid.FluidTankDefinition;
import net.vyroxes.minersprosperity.fluid.MultiFluidHandler;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityMachine extends TileEntity implements ITickable
{
    protected final MachineSlotLayout slotLayout;
    protected final CustomEnergyStorage storage;
    protected final FluidTankDefinition[] fluidTankDefinitions;
    protected final CustomFluidTank[] fluidTanks;
    protected final MultiFluidHandler fluidHandler;
    protected final int experienceTankIndex;
    protected final SidedIngredientHandler[] sidedIngredientHandlers;
    protected final CustomItemStackHandler customItemStackHandler;
    protected int cookTime;
    protected int totalCookTime;
    protected String customName;
    protected int redstoneControlButtonState;
    protected int slotEditedId;
    protected int energyUsage;
    protected int energyGeneration;
    protected int progressDirection = 0;
    private float clientRenderCookTime;
    private float clientPreviousRenderCookTime;
    private boolean clientProgressInitialized = false;
    private static final int PROGRESS_SYNC_INTERVAL = 10;

    protected TileEntityMachine(Builder builder)
    {
        this.slotLayout = new MachineSlotLayout(builder.inputs, builder.energySlots, builder.outputs, builder.upgradeSlots);
        this.storage = builder.energyCapacity > 0 ? new CustomEnergyStorage(builder.energyCapacity, builder.maxReceive, builder.maxExtract, builder.storedEnergy) : null;

        this.fluidTankDefinitions = builder.fluidTankDefinitions.toArray(new FluidTankDefinition[0]);
        this.fluidTanks = new CustomFluidTank[this.fluidTankDefinitions.length];

        int experienceTankIndex = -1;

        for (int i = 0; i < this.fluidTankDefinitions.length; i++)
        {
            FluidTankDefinition definition = this.fluidTankDefinitions[i];

            this.fluidTanks[i] = definition.createTank();

            if (definition.isExperienceTank())
            {
                if (experienceTankIndex >= 0) throw new IllegalStateException("A machine can only have one experience tank");
                experienceTankIndex = i;
            }
        }

        this.experienceTankIndex = experienceTankIndex;

        this.fluidHandler = this.fluidTanks.length > 0 ? new MultiFluidHandler(this.fluidTanks) : null;

        this.customItemStackHandler = this.slotLayout.getTotalSlots() > 0 ? new CustomItemStackHandler(this, this.slotLayout.getInputCount(), this.slotLayout.getEnergyCount(), this.slotLayout.getOutputCount(), this.slotLayout.getUpgradeCount()) : null;

        this.sidedIngredientHandlers = builder.sidedIngredientHandlerBuilder != null ? builder.sidedIngredientHandlerBuilder.build(this) : null;
    }

    public static class Builder
    {
        private int energyCapacity = 0;
        private int maxReceive = 0;
        private int maxExtract = 0;
        private int storedEnergy = 0;

        private final List<FluidTankDefinition> fluidTankDefinitions = new ArrayList<>();

        private int inputs = 0;
        private int energySlots = 0;
        private int outputs = 0;
        private int upgradeSlots = 0;

        private SidedIngredientHandler.Builder sidedIngredientHandlerBuilder;

        public Builder setEnergy(int capacity, int maxReceive, int maxExtract, int stored)
        {
            this.energyCapacity = capacity;
            this.maxReceive = maxReceive;
            this.maxExtract = maxExtract;
            this.storedEnergy = stored;
            return this;
        }

        public Builder setItemInput(int inputs)
        {
            this.inputs = inputs;
            return this;
        }

        public Builder setItemEnergy(int energySlots)
        {
            this.energySlots = energySlots;
            return this;
        }

        public Builder setItemOutput(int outputs)
        {
            this.outputs = outputs;
            return this;
        }

        public Builder setItemUpgrade(int upgradeSlots)
        {
            this.upgradeSlots = upgradeSlots;
            return this;
        }

        public Builder addFluidTank(@Nullable Fluid fluid, int capacity, int storedFluid, boolean canDrain, boolean canFill, SlotState.SlotType slotType, SlotState.SlotMode defaultMode)
        {
            this.fluidTankDefinitions.add(new FluidTankDefinition(fluid, capacity, storedFluid, canDrain, canFill, slotType, defaultMode, false));
            return this;
        }

        public Builder addExperienceTank(int capacity)
        {
            this.fluidTankDefinitions.add(new FluidTankDefinition(TileEntityMachine.getConfiguredXpFluid().getFluid(), capacity, 0, true, false, SlotState.SlotType.OUTPUT, SlotState.SlotMode.OUTPUT, true));
            return this;
        }

        public Builder setSidedIngredientHandler(SidedIngredientHandler.Builder builder)
        {
            this.sidedIngredientHandlerBuilder = builder;
            return this;
        }
    }

    public static FluidStack getConfiguredXpFluid()
    {
        String[] parts = ModConfig.MACHINES.experienceFluid.split(":");
        if (parts.length != 2) return new FluidStack(ModFluids.LIQUID_EXPERIENCE, 0);

        Fluid fluid = FluidRegistry.getFluid(parts[1]);
        if (fluid == null) fluid = ModFluids.LIQUID_EXPERIENCE;

        return new FluidStack(fluid, 0);
    }

    public MachineSlotLayout getSlotLayout()
    {
        return this.slotLayout;
    }

    public int getInputSlotCount()
    {
        return this.slotLayout.getInputCount();
    }

    public int getEnergySlotCount()
    {
        return this.slotLayout.getEnergyCount();
    }

    public int getOutputSlotCount()
    {
        return this.slotLayout.getOutputCount();
    }

    public int getUpgradeSlotCount()
    {
        return this.slotLayout.getUpgradeCount();
    }

    public int getInputSlotId(int index)
    {
        return this.slotLayout.getInputSlot(index);
    }

    public int getEnergySlotId(int index)
    {
        return this.slotLayout.getEnergySlot(index);
    }

    public int getEnergySlotId()
    {
        if (this.slotLayout.getEnergyCount() == 0) return -1;

        return this.slotLayout.getEnergySlot(0);
    }

    public int getOutputSlotId(int index)
    {
        return this.slotLayout.getOutputSlot(index);
    }

    public int getUpgradeSlotId(int index)
    {
        return this.slotLayout.getUpgradeSlot(index);
    }

    @Nullable
    public MachineUpgradeType getUpgradeType(int index)
    {
        if (index < 0 || index >= this.getUpgradeSlotCount()) return null;

        return switch (index)
        {
            case 0 -> MachineUpgradeType.MACHINE;
            case 1 -> MachineUpgradeType.SPEED;
            case 2 -> MachineUpgradeType.ENERGY;
            case 3 -> MachineUpgradeType.EXPERIENCE;
            default -> null;
        };
    }

    public int getUpgradeSlotId(MachineUpgradeType type)
    {
        if (type == null) return -1;

        for (int i = 0; i < this.getUpgradeSlotCount(); i++) if (this.getUpgradeType(i) == type) return this.slotLayout.getUpgradeSlot(i);

        return -1;
    }

    public int[] getInputSlotIds()
    {
        return this.slotLayout.getInputSlots();
    }

    public int[] getEnergySlotIds()
    {
        return this.slotLayout.getEnergySlots();
    }

    public int[] getOutputSlotIds()
    {
        return this.slotLayout.getOutputSlots();
    }

    public int[] getUpgradeSlotIds()
    {
        return this.slotLayout.getUpgradeSlots();
    }

    public ItemStack getInputStack(int index)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;

        return this.customItemStackHandler.getStackInSlot(this.slotLayout.getInputSlot(index));
    }

    public ItemStack getEnergyStack(int index)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;

        return this.customItemStackHandler.getStackInSlot(this.slotLayout.getEnergySlot(index));
    }

    public ItemStack getOutputStack(int index)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;

        return this.customItemStackHandler.getStackInSlot(this.slotLayout.getOutputSlot(index));
    }

    public ItemStack getUpgradeStack(int index)
    {
        if (this.customItemStackHandler == null) return ItemStack.EMPTY;

        return this.customItemStackHandler.getStackInSlot(this.slotLayout.getUpgradeSlot(index));
    }

    public ItemStack getUpgradeStack(MachineUpgradeType type)
    {
        int slot = this.getUpgradeSlotId(type);

        if (slot < 0 || this.customItemStackHandler == null) return ItemStack.EMPTY;

        return this.customItemStackHandler.getStackInSlot(slot);
    }

    public int getUpgradeCount(MachineUpgradeType type)
    {
        return this.getUpgradeStack(type).getCount();
    }

    public void setInputStack(int index, ItemStack stack)
    {
        if (this.customItemStackHandler == null) return;

        this.customItemStackHandler.setStackInSlot(this.slotLayout.getInputSlot(index), stack);
    }

    public void setOutputStack(int index, ItemStack stack)
    {
        if (this.customItemStackHandler == null) return;

        this.customItemStackHandler.setStackInSlot(this.slotLayout.getOutputSlot(index), stack);
    }

    public void clearInputStack(int index)
    {
        this.setInputStack(index, ItemStack.EMPTY);
    }

    @Nullable
    public SidedIngredientHandler getSidedHandler(EnumFacing side)
    {
        if (side == null || this.sidedIngredientHandlers == null) return null;

        return this.sidedIngredientHandlers[side.ordinal()];
    }

    public SlotState.SlotOutputMode getOutputMode(EnumFacing side, int outputIndex)
    {
        SidedIngredientHandler handler = this.getSidedHandler(side);

        if (handler == null) return SlotState.SlotOutputMode.DEFAULT;

        return handler.getSlotOutputMode(this.getOutputSlotId(outputIndex));
    }

    protected void runAutomaticItemIO()
    {
        if (this.sidedIngredientHandlers == null) return;

        for (EnumFacing facing : EnumFacing.values())
        {
            SidedIngredientHandler handler = this.sidedIngredientHandlers[facing.ordinal()];
            if (handler == null) continue;

            handler.autoInput();
            handler.autoOutput();
        }
    }

    protected void runAutomaticFluidIO()
    {
        if (this.sidedIngredientHandlers == null || this.fluidTanks.length == 0) return;

        for (EnumFacing facing : EnumFacing.values())
        {
            SidedIngredientHandler handler = this.sidedIngredientHandlers[facing.ordinal()];
            if (handler == null) continue;

            handler.autoInputFluid();
            handler.autoOutputFluid();
        }
    }

    protected void runAutomaticIO()
    {
        this.runAutomaticItemIO();
        this.runAutomaticFluidIO();
    }

    protected boolean transferEnergyFromItemSlots()
    {
        if (this.storage == null || this.customItemStackHandler == null || this.slotLayout.getEnergyCount() == 0) return false;

        int energyBefore = this.storage.getEnergyStored();

        for (int i = 0; i < this.slotLayout.getEnergyCount(); i++)
        {
            if (this.storage.getEnergyStored() >= this.storage.getMaxEnergyStored()) break;

            ItemStack stack = this.getEnergyStack(i);

            if (!stack.isEmpty()) this.setEnergyStoredFromItem(stack);
        }

        return energyBefore != this.storage.getEnergyStored();
    }

    protected void updateEnergyStorageUpgrade()
    {
        if (this.storage == null) return;

        int upgrades = this.getUpgradeCount(MachineUpgradeType.ENERGY);

        this.storage.setEnergyUpgraded(upgrades, false, this instanceof TileEntityProcessingMachine);
    }

    protected boolean canMachineOperate()
    {
        if (this.world == null) return false;

        if (this.redstoneControlButtonState <= 0) return true;

        return this.canOperateBasedOnRedstone(this.world.isBlockPowered(this.pos));
    }

    public int getFluidTankCount() { return this.fluidTanks.length; }

    public CustomFluidTank[] getFluidTanks() { return this.fluidTanks; }

    public FluidTankDefinition[] getFluidTankDefinitions() { return this.fluidTankDefinitions; }

    @Nullable
    public CustomFluidTank getFluidTank(int index)
    {
        if (index < 0 || index >= this.fluidTanks.length) return null;
        return this.fluidTanks[index];
    }

    public int getExperienceTankIndex() { return this.experienceTankIndex; }

    @Nullable
    public CustomFluidTank getExperienceTank()
    {
        if (this.experienceTankIndex < 0 || this.experienceTankIndex >= this.fluidTanks.length) return null;
        return this.fluidTanks[this.experienceTankIndex];
    }

    public int getExperienceTankStateId()
    {
        if (this.experienceTankIndex < 0 || this.sidedIngredientHandlers == null) return -1;
        return this.sidedIngredientHandlers[0].getFluidStateId(this.experienceTankIndex);
    }

    public int getFluidTankStateId(int tankIndex)
    {
        if (this.sidedIngredientHandlers == null) return -1;
        return this.sidedIngredientHandlers[0].getFluidStateId(tankIndex);
    }

    public CustomEnergyStorage getEnergyStorage() { return this.storage; }

    public CustomItemStackHandler getCustomItemStackHandler() { return this.customItemStackHandler; }

    public IFluidHandler[] getSidedIngredientHandlersFluid() { return this.sidedIngredientHandlers; }

    public IItemHandler[] getSidedIngredientHandlers() { return this.sidedIngredientHandlers; }

    @Deprecated
    @Nullable
    public IFluidHandler getSidedIngredientHandlerFluid(EnumFacing side) { return this.sidedIngredientHandlers[side.ordinal()]; }

    @Deprecated
    @Nullable
    public IItemHandler getSidedIngredientHandler(EnumFacing side) { return this.sidedIngredientHandlers[side.ordinal()]; }

    public boolean isSlotEnergy(int slot)
    {
        if (this.sidedIngredientHandlers != null) return this.sidedIngredientHandlers[0].isSlotEnergy(slot);
        return false;
    }

    public boolean isSlotOutput(int slot)
    {
        if (this.sidedIngredientHandlers != null) return this.sidedIngredientHandlers[0].isSlotOutput(slot);
        return false;
    }

    public int getSlotEditedId() { return this.slotEditedId; }

    public void setSlotEditedId(int slotEditedId) { this.slotEditedId = slotEditedId; }

    public int getEnergyHandlerStateId()
    {
        if (this.sidedIngredientHandlers == null) return -1;
        return this.sidedIngredientHandlers[0].getEnergyStateId();
    }

    public boolean hasSidedEnergyHandler() { return getEnergyHandlerStateId() >= 0; }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.customItemStackHandler != null) return true;

        if (capability == CapabilityEnergy.ENERGY && this.storage != null)
        {
            if (facing == null || !this.hasSidedEnergyHandler()) return true;

            if (this.sidedIngredientHandlers == null) return false;

            EnumFacing relativeSide = getRelativeSide(getMachineFacing(), facing);
            SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[relativeSide.ordinal()];

            return sidedHandler != null && sidedHandler.hasEnergyHandler();
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.fluidHandler != null) return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.customItemStackHandler != null)
        {
            if (facing == null || this.sidedIngredientHandlers == null) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.customItemStackHandler);

            EnumFacing relativeSide = getRelativeSide(getMachineFacing(), facing);

            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.sidedIngredientHandlers[relativeSide.ordinal()]);
        }

        if (capability == CapabilityEnergy.ENERGY && this.storage != null)
        {
            if (facing == null || !this.hasSidedEnergyHandler()) return CapabilityEnergy.ENERGY.cast(this.storage);

            if (this.sidedIngredientHandlers == null) return null;

            EnumFacing relativeSide = getRelativeSide(getMachineFacing(), facing);
            SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[relativeSide.ordinal()];

            if (sidedHandler == null || !sidedHandler.hasEnergyHandler()) return null;

            return CapabilityEnergy.ENERGY.cast(sidedHandler);
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.fluidHandler != null)
        {
            if (facing == null || this.sidedIngredientHandlers == null) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.fluidHandler);

            EnumFacing relativeSide = getRelativeSide(getMachineFacing(), facing);
            SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[relativeSide.ordinal()];

            if (sidedHandler == null) return null;

            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(sidedHandler);
        }

        return super.getCapability(capability, facing);
    }

    @Nullable
    public SoundEvent getActiveSound() { return null; }

    public float getActiveSoundVolume() { return 0.5F; }

    public EnumFacing getMachineFacing()
    {
        if (world != null)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockMachine) return state.getValue(BlockMachine.FACING);
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getWorldSide(EnumFacing machineFacing, EnumFacing relativeSide)
    {
        EnumFacing[] rotationMap = switch (machineFacing)
        {
            case NORTH -> new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
            case SOUTH -> new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            case WEST -> new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH};
            case EAST -> new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH};
            default -> new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
        };

        return switch (relativeSide)
        {
            case NORTH -> rotationMap[0];
            case SOUTH -> rotationMap[1];
            case EAST -> rotationMap[2];
            case WEST -> rotationMap[3];
            default -> relativeSide;
        };
    }

    public EnumFacing getRelativeSide(EnumFacing machineFacing, EnumFacing worldSide)
    {
        for (EnumFacing relativeSide : EnumFacing.values()) if (getWorldSide(machineFacing, relativeSide) == worldSide) return relativeSide;
        return worldSide;
    }

    public int getFluidStored()
    {
        CustomFluidTank experienceTank = this.getExperienceTank();
        return experienceTank != null ? experienceTank.getFluidAmount() : 0;
    }

    public void setFluidStored(Fluid fluid, int fluidStored)
    {
        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null) experienceTank.setFluidStored(fluid, fluidStored);
    }

    public int getEnergyStored() { return this.storage != null ? this.storage.getEnergyStored() : 0; }

    public void setEnergyStored(int energyStored) { if (this.storage != null) this.storage.setEnergyStored(energyStored); }

    public int getMaxEnergyStored() { return this.storage != null ? this.storage.getMaxEnergyStored() : 0; }

    public void setMaxEnergyStored(int maxEnergyStored) { if (this.storage != null) this.storage.setMaxEnergyStored(maxEnergyStored); }

    public int getMaxReceive() { return this.storage != null ? this.storage.getMaxReceive() : 0; }

    public void setMaxReceive(int maxReceive) { if (this.storage != null) this.storage.setMaxReceive(maxReceive); }

    public int getMaxExtract() { return this.storage != null ? this.storage.getMaxExtract() : 0; }

    public void setMaxExtract(int maxExtract) { if (this.storage != null) this.storage.setMaxExtract(maxExtract); }

    public int getEnergyUsage() { return this.energyUsage; }

    public int getEnergyGeneration() { return this.energyGeneration; }

    public int getRedstoneControlButtonState() { return this.redstoneControlButtonState; }

    public void setRedstoneControlButtonState(int redstoneControlButtonState)
    {
        this.redstoneControlButtonState = redstoneControlButtonState;

        if (this.world != null && this.world.isRemote) NetworkHandler.sendVariableUpdate(0, this.redstoneControlButtonState, this.pos);

        this.markDirty();
    }

    public boolean hasCustomName() { return this.customName != null && !this.customName.isEmpty(); }

    public void setCustomName(String customName)
    {
        this.customName = customName;
        this.markDirty();
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity packet)
    {
        NBTTagCompound tag = packet.getNbtCompound();
        this.readFromNBT(tag);
        if (tag.hasKey("ProgressDirection", 3)) this.progressDirection = tag.getInteger("ProgressDirection");
        this.applyClientProgressSync();
    }

    @Override
    public void handleUpdateTag(@NotNull NBTTagCompound tag)
    {
        this.readFromNBT(tag);
        if (tag.hasKey("ProgressDirection", 3)) this.progressDirection = tag.getInteger("ProgressDirection");
        this.clientRenderCookTime = this.cookTime;
        this.clientPreviousRenderCookTime = this.cookTime;
        this.clientProgressInitialized = true;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = this.writeToNBT(new NBTTagCompound());
        tag.setInteger("ProgressDirection", this.progressDirection);
        return new SPacketUpdateTileEntity(this.getPos(), -1, tag);
    }

    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = this.writeToNBT(new NBTTagCompound());
        tag.setInteger("ProgressDirection", this.progressDirection);
        return tag;
    }

    private void applyClientProgressSync()
    {
        if (this.world == null || !this.world.isRemote) return;
        if (!this.clientProgressInitialized || this.cookTime == 0 || Math.abs(this.clientRenderCookTime - this.cookTime) > 4.0F)
        {
            this.clientRenderCookTime = this.cookTime;
            this.clientPreviousRenderCookTime = this.cookTime;
            this.clientProgressInitialized = true;
        }
    }

    public @NotNull NBTTagCompound createStatesTag()
    {
        NBTTagCompound statesTag = new NBTTagCompound();
        statesTag.setInteger("RedstoneControlButtonState", this.redstoneControlButtonState);

        if (this.sidedIngredientHandlers != null)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[facing.ordinal()];
                sidedHandler.writeToNBT(statesTag);
            }
        }

        return statesTag;
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        if (this.customItemStackHandler != null) tag.setTag("Inventory", this.customItemStackHandler.serializeNBT());
        if (this.storage != null) this.storage.writeToNBT(tag);
        if (this.hasCustomName()) tag.setString("CustomName", this.customName);

        if (this.fluidTanks.length > 0)
        {
            NBTTagList tanksTag = new NBTTagList();

            for (CustomFluidTank fluidTank : this.fluidTanks)
            {
                NBTTagCompound tankTag = new NBTTagCompound();
                fluidTank.writeToNBT(tankTag);
                tanksTag.appendTag(tankTag);
            }

            tag.setTag("FluidTanks", tanksTag);
        }

        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setTag("States", this.createStatesTag());

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Inventory", 10) && this.customItemStackHandler != null) this.customItemStackHandler.deserializeNBT(tag.getCompoundTag("Inventory"));

        if (this.storage != null) this.storage.readFromNBT(tag);

        this.customName = tag.hasKey("CustomName", 8) ? tag.getString("CustomName") : null;

        if (tag.hasKey("FluidTanks", 9))
        {
            NBTTagList tanksTag = tag.getTagList("FluidTanks", 10);
            int count = Math.min(tanksTag.tagCount(), this.fluidTanks.length);

            for (int i = 0; i < count; i++) this.fluidTanks[i].readFromNBT(tanksTag.getCompoundTagAt(i));
        }

        this.cookTime = tag.hasKey("CookTime", 3) ? tag.getInteger("CookTime") : 0;
        this.totalCookTime = tag.hasKey("TotalCookTime", 3) ? tag.getInteger("TotalCookTime") : 0;

        if (tag.hasKey("States", 10))
        {
            NBTTagCompound statesTag = tag.getCompoundTag("States");
            this.redstoneControlButtonState = statesTag.getInteger("RedstoneControlButtonState");

            if (this.sidedIngredientHandlers != null)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[facing.ordinal()];
                    if (sidedHandler != null && statesTag.hasKey(facing.toString() + "SlotStates")) sidedHandler.readFromNBT(statesTag);
                }
            }
        }
    }

    public boolean canOperateBasedOnRedstone(boolean hasRedstoneSignal)
    {
        return switch (this.redstoneControlButtonState)
        {
            case 0 -> true;
            case 1 -> !hasRedstoneSignal;
            case 2 -> hasRedstoneSignal;
            default -> false;
        };
    }

    public void giveExperienceToPlayer(EntityPlayer entityPlayer)
    {
        if (entityPlayer == null || entityPlayer.world.isRemote) return;

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank == null || experienceTank.getFluidAmount() <= 0) return;
        if (ModConfig.MACHINES.mbToXpRatio <= 0) return;

        int mbPerXp = Math.max(1, 1000 / ModConfig.MACHINES.mbToXpRatio);
        int totalExperience = experienceTank.getFluidAmount() / mbPerXp;

        if (totalExperience <= 0) return;

        int remainingExperience = totalExperience;

        while (remainingExperience > 0)
        {
            int xpSplit = EntityXPOrb.getXPSplit(remainingExperience);
            remainingExperience -= xpSplit;
            entityPlayer.world.spawnEntity(new EntityXPOrb(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5D, entityPlayer.posZ + 0.5D, xpSplit));
        }

        experienceTank.drainInternal(totalExperience * mbPerXp, true);
        this.markDirty();
    }

    public void setEnergyStoredFromItem(ItemStack stack)
    {
        if (this.storage == null || stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (itemEnergy == null) return;

        int energyToExtract = this.storage.receiveEnergy(Integer.MAX_VALUE, true);
        int extractedEnergy = itemEnergy.extractEnergy(energyToExtract, false);
        this.storage.receiveEnergy(extractedEnergy, false);
    }

    public void chargeItem(ItemStack stack)
    {
        if (this.storage == null || stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (itemEnergy == null) return;

        int energyToReceive = this.storage.extractEnergy(Integer.MAX_VALUE, true);
        int receivedEnergy = itemEnergy.receiveEnergy(energyToReceive, false);
        this.storage.extractEnergy(receivedEnergy, false);
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this) return false;
        else return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public int getCookTime() { return this.cookTime; }

    public int getTotalCookTime() { return this.totalCookTime; }

    public float getRenderCookTime(float partialTicks)
    {
        if (this.world == null || !this.world.isRemote) return this.cookTime;
        if (!this.clientProgressInitialized) return this.cookTime;
        return this.clientPreviousRenderCookTime + (this.clientRenderCookTime - this.clientPreviousRenderCookTime) * partialTicks;
    }

    public float getRenderProgress(float partialTicks)
    {
        if (this.totalCookTime <= 0) return 0.0F;
        return MathHelper.clamp(this.getRenderCookTime(partialTicks) / this.totalCookTime, 0.0F, 1.0F);
    }

    protected void updateClientProgress()
    {
        if (this.world == null || !this.world.isRemote) return;

        if (!this.clientProgressInitialized)
        {
            this.clientRenderCookTime = this.cookTime;
            this.clientPreviousRenderCookTime = this.cookTime;
            this.clientProgressInitialized = true;
        }

        this.clientPreviousRenderCookTime = this.clientRenderCookTime;

        if (this.totalCookTime <= 0)
        {
            this.clientRenderCookTime = 0.0F;
            return;
        }

        this.clientRenderCookTime += this.progressDirection;
        this.clientRenderCookTime = MathHelper.clamp(this.clientRenderCookTime, 0.0F, this.totalCookTime);
    }

    protected void updateProgressSync(int direction, boolean force)
    {
        if (this.world == null || this.world.isRemote) return;

        boolean directionChanged = this.progressDirection != direction;

        this.progressDirection = direction;

        boolean hasProgress = this.cookTime > 0 || this.progressDirection != 0;

        if (directionChanged || force || (hasProgress && this.world.getTotalWorldTime() % PROGRESS_SYNC_INTERVAL == 0))
        {
            IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 2);
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.cookTime = value;
                break;
            case 1:
                this.totalCookTime = value;
                break;
            case 2:
                this.storage.setEnergyStored(value);
                break;
            case 3:
                this.storage.setMaxEnergyStored(value);
                break;
            case 4:
                this.storage.setMaxReceive(value);
                break;
            case 5:
                this.storage.setMaxExtract(value);
                break;
            case 6:
                this.energyUsage = value;
                break;
            case 7:
                this.energyGeneration = value;
                break;
            case 8:
                CustomFluidTank experienceTank = this.getExperienceTank();
                if (experienceTank != null) experienceTank.setFluidStored(getConfiguredXpFluid().getFluid(), value);
                break;
        }

        this.markDirty();
    }
}