package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.machine.recipe.LiquidFuelGeneratorRecipe;
import org.jetbrains.annotations.NotNull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;

import java.util.Objects;

public class TileEntityLiquidFuelGenerator extends TileEntityMachine
{
    private int fuelGeneration;

    private final IFluidHandler containerFluidHandler = new IFluidHandler()
    {
        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            CustomFluidTank tank = getFluidTank(0);
            return new IFluidTankProperties[]{new FluidTankProperties(tank.getFluid(), tank.getCapacity(), true, true)};
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) { return Objects.requireNonNull(getFluidTank(0)).fill(resource, doFill); }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            if (resource == null || resource.amount <= 0 || !resource.isFluidEqual(Objects.requireNonNull(getFluidTank(0)).getFluid())) return null;
            return Objects.requireNonNull(getFluidTank(0)).drainInternal(resource.amount, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) { return Objects.requireNonNull(getFluidTank(0)).drainInternal(maxDrain, doDrain); }
    };

    public IFluidHandler getContainerFluidHandler() { return this.containerFluidHandler; }


    public TileEntityLiquidFuelGenerator()
    {
        super(new Builder()
                .setItemInput(1)
                .setItemEnergy(1)
                .setItemOutput(1)
                .addFluidTank(null, 16000, 0, false, true, SlotState.SlotType.INPUT, SlotState.SlotMode.INPUT)
                .setItemUpgrade(3)
                .setEnergy(20000, 0, 2000, 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.OUTPUT)));

        this.fluidTanks[0] = new CustomFluidTank(null, 0, 16000, false, true)
        {
            @Override
            public boolean canFillFluidType(FluidStack fluid)
            {
                return super.canFillFluidType(fluid) && LiquidFuelGeneratorRecipe.getInstance().findRecipe(fluid) != null;
            }

            @Override
            protected void onContentsChanged()
            {
                super.onContentsChanged();
                TileEntityLiquidFuelGenerator.this.markDirty();
            }
        };
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.liquid_fuel_generator");
    }

    @Override
    public SoundEvent getActiveSound() { return ModSounds.ALLOY_FURNACE_ACTIVE; }

    @Override
    public float getActiveSoundVolume() { return 0.05F; }

    @Override
    public MachineUpgradeType getUpgradeType(int index)
    {
        return switch (index)
        {
            case 0 -> MachineUpgradeType.POWER;
            case 1 -> MachineUpgradeType.EFFICIENCY;
            case 2 -> MachineUpgradeType.ENERGY;
            default -> null;
        };
    }

    @Override
    public void update()
    {
        if (this.world == null) return;

        if (this.world.isRemote)
        {
            MinersProsperity.proxy.updateMachineSound(this);
            this.updateClientProgress();
            return;
        }

        int previousEnergy = this.storage.getEnergyStored();
        this.updateEnergyStorageUpgrade();
        this.runAutomaticIO();
        boolean containerChanged = this.processFluidContainer();
        this.transferEnergy();

        ItemStack battery = this.getEnergyStack(0);
        if (!battery.isEmpty()) this.chargeItem(battery);

        int powerUpgrades = this.getUpgradeCount(MachineUpgradeType.POWER);
        int power = powerUpgrades == 8 ? 10 : powerUpgrades + 1;

        boolean started = false;
        boolean completed = false;
        this.energyGeneration = 0;

        if (this.canMachineOperate())
        {
            if (this.totalCookTime <= 0)
            {
                LiquidFuelGeneratorRecipe.Recipe recipe = LiquidFuelGeneratorRecipe.getInstance().findRecipe(Objects.requireNonNull(this.getFluidTank(0)).getFluid());
                if (recipe != null && Objects.requireNonNull(this.getFluidTank(0)).getFluidAmount() >= recipe.input.amount)
                {
                    int efficiency = this.getUpgradeCount(MachineUpgradeType.EFFICIENCY);
                    int duration = (int) Math.min(Integer.MAX_VALUE, Math.round(recipe.totalCookTime * (1.0 + 0.5 * efficiency / 8.0)));
                    int firstGeneration = recipe.energyGeneration * Math.min(power, duration);
                    if (this.storage.getMaxEnergyStored() - this.storage.getEnergyStored() >= firstGeneration)
                    {
                        Objects.requireNonNull(this.getFluidTank(0)).drainInternal(recipe.input.amount, true);
                        this.cookTime = 0;
                        this.totalCookTime = duration;
                        this.fuelGeneration = recipe.energyGeneration;
                        started = true;
                    }
                }
            }

            int fuelTicks = Math.min(power, this.totalCookTime - this.cookTime);
            int generation = this.fuelGeneration * fuelTicks;

            if (fuelTicks > 0 && this.storage.getMaxEnergyStored() - this.storage.getEnergyStored() >= generation)
            {
                this.storage.addEnergy(generation, false);
                this.energyGeneration = generation;
                this.cookTime += fuelTicks;

                if (this.cookTime >= this.totalCookTime)
                {
                    this.cookTime = 0;
                    this.totalCookTime = 0;
                    completed = true;
                }
            }
        }

        boolean active = this.energyGeneration > 0;
        BlockMachine.MachineState machineState = active ? BlockMachine.MachineState.ACTIVE : this.storage.getEnergyStored() > 0 ? BlockMachine.MachineState.POWERED : BlockMachine.MachineState.OFF;

        if (BlockMachine.getMachineState(this.world, this.pos) != machineState) BlockMachine.setMachineState(machineState, this.world, this.pos);

        this.updateProgressSync(active && !completed ? power : 0, started || completed);
        if (active || started || containerChanged || previousEnergy != this.storage.getEnergyStored()) this.markDirty();
    }

    public static boolean isFuelContainer(ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        return LiquidFuelGeneratorRecipe.getInstance().findRecipe(FluidUtil.getFluidContained(stack)) != null;
    }

    public static boolean isFluidContainer(ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        ItemStack single = stack.copy();
        single.setCount(1);
        if (FluidUtil.getFluidHandler(single) == null) return false;
        FluidStack fluid = FluidUtil.getFluidContained(single);
        return fluid == null || LiquidFuelGeneratorRecipe.getInstance().findRecipe(fluid) != null;
    }

    private boolean processFluidContainer()
    {
        ItemStack input = this.getInputStack(0);
        if (input.isEmpty()) return false;

        FluidActionResult preview = FluidUtil.tryFillContainer(input, this.containerFluidHandler, Integer.MAX_VALUE, null, false);
        boolean filling = preview.isSuccess();
        if (!filling) preview = FluidUtil.tryEmptyContainer(input, this.containerFluidHandler, Integer.MAX_VALUE, null, false);
        if (!preview.isSuccess()) return false;

        ItemStack result = preview.getResult();
        ItemStack output = this.getOutputStack(0);
        if (!result.isEmpty() && !output.isEmpty()
                && (!ItemStack.areItemsEqual(output, result) || !ItemStack.areItemStackTagsEqual(output, result)
                || output.getCount() + result.getCount() > Math.min(output.getMaxStackSize(), this.customItemStackHandler.getSlotLimit(this.getOutputSlotId(0))))) return false;

        FluidActionResult transferred = filling
                ? FluidUtil.tryFillContainer(input, this.containerFluidHandler, Integer.MAX_VALUE, null, true)
                : FluidUtil.tryEmptyContainer(input, this.containerFluidHandler, Integer.MAX_VALUE, null, true);
        if (!transferred.isSuccess()) return false;

        input.shrink(1);
        if (input.isEmpty()) this.clearInputStack(0);
        if (output.isEmpty()) this.setOutputStack(0, transferred.getResult());
        else output.grow(transferred.getResult().getCount());
        return true;
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("FuelGeneration", this.fuelGeneration);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.fuelGeneration = Math.max(0, Math.min(Integer.MAX_VALUE / 10, tag.getInteger("FuelGeneration")));
        if (this.fuelGeneration == 0) this.totalCookTime = this.cookTime = 0;
    }

    private void transferEnergy()
    {
        int remaining = this.storage.getMaxExtract();

        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if (remaining <= 0) break;

            SidedIngredientHandler handler = this.getSidedHandler(this.getRelativeSide(this.getMachineFacing(), facing));
            if (handler == null || !handler.canExtract()) continue;

            TileEntity neighbor = this.world.getTileEntity(this.pos.offset(facing));
            if (neighbor == null || !neighbor.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) continue;

            IEnergyStorage target = neighbor.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
            if (target == null) continue;

            int available = handler.extractEnergy(remaining, true);
            int accepted = target.receiveEnergy(available, false);
            handler.extractEnergy(accepted, false);
            remaining -= accepted;
        }
    }
}