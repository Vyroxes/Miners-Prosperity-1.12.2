package net.vyroxes.minersprosperity.util.compat.baubles;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class IBaubleCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
    private final IBauble instance;

    public IBaubleCapabilityProvider(IBauble instance)
    {
        this.instance = instance;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE)
            return BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(instance);
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}
}