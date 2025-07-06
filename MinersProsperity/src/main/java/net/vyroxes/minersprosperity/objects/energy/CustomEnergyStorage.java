package net.vyroxes.minersprosperity.objects.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorage implements IEnergyStorage
{
    protected int energyStored;
    protected int maxEnergyStored;
    protected int maxReceive;
    protected int maxExtract;
    protected int baseMaxEnergyStored;
    protected int baseMaxReceive;
    protected int baseMaxExtract;
    protected int maxEnergyStoredModifier;
    protected int maxReceiveModifier;
    protected int maxExtractModifier;


    public CustomEnergyStorage(int maxEnergyStored, int maxReceive, int maxExtract, int energyStored)
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
        if (!canReceive()) return 0;

        int energyReceived = Math.min(maxEnergyStored - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate) energyStored += energyReceived;

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract()) return 0;

        int energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate) energyStored -= energyExtracted;

        return energyExtracted;
    }

    public int addEnergy(int energy, boolean simulate)
    {
        if (!simulate)
        {
            if (energy + this.energyStored > this.maxEnergyStored) this.energyStored = this.maxEnergyStored;
            else this.energyStored += energy;
        }

        return Math.max(this.energyStored - energy, 0);
    }

    public int useEnergy(int energy, boolean simulate)
    {
        if (!simulate)
        {
            if (this.energyStored >= energy) this.energyStored -= energy;
            else this.energyStored = 0;
        }

        return Math.max(this.energyStored - energy, 0);
    }

    public int setEnergyUpgraded(int count, boolean simulate)
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

    public void setEnergyStored(int energyStored)
    {
        this.energyStored = Math.min(energyStored, this.maxEnergyStored);
    }

    @Override
    public int getEnergyStored()
    {
        return this.energyStored;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return this.maxEnergyStored;
    }

    public void setMaxEnergyStored(int maxEnergyStored)
    {
        this.maxEnergyStored = maxEnergyStored;
    }

    public int getMaxReceive()
    {
        return this.maxReceive;
    }

    public void setMaxReceive(int maxReceive)
    {
        this.maxReceive = maxReceive;
    }

    public int getMaxExtract()
    {
        return this.maxExtract;
    }

    public void setMaxExtract(int maxExtract)
    {
        this.maxExtract = maxExtract;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.energyStored = compound.getInteger("Energy");
        this.maxEnergyStored = compound.getInteger("MaxEnergy");
        this.maxReceive = compound.getInteger("MaxReceive");
        this.maxExtract = compound.getInteger("MaxExtract");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Energy", this.energyStored);
        compound.setInteger("MaxEnergy", this.maxEnergyStored);
        compound.setInteger("MaxReceive", this.maxReceive);
        compound.setInteger("MaxExtract", this.maxExtract);
    }
}