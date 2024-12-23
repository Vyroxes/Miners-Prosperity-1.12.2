package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.blocks.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.objects.blocks.machines.MachineAlloyFurnace;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class TileEntityAlloyFurnace extends TileEntity implements ITickable
{
    public final CustomEnergyStorage storage = new CustomEnergyStorage(20000, 200, 0, 0);
    private int cookTime;
    private int totalCookTime;
    private String customName;
    private int redstoneControlButtonState;
    private int[] input1State = new int[6];;
    private int[] input2State = new int[6];;
    private int[] energyState = new int[6];;
    private int[] outputState = new int[6];;
    private String slot;
    private EnumFacing facing;
    private int currentFace;

    private final ItemStackHandler machineItemStacks = new ItemStackHandler(4)
    {
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack)
        {
            return 64;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
            //System.out.println(facing);

            if (world.isRemote)
            {
                if (facing == null)
                {
                    return super.insertItem(slot, stack, simulate);
                }
            }

            if (facing == null || currentFace < 0 || currentFace >= 6)
            {
                return super.insertItem(slot, stack, simulate);
            }

            ItemStack slot0 = this.getStackInSlot(0);
            ItemStack slot1 = this.getStackInSlot(1);

            if (input1State[currentFace] == 1 && isValidInputForSlot(stack, slot0, slot1, true))
            {
                return super.insertItem(0, stack, false);
            }

            if (input2State[currentFace] == 1 && isValidInputForSlot(stack, slot0, slot1, false))
            {
                return super.insertItem(1, stack, false);
            }

            if (energyState[currentFace] == 1)
            {
                return super.insertItem(2, stack, false);
            }

            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            //System.out.println(facing);

            if (world.isRemote)
            {
                if (facing == null)
                {
                    return super.extractItem(slot, amount, simulate);
                }
            }

            if (facing == null || currentFace < 0 || currentFace >= 6)
            {
                return super.extractItem(slot, amount, simulate);
            }

            ItemStack slot0 = this.getStackInSlot(0);
            ItemStack slot1 = this.getStackInSlot(1);
            ItemStack slot2 = this.getStackInSlot(2);
            ItemStack slot3 = this.getStackInSlot(3);

            if (input1State[currentFace] == 2)
            {
                if (!slot0.isEmpty())
                {
                    return super.extractItem(0, amount, simulate);
                }
            }

            if (input2State[currentFace] == 2)
            {
                if (!slot1.isEmpty())
                {
                    return super.extractItem(1, amount, simulate);
                }
            }

            if (energyState[currentFace] == 2)
            {
                if (!slot2.isEmpty())
                {
                    return super.extractItem(2, amount, simulate);
                }
            }

            if (outputState[currentFace] == 2)
            {
                if (!slot3.isEmpty())
                {
                    return super.extractItem(3, amount, simulate);
                }
            }

            return ItemStack.EMPTY;
        }
    };

    private boolean isValidInputForSlot(ItemStack stack, ItemStack slot0, ItemStack slot1, boolean isSlot0)
    {
        RecipesAlloyFurnace recipes = RecipesAlloyFurnace.getInstance();

        if (!recipes.isInputInAnyRecipe(stack))
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
            return !recipes.getResult(stack, otherSlot).isEmpty() || !recipes.getResult(otherSlot, stack).isEmpty();
        }

        if (!isSlot0 && slot1.isEmpty() && !slot0.isEmpty())
        {
            return !recipes.getResult(stack, otherSlot).isEmpty() || !recipes.getResult(otherSlot, stack).isEmpty();
        }

        ItemStack currentSlot = isSlot0 ? slot0 : slot1;
        return !currentSlot.isEmpty() && currentSlot.getItem().equals(stack.getItem());
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return true;
        }
        if (capability == CapabilityEnergy.ENERGY)
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
            this.facing = facing;
            this.currentFace(facing);
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.machineItemStacks);
        }
        if (capability == CapabilityEnergy.ENERGY)
        {
            return CapabilityEnergy.ENERGY.cast(this.storage);
        }


//        if (this.world.isRemote)
//        {
//            NetworkHandler.sendFacingState(this.facing, this.pos);
//        }
//        if (!this.world.isRemote)
//        {
//            NetworkHandler.sendTileEntitySync(this.pos, this.facing, this.currentFace);
//        }

        this.markDirty();

        return super.getCapability(capability, facing);
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;

        if (!this.world.isRemote)
        {
            NetworkHandler.sendFacingState(this.facing, this.pos);
        }

        this.markDirty();
    }

    public void setCurrentFace(int currentFace)
    {
        this.currentFace = currentFace;

        this.markDirty();
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

    public String getSlot()
    {
        return this.slot;
    }

    public void setSlot(String slot)
    {
        this.slot = slot;
    }

    public void currentFace(EnumFacing facing)
    {
        if (!this.hasWorld())
        {
            return;
        }

        IBlockState state = this.world.getBlockState(this.pos);
        if (!(state.getBlock() instanceof MachineAlloyFurnace))
        {
            return;
        }

        EnumFacing machineFacing = state.getValue(BlockHorizontal.FACING);

        if (this.facing == null)
        {
            this.currentFace = -1;
            return;
        }

        if (this.facing == EnumFacing.UP)
        {
            this.currentFace = 4;
            return;
        }

        if (this.facing == EnumFacing.DOWN)
        {
            this.currentFace = 5;
            return;
        }

        int[][] faceMapping = {
                {0, 1, 3, 2}, // facing == NORTH
                {1, 0, 2, 3}, // facing == SOUTH
                {3, 2, 0, 1}, // facing == EAST
                {2, 3, 1, 0}  // facing == WEST
        };

        int facingIndex = switch (this.facing)
        {
            case NORTH -> 0;
            case SOUTH -> 1;
            case EAST -> 2;
            case WEST -> 3;
            default -> -1;
        };

        int machineFacingIndex = switch (machineFacing)
        {
            case NORTH -> 0;
            case SOUTH -> 1;
            case EAST -> 2;
            case WEST -> 3;
            default -> -1;
        };

        if (facingIndex != -1 && machineFacingIndex != -1)
        {
            this.currentFace = faceMapping[facingIndex][machineFacingIndex];
        }
        else
        {
            this.currentFace = -1;
        }

        if (!this.world.isRemote)
        {
            //NetworkHandler.sendTileEntitySync(this.pos, this.facing, this.currentFace);
        }

        this.markDirty();
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

    public void setSlotsState()
    {
        int[][] slotsState = {this.input1State, this.input2State, this.energyState, this.outputState};

        if (this.world.isRemote)
        {
            NetworkHandler.sendSlotsStateUpdate(slotsState, this.pos);
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

//    @Override
//    public void markDirty()
//    {
//        super.markDirty();
//        if (this.world != null && !this.world.isRemote && this.world instanceof WorldServer worldServer)
//        {
//            worldServer.getPlayerChunkMap().markBlockForUpdate(this.pos);
//        }
//    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());

//        if (this.world.isRemote)
//        {
//            IBlockState state = this.world.getBlockState(getPos());
//            this.world.notifyBlockUpdate(this.pos, state, state, 3);
//        }
    }

    @Override
    public void handleUpdateTag(@NotNull NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        //NBTTagCompound tag = new NBTTagCompound();
        //this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
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

        for (int i = 0; i < input1State.length; i++)
        {
            statesTag.setInteger("Input1State[" + i + "]", input1State[i]);
        }

        for (int i = 0; i < input2State.length; i++)
        {
            statesTag.setInteger("Input2State[" + i + "]", input2State[i]);
        }

        for (int i = 0; i < energyState.length; i++)
        {
            statesTag.setInteger("EnergyState[" + i + "]", energyState[i]);
        }

        for (int i = 0; i < outputState.length; i++)
        {
            statesTag.setInteger("OutputState[" + i + "]", outputState[i]);
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

            for (int i = 0; i < input1State.length; i++)
            {
                this.input1State[i] = statesTag.getInteger("Input1State[" + i + "]");
            }

            for (int i = 0; i < input2State.length; i++)
            {
                this.input2State[i] = statesTag.getInteger("Input2State[" + i + "]");
            }

            for (int i = 0; i < energyState.length; i++)
            {
                this.energyState[i] = statesTag.getInteger("EnergyState[" + i + "]");
            }

            for (int i = 0; i < outputState.length; i++)
            {
                this.outputState[i] = statesTag.getInteger("OutputState[" + i + "]");
            }
        }

        this.markDirty();
    }

    public boolean isPowered()
    {
        return this.cookTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isPowered(TileEntityAlloyFurnace tileEntity)
    {
        return tileEntity.cookTime > 0;
    }

    public void setEnergyStoredFromItem(ItemStack stack)
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
        System.out.println("Facing: " + this.facing);

        boolean wasPowered = this.isPowered();
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

    public int[] getInput1State()
    {
        return this.input1State;
    }

    public void setInput1State(int[] input1State)
    {
        this.input1State = input1State;

        this.markDirty();
    }

    public void setInput1State(int id, int input1State)
    {
        this.input1State[id] = input1State;

        this.markDirty();
    }

    public int[] getInput2State()
    {
        return this.input2State;
    }

    public void setInput2State(int id, int input2State)
    {
        this.input2State[id] = input2State;

        this.markDirty();
    }

    public void setInput2State(int[] input2State)
    {
        this.input2State = input2State;

        this.markDirty();
    }

    public int[] getEnergyState()
    {
        return this.energyState;
    }

    public void setEnergyState(int id, int energyState)
    {
        this.energyState[id] = energyState;

        this.markDirty();
    }

    public void setEnergyState(int[] energyState)
    {
        this.energyState = energyState;

        this.markDirty();
    }

    public int[] getOutputState()
    {
        return this.outputState;
    }

    public void setOutputState(int id, int outputState)
    {
        this.outputState[id] = outputState;

        this.markDirty();
    }

    public void setOutputState(int[] outputState)
    {
        this.outputState = outputState;

        this.markDirty();
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