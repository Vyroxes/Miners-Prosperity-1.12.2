package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;

import java.util.function.Supplier;

public abstract class TileEntityProcessingMachine extends TileEntityMachine
{
    private Object cachedRecipe;
    private ItemStack[] cachedRecipeInputs = new ItemStack[0];
    private boolean recipeCacheInitialized = false;

    protected TileEntityProcessingMachine(Builder builder)
    {
        super(builder);
    }

    protected abstract boolean canProcess();
    protected abstract int calculateTotalCookTime();
    protected abstract int calculateEnergyUsage();
    protected abstract void finishProcess();

    @SuppressWarnings("unchecked")
    protected final <R> R getCachedRecipe(Supplier<R> resolver)
    {
        if (!this.recipeCacheInitialized || this.haveRecipeInputsChanged())
        {
            this.cachedRecipe = resolver.get();
            this.cacheRecipeInputs();
            this.recipeCacheInitialized = true;
        }

        return (R) this.cachedRecipe;
    }

    protected final void invalidateRecipeCache()
    {
        this.cachedRecipe = null;
        this.cachedRecipeInputs = new ItemStack[0];
        this.recipeCacheInitialized = false;
    }

    private boolean haveRecipeInputsChanged()
    {
        int inputCount = this.getInputSlotCount();

        if (this.cachedRecipeInputs.length != inputCount) return true;

        for (int i = 0; i < inputCount; i++) if (!this.areRecipeStacksEqual(this.getInputStack(i), this.cachedRecipeInputs[i])) return true;

        return false;
    }

    private void cacheRecipeInputs()
    {
        int inputCount = this.getInputSlotCount();
        this.cachedRecipeInputs = new ItemStack[inputCount];

        for (int i = 0; i < inputCount; i++)
        {
            ItemStack stack = this.getInputStack(i);
            this.cachedRecipeInputs[i] = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
        }
    }

    private boolean areRecipeStacksEqual(ItemStack first, ItemStack second)
    {
        if (first.isEmpty() && second.isEmpty()) return true;
        if (first.isEmpty() || second.isEmpty()) return false;
        if (first.getCount() != second.getCount()) return false;
        if (!ItemStack.areItemsEqual(first, second)) return false;

        return ItemStack.areItemStackTagsEqual(first, second);
    }

    protected int getProgressRegressionSpeed()
    {
        return 2;
    }

    public boolean isActive()
    {
        if (this.world == null) return this.cookTime > 0;

        boolean canOperate = this.canMachineOperate();

        return (this.canProcess() && canOperate) || this.cookTime > 0;
    }

    @Override
    public void update()
    {
        if (this.world == null) return;

        if (this.world.isRemote)
        {
            MinersProsperity.proxy.updateMachineSound(this);
            this.updateClientProgress();
            return;
        }

        boolean stateChanged = false;
        boolean completedCycle = false;

        this.runAutomaticIO();

        if (this.storage != null)
        {
            this.updateEnergyStorageUpgrade();

            if (this.transferEnergyFromItemSlots()) stateChanged = true;
        }

        boolean canOperate = this.canMachineOperate();
        boolean canProcess = this.canProcess();

        if (this.cookTime > 0 && canProcess && canOperate)
        {
            if (this.storage != null) this.storage.useEnergy(this.energyUsage, false);
            stateChanged = true;
        }
        else if (this.cookTime > 0)
        {
            this.cookTime = MathHelper.clamp(this.cookTime - this.getProgressRegressionSpeed(), 0, this.totalCookTime);
            this.energyUsage = 0;
            stateChanged = true;
        }

        if (canProcess && canOperate)
        {
            completedCycle = this.processCooking();
            stateChanged = true;
        }

        boolean canProcessAfter = this.canProcess();
        boolean active = (canProcessAfter && canOperate) || this.cookTime > 0;

        BlockMachine.MachineState machineState = active ? BlockMachine.MachineState.ACTIVE : this.storage != null && this.storage.getEnergyStored() > 0 ? BlockMachine.MachineState.POWERED : BlockMachine.MachineState.OFF;

        if (BlockMachine.getMachineState(this.world, this.pos) != machineState) BlockMachine.setMachineState(machineState, this.world, this.pos);

        int progressDirection;

        if (canProcessAfter && canOperate) progressDirection = 1;
        else if (this.cookTime > 0) progressDirection = -this.getProgressRegressionSpeed();
        else progressDirection = 0;

        this.updateProgressSync(progressDirection, completedCycle);

        if (stateChanged) this.markDirty();
    }

    private boolean processCooking()
    {
        this.totalCookTime = this.calculateTotalCookTime();
        this.energyUsage = this.calculateEnergyUsage();

        if (this.totalCookTime <= 0)
        {
            this.cookTime = 0;
            return false;
        }

        ++this.cookTime;

        boolean completed = false;

        if (this.cookTime >= this.totalCookTime)
        {
            this.finishProcess();
            this.cookTime = 0;
            completed = true;

            if (!this.canProcess()) this.energyUsage = 0;
        }

        this.markDirty();

        return completed;
    }
}