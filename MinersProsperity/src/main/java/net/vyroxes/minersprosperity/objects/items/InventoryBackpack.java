package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryBackpack extends ItemStackHandler implements ICapabilityProvider
{
    private final String name;

    @CapabilityInject(InventoryBackpack.class)
    public static final Capability<InventoryBackpack> BACKPACK_CAPABILITY = null;

    public InventoryBackpack(int size)
    {
        super(size);
        this.name = "Backpack";
    }

    public InventoryBackpack(int size, String name)
    {
        super(size);
        this.name = name;
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.getSlots(); i++)
        {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }
        compound.setTag("Items", itemList);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList itemList = compound.getTagList("Items", 10);
        for (int i = 0; i < itemList.tagCount(); i++)
        {
            NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
            int slot = itemTag.getInteger("Slot");
            if (slot >= 0 && slot < this.getSlots())
            {
                this.setStackInSlot(slot, new ItemStack(itemTag));
            }
        }
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this);
        }
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, net.minecraft.util.EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    private boolean autoCollect;

    public boolean getAutoCollectState()
    {
        return this.autoCollect;
    }

    public void setAutoCollectState(boolean autoCollect)
    {
        this.autoCollect = autoCollect;
    }
}