package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.objects.blocks.machines.Machine;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;

public class TileEntityAdvancedSolarPanel extends TileEntityMachine
{
    public TileEntityAdvancedSolarPanel()
    {
        super(new Builder()
                .setItemEnergy(1)
                .setEnergy(80000, 0, 800, 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder()
                        .setEnergySlots(1)));
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.advanced_solar_panel");
    }

    private void transferEnergy()
    {
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            BlockPos targetPos = pos.offset(facing);
            TileEntity neighbor = world.getTileEntity(targetPos);

            if (neighbor != null && neighbor.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
            {
                IEnergyStorage neighborStorage = neighbor.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());

                if (neighborStorage != null)
                {
                    int energyExtracted = this.storage.extractEnergy(this.storage.getMaxExtract(), true);
                    int energyAccepted = neighborStorage.receiveEnergy(energyExtracted, false);
                    this.storage.extractEnergy(energyAccepted, false);
                }
            }
        }
    }

    @Override
    public void update()
    {
        boolean wasPowered = Machine.getStatePowered(this.world, this.pos);
        boolean stateChanged = false;

        if (!world.isRemote)
        {
            if (this.storage.canExtract()) transferEnergy();

            boolean isDay = world.isDaytime();
            boolean isRaining = world.isRaining();
            boolean isThunderstorm = world.isThundering();
            boolean hasBlockAbove = hasBlockAbove();

            ItemStack energy = customItemStackHandler.getStackInSlot(0);

            if (!energy.isEmpty() && this.storage.getEnergyStored() > 0)
            {
                chargeItem(energy);
            }

            if (!hasBlockAbove && isDay)
            {
                if (isThunderstorm)
                {
                    this.energyGeneration = 400;
                    if (this.storage.getEnergyStored() != this.storage.getMaxEnergyStored()) this.storage.addEnergy(this.energyGeneration, false);
                    stateChanged = true;
                }
                else if (isRaining)
                {
                    this.energyGeneration = 600;
                    if (this.storage.getEnergyStored() != this.storage.getMaxEnergyStored()) this.storage.addEnergy(this.energyGeneration, false);
                    stateChanged = true;
                }
                else
                {
                    this.energyGeneration = 800;
                    if (this.storage.getEnergyStored() != this.storage.getMaxEnergyStored()) this.storage.addEnergy(this.energyGeneration, false);
                    stateChanged = true;
                }
            }
            else
            {
                this.energyGeneration = 0;
                stateChanged = true;
            }

            if (wasPowered != (!hasBlockAbove && isDay))
            {
                Machine.setStatePowered(!hasBlockAbove && isDay, world, pos);
            }
        }

        if (stateChanged)
        {
            this.markDirty();
        }
    }

    private boolean hasBlockAbove()
    {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        int maxHeight = world.getHeight();

        for (int y = checkPos.getY(); y < maxHeight; y++)
        {
            checkPos.setPos(pos.getX(), y, pos.getZ());
            if (!world.isAirBlock(checkPos))
            {
                return true;
            }
        }

        return false;
    }
}

