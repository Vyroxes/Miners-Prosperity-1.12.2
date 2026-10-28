package net.vyroxes.minersprosperity.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.init.ModSounds;
import net.vyroxes.minersprosperity.machine.recipe.PulverizerRecipe;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import java.util.List;

public class TileEntityPulverizer extends TileEntityProcessingMachine
{
    public TileEntityPulverizer()
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
    public ITextComponent getDisplayName() { return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.pulverizer"); }

    @Override
    public SoundEvent getActiveSound() { return ModSounds.PULVERIZER_ACTIVE; }

    @Override
    public float getActiveSoundVolume() { return 0.05F; }

    private PulverizerRecipe.Recipe getRecipe()
    {
        return this.getCachedRecipe(() ->
        {
            ItemStack input = this.getInputStack(0);
            if (input.isEmpty()) return null;

            List<PulverizerRecipe.Recipe> recipes = PulverizerRecipe.getInstance().findRecipes(input);
            return recipes.isEmpty() ? null : recipes.get(0);
        });
    }

    @Override
    protected int calculateTotalCookTime()
    {
        PulverizerRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return 0;

        int speedUpgradesCount = this.getUpgradeCount(MachineUpgradeType.SPEED);
        int baseTotalCookTime = recipe.totalCookTime;

        return Math.max(1, Math.round(baseTotalCookTime * (1.0F - 0.9F * speedUpgradesCount / 8)));
    }

    private int calculateInputsCount()
    {
        ItemStack input = this.getInputStack(0);
        ItemStack output = this.getOutputStack(0);

        if (input.isEmpty()) return 0;

        PulverizerRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return 0;

        int requiredQuantity = recipe.getRequiredQuantity(input);
        if (requiredQuantity <= 0) return 0;

        int maxSets = input.getCount() / requiredQuantity;

        int upgrades = this.getUpgradeCount(MachineUpgradeType.MACHINE);
        int machineUpgradesCount = upgrades == 8 ? 10 : upgrades + 1;

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
        PulverizerRecipe.Recipe recipe = this.getRecipe();
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

        ItemStack input = this.getInputStack(0);
        if (input.isEmpty()) return false;

        PulverizerRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return false;

        ItemStack result = recipe.result;
        if (result.isEmpty()) return false;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return false;

        int totalCookTime = this.calculateTotalCookTime();
        int energyUsage = this.calculateEnergyUsage();
        long requiredEnergy = (long) energyUsage * Math.max(0, totalCookTime - this.cookTime);

        if (this.storage.getEnergyStored() < requiredEnergy) return false;

        SidedIngredientHandler handler = this.getSidedHandler(EnumFacing.UP);
        if (handler == null) return false;

        int experienceUpgrades = this.getUpgradeCount(MachineUpgradeType.EXPERIENCE);
        float experienceModifier = 1.0F + (float) experienceUpgrades / 10.0F;
        int experience = (int) (experienceModifier * recipe.experience * quantity * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null && experienceTank.getFluidAmount() + experience > experienceTank.getCapacity() && handler.getSlotOutputMode(this.getExperienceTankStateId()) == SlotState.SlotOutputMode.DEFAULT) return false;

        if (this.getOutputMode(EnumFacing.UP, 0) != SlotState.SlotOutputMode.DEFAULT) return true;

        ItemStack output = this.getOutputStack(0);

        if (output.isEmpty()) return result.getCount() * quantity <= result.getMaxStackSize();
        if (!output.isItemEqual(result)) return false;

        return output.getCount() + result.getCount() * quantity <= output.getMaxStackSize();
    }

    @Override
    protected void finishProcess()
    {
        ItemStack input = this.getInputStack(0);
        ItemStack output = this.getOutputStack(0);

        PulverizerRecipe.Recipe recipe = this.getRecipe();
        if (recipe == null) return;

        int quantity = this.calculateInputsCount();
        if (quantity <= 0) return;

        int requiredQuantity = recipe.getRequiredQuantity(input);
        if (requiredQuantity <= 0) return;

        ItemStack result = recipe.result;
        if (result.isEmpty()) return;

        int experienceUpgrades = this.getUpgradeCount(MachineUpgradeType.EXPERIENCE);
        float experienceModifier = 1.0F + (float) experienceUpgrades / 10.0F;
        int experience = (int) (experienceModifier * recipe.experience * quantity * ((float) 1000 / ModConfig.MACHINES.mbToXpRatio));

        SidedIngredientHandler handler = this.getSidedHandler(EnumFacing.UP);
        if (handler == null) return;

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

        input.shrink(quantity * requiredQuantity);

        if (input.isEmpty()) this.clearInputStack(0);

        CustomFluidTank experienceTank = this.getExperienceTank();
        if (experienceTank != null && handler.getSlotOutputMode(this.getExperienceTankStateId()) != SlotState.SlotOutputMode.VOID_ALL) experienceTank.fillInternal(new FluidStack(getConfiguredXpFluid(), experience), true);

        this.invalidateRecipeCache();
        this.markDirty();
    }
}