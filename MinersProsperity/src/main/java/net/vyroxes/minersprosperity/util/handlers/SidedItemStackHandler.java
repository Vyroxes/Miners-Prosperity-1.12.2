package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import org.jetbrains.annotations.NotNull;

public class SidedItemStackHandler implements IItemHandler
{
    private final TileEntityAlloyFurnace tileEntity;
    private final ItemStackHandler itemStackHandler;
    private final EnumFacing facing;

    public static class SlotState
    {
        public enum SlotType
        {
            INPUT,
            OUTPUT
        }

        public enum IngredientType
        {
            ITEM,
            FLUID
        }

        public enum SlotMode
        {
            DISABLED,
            INPUT,
            AUTO_INPUT,
            OUTPUT,
            AUTO_OUTPUT
        }

        public enum SlotOutputMode
        {
            DEFAULT,
            VOID_EXCESS,
            VOID_ALL
        }

        private SlotType slotType;
        private IngredientType ingredientType;
        private SlotMode slotMode;
        private SlotOutputMode slotOutputMode;

        public SlotState(SlotType slotType, IngredientType ingredientType, SlotMode slotMode, SlotOutputMode slotOutputMode)
        {
            this.slotType = slotType;
            this.ingredientType = ingredientType;
            this.slotMode = slotMode;
            this.slotOutputMode = slotOutputMode;
        }

        public SlotType getSlotType()
        {
            return this.slotType;
        }

        public void setSlotType(SlotType slotType)
        {
            this.slotType = slotType;
        }

        public IngredientType getIngredientType()
        {
            return this.ingredientType;
        }

        public void setIngredientType(IngredientType ingredientType)
        {
            this.ingredientType = ingredientType;
        }

        public SlotMode getSlotMode()
        {
            return this.slotMode;
        }

        public void setSlotMode(SlotMode slotMode)
        {
            this.slotMode = slotMode;
        }

        public SlotOutputMode getSlotOutputMode()
        {
            return this.slotOutputMode;
        }

        public void setSlotOutputMode(SlotOutputMode slotOutputMode)
        {
            this.slotOutputMode = slotOutputMode;
        }

        public void writeToNBT(@NotNull NBTTagCompound tag)
        {
            tag.setString("SlotType", getSlotType().name());
            tag.setString("IngredientType", getIngredientType().name());
            tag.setString("SlotMode", getSlotMode().name());
            tag.setString("SlotOutputMode", getSlotOutputMode().name());
        }

        public void readFromNBT(@NotNull NBTTagCompound tag)
        {
            if (tag.hasKey("SlotType"))
            {
                this.slotType = SlotType.valueOf(tag.getString("SlotType"));
            }
            if (tag.hasKey("IngredientType"))
            {
                this.ingredientType = IngredientType.valueOf(tag.getString("IngredientType"));
            }
            if (tag.hasKey("SlotMode"))
            {
                this.slotMode = SlotMode.valueOf(tag.getString("SlotMode"));
            }
            if (tag.hasKey("SlotOutputMode"))
            {
                this.slotOutputMode = SlotOutputMode.valueOf(tag.getString("SlotOutputMode"));
            }
        }
    }

    private final SlotState[] slotStates;

    public SidedItemStackHandler(TileEntityAlloyFurnace tileEntity, ItemStackHandler itemStackHandler, int inputs, int outputs, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.itemStackHandler = itemStackHandler;
        this.facing = facing;
        this.slotStates = new SlotState[inputs + outputs];

        for (int i = 0; i < inputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.INPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs; i < inputs + outputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.OUTPUT, SlotState.SlotOutputMode.DEFAULT);
        }
    }

    public boolean isSlotOutput(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType().equals(SlotState.SlotType.OUTPUT);
        }

        return false;
    }

    public SlotState getSlotState(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id];
        }

        return null;
    }

    public void setSlotState(int id, SlotState slotState)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id] = slotState;

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, slotState, this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotType getSlotType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotType();
        }

        return null;
    }

    public void setSlotType(int id, SlotState.SlotType slotType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotType(slotType);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.IngredientType getIngredientType(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getIngredientType();
        }

        return null;
    }

    public void setIngredientType(int id, SlotState.IngredientType ingredientType)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setIngredientType(ingredientType);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotMode getSlotMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotMode();
        }

        return null;
    }

    public void setSlotMode(int id, SlotState.SlotMode slotMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotMode(slotMode);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    public SlotState.SlotOutputMode getSlotOutputMode(int id)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            return this.slotStates[id].getSlotOutputMode();
        }

        return null;
    }

    public void setSlotOutputMode(int id, SlotState.SlotOutputMode slotOutputMode)
    {
        if (id >= 0 && id < this.slotStates.length && this.slotStates[id] != null)
        {
            this.slotStates[id].setSlotOutputMode(slotOutputMode);

            if (this.tileEntity.getWorld().isRemote)
            {
                NetworkHandler.sendSlotsStateUpdate(this.facing, id, this.slotStates[id], this.tileEntity.getPos());
            }

            this.tileEntity.markDirty();
        }
    }

    @Override
    public int getSlots()
    {
        return this.itemStackHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        return this.itemStackHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (getSlotState(slot).slotMode != SlotState.SlotMode.INPUT && getSlotState(slot).slotMode != SlotState.SlotMode.AUTO_INPUT)
        {
            return stack;
        }
        else if (isItemValid(slot, stack))
        {
            return itemStackHandler.insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (getSlotState(slot).slotMode != SlotState.SlotMode.OUTPUT && getSlotState(slot).slotMode != SlotState.SlotMode.AUTO_OUTPUT)
        {
            return ItemStack.EMPTY;
        }

        return itemStackHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.itemStackHandler.getSlotLimit(slot);
    }

    public void writeToNBT(@NotNull NBTTagCompound tag)
    {
        NBTTagList slotsTagList = new NBTTagList();

        for (SlotState slotState : this.slotStates)
        {
            NBTTagCompound slotTag = new NBTTagCompound();
            if (slotState != null)
            {
                slotState.writeToNBT(slotTag);
            }
            slotsTagList.appendTag(slotTag);
        }

        tag.setTag(this.facing.toString() + "SlotStates", slotsTagList);
    }

    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        if (tag.hasKey(this.facing.toString() + "SlotStates", 9))
        {
            NBTTagList slotsTagList = tag.getTagList(this.facing.toString() + "SlotStates", 10);
            for (int i = 0; i < slotsTagList.tagCount(); i++)
            {
                NBTTagCompound slotTag = slotsTagList.getCompoundTagAt(i);
                if (i < this.slotStates.length)
                {
                    this.slotStates[i].readFromNBT(slotTag);
                }
            }
        }
    }
}