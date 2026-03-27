package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.vyroxes.minersprosperity.objects.energy.CustomEnergyStorage;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BatteryCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
    private final ItemStack stack;
    private final CustomEnergyStorage storage;

    public BatteryCapabilityProvider(ItemStack stack, CustomEnergyStorage storage)
    {
        this.stack = stack;
        this.storage = storage;

        if (stack.hasTagCompound())
            storage.readFromNBT(stack.getTagCompound());
    }

    @Override
    public boolean hasCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Override
    public <T> T getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(new ItemBattery.EnergyStorageWrapper(stack, storage));
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
    public void deserializeNBT(NBTTagCompound nbt)
    {
        storage.readFromNBT(nbt);
        stack.setTagCompound(nbt);
    }
}