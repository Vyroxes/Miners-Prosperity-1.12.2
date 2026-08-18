package net.vyroxes.minersprosperity.item.backpack;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound>
{
    private final ItemStackHandler mainInventory;
    private final ItemStackHandler configInventory;

    public BackpackCapabilityProvider(int mainSize, int configSize)
    {
        this.mainInventory = new ItemStackHandler(mainSize);
        this.configInventory = new ItemStackHandler(configSize);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (facing == null)
            {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mainInventory);
            }
            else if (facing == EnumFacing.DOWN)
            {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(configInventory);
            }
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Inventory", mainInventory.serializeNBT());
        tag.setTag("ConfigurationInventory", configInventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        mainInventory.deserializeNBT(nbt.getCompoundTag("Inventory"));
        configInventory.deserializeNBT(nbt.getCompoundTag("ConfigurationInventory"));
    }

    public ItemStackHandler getMainInventory()
    {
        return mainInventory;
    }

    public ItemStackHandler getConfigInventory()
    {
        return configInventory;
    }
}