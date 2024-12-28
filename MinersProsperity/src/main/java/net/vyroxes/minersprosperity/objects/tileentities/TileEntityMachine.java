package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.blocks.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.util.handlers.CustomItemStackHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedItemStackHandler;

public abstract class TileEntityMachine extends TileEntity implements ITickable
{
    protected final CustomEnergyStorage storage;
    protected final SidedItemStackHandler[] sidedItemStackHandlers;
    protected final CustomItemStackHandler customItemStackHandler;

    public TileEntityMachine(int energyCapacity, int energyTransferRate, int inputs, int energy, int outputs)
    {
        this.storage = new CustomEnergyStorage(energyCapacity, energyTransferRate, 0, 0);
        this.customItemStackHandler = new CustomItemStackHandler(inputs, energy, outputs);
        this.sidedItemStackHandlers = new SidedItemStackHandler[EnumFacing.values().length];

        for (EnumFacing facing : EnumFacing.values())
        {
            this.sidedItemStackHandlers[facing.ordinal()] = new SidedItemStackHandler(this, customItemStackHandler, inputs, energy, outputs, facing);
        }
    }

    public IItemHandler getSidedItemHandler(EnumFacing side)
    {
        return this.sidedItemStackHandlers[side.ordinal()];
    }
}