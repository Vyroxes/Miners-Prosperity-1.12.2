package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;

public class TileEntitySolidFuelGenerator extends TileEntityMachine
{
    public TileEntitySolidFuelGenerator()
    {
        super(new Builder()
                .setItemInput(1)
                .setItemEnergy(1)
                .setItemUpgrade(3)
                .setEnergy(20000, 0, 2000, 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.OUTPUT)));
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.solid_fuel_generator");
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
        this.transferEnergy();

        ItemStack battery = this.getEnergyStack(0);
        if (!battery.isEmpty()) this.chargeItem(battery);

        int powerUpgrades = this.getUpgradeCount(MachineUpgradeType.POWER);
        int power = powerUpgrades == 8 ? 10 : powerUpgrades + 1;
        int baseGeneration = ModConfig.MACHINES.solidFuelGeneratorBaseGeneration;
        boolean started = false;
        boolean completed = false;
        this.energyGeneration = 0;

        if (this.canMachineOperate())
        {
            if (this.totalCookTime <= 0)
            {
                ItemStack fuel = this.getInputStack(0);
                int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
                int efficiency = this.getUpgradeCount(MachineUpgradeType.EFFICIENCY);
                int duration = (int) Math.min(Integer.MAX_VALUE, Math.round(burnTime * (1.0 + 0.5 * efficiency / 8.0)));
                int firstGeneration = baseGeneration * Math.min(power, duration);

                if (duration > 0 && this.storage.getMaxEnergyStored() - this.storage.getEnergyStored() >= firstGeneration)
                {
                    ItemStack container = fuel.getItem().getContainerItem(fuel);
                    if (container.isEmpty() || fuel.getCount() == 1)
                    {
                        fuel.shrink(1);
                        if (fuel.isEmpty()) this.setInputStack(0, container);
                        this.cookTime = 0;
                        this.totalCookTime = duration;
                        started = true;
                    }
                }
            }

            int fuelTicks = Math.min(power, this.totalCookTime - this.cookTime);
            int generation = baseGeneration * fuelTicks;

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
        if (active || started || previousEnergy != this.storage.getEnergyStored()) this.markDirty();
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