package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.machine.recipe.AlloyFurnaceRecipe;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import java.util.List;

public class TileEntityAlloyFurnace extends TileEntityProcessingMachine
{
    public TileEntityAlloyFurnace()
    {
        super(new Builder()
                .setItemInput(2)
                .setItemEnergy(1)
                .setItemOutput(1)
                .setItemUpgrade(4)
                .setEnergy(20000, 200, 0, 0)
                .addExperienceTank(Integer.MAX_VALUE)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder().setEnergyHandler(SlotState.SlotMode.INPUT)));
    }

    @Override
    public ITextComponent getDisplayName() { return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.alloy_furnace"); }

    @Override
    public SoundEvent getActiveSound() { return ModSounds.ALLOY_FURNACE_ACTIVE; }

    @Override
    public float getActiveSoundVolume() { return 0.05F; }

    private AlloyFurnaceRecipe.Recipe getRecipe()
    {
        return this.getCachedRecipe(() ->
        {
            ItemStack input1 = this.getInputStack(0);
            ItemStack input2 = this.getInputStack(1);

            if (input1.isEmpty() || input2.isEmpty()) return null;

            List<AlloyFurnaceRecipe.Recipe> recipes = AlloyFurnaceRecipe.getInstance().findRecipes(input1, input2);

            return recipes.isEmpty() ? null : recipes.get(0);
        });
    }

    @Override
    protected int calculateTotalCookTime()
    {
        AlloyFurnaceRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return 0;

        int speedUpgradesCount = this.getUpgradeCount(MachineUpgradeType.SPEED);
        int baseTotalCookTime = recipe.totalCookTime;

        return Math.max(1, Math.round(baseTotalCookTime * (1.0F - 0.9F * speedUpgradesCount / 8)));
    }

    private int calculateInputsCount()
    {
        ItemStack input1 = this.getInputStack(0);
        ItemStack input2 = this.getInputStack(1);
        ItemStack output = this.getOutputStack(0);

        AlloyFurnaceRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return 0;

        int requiredQuantity1 = recipe.getRequiredQuantity(input1);
        int requiredQuantity2 = recipe.getRequiredQuantity(input2);

        int upgrades = this.getUpgradeCount(MachineUpgradeType.MACHINE);
        int machineUpgradesCount = upgrades == 8 ? 10 : upgrades + 1;

        int maxSetsByInput1 = 0;
        if (requiredQuantity1 > 0) maxSetsByInput1 = input1.getCount() / requiredQuantity1;

        int maxSetsByInput2 = 0;
        if (requiredQuantity2 > 0) maxSetsByInput2 = input2.getCount() / requiredQuantity2;

        int maxSets = Math.min(maxSetsByInput1, maxSetsByInput2);

        if (machineUpgradesCount == 1 && maxSets > 0) return 1;

        maxSets = Math.min(maxSets, machineUpgradesCount);

        ItemStack result = recipe.result;
        if (result.isEmpty()) return 0;

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
        AlloyFurnaceRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return 0;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return 0;

        int speedUpgradesCount = this.getUpgradeCount(MachineUpgradeType.SPEED);
        int energyUpgradesCount = this.getUpgradeCount(MachineUpgradeType.ENERGY);

        int totalCookTime = this.calculateTotalCookTime();
        if (totalCookTime <= 0) return 0;

        double speedModifier = 1.0 + 4.0 * speedUpgradesCount / 8;
        double energyModifier = 1.0 - 0.5 * energyUpgradesCount / 8;
        double processEnergy = (double) recipe.energyUsage * recipe.totalCookTime * speedModifier * energyModifier * quantity;

        return (int) Math.ceil(processEnergy / totalCookTime);
    }

    @Override
    protected boolean canProcess()
    {
        if (this.storage == null) return false;

        ItemStack input1 = this.getInputStack(0);
        ItemStack input2 = this.getInputStack(1);

        if (input1.isEmpty() || input2.isEmpty()) return false;

        AlloyFurnaceRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return false;

        ItemStack result = recipe.result;
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
        int experience = (int) (experienceModifier * recipe.experience * quantity * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

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
        ItemStack input1 = this.getInputStack(0);
        ItemStack input2 = this.getInputStack(1);
        ItemStack output = this.getOutputStack(0);

        AlloyFurnaceRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return;

        int requiredQuantity1 = recipe.getRequiredQuantity(input1);
        int requiredQuantity2 = recipe.getRequiredQuantity(input2);

        ItemStack result = recipe.result;
        if (result.isEmpty()) return;

        int experienceUpgrades = this.getUpgradeCount(MachineUpgradeType.EXPERIENCE);
        float experienceModifier = 1.0F + (float) experienceUpgrades / 10.0F;
        int experience = (int) (experienceModifier * recipe.experience * quantity * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

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

        input1.shrink(quantity * requiredQuantity1);
        input2.shrink(quantity * requiredQuantity2);

        if (input1.isEmpty()) this.clearInputStack(0);
        if (input2.isEmpty()) this.clearInputStack(1);

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null && sidedIngredientHandler.getSlotOutputMode(this.getExperienceTankStateId()) != SlotState.SlotOutputMode.VOID_ALL) experienceTank.fillInternal(new FluidStack(getConfiguredXpFluid(), experience), true);

        this.invalidateRecipeCache();
        this.markDirty();
    }
}