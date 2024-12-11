package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackInventory extends ItemStackHandler implements ICapabilityProvider
{
    private final String name;

    @CapabilityInject(BackpackInventory.class)
    public static final Capability<BackpackInventory> BACKPACK_CAPABILITY = null;

    public BackpackInventory(int size)
    {
        super(size);
        this.name = "Backpack";
    }

    public BackpackInventory(int size, String name)
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

//package net.vyroxes.minersprosperity.objects.items;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.inventory.ItemStackHelper;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TextComponentString;
//
//public class BackpackInventory implements IInventory
//{
//    private final NonNullList<ItemStack> backpackContents;
//    private final int size;
//    private final String name;
//
//    public BackpackInventory(int size)
//    {
//        this.size = size;
//        this.backpackContents = NonNullList.withSize(size, ItemStack.EMPTY);
//        this.name = "Backpack";
//    }
//
//    public BackpackInventory(int size, String name)
//    {
//        this.size = size;
//        this.backpackContents = NonNullList.withSize(size, ItemStack.EMPTY);
//        this.name = name;
//    }
//
//    @Override
//    public int getSizeInventory()
//    {
//        return size;
//    }
//
//    @Override
//    public boolean isEmpty()
//    {
//        for (ItemStack stack : backpackContents)
//        {
//            if (!stack.isEmpty())
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public ItemStack getStackInSlot(int index)
//    {
//        if (index < 0 || index >= this.size)
//        {
//            return ItemStack.EMPTY;
//        }
//        return backpackContents.get(index);
//    }
//
//    @Override
//    public ItemStack decrStackSize(int index, int count)
//    {
//        if (index < 0 || index >= this.size)
//        {
//            return ItemStack.EMPTY;
//        }
//        return ItemStackHelper.getAndSplit(backpackContents, index, count);
//    }
//
//    @Override
//    public ItemStack removeStackFromSlot(int index)
//    {
//        if (index < 0 || index >= this.size)
//        {
//            return ItemStack.EMPTY;
//        }
//        return ItemStackHelper.getAndRemove(backpackContents, index);
//    }
//
//    @Override
//    public void setInventorySlotContents(int index, ItemStack stack)
//    {
//        if (index < 0 || index >= this.size)
//        {
//            return;
//        }
//        backpackContents.set(index, stack);
//    }
//
//    @Override
//    public int getInventoryStackLimit()
//    {
//        return 64;
//    }
//
//    @Override
//    public boolean isUsableByPlayer(EntityPlayer player)
//    {
//        return true;
//    }
//
//    @Override
//    public void openInventory(EntityPlayer player) { }
//
//    @Override
//    public void closeInventory(EntityPlayer player) { }
//
//    @Override
//    public boolean isItemValidForSlot(int index, ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public int getField(int id) { return 0; }
//
//    @Override
//    public void setField(int id, int value) { }
//
//    @Override
//    public int getFieldCount() { return 0; }
//
//    @Override
//    public void clear()
//    {
//        backpackContents.clear();
//    }
//
//    public void writeToNBT(NBTTagCompound compound)
//    {
//        NBTTagList itemList = new NBTTagList();
//        for (int i = 0; i < this.getSizeInventory(); i++)
//        {
//            ItemStack stack = this.getStackInSlot(i);
//            if (!stack.isEmpty())
//            {
//                NBTTagCompound itemTag = new NBTTagCompound();
//                itemTag.setInteger("Slot", i);
//                stack.writeToNBT(itemTag);
//                itemList.appendTag(itemTag);
//            }
//        }
//        compound.setTag("Items", itemList);
//    }
//
//    public void readFromNBT(NBTTagCompound compound)
//    {
//        NBTTagList itemList = compound.getTagList("Items", 10);
//        for (int i = 0; i < itemList.tagCount(); i++)
//        {
//            NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
//            int slot = itemTag.getInteger("Slot");
//            if (slot >= 0 && slot < this.getSizeInventory())
//            {
//                this.setInventorySlotContents(slot, new ItemStack(itemTag));
//            }
//        }
//    }
//
//    @Override
//    public String getName()
//    {
//        return this.name;
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }
//
//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return new TextComponentString(this.getName());
//    }
//    
//    private boolean autoCollect;
//
//    public boolean getAutoCollectState()
//    {
//        return this.autoCollect;
//    }
//
//    public void setAutoCollectState(boolean autoCollect)
//    {
//        this.autoCollect = autoCollect;
//    }
//
//	@Override
//	public void markDirty() {}
//}