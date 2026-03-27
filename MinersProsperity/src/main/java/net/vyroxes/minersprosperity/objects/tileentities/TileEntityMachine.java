package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
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
import net.vyroxes.minersprosperity.init.FluidInit;
import net.vyroxes.minersprosperity.objects.blocks.machines.Machine;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.containers.ContainerElectricFurnace;
import net.vyroxes.minersprosperity.objects.containers.ContainerMetalPress;
import net.vyroxes.minersprosperity.objects.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.objects.fluids.CustomFluidTank;
import net.vyroxes.minersprosperity.util.handlers.CustomItemStackHandler;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;
import org.jetbrains.annotations.NotNull;

public abstract class TileEntityMachine extends TileEntity implements ITickable
{
    protected final CustomEnergyStorage storage;
    protected final CustomFluidTank tank;
    protected final SidedIngredientHandler[] sidedIngredientHandlers;
    protected final CustomItemStackHandler customItemStackHandler;
    protected boolean fluidXpInitialized = false;
    protected int cookTime;
    protected int totalCookTime;
    protected String customName;
    protected int redstoneControlButtonState;
    protected int slotEditedId;
    protected int energyUsage;
    protected int energyGeneration;

    protected TileEntityMachine(Builder builder)
    {
        this.storage = builder.energyCapacity > 0
                ? new CustomEnergyStorage(builder.energyCapacity, builder.maxReceive, builder.maxExtract, builder.storedEnergy)
                : null;

        this.tank = builder.tankCapacity > 0
                ? new CustomFluidTank(builder.fluid, builder.storedFluid, builder.tankCapacity, builder.canDrain, builder.canFill)
                : null;

        this.customItemStackHandler = builder.inputs + builder.energySlots + builder.outputs + builder.upgradeSlots > 0
                ? new CustomItemStackHandler(this, builder.inputs, builder.energySlots, builder.outputs, builder.upgradeSlots)
                : null;

        this.sidedIngredientHandlers = builder.sidedIngredientHandlerBuilder != null
                ? builder.sidedIngredientHandlerBuilder.build(this)
                : null;
    }

    public static class Builder
    {
        private int energyCapacity = 0;
        private int maxReceive = 0;
        private int maxExtract = 0;
        private int storedEnergy = 0;

        private Fluid fluid = null;
        private int tankCapacity = 0;
        private int storedFluid = 0;
        private boolean canDrain = false;
        private boolean canFill = false;

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

        public Builder setFluid(Fluid fluid, int capacity, int stored, boolean canDrain, boolean canFill)
        {
            this.fluid = fluid;
            this.tankCapacity = capacity;
            this.storedFluid = stored;
            this.canDrain = canDrain;
            this.canFill = canFill;
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

        public Builder setSidedIngredientHandler(SidedIngredientHandler.Builder builder)
        {
            this.sidedIngredientHandlerBuilder = builder;
            return this;
        }

        public TileEntityMachine build(TileEntityMachine machine)
        {
            return machine;
        }
    }

    public static FluidStack getConfiguredXpFluid()
    {
        String[] parts = ModConfig.MACHINES.experienceFluid.split(":");
        if (parts.length != 2) return new FluidStack(FluidInit.LIQUID_EXPERIENCE, 0);

        Fluid fluid = FluidRegistry.getFluid(parts[1]);
        if (fluid == null) fluid = FluidInit.LIQUID_EXPERIENCE;

        return new FluidStack(fluid, 0);
    }

    public int[] getInputSlotIds()
    {
        int inputs = this.customItemStackHandler.getInputSlots();
        int[] result = new int[inputs];

        for (int i = 0; i < inputs; i++)
        {
            result[i] = i;
        }

        return result;
    }

    public int getEnergySlotId()
    {
        return this.customItemStackHandler.getInputSlots();
    }

    public int[] getOutputSlotIds()
    {
        int inputs = this.customItemStackHandler.getInputSlots();
        int energy = this.customItemStackHandler.getEnergySlots();
        int outputs = this.customItemStackHandler.getOutputSlots();

        int start = inputs + energy;
        int[] result = new int[outputs];

        for (int i = 0; i < outputs; i++)
        {
            result[i] = start + i;
        }

        return result;
    }

    public int getLiquidExperienceTankId()
    {
        return this.customItemStackHandler.getInputSlots() + this.customItemStackHandler.getEnergySlots() + this.customItemStackHandler.getOutputSlots();
    }

    public CustomEnergyStorage getEnergyStorage()
    {
        return this.storage;
    }

    public CustomFluidTank getFluidTank()
    {
        return this.tank;
    }

    public CustomItemStackHandler getCustomItemStackHandler()
    {
        return this.customItemStackHandler;
    }

    public IFluidHandler[] getSidedIngredientHandlersFluid()
    {
        return this.sidedIngredientHandlers;
    }

    public IItemHandler[] getSidedIngredientHandlers()
    {
        return this.sidedIngredientHandlers;
    }

    public IFluidHandler getSidedIngredientHandlerFluid(EnumFacing side)
    {
        return this.sidedIngredientHandlers[side.ordinal()];
    }

    public IItemHandler getSidedIngredientHandler(EnumFacing side)
    {
        return this.sidedIngredientHandlers[side.ordinal()];
    }

    public boolean isSlotEnergy(int slot)
    {
        if (this.sidedIngredientHandlers != null)
        {
            return this.sidedIngredientHandlers[0].isSlotEnergy(slot);
        }

        return false;
    }

    public boolean isSlotOutput(int slot)
    {
        if (this.sidedIngredientHandlers != null)
        {
            return this.sidedIngredientHandlers[0].isSlotOutput(slot);
        }

        return false;
    }

    public int getSlotEditedId()
    {
        return this.slotEditedId;
    }

    public void setSlotEditedId(int slotEditedId)
    {
        this.slotEditedId = slotEditedId;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing)
    {
        if (customItemStackHandler != null)
        {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            {
                return true;
            }
        }

        if (this.storage != null)
        {
            if (capability == CapabilityEnergy.ENERGY)
            {
                return true;
            }
        }

        if (this.tank != null)
        {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            {
                return true;
            }
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing)
    {
        if (this.customItemStackHandler != null)
        {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            {
                if (facing == null)
                {
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.customItemStackHandler);
                }

                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getSidedIngredientHandler(getRelativeSide(getMachineFacing(), facing)));
            }
        }

        if (this.storage != null)
        {
            if (capability == CapabilityEnergy.ENERGY)
            {
                return CapabilityEnergy.ENERGY.cast(this.storage);
            }
        }

        if (this.tank != null)
        {
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            {
                if (facing == null)
                {
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
                }

                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getSidedIngredientHandlerFluid(getRelativeSide(getMachineFacing(), facing)));
            }
        }

        return super.getCapability(capability, facing);
    }

    public EnumFacing getMachineFacing()
    {
        if (world != null)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof Machine)
            {
                return state.getValue(Machine.FACING);
            }
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getRelativeSide(EnumFacing machineFacing, EnumFacing pipeDirection)
    {
        EnumFacing[] rotationMap = switch (machineFacing)
        {
            case NORTH -> new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
            case SOUTH -> new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            case WEST -> new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH};
            case EAST -> new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH};
            default -> new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
        };

        return switch (pipeDirection)
        {
            case NORTH -> rotationMap[0];
            case SOUTH -> rotationMap[1];
            case EAST -> rotationMap[2];
            case WEST -> rotationMap[3];
            default -> pipeDirection;
        };
    }

    public int getFluidStored()
    {
        return this.tank.getFluidAmount();
    }

    public void setFluidStored(Fluid fluid, int fluidStored)
    {
        this.tank.setFluidStored(fluid, fluidStored);
    }

    public int getEnergyStored()
    {
        return this.storage.getEnergyStored();
    }

    public void setEnergyStored(int energyStored)
    {
        this.storage.setEnergyStored(energyStored);
    }

    public int getMaxEnergyStored()
    {
        return this.storage.getMaxEnergyStored();
    }

    public void setMaxEnergyStored(int maxEnergyStored)
    {
        this.storage.setMaxEnergyStored(maxEnergyStored);
    }

    public int getMaxReceive()
    {
        return this.storage.getMaxReceive();
    }

    public void setMaxReceive(int maxReceive)
    {
        this.storage.setMaxReceive(maxReceive);
    }

    public int getMaxExtract()
    {
        return this.storage.getMaxExtract();
    }

    public void setMaxExtract(int maxExtract)
    {
        this.storage.setMaxExtract(maxExtract);
    }

    public int getEnergyUsage()
    {
        return this.energyUsage;
    }

    public int getEnergyGeneration()
    {
        return this.energyGeneration;
    }

    public int getRedstoneControlButtonState()
    {
        return this.redstoneControlButtonState;
    }

    public void setRedstoneControlButtonState(int redstoneControlButtonState)
    {
        this.redstoneControlButtonState = redstoneControlButtonState;

        if (this.world.isRemote)
        {
            NetworkHandler.sendVariableUpdate(0, this.redstoneControlButtonState, this.pos);
        }

        this.markDirty();
    }

    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;

        this.markDirty();
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(@NotNull NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.getPos(), -1, tag);
    }

    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        if (this.customItemStackHandler != null) tag.setTag("Inventory", this.customItemStackHandler.serializeNBT());

        if (this.storage != null) this.storage.writeToNBT(tag);

        if (this.hasCustomName()) tag.setString("CustomName", this.customName);

        return tag;
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
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Inventory", 10))
        {
            if (this.customItemStackHandler != null) this.customItemStackHandler.deserializeNBT(tag.getCompoundTag("Inventory"));
        }

        if (this.storage != null) this.storage.readFromNBT(tag);

        if (tag.hasKey("CustomName", 8))
        {
            this.setCustomName(tag.getString("CustomName"));
        }

        this.markDirty();
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
        if (!entityPlayer.world.isRemote && this.getFluidStored() >= 10)
        {
            if (entityPlayer.openContainer instanceof ContainerAlloyFurnace containerAlloyFurnace)
            {
                containerAlloyFurnace.giveExperienceToPlayer(entityPlayer);
            }
            else if (entityPlayer.openContainer instanceof ContainerMetalPress containerMetalPress)
            {
                containerMetalPress.giveExperienceToPlayer(entityPlayer);
            }
            else if (entityPlayer.openContainer instanceof ContainerElectricFurnace containerElectricFurnace)
            {
                containerElectricFurnace.giveExperienceToPlayer(entityPlayer);
            }
        }
    }

    public void setEnergyStoredFromItem(ItemStack stack)
    {
        if (stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

        if (itemEnergy == null) return;

        int energyToExtract = this.storage.receiveEnergy(Integer.MAX_VALUE, true);
        int extractedEnergy = itemEnergy.extractEnergy(energyToExtract, false);
        this.storage.receiveEnergy(extractedEnergy, false);
    }

    public void chargeItem(ItemStack stack)
    {
        if (stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

        if (itemEnergy == null) return;

        int energyToReceive = this.storage.extractEnergy(Integer.MAX_VALUE, true);
        int receivedEnergy = itemEnergy.receiveEnergy(energyToReceive, false);
        this.storage.extractEnergy(receivedEnergy, false);
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public int getCookTime()
    {
        return this.cookTime;
    }

    public int getTotalCookTime()
    {
        return this.totalCookTime;
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
                this.tank.setFluidStored(getConfiguredXpFluid().getFluid(), value);
        }

        this.markDirty();
    }
}