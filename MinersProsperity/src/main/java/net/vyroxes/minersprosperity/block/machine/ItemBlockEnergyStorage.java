package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.machine.EnergyStorageTier;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class ItemBlockEnergyStorage extends ItemBlockMachine
{
    private final EnergyStorageTier tier;

    public ItemBlockEnergyStorage(BlockMachineEnergyStorage block, EnergyStorageTier tier)
    {
        super(block);
        this.tier = tier;
        this.setMaxStackSize(1);
    }

    @Override
    public boolean showDurabilityBar(@NotNull ItemStack stack) { return true; }

    @Override
    public double getDurabilityForDisplay(@NotNull ItemStack stack)
    {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
        return energy == null ? 1.0D : 1.0D - MathHelper.clamp((double) energy.getEnergyStored() / energy.getMaxEnergyStored(), 0.0D, 1.0D);
    }

    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new EnergyStorageCapability(stack, this.tier);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items)
    {
        if (!this.isInCreativeTab(tab)) return;
        if (this.tier != EnergyStorageTier.CREATIVE) items.add(this.createStack(0));
        items.add(this.createStack(this.tier.capacity));
    }

    private ItemStack createStack(int energy)
    {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Energy", energy);
        tag.setInteger("EnergyCapacity", this.tier.capacity);
        stack.setTagCompound(tag);
        return stack;
    }

    private static final class EnergyStorageCapability implements ICapabilityProvider, IEnergyStorage
    {
        private final ItemStack stack;
        private final EnergyStorageTier tier;

        private EnergyStorageCapability(ItemStack stack, EnergyStorageTier tier)
        {
            this.stack = stack;
            this.tier = tier;
        }

        @Override
        public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) { return capability == CapabilityEnergy.ENERGY; }

        @Override
        public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : null;
        }

        @Override
        public int receiveEnergy(int amount, boolean simulate)
        {
            if (this.stack.getCount() != 1 || !this.canReceive() || amount <= 0) return 0;
            int received = Math.min(this.tier.capacity - this.getEnergyStored(), Math.min(this.tier.transfer, amount));
            if (!simulate && received > 0) this.setEnergy(this.getEnergyStored() + received);
            return received;
        }

        @Override
        public int extractEnergy(int amount, boolean simulate)
        {
            if (this.stack.getCount() != 1 || amount <= 0) return 0;
            int extracted = Math.min(this.getEnergyStored(), Math.min(this.tier.transfer, amount));
            if (!simulate && extracted > 0 && this.tier != EnergyStorageTier.CREATIVE) this.setEnergy(this.getEnergyStored() - extracted);
            return extracted;
        }

        private void setEnergy(int energy)
        {
            if (!this.stack.hasTagCompound()) this.stack.setTagCompound(new NBTTagCompound());
            this.stack.getTagCompound().setInteger("Energy", energy);
            this.stack.getTagCompound().setInteger("EnergyCapacity", this.tier.capacity);
        }

        @Override
        public int getEnergyStored()
        {
            if (this.tier == EnergyStorageTier.CREATIVE) return this.tier.capacity;
            NBTTagCompound tag = this.stack.getTagCompound();
            return tag == null ? 0 : MathHelper.clamp(tag.getInteger("Energy"), 0, this.tier.capacity);
        }

        @Override
        public int getMaxEnergyStored() { return this.tier.capacity; }
        @Override
        public boolean canExtract() { return true; }
        @Override
        public boolean canReceive() { return this.tier != EnergyStorageTier.CREATIVE; }
    }
}