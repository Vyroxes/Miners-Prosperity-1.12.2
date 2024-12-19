package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.blocks.machines.MachineAlloyFurnace;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class TileEntityAlloyFurnace extends TileEntity implements ITickable
{
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public int burnTime;
    public int currentItemBurnTime;
    public int cookTime;
    public int totalCookTime;
    public String customName;
    public int redstoneControlButtonState;
    public int[] input1State = new int[6];;
    public int[] input2State = new int[6];;
    public int[] fuelState = new int[6];;
    public int[] outputState = new int[6];;
    public String slot;
    public EnumFacing facing;
    public int currentFace;
    public EnumFacing machineFacing;

    private final ItemStackHandler machineItemStacks = new ItemStackHandler(4)
    {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
            if (facing == null || currentFace < 0 || currentFace >= 6)
            {
                return super.insertItem(slot, stack, simulate);
            }

            ItemStack slot0 = this.getStackInSlot(0);
            ItemStack slot1 = this.getStackInSlot(1);

            if (input1State[currentFace] == 1 && isValidInputForSlot(stack, slot0, slot1, true))
            {
                return super.insertItem(0, stack, simulate);
            }

            if (input2State[currentFace] == 1 && isValidInputForSlot(stack, slot0, slot1, false))
            {
                return super.insertItem(1, stack, simulate);
            }

            if (fuelState[currentFace] == 1 && isItemFuel(stack))
            {
                return super.insertItem(2, stack, simulate);
            }

            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
        {
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

            if (fuelState[currentFace] == 2)
            {
                if (!slot2.isEmpty())
                {
                    return super.extractItem(2, amount, simulate);
                }
            }

            if (outputState[currentFace] == 1)
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

    public void setSlot(String slot)
    {
        this.slot = slot;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
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
        return super.getCapability(capability, facing);
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

        machineFacing = state.getValue(BlockHorizontal.FACING);

        if (facing == null)
        {
            currentFace = -1;
            return;
        }

        if (facing == EnumFacing.UP)
        {
            currentFace = 4;
            return;
        }

        if (facing == EnumFacing.DOWN)
        {
            currentFace = 5;
            return;
        }

        int[][] faceMapping = {
                {0, 1, 3, 2}, // facing == NORTH
                {1, 0, 2, 3}, // facing == SOUTH
                {3, 2, 0, 1}, // facing == EAST
                {2, 3, 1, 0}  // facing == WEST
        };

        int facingIndex = switch (facing)
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
            currentFace = faceMapping[facingIndex][machineFacingIndex];
        }
        else
        {
            currentFace = -1;
        }
    }
    
    public void setRedstoneControlButtonState()
    {
		this.markDirty();
		
		NetworkHandler.sendButtonStateUpdate(this.redstoneControlButtonState, this.pos);
    }

    public void setSlotsState()
    {
        int[][] slotsState = {this.input1State, this.input2State, this.fuelState, this.outputState};

        this.markDirty();

        NetworkHandler.sendSlotsStateUpdate(slotsState, this.pos);
    }
    
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.alloy_furnace");
    }
    
    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
        this.markDirty();
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() 
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, 1, tag);
    }
    
    @Override
    public @NotNull NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.writeToNBT(new NBTTagCompound());
        tag.setTag("Inventory", this.machineItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.burnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("RedstoneControlButtonState", this.redstoneControlButtonState);

        if (this.hasCustomName())
        {
            tag.setString("CustomName", this.customName);
        }

        for (int i = 0; i < input1State.length; i++)
        {
            tag.setInteger("Input1State[" + i + "]", input1State[i]);
        }

        for (int i = 0; i < input2State.length; i++)
        {
            tag.setInteger("Input2State[" + i + "]", input2State[i]);
        }

        for (int i = 0; i < fuelState.length; i++)
        {
            tag.setInteger("FuelState[" + i + "]", fuelState[i]);
        }

        for (int i = 0; i < outputState.length; i++)
        {
            tag.setInteger("OutputState[" + i + "]", outputState[i]);
        }

        return tag;
    }
    
    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setTag("Inventory", this.machineItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.burnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("RedstoneControlButtonState", this.redstoneControlButtonState);

        for (int i = 0; i < input1State.length; i++)
        {
            tag.setInteger("Input1State[" + i + "]", input1State[i]);
        }

        for (int i = 0; i < input2State.length; i++)
        {
            tag.setInteger("Input2State[" + i + "]", input2State[i]);
        }

        for (int i = 0; i < fuelState.length; i++)
        {
            tag.setInteger("FuelState[" + i + "]", fuelState[i]);
        }

        for (int i = 0; i < outputState.length; i++)
        {
            tag.setInteger("OutputState[" + i + "]", outputState[i]);
        }

        if (this.hasCustomName()) 
        {
            tag.setString("CustomName", this.customName);
        }

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.machineItemStacks.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.burnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("TotalCookTime");
        this.currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
        this.redstoneControlButtonState = compound.getInteger("RedstoneControlButtonState");
        
        if (compound.hasKey("CustomName", 8)) 
        {
            this.setCustomName(compound.getString("CustomName"));
        }

        for (int i = 0; i < input1State.length; i++)
        {
            this.input1State[i] = compound.getInteger("Input1State[" + i + "]");
        }

        for (int i = 0; i < input2State.length; i++)
        {
            this.input2State[i] = compound.getInteger("Input2State[" + i + "]");
        }

        for (int i = 0; i < fuelState.length; i++)
        {
            this.fuelState[i] = compound.getInteger("FuelState[" + i + "]");
        }

        for (int i = 0; i < outputState.length; i++)
        {
            this.outputState[i] = compound.getInteger("OutputState[" + i + "]");
        }

        this.markDirty();
    }
    
    public boolean isActive()
    {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isActive(TileEntityAlloyFurnace tileentity)
    {
        return tileentity.burnTime > 0;
    }

    @Override
    public void update()
    {
        boolean wasActive = this.isActive();
        boolean stateChanged = false;

        if (this.burnTime > 0)
        {
            --this.burnTime;
            this.markDirty();
        }

        if (!this.world.isRemote)
        {
            boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
            boolean canOperate = this.canOperateBasedOnRedstone(hasRedstoneSignal);

            ItemStack input1 = machineItemStacks.getStackInSlot(0);
            ItemStack input2 = machineItemStacks.getStackInSlot(1);
            ItemStack fuel = machineItemStacks.getStackInSlot(2);
            ItemStack output = machineItemStacks.getStackInSlot(3);

            if (this.shouldSlowDownCooking(canOperate, output))
            {
                this.decreaseCookTime();
            }

            if (canOperate && this.canStartBurning(fuel, input1, input2))
            {
                this.startBurning(fuel);
                stateChanged = true;
            }

            if (this.isActive() && this.canSmelt())
            {
                this.processCooking(input1, input2);
                stateChanged = true;
            }
            else
            {
                this.decreaseCookTime();
            }

            if (wasActive != this.isActive())
            {
                MachineAlloyFurnace.setStateActive(this.isActive(), world, pos);
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

    private boolean shouldSlowDownCooking(boolean canOperate, ItemStack output)
    {
        return (!canOperate && this.cookTime > 0) || (canOperate && this.cookTime > 0 && output.getCount() == output.getMaxStackSize());
    }

    private void decreaseCookTime()
    {
        this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
        this.markDirty();
    }

    private boolean canStartBurning(ItemStack fuel, ItemStack input1, ItemStack input2)
    {
        return !this.isActive() && this.canSmelt() && !fuel.isEmpty() && (!input1.isEmpty() || !input2.isEmpty());
    }

    private void startBurning(ItemStack fuel)
    {
        this.burnTime = getItemBurnTime(fuel);
        this.currentItemBurnTime = this.burnTime;

        if (this.isActive() && !fuel.isEmpty())
        {
            Item item = fuel.getItem();
            fuel.shrink(1);

            if (fuel.isEmpty())
            {
                ItemStack containerItem = item.getContainerItem(fuel);
                machineItemStacks.setStackInSlot(2, containerItem);
            }
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

    public static int getItemBurnTime(ItemStack fuel)
    {
        return TileEntityFurnace.getItemBurnTime(fuel);
    }

    public static boolean isItemFuel(ItemStack fuel)
    {
        return getItemBurnTime(fuel) > 0;
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

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }

        this.markDirty();
    }
}