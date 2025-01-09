package net.vyroxes.minersprosperity.objects.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorage implements IEnergyStorage
{
    protected long energyStored;
    protected long maxEnergyStored;
    protected long maxReceive;
    protected long maxExtract;
    protected long baseMaxEnergyStored;
    protected long baseMaxReceive;
    protected long baseMaxExtract;
    protected long maxEnergyStoredModifier;
    protected long maxReceiveModifier;
    protected long maxExtractModifier;


    public CustomEnergyStorage(long maxEnergyStored, long maxReceive, long maxExtract, long energyStored)
    {
        this.maxEnergyStored = maxEnergyStored;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = Math.max(0 , Math.min(maxEnergyStored, energyStored));
        this.baseMaxEnergyStored = maxEnergyStored;
        this.baseMaxReceive = maxReceive;
        this.maxEnergyStoredModifier = (maxEnergyStored * 10 - maxEnergyStored)/8;
        this.maxReceiveModifier = (maxReceive * 10 - maxReceive)/8;
        this.maxExtractModifier = (maxExtract * 10 - maxExtract)/8;
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

    public long setEnergyUpgraded(int count, boolean simulate)
    {
        if (!simulate)
        {
            this.maxEnergyStored = this.maxEnergyStoredModifier * count + this.baseMaxEnergyStored;
            this.maxReceive = this.maxReceiveModifier * count + this.baseMaxReceive;
            this.maxExtract = this.maxExtractModifier * count + this.baseMaxExtract;

            if (this.energyStored > this.maxEnergyStored) this.energyStored = this.maxEnergyStored;
        }
        return this.maxEnergyStoredModifier * count + this.baseMaxEnergyStored;
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
        this.energyStored = Math.min(energyStored, this.maxEnergyStored);
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
        this.maxEnergyStored = compound.getLong("MaxEnergy");
        this.maxReceive = compound.getLong("MaxReceive");
        this.maxExtract = compound.getLong("MaxExtract");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setLong("Energy", this.energyStored);
        compound.setLong("MaxEnergy", this.maxEnergyStored);
        compound.setLong("MaxReceive", this.maxReceive);
        compound.setLong("MaxExtract", this.maxExtract);
    }
}