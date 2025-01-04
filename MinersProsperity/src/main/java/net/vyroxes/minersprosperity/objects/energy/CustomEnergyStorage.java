package net.vyroxes.minersprosperity.objects.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;

public class CustomEnergyStorage implements IEnergyStorage
{
    protected long energyStored;
    protected long maxEnergyStored;
    protected long maxReceive;
    protected long maxExtract;

    public CustomEnergyStorage(long maxEnergyStored, long maxReceive, long maxExtract, long energyStored)
    {
        this.maxEnergyStored = maxEnergyStored;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = Math.max(0 , Math.min(maxEnergyStored, energyStored));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        long energyReceived = Math.min(maxEnergyStored - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energyStored += energyReceived;
        return (int) energyReceived;
    }

    public long receiveEnergy(long maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        long energyReceived = Math.min(maxEnergyStored - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energyStored += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        long energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energyStored -= energyExtracted;
        return (int) energyExtracted;
    }

    public long extractEnergy(long maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        long energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energyStored -= energyExtracted;
        return energyExtracted;
    }

    public long useEnergy(long energyUsage, boolean simulate)
    {
        long energyMissing = 0;
        if (this.energyStored < energyUsage) energyMissing = energyUsage - this.energyStored;
        if (!simulate && energyUsage != 0 && energyMissing == 0)
            this.energyStored -= energyUsage;
        return energyMissing;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

    public void setEnergyStored(long energyStored)
    {
        this.energyStored = energyStored;
    }

    @Override
    public int getEnergyStored()
    {
        return (int) this.energyStored;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return (int) this.maxEnergyStored;
    }

    public void setMaxEnergyStored(long maxEnergyStored)
    {
        this.maxEnergyStored = maxEnergyStored;
    }

    public long getMaxReceive()
    {
        return this.maxReceive;
    }

    public void setMaxReceive(long maxReceive)
    {
        this.maxReceive = maxReceive;
    }

    public long getMaxExtract()
    {
        return this.maxExtract;
    }

    public void setMaxExtract(long maxExtract)
    {
        this.maxExtract = maxExtract;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.energyStored = compound.getLong("Energy");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setLong("Energy", this.energyStored);
    }
}