package net.vyroxes.minersprosperity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.block.machine.BlockMachineSolarPanel;
import net.vyroxes.minersprosperity.machine.SolarPanelTier;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;

public class TileEntitySolarPanel extends TileEntityMachine
{
    private SolarPanelTier tier;
    private static final float MIN_DAY_GENERATION_FACTOR = 0.20F;

    public TileEntitySolarPanel()
    {
        this(SolarPanelTier.BASIC);
    }

    public TileEntitySolarPanel(SolarPanelTier tier)
    {
        super(new Builder()
                .setItemEnergy(1)
                .setEnergy(tier.capacity, 0, tier.maxExtract, 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.OUTPUT)));

        this.tier = tier;
    }

    @Override
    public ITextComponent getDisplayName() { return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation(tier.translationKey); }

    @Override
    public SoundEvent getActiveSound() { return ModSounds.SOLAR_PANEL_ACTIVE; }

    @Override
    public float getActiveSoundVolume() { return 0.005F; }

    @Override
    public void onLoad() {
        super.onLoad();

        Block block = getBlockType();

        if (block instanceof BlockMachineSolarPanel)
        {
            this.tier = ((BlockMachineSolarPanel) block).getTier();

            this.storage.setMaxEnergyStored(this.tier.capacity);
            this.storage.setMaxReceive(0);
            this.storage.setMaxExtract(this.tier.maxExtract);

            if (this.storage.getEnergyStored() > this.storage.getMaxEnergyStored()) this.storage.setEnergyStored(this.storage.getMaxEnergyStored());
        }
    }

    public SolarPanelTier getTier()
    {
        return this.tier;
    }

    @Override
    public void chargeItem(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;

        IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

        if (itemEnergy == null) return;

        if (tier == SolarPanelTier.CREATIVE)
        {
            int missing = itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored();

            while (missing > 0)
            {
                int received = itemEnergy.receiveEnergy(missing, false);

                if (received <= 0) break;

                missing -= received;
            }

            return;
        }

        int energyToReceive = this.storage.extractEnergy(Integer.MAX_VALUE, true);
        int receivedEnergy = itemEnergy.receiveEnergy(energyToReceive, false);

        this.storage.extractEnergy(receivedEnergy, false);
    }

    private void transferEnergy()
    {
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            EnumFacing relativeSide = this.getRelativeSide(this.getMachineFacing(), facing);
            SidedIngredientHandler energyHandler = this.getSidedHandler(relativeSide);

            if (energyHandler == null || !energyHandler.canExtract()) continue;

            BlockPos targetPos = this.pos.offset(facing);
            TileEntity neighbor = this.world.getTileEntity(targetPos);

            if (neighbor == null || !neighbor.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) continue;

            IEnergyStorage neighborStorage = neighbor.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());

            if (neighborStorage == null) continue;

            int energyExtracted = energyHandler.extractEnergy(this.storage.getMaxExtract(), true);

            if (energyExtracted <= 0) continue;

            int energyAccepted = neighborStorage.receiveEnergy(energyExtracted, false);

            if (energyAccepted > 0)
            {
                energyHandler.extractEnergy(energyAccepted, false);
                if (this.tier == SolarPanelTier.CREATIVE) this.storage.addEnergy(energyAccepted, false);
            }
        }
    }

    @Override
    public void update()
    {
        if (this.world == null) return;

        if (this.world.isRemote)
        {
            MinersProsperity.proxy.updateMachineSound(this);
            return;
        }

        this.runAutomaticItemIO();

        if (this.storage.canExtract()) this.transferEnergy();

        boolean canOperate = this.canMachineOperate();
        boolean isDay = this.world.isDaytime();
        boolean isRaining = this.world.isRaining();
        float lightFactor = this.calculateLightObstructionFactor();
        float dayTimeFactor = this.calculateDayTimeGenerationFactor();
        boolean active = lightFactor > 0 && dayTimeFactor > 0 && isDay && canOperate;

        ItemStack energy = this.getEnergyStack(0);
        if (!energy.isEmpty() && this.storage.getEnergyStored() > 0) this.chargeItem(energy);

        if (active)
        {
            int baseGeneration = isRaining ? this.tier.generationRain : this.tier.generationClear;
            this.energyGeneration = Math.round(baseGeneration * lightFactor * dayTimeFactor);

            if (this.storage.getEnergyStored() != this.storage.getMaxEnergyStored()) this.storage.addEnergy(this.energyGeneration, false);
        }
        else this.energyGeneration = 0;

        BlockMachine.MachineState machineState = active ? BlockMachine.MachineState.ACTIVE : this.storage.getEnergyStored() > 0 ? BlockMachine.MachineState.POWERED : BlockMachine.MachineState.OFF;

        if (BlockMachine.getMachineState(this.world, this.pos) != machineState) BlockMachine.setMachineState(machineState, this.world, this.pos);
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

    private float calculateDayTimeGenerationFactor()
    {
        long time = this.world.getWorldTime() % 24000L;

        if (time < 0) time += 24000L;

        if (time > 12000L) return 0.0F;

        if (time < 1000L)
        {
            float progress = time / 1000.0F;
            return MIN_DAY_GENERATION_FACTOR + (1.0F - MIN_DAY_GENERATION_FACTOR) * progress;
        }

        if (time <= 11000L) return 1.0F;

        float progress = (time - 11000L) / 1000.0F;

        return 1.0F - (1.0F - MIN_DAY_GENERATION_FACTOR) * progress;
    }
}