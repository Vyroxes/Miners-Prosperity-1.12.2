package net.vyroxes.minersprosperity.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.block.machine.BlockMachineEnergyStorage;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.machine.EnergyStorageTier;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import org.jetbrains.annotations.NotNull;

public class TileEntityEnergyStorage extends TileEntityMachine
{
    private EnergyStorageTier tier;
    private final IEnergyStorage[] energyHandlers = new IEnergyStorage[7];
    private int lastEnergy = -1;
    private int lastRenderLevel = -1;

    public TileEntityEnergyStorage() { this(EnergyStorageTier.BASIC); }

    public TileEntityEnergyStorage(EnergyStorageTier tier)
    {
        super(new Builder().setItemEnergy(2)
                .setEnergy(tier.capacity, tier.transfer, tier.transfer, tier == EnergyStorageTier.CREATIVE ? tier.capacity : 0)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.INPUT_OUTPUT)));
        this.tier = tier;
    }

    public EnergyStorageTier getTier() { return this.tier; }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation(this.tier.translationKey);
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        Block block = this.getBlockType();
        if (block instanceof BlockMachineEnergyStorage) this.tier = ((BlockMachineEnergyStorage) block).getTier();
        this.applyTier();
    }

    private void applyTier()
    {
        this.storage.setMaxEnergyStored(this.tier.capacity);
        this.storage.setMaxReceive(this.tier.transfer);
        this.storage.setMaxExtract(this.tier.transfer);
        this.storage.setEnergyStored(this.tier == EnergyStorageTier.CREATIVE ? this.tier.capacity : Math.max(0, this.storage.getEnergyStored()));
    }

    public boolean isEnergyItemValid(int index, ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
        return energy != null && (index == 0 ? energy.canExtract() : index == 1 && energy.canReceive());
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing)
    {
        if (capability != CapabilityEnergy.ENERGY) return super.getCapability(capability, facing);
        int index = facing == null ? 6 : facing.ordinal();
        if (this.energyHandlers[index] == null)
        {
            IEnergyStorage delegate = super.getCapability(CapabilityEnergy.ENERGY, facing);
            if (delegate == null) return null;
            this.energyHandlers[index] = new IEnergyStorage()
            {
                @Override
                public int receiveEnergy(int amount, boolean simulate)
                {
                    int received = delegate.receiveEnergy(Math.max(0, amount), simulate);
                    if (!simulate && received > 0) markDirty();
                    return received;
                }

                @Override
                public int extractEnergy(int amount, boolean simulate)
                {
                    int extracted = delegate.extractEnergy(Math.max(0, amount), simulate || tier == EnergyStorageTier.CREATIVE);
                    if (!simulate && extracted > 0 && tier != EnergyStorageTier.CREATIVE) markDirty();
                    return extracted;
                }

                @Override
                public int getEnergyStored() { return delegate.getEnergyStored(); }
                @Override
                public int getMaxEnergyStored() { return delegate.getMaxEnergyStored(); }
                @Override
                public boolean canExtract() { return delegate.canExtract(); }
                @Override
                public boolean canReceive() { return delegate.canReceive(); }
            };
        }
        return CapabilityEnergy.ENERGY.cast(this.energyHandlers[index]);
    }

    @Override
    public void chargeItem(ItemStack stack)
    {
        if (this.tier != EnergyStorageTier.CREATIVE)
        {
            super.chargeItem(stack);
            return;
        }
        IEnergyStorage item = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (item != null) item.receiveEnergy(Integer.MAX_VALUE, false);
    }

    private void transferEnergy()
    {
        int remaining = this.tier.transfer;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if (remaining <= 0) break;
            IEnergyStorage source = this.getCapability(CapabilityEnergy.ENERGY, facing);
            if (source == null || !source.canExtract()) continue;
            TileEntity neighbor = this.world.getTileEntity(this.pos.offset(facing));
            if (neighbor == null) continue;
            IEnergyStorage target = neighbor.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
            if (target == null) continue;
            int available = source.extractEnergy(remaining, true);
            int received = target.receiveEnergy(available, false);
            if (received > 0) remaining -= source.extractEnergy(received, false);
        }
    }

    @Override
    public void update()
    {
        if (this.world == null || this.world.isRemote) return;
        if (this.canMachineOperate())
        {
            this.runAutomaticItemIO();
            this.setEnergyStoredFromItem(this.getEnergyStack(0));
            this.chargeItem(this.getEnergyStack(1));
            this.transferEnergy();
            if (!this.getEnergyStack(0).isEmpty() || !this.getEnergyStack(1).isEmpty()) this.markDirty();
        }
        if (this.lastEnergy != this.getEnergyStored())
        {
            this.lastEnergy = this.getEnergyStored();
            this.markDirty();
            this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
        }
        int renderLevel = (int) ((long) this.getEnergyStored() * 15 / this.getMaxEnergyStored());
        if (renderLevel != this.lastRenderLevel)
        {
            this.lastRenderLevel = renderLevel;
            net.minecraft.block.state.IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 2);
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setString("StorageTier", this.tier.name());
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        if (tag.hasKey("StorageTier", 8))
        {
            try { this.tier = EnergyStorageTier.valueOf(tag.getString("StorageTier")); }
            catch (IllegalArgumentException ignored) { this.tier = EnergyStorageTier.BASIC; }
        }
        super.readFromNBT(tag);
        this.applyTier();
    }
}