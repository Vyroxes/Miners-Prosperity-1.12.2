package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Objects;

public class CustomItemStackHandler
{
    private final ItemStack stack;
    private Item item;
    private int meta;
    private NBTTagCompound nbt;

    public CustomItemStackHandler(ItemStack stack)
    {
        this.stack = stack;
        if (!stack.isEmpty())
        {
            this.item = stack.getItem();
            this.meta = stack.getMetadata();
            this.nbt = stack.getTagCompound();
        }
    }

    public ItemStack getItemStack()
    {
        return stack;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof CustomItemStackHandler other))
        {
            return false;
        }

        return Objects.equals(this.item, other.item) && this.meta == other.meta && Objects.equals(this.nbt, other.nbt);
    }

    @Override
    public int hashCode()
    {
        int result = Objects.hash(this.item, this.meta);
        result = 31 * result + (this.nbt != null ? this.nbt.toString().hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "CustomItemStack{" + "item=" + this.item + ", meta=" + this.meta + ", nbt=" + this.nbt + ", hash=" + this.hashCode() + '}';
    }
}