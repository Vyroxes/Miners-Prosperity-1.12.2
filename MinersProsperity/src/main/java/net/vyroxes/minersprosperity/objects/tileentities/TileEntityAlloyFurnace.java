package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.blocks.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.objects.blocks.machines.MachineAlloyFurnace;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedItemHandler;
import org.jetbrains.annotations.NotNull;

public class TileEntityAlloyFurnace extends TileEntity implements ITickable
{
    public final CustomEnergyStorage storage = new CustomEnergyStorage(20000, 200, 0, 0);
    private int cookTime;
    private int totalCookTime;
    private String customName;
    private int redstoneControlButtonState;
    private int slotId;
    private final SidedItemHandler[] sidedHandlers = new SidedItemHandler[EnumFacing.values().length];
    public TileEntityAlloyFurnace()
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            sidedHandlers[facing.ordinal()] = new SidedItemHandler(
                    this,
                    machineItemStacks,
                    3,
                    1,
                    facing
            );
        }
    }

    public IItemHandler getSidedItemHandler(EnumFacing side)
    {
        return sidedHandlers[side.ordinal()];
    }

    private final ItemStackHandler machineItemStacks = new ItemStackHandler(4)
    {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
            if (slot >= 0 && slot <= 2)
            {
                if (isItemValid(slot, stack))
                {
                    return super.insertItem(slot, stack, simulate);
                }
            }

            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot)
        {
            if (slot == 2)
            {
                return 1;
            }

            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            if (slot == 0)
            {
                return isValidInputForSlot(stack, getStackInSlot(0), getStackInSlot(1), true);
            }
            else if (slot == 1)
            {
                return isValidInputForSlot(stack, getStackInSlot(0), getStackInSlot(1), false);
            }
            else if (slot == 2)
            {
                return isItemEnergy(stack);
            }
            return false;
        }
    };

    public ItemStack getItemStackInSlot(int slot)
    {
        return machineItemStacks.getStackInSlot(slot);
    }

    public boolean isValidInputForSlot(ItemStack stack, ItemStack slot0, ItemStack slot1, boolean isSlot0)
    {
        RecipesAlloyFurnace recipes = RecipesAlloyFurnace.getInstance();

        if (stack.isEmpty())
        {
            return false;
        }

        if (slot0.isEmpty() && slot1.isEmpty())
        {
            return true;
        }

        ItemStack otherSlot = isSlot0 ? slot1 : slot0;

        if (isSlot0 && slot0.isEmpty() && !otherSlot.isEmpty())
        {
            return !recipes.findRecipes(stack, otherSlot).isEmpty();
        }

        if (!isSlot0 && slot1.isEmpty() && !slot0.isEmpty())
        {
            return !recipes.findRecipes(stack, otherSlot).isEmpty();
        }

        ItemStack currentSlot = isSlot0 ? slot0 : slot1;
        return !currentSlot.isEmpty() && currentSlot.getItem().equals(stack.getItem());
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY)
        {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (facing == null)
            {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.machineItemStacks);
            }

            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getSidedItemHandler(getRelativeSide(getMachineFacing(), facing)));
        }

        if (capability == CapabilityEnergy.ENERGY)
        {
            return CapabilityEnergy.ENERGY.cast(this.storage);
        }

        return super.getCapability(capability, facing);
    }

    private EnumFacing getMachineFacing()
    {
        if (world != null)
        {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof MachineAlloyFurnace)
            {
                return state.getValue(MachineAlloyFurnace.FACING);
            }
        }
        return EnumFacing.NORTH;
    }

    public int getEnergyStored()
    {
        return this.storage.getEnergyStored();
    }

    public void setEnergyStored(int energyStored)
    {
        this.storage.setEnergyStored(energyStored);
    }

    public int getMaxEnergyStored()
    {
        return this.storage.getMaxEnergyStored();
    }

    public int getMaxReceive()
    {
        return this.storage.getMaxReceive();
    }

    public int getEnergyUsage()
    {
        return this.storage.getEnergyUsage();
    }

    public int getSlotId()
    {
        return this.slotId;
    }

    public void setSlotId(int slotId)
    {
        this.slotId = slotId;
    }

    private EnumFacing getRelativeSide(EnumFacing machineFacing, EnumFacing pipeDirection)
    {
        EnumFacing[] rotationMap = switch (machineFacing)
        {
            case NORTH -> new EnumFacing[]{EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
            case SOUTH -> new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST};
            case WEST -> new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.NORTH};
            case EAST -> new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH};
            default -> new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
        };

        return switch (pipeDirection)
        {
            case NORTH -> rotationMap[0];
            case SOUTH -> rotationMap[1];
            case EAST -> rotationMap[2];
            case WEST -> rotationMap[3];
            default -> pipeDirection;
        };
    }

    public boolean isItemEnergy(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null)
        {
            if (tag.hasKey("mekData"))
            {
                NBTTagCompound mekData = tag.getCompoundTag("mekData");

                return mekData.hasKey("energyStored");
            }
            else return tag.hasKey("Energy") || tag.hasKey("energy");
        }

        return false;
    }

    public int getRedstoneControlButtonState()
    {
        return this.redstoneControlButtonState;
    }

    public void setRedstoneControlButtonState(int redstoneControlButtonState)
    {
        this.redstoneControlButtonState = redstoneControlButtonState;

        if (this.world.isRemote)
        {
            NetworkHandler.sendButtonStateUpdate(this.redstoneControlButtonState, this.pos);
        }

		this.markDirty();
    }
    
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;

        this.markDirty();
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.alloy_furnace");
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(@NotNull NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.getPos(), -1, tag);
    }
    
    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setTag("Inventory", this.machineItemStacks.serializeNBT());
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("Energy", this.storage.getEnergyStored());
        tag.setInteger("EnergyUsage", this.storage.getEnergyUsage());
        tag.setInteger("MaxReceive", this.storage.getMaxReceive());

        if (this.hasCustomName())
        {
            tag.setString("CustomName", this.customName);
        }

        tag.setTag("States", createStatesTag());

        return tag;
    }

    private @NotNull NBTTagCompound createStatesTag()
    {
        NBTTagCompound statesTag = new NBTTagCompound();
        statesTag.setInteger("RedstoneControlButtonState", this.redstoneControlButtonState);

        for (EnumFacing facing : EnumFacing.values())
        {
            SidedItemHandler sidedHandler = this.sidedHandlers[facing.ordinal()];

            NBTTagList inputStates = new NBTTagList();
            for (SidedItemHandler.SlotState inputState : sidedHandler.getInputs())
            {
                NBTTagCompound inputStateTag = new NBTTagCompound();
                inputStateTag.setString("state", inputState.name());
                inputStates.appendTag(inputStateTag);
            }
            statesTag.setTag(facing.getName() + "Inputs", inputStates);

            NBTTagList outputStates = new NBTTagList();
            for (SidedItemHandler.SlotState outputState : sidedHandler.getOutputs())
            {
                NBTTagCompound outputStateTag = new NBTTagCompound();
                outputStateTag.setString("state", outputState.name());
                outputStates.appendTag(outputStateTag);
            }
            statesTag.setTag(facing.getName() + "Outputs", outputStates);
        }

        return statesTag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("Inventory", 10))
        {
            this.machineItemStacks.deserializeNBT(compound.getCompoundTag("Inventory"));
        }

        if (compound.hasKey("CookTime", 3))
        {
            this.cookTime = compound.getInteger("CookTime");
        }

        if (compound.hasKey("TotalCookTime", 3))
        {
            this.totalCookTime = compound.getInteger("TotalCookTime");
        }

        if (compound.hasKey("Energy", 3))
        {
            this.storage.setEnergyStored(compound.getInteger("Energy"));
        }

        if (compound.hasKey("EnergyUsage", 3))
        {
            this.storage.setEnergyUsage(compound.getInteger("EnergyUsage"));
        }

        if (compound.hasKey("MaxReceive", 3))
        {
            this.storage.setMaxReceive(compound.getInteger("MaxReceive"));
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.setCustomName(compound.getString("CustomName"));
        }

        if (compound.hasKey("States"))
        {
            NBTTagCompound statesTag = compound.getCompoundTag("States");

            this.redstoneControlButtonState = statesTag.getInteger("RedstoneControlButtonState");

            for (EnumFacing facing : EnumFacing.values())
            {
                String facingName = facing.getName();

                if (statesTag.hasKey(facingName + "Inputs"))
                {
                    NBTTagList inputList = statesTag.getTagList(facingName + "Inputs", Constants.NBT.TAG_COMPOUND);
                    SidedItemHandler sidedHandler = this.sidedHandlers[facing.ordinal()];
                    for (int i = 0; i < inputList.tagCount(); i++)
                    {
                        NBTTagCompound tag = inputList.getCompoundTagAt(i);
                        sidedHandler.getInputs()[i] = SidedItemHandler.SlotState.valueOf(tag.getString("state"));
                    }
                }

                if (statesTag.hasKey(facingName + "Outputs"))
                {
                    NBTTagList outputList = statesTag.getTagList(facingName + "Outputs", Constants.NBT.TAG_COMPOUND);
                    SidedItemHandler sidedHandler = this.sidedHandlers[facing.ordinal()];
                    for (int i = 0; i < outputList.tagCount(); i++)
                    {
                        NBTTagCompound tag = outputList.getCompoundTagAt(i);
                        sidedHandler.getOutputs()[i] = SidedItemHandler.SlotState.valueOf(tag.getString("state"));
                    }
                }
            }
        }

        this.markDirty();
    }

    private boolean isPowered()
    {
        boolean canOperate;
        if (this.redstoneControlButtonState > 0)
        {
            boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
            canOperate = this.canOperateBasedOnRedstone(hasRedstoneSignal);
        }
        else
        {
            canOperate = true;
        }
        return (this.canSmelt() && canOperate) || this.cookTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isPowered(TileEntityAlloyFurnace tileEntity)
    {
        return tileEntity.cookTime > 0;
    }

    private boolean canOperateBasedOnRedstone(boolean hasRedstoneSignal)
    {
        return switch (this.redstoneControlButtonState)
        {
            case 0 -> true;
            case 1 -> !hasRedstoneSignal;
            case 2 -> hasRedstoneSignal;
            default -> false;
        };
    }

    private void setEnergyStoredFromItem(ItemStack stack)
    {
        if (!stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

            if (itemEnergy != null)
            {
                int energyToExtract = Math.min(itemEnergy.getEnergyStored(), this.storage.getMaxEnergyStored() - this.storage.getEnergyStored());
                int extractedEnergy = itemEnergy.extractEnergy(energyToExtract, false);
                this.storage.setEnergyStored(this.storage.getEnergyStored() + extractedEnergy);
            }
        }

        this.markDirty();
    }

    @Override
    public void update()
    {
        boolean wasPowered = MachineAlloyFurnace.getStatePowered(this.world, this.pos);
        boolean stateChanged = false;

        if (!this.world.isRemote)
        {
            boolean canOperate;

            if (this.redstoneControlButtonState > 0)
            {
                boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
                canOperate = this.canOperateBasedOnRedstone(hasRedstoneSignal);
            }
            else
            {
                canOperate = true;
            }

            ItemStack input1 = machineItemStacks.getStackInSlot(0);
            ItemStack input2 = machineItemStacks.getStackInSlot(1);
            ItemStack energy = machineItemStacks.getStackInSlot(2);

            if (!energy.isEmpty() && this.storage.getEnergyStored() != this.storage.getMaxEnergyStored())
            {
                setEnergyStoredFromItem(energy);
                stateChanged = true;
            }

            if (this.cookTime > 0 && this.canSmelt() && canOperate)
            {
                this.storage.setEnergyUsage(RecipesAlloyFurnace.getInstance().getEnergyUsage(input1, input2));
                this.storage.setEnergyStored(this.storage.getEnergyStored() - this.storage.getEnergyUsage());
                stateChanged = true;
            }
            else if (this.cookTime > 0 && !this.canSmelt() || this.cookTime > 0 && !canOperate)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                this.storage.setEnergyUsage(0);
                stateChanged = true;
            }

            if (this.canSmelt() && canOperate)
            {

                this.processCooking(input1, input2);
                stateChanged = true;
            }

            if (wasPowered != this.isPowered())
            {
                MachineAlloyFurnace.setStatePowered(this.isPowered(), world, pos);
                stateChanged = true;
            }
        }

        if (stateChanged)
        {
            this.markDirty();
        }
    }

    private void processCooking(ItemStack input1, ItemStack input2)
    {
        if (this.cookTime == 0)
        {
            this.totalCookTime = RecipesAlloyFurnace.getInstance().getCookTime(input1, input2);
        }

        ++this.cookTime;

        if (this.cookTime >= this.totalCookTime)
        {
            this.cookTime = 0;
            this.totalCookTime = RecipesAlloyFurnace.getInstance().getCookTime(input1, input2);
            this.smeltItem();
        }

        this.markDirty();
    }

    private boolean canSmelt()
    {
        ItemStack input1 = machineItemStacks.getStackInSlot(0);
        ItemStack input2 = machineItemStacks.getStackInSlot(1);
        //int energy = RecipesAlloyFurnace.getInstance().getEnergyUsage(input1, input2) * RecipesAlloyFurnace.getInstance().getCookTime(input1, input2);
        int energy = RecipesAlloyFurnace.getInstance().getEnergyUsage(input1, input2);

        if (this.storage.getEnergyStored() > energy)
        {
            if (input1.isEmpty() || input2.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack result = RecipesAlloyFurnace.getInstance().getResult(input1, input2);

                if (result.isEmpty())
                {
                    result = RecipesAlloyFurnace.getInstance().getResult(input2, input1);
                }

                if (result.isEmpty())
                {
                    return false;
                }
                else
                {
                    ItemStack output = machineItemStacks.getStackInSlot(3);

                    if (output.isEmpty())
                    {
                        return true;
                    }
                    if (!output.isItemEqual(result))
                    {
                        return false;
                    }

                    int res = output.getCount() + result.getCount();
                    return res <= 64 && res <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    private void smeltItem()
    {
        ItemStack input1 = machineItemStacks.getStackInSlot(0);
        ItemStack input2 = machineItemStacks.getStackInSlot(1);

        if (this.canSmelt())
        {
            ItemStack result = RecipesAlloyFurnace.getInstance().getResult(input1, input2);
            ItemStack output = machineItemStacks.getStackInSlot(3);

            if (output.isEmpty())
            {
                machineItemStacks.setStackInSlot(3, result.copy());
                this.markDirty();
            }
            else if (output.isItemEqual(result))
            {
                output.grow(result.getCount());
                this.markDirty();
            }

            input1.shrink(1);
            input2.shrink(1);

            if (input1.isEmpty()) machineItemStacks.setStackInSlot(0, ItemStack.EMPTY);
            if (input2.isEmpty()) machineItemStacks.setStackInSlot(1, ItemStack.EMPTY);

            this.markDirty();
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
    	if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public int getCookTime()
    {
        return this.cookTime;
    }

    public int getTotalCookTime()
    {
        return this.totalCookTime;
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.cookTime = value;
                break;
            case 1:
                this.totalCookTime = value;
                break;
            case 2:
                this.storage.setEnergyStored(value);
                break;
            case 3:
                this.storage.setEnergyUsage(value);
        }

        this.markDirty();
    }
}