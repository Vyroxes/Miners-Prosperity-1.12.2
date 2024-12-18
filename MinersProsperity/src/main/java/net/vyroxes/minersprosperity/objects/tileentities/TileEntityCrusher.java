package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
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
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.blocks.machines.Crusher;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesCrusher;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TileEntityCrusher extends TileEntity implements ITickable
{
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public int crusherBurnTime;
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
    public int currentGuiId = GuiHandler.GUI_CRUSHER;
    public EnumFacing facing;
    public int currentFace;
    public EnumFacing crusherFacing;
    //public int[] crusherVariables = new int[4];

    private final ItemStackHandler crusherItemStacks = new ItemStackHandler(4)
    {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
//            System.out.println("Facing: " + facing);
//            System.out.println("Current Face: " + currentFace);
//            System.out.println("Input 1: " + Arrays.toString(input1State));
//            System.out.println("Input 2: " + Arrays.toString(input2State));
//            System.out.println("Fuel: " + Arrays.toString(fuelState));
//            System.out.println("Output: " + Arrays.toString(outputState));

            ItemStack slot0 = this.getStackInSlot(0);
            ItemStack slot1 = this.getStackInSlot(1);

            if (facing != null)
            {
                if (currentFace == 0)
                {
                    if (input1State[0] == 1)
                    {
                        if (RecipesCrusher.getInstance().isInputInAnyRecipe(stack))
                        {
                            System.out.println("Slot0: " + slot0.getDisplayName());
                            System.out.println("Stack: " + stack.getDisplayName());
                            if (slot0.isEmpty() && slot1.isEmpty())
                            {
                                return super.insertItem(0, stack, simulate);
                            }
                            else if (slot0.isEmpty() && !slot1.isEmpty())
                            {
                                if (!RecipesCrusher.getInstance().getCrusherResult(stack, slot1).isEmpty() || !RecipesCrusher.getInstance().getCrusherResult(slot1, stack).isEmpty())
                                {
                                    return super.insertItem(0, stack, simulate);
                                }
                            }
                            else if (!slot0.isEmpty() && slot0.getItem().equals(stack.getItem()))
                            {
                                return super.insertItem(0, stack, simulate);
                            }
                        }
                    }

                    if (input2State[0] == 1)
                    {
                        if (RecipesCrusher.getInstance().isInputInAnyRecipe(stack))
                        {
                            if (slot0.isEmpty() && slot1.isEmpty())
                            {
                                return super.insertItem(1, stack, simulate);
                            }
                            else if (!slot0.isEmpty() && slot1.isEmpty())
                            {
                                if (!RecipesCrusher.getInstance().getCrusherResult(stack, slot0).isEmpty() || !RecipesCrusher.getInstance().getCrusherResult(slot0, stack).isEmpty()) {
                                    return super.insertItem(1, stack, simulate);
                                }
                            }
                            else if (!slot1.isEmpty() && slot1.getItem().equals(stack.getItem()))
                            {
                                return super.insertItem(1, stack, simulate);
                            }
                        }
                    }

                    if (fuelState[0] == 1)
                    {
                        if (isItemFuel(stack))
                        {
                            return super.insertItem(2, stack, simulate);
                        }
                    }
                }
                else if (currentFace == 1)
                {
                    if (input1State[1] == 1)
                    {
                        return super.insertItem(0, stack, false);
                    }

                    if (input2State[1] == 1)
                    {
                        return super.insertItem(1, stack, false);
                    }

                    if (fuelState[1] == 1)
                    {
                        return super.insertItem(2, stack, false);
                    }
                }
                if (currentFace == 2)
                {
                    if (input1State[2] == 1)
                    {
                        return super.insertItem(0, stack, simulate);
                    }

                    if (input2State[2] == 1)
                    {
                        return super.insertItem(1, stack, simulate);
                    }

                    if (fuelState[2] == 1)
                    {
                        return super.insertItem(2, stack, simulate);
                    }
                }
                else if (currentFace == 3)
                {
                    if (input1State[3] == 1)
                    {
                        return super.insertItem(0, stack, simulate);
                    }

                    if (input2State[3] == 1)
                    {
                        return super.insertItem(1, stack, simulate);
                    }

                    if (fuelState[3] == 1)
                    {
                        return super.insertItem(2, stack, simulate);
                    }
                }
                if (currentFace == 4)
                {
                    if (input1State[4] == 1)
                    {
                        return super.insertItem(0, stack, simulate);
                    }

                    if (input2State[4] == 1)
                    {
                        return super.insertItem(1, stack, simulate);
                    }

                    if (fuelState[4] == 1)
                    {
                        return super.insertItem(2, stack, simulate);
                    }
                }
                else if (currentFace == 5)
                {
                    if (input1State[5] == 1)
                    {
                        return super.insertItem(0, stack, simulate);
                    }

                    if (input2State[5] == 1)
                    {
                        return super.insertItem(1, stack, simulate);
                    }

                    if (fuelState[5] == 1)
                    {
                        return super.insertItem(2, stack, simulate);
                    }
                }
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return super.extractItem(slot, amount, simulate);
        }
    };

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
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.crusherItemStacks);
        }
        return super.getCapability(capability, facing);
    }

    public void currentFace(EnumFacing facing)
    {
        if (this.hasWorld())
        {
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof Crusher)
            {
                crusherFacing = state.getValue(BlockHorizontal.FACING);
            }

            if (facing == null)
            {
                currentFace = -1;
            }
            else if (facing == EnumFacing.NORTH)
            {
                if (crusherFacing == EnumFacing.NORTH)
                {
                    currentFace = 0;
                }
                else if (crusherFacing == EnumFacing.SOUTH)
                {
                    currentFace = 1;
                }
                else if (crusherFacing == EnumFacing.EAST)
                {
                    currentFace = 2;
                }
                else if (crusherFacing == EnumFacing.WEST)
                {
                    currentFace = 3;
                }
            }
            else if (facing == EnumFacing.SOUTH)
            {
                if (crusherFacing == EnumFacing.NORTH)
                {
                    currentFace = 1;
                }
                else if (crusherFacing == EnumFacing.SOUTH)
                {
                    currentFace = 0;
                }
                else if (crusherFacing == EnumFacing.EAST)
                {
                    currentFace = 3;
                }
                else if (crusherFacing == EnumFacing.WEST)
                {
                    currentFace = 2;
                }
            }
            else if (facing == EnumFacing.EAST)
            {
                if (crusherFacing == EnumFacing.NORTH)
                {
                    currentFace = 3;
                }
                else if (crusherFacing == EnumFacing.SOUTH)
                {
                    currentFace = 2;
                }
                else if (crusherFacing == EnumFacing.EAST)
                {
                    currentFace = 0;
                }
                else if (crusherFacing == EnumFacing.WEST)
                {
                    currentFace = 1;
                }
            }
            else if (facing == EnumFacing.WEST)
            {
                if (crusherFacing == EnumFacing.NORTH)
                {
                    currentFace = 2;
                }
                else if (crusherFacing == EnumFacing.SOUTH)
                {
                    currentFace = 3;
                }
                else if (crusherFacing == EnumFacing.EAST)
                {
                    currentFace = 1;
                }
                else if (crusherFacing == EnumFacing.WEST)
                {
                    currentFace = 0;
                }
            }
            else if (facing == EnumFacing.UP)
            {
                currentFace = 4;
            }
            else if (facing == EnumFacing.DOWN)
            {
                currentFace = 5;
            }
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
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.crusher");
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
        tag.setTag("Inventory", this.crusherItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.crusherBurnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("ButtonState", this.redstoneControlButtonState);
        tag.setInteger("CurrentGuiId", this.currentGuiId);

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
        tag.setTag("Inventory", this.crusherItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.crusherBurnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("ButtonState", this.redstoneControlButtonState);
        tag.setInteger("CurrentGuiId", this.currentGuiId);

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
        this.crusherItemStacks.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.crusherBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("TotalCookTime");
        this.currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
        this.redstoneControlButtonState = compound.getInteger("ButtonState");
        this.currentGuiId = compound.getInteger("CurrentGuiId");
        
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
        return this.crusherBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isActive(TileEntityCrusher tileentity)
    {
        return tileentity.crusherBurnTime > 0;
    }

    public static int getCookTime(ItemStack input1, ItemStack input2)
    {

        return RecipesCrusher.getInstance().getCookTime(input1, input2);
    }

    @Override
    public void update() {
        System.out.println("CookTime: " + this.cookTime);

        boolean wasActive = this.isActive();
        boolean stateChanged = false;

        if (this.crusherBurnTime > 0) {
            --this.crusherBurnTime;
            this.markDirty();
        }

        if (!this.world.isRemote) {
            boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
            boolean canOperate = this.canOperateBasedOnRedstone(hasRedstoneSignal);

            ItemStack input1 = crusherItemStacks.getStackInSlot(0);
            ItemStack input2 = crusherItemStacks.getStackInSlot(1);
            ItemStack fuel = crusherItemStacks.getStackInSlot(2);
            ItemStack output = crusherItemStacks.getStackInSlot(3);

            if (this.shouldSlowDownCooking(canOperate, output)) {
                this.decreaseCookTime();
            }

            if (canOperate && this.canStartBurning(fuel, input1, input2)) {
                this.startBurning(fuel);
                stateChanged = true;
            }

            if (this.isActive() && this.canSmelt()) {
                this.processCooking(input1, input2);
                stateChanged = true;
            } else {
                this.decreaseCookTime();
            }

            if (wasActive != this.isActive()) {
                Crusher.setStateActive(this.isActive(), world, pos);
                stateChanged = true;
            }
        }

        if (stateChanged) {
            this.markDirty();
        }
    }

    private boolean canOperateBasedOnRedstone(boolean hasRedstoneSignal) {
        return switch (this.redstoneControlButtonState) {
            case 0 -> true;
            case 1 -> !hasRedstoneSignal;
            case 2 -> hasRedstoneSignal;
            default -> false;
        };
    }

    private boolean shouldSlowDownCooking(boolean canOperate, ItemStack output) {
        return (!canOperate && this.cookTime > 0) || (canOperate && this.cookTime > 0 && output.getCount() == output.getMaxStackSize());
    }

    private void decreaseCookTime() {
        this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
        this.markDirty();
    }

    private boolean canStartBurning(ItemStack fuel, ItemStack input1, ItemStack input2) {
        return !this.isActive() && this.canSmelt() && !fuel.isEmpty() && (!input1.isEmpty() || !input2.isEmpty());
    }

    private void startBurning(ItemStack fuel) {
        this.crusherBurnTime = getItemBurnTime(fuel);
        this.currentItemBurnTime = this.crusherBurnTime;

        if (this.isActive() && !fuel.isEmpty()) {
            Item item = fuel.getItem();
            fuel.shrink(1);

            if (fuel.isEmpty()) {
                ItemStack containerItem = item.getContainerItem(fuel);
                crusherItemStacks.setStackInSlot(2, containerItem);
            }
        }
    }

    private void processCooking(ItemStack input1, ItemStack input2) {
        if (this.cookTime == 0) {
            this.totalCookTime = RecipesCrusher.getInstance().getCookTime(input1, input2);
        }

        ++this.cookTime;

        if (this.cookTime >= this.totalCookTime) {
            this.cookTime = 0;
            this.totalCookTime = RecipesCrusher.getInstance().getCookTime(input1, input2);
            this.smeltItem();
        }

        this.markDirty();
    }

    private boolean canSmelt() {
        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
        ItemStack input2 = crusherItemStacks.getStackInSlot(1);

        if (input1.isEmpty() || input2.isEmpty()) {
            return false;
        } else {
            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);

            if (result.isEmpty()) {
                result = RecipesCrusher.getInstance().getCrusherResult(input2, input1);
            }

            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output = crusherItemStacks.getStackInSlot(3);

                if (output.isEmpty()) {
                    return true;
                }
                if (!output.isItemEqual(result)) {
                    return false;
                }

                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
    }

    private void smeltItem() {
        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
        ItemStack input2 = crusherItemStacks.getStackInSlot(1);

        if (this.canSmelt()) {
            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);
            ItemStack output = crusherItemStacks.getStackInSlot(3);

            if (output.isEmpty()) {
                crusherItemStacks.setStackInSlot(3, result.copy());
                this.markDirty();
            } else if (output.isItemEqual(result)) {
                output.grow(result.getCount());
                this.markDirty();
            }

            input1.shrink(1);
            input2.shrink(1);

            if (input1.isEmpty()) crusherItemStacks.setStackInSlot(0, ItemStack.EMPTY);
            if (input2.isEmpty()) crusherItemStacks.setStackInSlot(1, ItemStack.EMPTY);

            this.markDirty();
        }
    }

//    @Override
//    public void update()
//    {
////        System.out.println("Input 1: " + Arrays.toString(this.input1State));
////        System.out.println("Input 2: " + Arrays.toString(this.input2State));
////        System.out.println("Fuel: " + Arrays.toString(this.fuelState));
////        System.out.println("Output: " + Arrays.toString(this.outputState));
//        System.out.println("CookTime: " + this.cookTime);
//        System.out.println("Input 1: " + this.crusherItemStacks.getStackInSlot(0).getDisplayName());
//
//        boolean flag = this.isActive();
//        boolean flag1 = false;
//
//        if (this.crusherBurnTime > 0)
//        {
//            if (!Crusher.getStateActive(world, pos))
//            {
//            	Crusher.setStateActive(true, world, pos);
//            }
//        	--this.crusherBurnTime;
//        	this.markDirty();
//        }
//
//        if (!this.world.isRemote)
//        {
//        	boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
//            boolean canBurn = switch (this.redstoneControlButtonState)
//            {
//                case 0 -> true;
//                case 1 -> !hasRedstoneSignal;
//                case 2 -> hasRedstoneSignal;
//                default -> false;
//            };
//
//            ItemStack input1 = crusherItemStacks.getStackInSlot(0);
//            ItemStack input2 = crusherItemStacks.getStackInSlot(1);
//            ItemStack fuel = crusherItemStacks.getStackInSlot(2);
//            ItemStack output = crusherItemStacks.getStackInSlot(3);
//
//            if (!canBurn && this.cookTime > 0 || canBurn && this.cookTime > 0 && output.getCount() == output.getMaxStackSize())
//            {
//            	this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
//                this.markDirty();
//            }
//
//            if (canBurn && (this.isActive() || (!fuel.isEmpty() && (!input1.isEmpty() || !input2.isEmpty()))))
//            {
//                if (!this.isActive() && this.canSmelt())
//                {
//                    this.crusherBurnTime = getItemBurnTime(fuel);
//                    this.currentItemBurnTime = crusherBurnTime;
//
//                    if (this.isActive())
//                    {
//                        flag1 = true;
//
//                        if (!fuel.isEmpty())
//                        {
//                            Item item = fuel.getItem();
//                            fuel.shrink(1);
//
//                            if (fuel.isEmpty())
//                            {
//                                ItemStack item1 = item.getContainerItem(fuel);
//                                crusherItemStacks.setStackInSlot(2, item1);
//                            }
//                        }
//                    }
//                }
//
//                if (this.isActive() && this.canSmelt())
//                {
//                    if (cookTime == 0)
//                    {
//                        totalCookTime = RecipesCrusher.getInstance().getCookTime(input1, input2);
//                    }
//
//                    ++this.cookTime;
//
//                    if (this.cookTime >= this.totalCookTime)
//                    {
//                        this.cookTime = 0;
//                        this.totalCookTime = TileEntityCrusher.getCookTime(input1, input2);
//                        this.smeltItem();
//                        flag1 = true;
//                    }
//                }
//                else
//                {
//                    //this.cookTime = 0;
//                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
//                }
//                this.markDirty();
//            }
//            else if (!this.isActive() && this.cookTime > 0)
//            {
//                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
//                this.markDirty();
//            }
//
//            if (flag != this.isActive())
//            {
//                flag1 = true;
//                Crusher.setStateActive(this.isActive(), world, pos);
//                this.markDirty();
//            }
//        }
//
//        if (flag1)
//        {
//            this.markDirty();
//        }
//    }
//
//    private boolean canSmelt()
//    {
//        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
//        ItemStack input2 = crusherItemStacks.getStackInSlot(1);
//
//        if (input1.isEmpty() || input2.isEmpty())
//        {
//            return false;
//        }
//        else
//        {
//            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);
//
//            if (result.isEmpty())
//            {
//                result = RecipesCrusher.getInstance().getCrusherResult(input2, input1);
//            }
//
//            if (result.isEmpty())
//            {
//                return false;
//            }
//            else
//            {
//                ItemStack output = crusherItemStacks.getStackInSlot(3);
//
//                if (output.isEmpty())
//                {
//                    return true;
//                }
//                if (!output.isItemEqual(result))
//                {
//                    return false;
//                }
//
//                int res = output.getCount() + result.getCount();
//                return res <= 64 && res <= output.getMaxStackSize();
//            }
//        }
//    }
//
//    private void smeltItem()
//    {
//        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
//        ItemStack input2 = crusherItemStacks.getStackInSlot(1);
//
//        if (this.canSmelt())
//        {
//            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);
//            ItemStack output = crusherItemStacks.getStackInSlot(3);
//
//            if (output.isEmpty())
//            {
//                crusherItemStacks.setStackInSlot(3, result.copy());
//                this.markDirty();
//            }
//            else if (output.isItemEqual(result))
//            {
//                output.grow(result.getCount());
//                this.markDirty();
//            }
//
//            input1.shrink(1);
//            input2.shrink(1);
//
//            if (input1.isEmpty()) crusherItemStacks.setStackInSlot(0, ItemStack.EMPTY);
//            if (input2.isEmpty()) crusherItemStacks.setStackInSlot(1, ItemStack.EMPTY);
//
//            this.markDirty();
//        }
//    }

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

//    public void setCrusherVariables()
//    {
//        crusherVariables[0] = this.crusherBurnTime;
//        crusherVariables[1] = this.currentItemBurnTime;
//        crusherVariables[2] = this.cookTime;
//        crusherVariables[3] = this.totalCookTime;
//
//        this.markDirty();
//
//        NetworkHandler.sendCrusherVariablesUpdate(crusherVariables, this.pos);
//    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.crusherBurnTime = value;
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

//        crusherVariables[0] = this.crusherBurnTime;
//        crusherVariables[1] = this.currentItemBurnTime;
//        crusherVariables[2] = this.cookTime;
//        crusherVariables[3] = this.totalCookTime;
//
//        this.markDirty();
//
//        NetworkHandler.sendCrusherVariablesUpdate(crusherVariables, this.pos);
    }
}