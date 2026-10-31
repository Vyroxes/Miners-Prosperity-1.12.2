package net.vyroxes.minersprosperity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.vyroxes.minersprosperity.block.machine.BlockMachineFluidTank;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import net.vyroxes.minersprosperity.machine.FluidTankTier;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TileEntityFluidTank extends TileEntityMachine
{
    private FluidTankTier tier;
    private boolean fluidChanged;
    private long transferTick = Long.MIN_VALUE;
    private int filledThisTick;
    private int drainedThisTick;

    public TileEntityFluidTank()
    {
        this(FluidTankTier.BASIC);
    }

    public TileEntityFluidTank(FluidTankTier tier)
    {
        super(new Builder().setItemInput(2).setItemOutput(2)
                .addFluidTank(null, tier.capacity, 0, true, true, SlotState.SlotType.INPUT, SlotState.SlotMode.INPUT_OUTPUT)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder()));
        this.tier = tier;
        this.fluidTanks[0] = new CustomFluidTank(null, 0, tier.capacity, true, true)
        {
            @Override
            public int fill(FluidStack resource, boolean doFill)
            {
                if (resource == null || resource.amount <= 0) return 0;
                resetTransferBudget();
                FluidStack request = resource.copy();
                request.amount = Math.min(request.amount, getTier().transfer - filledThisTick);
                if (request.amount <= 0) return 0;
                if (getTier() == FluidTankTier.CREATIVE)
                {
                    if (this.getFluid() != null && !this.getFluid().isFluidEqual(request)) return 0;
                    if (doFill && this.getFluid() == null)
                    {
                        FluidStack selected = request.copy();
                        selected.amount = this.getCapacity();
                        this.setFluid(selected);
                        this.onContentsChanged();
                    }
                    return request.amount;
                }
                int filled = super.fill(request, doFill);
                if (doFill) filledThisTick += filled;
                return filled;
            }

            @Override
            public FluidStack drainInternal(int maxDrain, boolean doDrain)
            {
                resetTransferBudget();
                int amount = Math.min(maxDrain, getTier().transfer - drainedThisTick);
                if (amount <= 0) return null;
                FluidStack drained = super.drainInternal(amount, doDrain && getTier() != FluidTankTier.CREATIVE);
                if (doDrain && drained != null && getTier() != FluidTankTier.CREATIVE) drainedThisTick += drained.amount;
                return drained;
            }

            @Override
            protected void onContentsChanged()
            {
                TileEntityFluidTank.this.fluidChanged = true;
                TileEntityFluidTank.this.markDirty();
            }
        };
    }

    private void resetTransferBudget()
    {
        long tick = this.world == null ? 0 : this.world.getTotalWorldTime();
        if (this.transferTick == tick) return;
        this.transferTick = tick;
        this.filledThisTick = this.drainedThisTick = 0;
    }

    public FluidTankTier getTier() { return this.tier; }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation(this.tier.translationKey);
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        Block block = this.getBlockType();
        if (block instanceof BlockMachineFluidTank) this.tier = ((BlockMachineFluidTank) block).getTier();
        this.applyTierCapacity();
    }

    private void applyTierCapacity()
    {
        CustomFluidTank tank = this.getFluidTank(0);
        tank.setCapacity(this.tier.capacity);
        if (tank.getFluid() != null)
        {
            tank.getFluid().amount = this.tier == FluidTankTier.CREATIVE ? this.tier.capacity : Math.min(tank.getFluidAmount(), this.tier.capacity);
        }
        this.fluidChanged = true;
    }

    public boolean isContainerValid(int input, ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        ItemStack single = stack.copy();
        single.setCount(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(single);
        if (handler == null) return false;
        if (input == 0)
        {
            FluidStack fluid = handler.drain(Integer.MAX_VALUE, false);
            return fluid != null && fluid.amount > 0;
        }
        if (input != 1) return false;
        FluidStack fluid = Objects.requireNonNull(this.getFluidTank(0)).getFluid();
        if (fluid != null) return handler.fill(fluid.copy(), false) > 0;
        for (net.minecraftforge.fluids.capability.IFluidTankProperties properties : handler.getTankProperties())
        {
            FluidStack contained = properties.getContents();
            if (properties.canFill() && (contained == null || contained.amount < properties.getCapacity())) return true;
        }
        return false;
    }

    private void processContainer(int inputIndex, boolean emptyContainer)
    {
        ItemStack input = this.getInputStack(inputIndex);
        if (input.isEmpty()) return;
        CustomFluidTank tank = this.getFluidTank(0);
        FluidActionResult preview = emptyContainer
                ? FluidUtil.tryEmptyContainer(input, tank, Integer.MAX_VALUE, null, false)
                : FluidUtil.tryFillContainer(input, tank, Integer.MAX_VALUE, null, false);
        if (!preview.isSuccess()) return;

        ItemStack result = preview.getResult();
        IFluidHandlerItem remaining = FluidUtil.getFluidHandler(result);
        boolean partial = emptyContainer ? FluidUtil.getFluidContained(result) != null
                : remaining != null && Objects.requireNonNull(tank).getFluid() != null && remaining.fill(tank.getFluid().copy(), false) > 0;
        if (partial && input.getCount() != 1) return;
        ItemStack output = this.getOutputStack(inputIndex);
        if (!partial && !result.isEmpty() && !output.isEmpty()
                && (!ItemStack.areItemsEqual(output, result) || !ItemStack.areItemStackTagsEqual(output, result)
                || output.getCount() + result.getCount() > Math.min(output.getMaxStackSize(), this.customItemStackHandler.getSlotLimit(this.getOutputSlotId(inputIndex))))) return;

        FluidActionResult action = emptyContainer
                ? FluidUtil.tryEmptyContainer(input, tank, Integer.MAX_VALUE, null, true)
                : FluidUtil.tryFillContainer(input, tank, Integer.MAX_VALUE, null, true);
        if (!action.isSuccess()) return;
        if (partial) this.setInputStack(inputIndex, action.getResult());
        else
        {
            input.shrink(1);
            if (input.isEmpty()) this.clearInputStack(inputIndex);
            if (output.isEmpty()) this.setOutputStack(inputIndex, action.getResult());
            else output.grow(action.getResult().getCount());
        }
        this.markDirty();
    }

    @Override
    public void update()
    {
        if (this.world == null || this.world.isRemote) return;
        if (this.canMachineOperate())
        {
            this.runAutomaticIO();
            this.processContainer(0, true);
            this.processContainer(1, false);
        }
        if (this.fluidChanged)
        {
            this.fluidChanged = false;
            IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 2);
            this.world.updateComparatorOutputLevel(this.pos, state.getBlock());
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setString("TankTier", this.tier.name());
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        if (tag.hasKey("TankTier", 8))
        {
            try { this.tier = FluidTankTier.valueOf(tag.getString("TankTier")); }
            catch (IllegalArgumentException ignored) { this.tier = FluidTankTier.BASIC; }
        }
        super.readFromNBT(tag);
        this.applyTierCapacity();
    }
}