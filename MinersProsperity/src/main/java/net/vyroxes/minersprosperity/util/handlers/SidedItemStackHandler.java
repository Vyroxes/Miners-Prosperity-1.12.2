package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import org.jetbrains.annotations.NotNull;

public class SidedItemStackHandler implements IItemHandler
{
    private final TileEntityMachine tileEntity;
    private final CustomItemStackHandler customItemStackHandler;
    private final EnumFacing facing;

    private final SlotState[] slotStates;

    public SidedItemStackHandler(TileEntityMachine tileEntity, CustomItemStackHandler customItemStackHandler, int inputs, int energy, int outputs, EnumFacing facing)
    {
        this.tileEntity = tileEntity;
        this.customItemStackHandler = customItemStackHandler;
        this.facing = facing;
        this.slotStates = new SlotState[inputs + energy + outputs];

        for (int i = 0; i < inputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.INPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs; i < inputs + energy; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.ENERGY, SlotState.IngredientType.ITEM, SlotState.SlotMode.INPUT, SlotState.SlotOutputMode.DEFAULT);
        }
        for (int i = inputs + energy; i < inputs + energy + outputs; i++)
        {
            this.slotStates[i] = new SlotState(SlotState.SlotType.OUTPUT, SlotState.IngredientType.ITEM, SlotState.SlotMode.OUTPUT, SlotState.SlotOutputMode.DEFAULT);
        }
    }

    public boolean isSlotEnergy(int id)
    {
        if (id >= 0 && id < this.slotStates.length)
        {
            return this.slotStates[id].getSlotType().equals(SlotState.SlotType.ENERGY);
        }

        return false;
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
        return this.customItemStackHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot)
    {
        return this.customItemStackHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (getSlotState(slot).getSlotMode() != SlotState.SlotMode.INPUT && getSlotState(slot).getSlotMode() != SlotState.SlotMode.AUTO_INPUT)
        {
            return stack;
        }
        else if (isItemValid(slot, stack))
        {
            return customItemStackHandler.insertItem(slot, stack, simulate);
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (getSlotState(slot).getSlotMode() != SlotState.SlotMode.OUTPUT && getSlotState(slot).getSlotMode() != SlotState.SlotMode.AUTO_OUTPUT)
        {
            return ItemStack.EMPTY;
        }

        return customItemStackHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.customItemStackHandler.getSlotLimit(slot);
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