package net.vyroxes.minersprosperity.item.battery;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.energy.CustomEnergyStorage;
import org.jetbrains.annotations.Nullable;
import javax.annotation.Nonnull;

public class BatteryCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
    private final CustomEnergyStorage storage;
    private final IEnergyStorage wrapper;

    public BatteryCapabilityProvider(ItemStack stack, CustomEnergyStorage storage)
    {
        this.storage = storage;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy")) storage.readFromNBT(stack.getTagCompound());

        this.wrapper = new ItemBattery.EnergyStorageWrapper(stack, storage);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) { return capability == CapabilityEnergy.ENERGY; }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(this.wrapper);

        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        storage.writeToNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) { storage.readFromNBT(nbt); }
}