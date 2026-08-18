package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;

public class TileEntityElectricFurnace extends TileEntityProcessingMachine
{
    public TileEntityElectricFurnace()
    {
        super(new Builder()
                .setItemInput(1)
                .setItemEnergy(1)
                .setItemOutput(1)
                .setItemUpgrade(4)
                .setEnergy(20000, 200, 0, 0)
                .addExperienceTank(Integer.MAX_VALUE)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.INPUT)));
    }

    @Override
    public ITextComponent getDisplayName() { return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.electric_furnace"); }

    @Override
    public SoundEvent getActiveSound() { return ModSounds.ELECTRIC_FURNACE_ACTIVE; }

    @Override
    public float getActiveSoundVolume() { return 0.05F; }

    @Override
    protected int calculateTotalCookTime()
    {
        int speedUpgradesCount = this.getUpgradeCount(MachineUpgradeType.SPEED);
        int baseTotalCookTime = ModConfig.MACHINES.electricFurnaceBaseCookTime;
        int totalCookTimeModifier = (baseTotalCookTime - baseTotalCookTime / 25) / 8;

        return baseTotalCookTime - totalCookTimeModifier * speedUpgradesCount;
    }

    private int calculateInputsCount()
    {
        ItemStack input = this.getInputStack(0);
        ItemStack output = this.getOutputStack(0);

        if (input.isEmpty()) return 0;

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        if (result.isEmpty()) return 0;

        int upgrades = this.getUpgradeCount(MachineUpgradeType.MACHINE);
        int machineUpgradesCount = upgrades == 8 ? 10 : upgrades + 1;

        int maxSets = input.getCount();

        if (machineUpgradesCount == 1 && maxSets > 0) return 1;

        maxSets = Math.min(maxSets, machineUpgradesCount);

        if (this.getOutputMode(EnumFacing.UP, 0) == SlotState.SlotOutputMode.DEFAULT)
        {
            if (output.isEmpty())
            {
                int maxByOutput = result.getMaxStackSize() / result.getCount();
                maxSets = Math.min(maxSets, maxByOutput);
            }
            else
            {
                if (!output.isItemEqual(result)) return 0;

                int maxByOutput = (output.getMaxStackSize() - output.getCount()) / result.getCount();
                maxSets = Math.min(maxSets, maxByOutput);
            }
        }

        return maxSets;
    }

    @Override
    protected int calculateEnergyUsage()
    {
        int machineUpgradesCount = this.calculateInputsCount();
        int speedUpgradesCount = this.getUpgradeCount(MachineUpgradeType.SPEED);
        int energyUpgradesCount = this.getUpgradeCount(MachineUpgradeType.ENERGY);

        int baseTotalCookTime = ModConfig.MACHINES.electricFurnaceBaseCookTime;
        int totalCookTimeModifier = (baseTotalCookTime - baseTotalCookTime / 25) / 8;
        int totalCookTime = baseTotalCookTime - totalCookTimeModifier * speedUpgradesCount;

        if (totalCookTime <= 0) return 0;

        int baseEnergyUsage = ModConfig.MACHINES.electricFurnaceBaseEnergyUsage;
        float energyUsageModifier = (float) (baseEnergyUsage * baseTotalCookTime * 5 - baseEnergyUsage * baseTotalCookTime) / 8;
        float energyUsage = (energyUsageModifier * speedUpgradesCount + baseEnergyUsage * baseTotalCookTime) / totalCookTime;
        float energyModifier = (energyUsage - energyUsage / 10) / 8;
        float machineUpgradesModifier = 1;

        if (machineUpgradesCount > 1) machineUpgradesModifier += (float) machineUpgradesCount * 2 / 10;

        return (int) ((energyUsage - energyModifier * energyUpgradesCount) * machineUpgradesModifier);
    }

    @Override
    protected boolean canProcess()
    {
        if (this.storage == null) return false;

        ItemStack input = this.getInputStack(0);
        if (input.isEmpty()) return false;

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        if (result.isEmpty()) return false;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return false;

        int totalCookTime = this.calculateTotalCookTime();
        int energyUsage = this.calculateEnergyUsage();
        long requiredEnergy = (long) energyUsage * Math.max(0, totalCookTime - this.cookTime);

        if (this.storage.getEnergyStored() < requiredEnergy) return false;

        SidedIngredientHandler sidedIngredientHandler = this.getSidedHandler(EnumFacing.UP);
        if (sidedIngredientHandler == null) return false;

        int experienceUpgrades = this.getUpgradeCount(MachineUpgradeType.EXPERIENCE);
        float experienceModifier = 1.0F + (float) experienceUpgrades / 10.0F;
        int experience = (int) (experienceModifier * FurnaceRecipes.instance().getSmeltingExperience(result) * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null && experienceTank.getFluidAmount() + experience > experienceTank.getCapacity() && sidedIngredientHandler.getSlotOutputMode(this.getExperienceTankStateId()) == SlotState.SlotOutputMode.DEFAULT) return false;

        if (this.getOutputMode(EnumFacing.UP, 0) != SlotState.SlotOutputMode.DEFAULT) return true;

        ItemStack output = this.getOutputStack(0);

        if (output.isEmpty()) return result.getCount() * quantity <= result.getMaxStackSize();
        if (!output.isItemEqual(result)) return false;

        int resultCount = output.getCount() + result.getCount() * quantity;

        return resultCount <= output.getMaxStackSize();
    }

    @Override
    protected void finishProcess()
    {
        ItemStack input = this.getInputStack(0);
        ItemStack output = this.getOutputStack(0);

        if (input.isEmpty()) return;

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        if (result.isEmpty()) return;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return;

        int experienceUpgrades = this.getUpgradeCount(MachineUpgradeType.EXPERIENCE);
        float experienceModifier = 1.0F + (float) experienceUpgrades / 10.0F;
        int experience = (int) (experienceModifier * FurnaceRecipes.instance().getSmeltingExperience(result) * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

        SidedIngredientHandler sidedIngredientHandler = this.getSidedHandler(EnumFacing.UP);
        if (sidedIngredientHandler == null) return;

        if (this.getOutputMode(EnumFacing.UP, 0) != SlotState.SlotOutputMode.VOID_ALL)
        {
            if (output.isEmpty())
            {
                ItemStack resultCopy = result.copy();
                resultCopy.setCount(result.getCount() * quantity);
                this.setOutputStack(0, resultCopy);
            }
            else if (output.isItemEqual(result)) output.grow(result.getCount() * quantity);
        }

        input.shrink(quantity);

        if (input.isEmpty()) this.clearInputStack(0);

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null && sidedIngredientHandler.getSlotOutputMode(this.getExperienceTankStateId()) != SlotState.SlotOutputMode.VOID_ALL) experienceTank.fillInternal(new FluidStack(getConfiguredXpFluid(), experience), true);

        this.markDirty();
    }
}