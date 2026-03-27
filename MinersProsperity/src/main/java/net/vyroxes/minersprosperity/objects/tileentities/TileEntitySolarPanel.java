package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.vyroxes.minersprosperity.objects.blocks.machines.MachineSolarPanel;
import net.vyroxes.minersprosperity.objects.enums.ESolarPanelTier;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;

public class TileEntitySolarPanel extends TileEntityMachine
{
    private ESolarPanelTier tier;

    public TileEntitySolarPanel()
    {
        this(ESolarPanelTier.BASIC);
    }

    public TileEntitySolarPanel(ESolarPanelTier tier)
    {
        super(new Builder()
                .setItemEnergy(1)
                .setEnergy(tier.capacity, 0, tier.maxExtract, 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder()
                        .setEnergySlots(1)));

        this.tier = tier;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation(tier.translationKey);
    }

    @Override
    public void onLoad() {
        if (!world.isRemote)
        {
            Block block = getBlockType();
            if (block instanceof MachineSolarPanel)
            {
                this.tier = ((MachineSolarPanel) block).getTier();
            }
        }
    }

    public ESolarPanelTier getTier()
    {
        return this.tier;
    }

    @Override
    public void chargeItem(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

        if (itemEnergy == null) return;

        if (tier == ESolarPanelTier.CREATIVE)
        {
            int energyToReceive = this.storage.extractEnergy(Integer.MAX_VALUE, true);
            itemEnergy.receiveEnergy(energyToReceive, false);
        }
        else
        {
            int energyToReceive = this.storage.extractEnergy(Integer.MAX_VALUE, true);
            int receivedEnergy = itemEnergy.receiveEnergy(energyToReceive, false);
            this.storage.extractEnergy(receivedEnergy, false);
        }
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
                    if (tier == ESolarPanelTier.CREATIVE)
                    {
                        this.storage.addEnergy(energyAccepted, false);
                    }
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
            if (this.storage.canExtract())
                transferEnergy();

            boolean isDay = world.isDaytime();
            boolean isRaining = world.isRaining();
            float lightFactor = calculateLightObstructionFactor();

            ItemStack energy = customItemStackHandler.getStackInSlot(0);

            if (!energy.isEmpty() && this.storage.getEnergyStored() > 0)
            {
                chargeItem(energy);
            }

            if (lightFactor > 0 && isDay)
            {
                int baseGeneration;

                if (isRaining)
                {
                    baseGeneration = tier.generationRain;
                }
                else
                {
                    baseGeneration = tier.generationClear;
                }

                this.energyGeneration = Math.round(baseGeneration * lightFactor);
                if (this.storage.getEnergyStored() != this.storage.getMaxEnergyStored())
                {
                    this.storage.addEnergy(this.energyGeneration, false);
                }
                stateChanged = true;
            }
            else
            {
                this.energyGeneration = 0;
                stateChanged = true;
            }

            if (wasPowered != (lightFactor > 0 && isDay))
            {
                Machine.setStatePowered(lightFactor > 0 && isDay, world, pos);
            }
        }

        if (stateChanged)
        {
            this.markDirty();
        }
    }

    private float calculateLightObstructionFactor()
    {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        int maxHeight = world.getHeight();

        float reductionFactor = 1.0f;
        for (int y = checkPos.getY(); y < maxHeight; y++)
        {
            checkPos.setPos(pos.getX(), y, pos.getZ());

            IBlockState state = world.getBlockState(checkPos);
            if (state.getBlock().isAir(state, world, checkPos)) continue;

            if (state.getLightOpacity(world, checkPos) >= 15)
            {
                return 0.0f;
            }

            reductionFactor *= 0.8f;
        }

        return reductionFactor;
    }
}