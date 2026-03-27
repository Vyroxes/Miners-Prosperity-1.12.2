package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.objects.blocks.machines.Machine;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;
import net.vyroxes.minersprosperity.util.handlers.SlotState;
import org.jetbrains.annotations.NotNull;

public class TileEntityElectricFurnace extends TileEntityMachine
{
    public TileEntityElectricFurnace()
    {
        super(new Builder()
                .setItemInput(1)
                .setItemEnergy(1)
                .setItemOutput(1)
                .setItemUpgrade(4)
                .setEnergy(20000, 200, 0, 0)
                .setFluid(getConfiguredXpFluid().getFluid(), Integer.MAX_VALUE, 0, true, false)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder()
                        .setInputs(1)
                        .setEnergySlots(1)
                        .setOutputs(1)
                        .setUpgradeSlots(4)));
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.electric_furnace");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);

        if (this.tank != null) this.tank.writeToNBT(tag);

        tag.setTag("States", createStatesTag());

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("CookTime", 3))
        {
            this.cookTime = tag.getInteger("CookTime");
        }

        if (tag.hasKey("TotalCookTime", 3))
        {
            this.totalCookTime = tag.getInteger("TotalCookTime");
        }

        if (this.tank != null) this.tank.readFromNBT(tag);

        if (tag.hasKey("States"))
        {
            NBTTagCompound statesTag = tag.getCompoundTag("States");

            this.redstoneControlButtonState = statesTag.getInteger("RedstoneControlButtonState");

            if (this.sidedIngredientHandlers != null)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    if (statesTag.hasKey(facing.toString() + "SlotStates"))
                    {
                        SidedIngredientHandler sidedHandler = this.sidedIngredientHandlers[facing.ordinal()];

                        sidedHandler.readFromNBT(statesTag);
                    }
                }
            }
        }

        this.markDirty();
    }

    public boolean isPowered()
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

    @Override
    public void update()
    {
        boolean wasPowered = Machine.getStatePowered(this.world, this.pos);
        boolean stateChanged = false;

        if (!this.world.isRemote)
        {
            for (EnumFacing facing : EnumFacing.values())
            {
                SidedIngredientHandler sidedHandler = (SidedIngredientHandler) this.getSidedIngredientHandler(facing);
                sidedHandler.autoInput();
                sidedHandler.autoOutput();
                sidedHandler.autoDrain();
            }

            int upgradesCount = this.getCustomItemStackHandler().getStackInSlot(5).getCount();
            if (this.storage.getEnergyStored() != this.storage.setEnergyUpgraded(upgradesCount, true))
            {
                this.storage.setEnergyUpgraded(upgradesCount, false);
            }

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

            ItemStack energy = customItemStackHandler.getStackInSlot(1);

            if (!energy.isEmpty() && this.storage.getEnergyStored() != this.storage.getMaxEnergyStored())
            {
                setEnergyStoredFromItem(energy);
                stateChanged = true;
            }

            if (this.cookTime > 0 && this.canSmelt() && canOperate)
            {
                this.storage.useEnergy(this.energyUsage, false);
                stateChanged = true;
            }
            else if (this.cookTime > 0 && !this.canSmelt() || this.cookTime > 0 && !canOperate)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                this.energyUsage = 0;
                stateChanged = true;
            }

            if (this.canSmelt() && canOperate)
            {
                this.processCooking();
                stateChanged = true;
            }

            if (wasPowered != this.isPowered())
            {
                Machine.setStatePowered(this.isPowered(), world, pos);
                stateChanged = true;
            }
        }

        if (stateChanged)
        {
            this.markDirty();
        }
    }

    private int calculateTotalCookTime()
    {
        int speedUpgradesCount = this.getCustomItemStackHandler().getStackInSlot(4).getCount();
        int baseTotalCookTime = ModConfig.MACHINES.electricFurnaceBaseCookTime;
        int totalCookTimeModifier = (baseTotalCookTime - baseTotalCookTime/25)/8;

        return baseTotalCookTime - totalCookTimeModifier * speedUpgradesCount;
    }

    private int calculateInputsCount()
    {
        ItemStack input1 = customItemStackHandler.getStackInSlot(0);
        ItemStack output = customItemStackHandler.getStackInSlot(2);

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input1);
        if (!result.isEmpty())
        {
            int requiredQuantity1 = 1;

            int machineUpgradesCount;
            if (this.getCustomItemStackHandler().getStackInSlot(3).getCount() == 8 ) machineUpgradesCount = 10;
            else machineUpgradesCount = this.getCustomItemStackHandler().getStackInSlot(3).getCount() + 1;

            int maxSetsByInput1;
            maxSetsByInput1 = input1.getCount() / requiredQuantity1;

            if (machineUpgradesCount == 1 && maxSetsByInput1 > 0)
            {
                return 1;
            }
            else
            {
                maxSetsByInput1 = Math.min(maxSetsByInput1, machineUpgradesCount);

                SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) this.getSidedIngredientHandler(EnumFacing.UP);

                if (!output.isEmpty() && output.isItemEqual(result) && sidedIngredientHandler.getSlotOutputMode(2) == SlotState.SlotOutputMode.DEFAULT)
                {
                    int count = ((output.getMaxStackSize() - output.getCount()) / result.getCount());
                    if (count < maxSetsByInput1) maxSetsByInput1 = count;
                }

                return maxSetsByInput1;
            }
        }

        return 0;
    }

    private int calculateEnergyUsage()
    {
        int machineUpgradesCount = this.calculateInputsCount();
        int speedUpgradesCount = this.getCustomItemStackHandler().getStackInSlot(4).getCount();
        int energyUpgradesCount = this.getCustomItemStackHandler().getStackInSlot(5).getCount();
        int baseTotalCookTime = ModConfig.MACHINES.electricFurnaceBaseCookTime;
        int totalCookTimeModifier = (baseTotalCookTime - baseTotalCookTime/25)/8;
        int totalCookTime = baseTotalCookTime - totalCookTimeModifier * speedUpgradesCount;
        if (totalCookTime > 0)
        {
            int baseEnergyUsage = ModConfig.MACHINES.electricFurnaceBaseEnergyUsage;
            float energyUsageModifier = (float) (baseEnergyUsage * baseTotalCookTime * 5 - baseEnergyUsage * baseTotalCookTime)/8;
            float energyUsage = (energyUsageModifier * speedUpgradesCount + baseEnergyUsage * baseTotalCookTime)/totalCookTime;
            float energyModifier = (energyUsage - energyUsage/10)/8;
            float machineUpgradesModifier = 1;
            if (machineUpgradesCount > 1) machineUpgradesModifier += (float) (machineUpgradesCount) * 2/10;

            return (int) ((energyUsage - energyModifier * energyUpgradesCount) * machineUpgradesModifier);
        }

        return 0;
    }

    private void processCooking()
    {
        this.totalCookTime = this.calculateTotalCookTime();
        this.energyUsage = this.calculateEnergyUsage();

        ++this.cookTime;

        if (this.cookTime >= this.totalCookTime)
        {
            this.smeltItem();
            this.cookTime = 0;
            if (!this.canSmelt()) this.energyUsage = 0;
        }

        this.markDirty();
    }

    private boolean canSmelt()
    {
        ItemStack input1 = customItemStackHandler.getStackInSlot(0);

        if (this.storage.getEnergyStored() >= this.calculateEnergyUsage() * (this.calculateTotalCookTime() - this.cookTime))
        {
            if (input1.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input1);

                if (result.isEmpty())
                {
                    return false;
                }
                else
                {
                    ItemStack output = customItemStackHandler.getStackInSlot(2);

                    SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) this.getSidedIngredientHandler(EnumFacing.UP);

                    int upgradesCount = this.getCustomItemStackHandler().getStackInSlot(6).getCount();
                    float experienceModifier = 1 + (float) upgradesCount/10;
                    int experience = (int) (experienceModifier * FurnaceRecipes.instance().getSmeltingExperience(result) * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

                    if (this.tank.getFluidAmount() + experience > this.tank.getCapacity() && sidedIngredientHandler.getSlotOutputMode(sidedIngredientHandler.getSlots()) == SlotState.SlotOutputMode.DEFAULT) return false;

                    if (output.isEmpty() || sidedIngredientHandler.getSlotOutputMode(2) != SlotState.SlotOutputMode.DEFAULT) return true;

                    if (!output.isItemEqual(result)) return false;

                    int quantity = this.calculateInputsCount();

                    if (quantity > 0)
                    {
                        int res = output.getCount() + result.getCount() * quantity;
                        return res <= 64 && res <= output.getMaxStackSize();
                    }
                }
            }
        }
        return false;
    }

    private void smeltItem()
    {
        ItemStack input1 = customItemStackHandler.getStackInSlot(0);

        if (this.canSmelt())
        {
            int upgradesCount = this.getCustomItemStackHandler().getStackInSlot(6).getCount();
            float experienceModifier = 1 + (float) upgradesCount/10;
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input1);
            ItemStack output = customItemStackHandler.getStackInSlot(2);
            int experience = (int) (experienceModifier * FurnaceRecipes.instance().getSmeltingExperience(result) * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

            if (!result.isEmpty())
            {
                int quantity = this.calculateInputsCount();
                int requiredQuantity1 = 1;

                SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) this.getSidedIngredientHandler(EnumFacing.UP);

                if (quantity > 0)
                {
                    if (sidedIngredientHandler.getSlotOutputMode(2) != SlotState.SlotOutputMode.VOID_ALL)
                    {
                        if (output.isEmpty())
                        {
                            int count = result.getCount() * quantity;
                            ItemStack resultCopy = result.copy();
                            resultCopy.setCount(count);
                            customItemStackHandler.setStackInSlot(2, resultCopy);
                            this.markDirty();
                        }
                        else if (output.isItemEqual(result) && output.getCount() + result.getCount() * quantity <= result.getMaxStackSize())
                        {
                            output.grow(result.getCount() * quantity);
                            this.markDirty();
                        }
                        else if (output.isItemEqual(result) && output.getCount() + result.getCount() * quantity > result.getMaxStackSize())
                        {
                            output.grow(result.getMaxStackSize() - output.getCount());
                            this.markDirty();
                        }

                        if (output.getCount() > this.customItemStackHandler.getStackInSlot(2).getMaxStackSize())
                            output.setCount(this.customItemStackHandler.getStackInSlot(2).getMaxStackSize());
                    }

                    input1.shrink(quantity * requiredQuantity1);
                }

                if (input1.isEmpty())
                    customItemStackHandler.setStackInSlot(0, ItemStack.EMPTY);

                if (sidedIngredientHandler.getSlotOutputMode(sidedIngredientHandler.getSlots()) != SlotState.SlotOutputMode.VOID_ALL)
                    this.tank.fillInternal(new FluidStack(getConfiguredXpFluid(), experience), true);

                this.markDirty();
            }
        }
    }
}