package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import org.jetbrains.annotations.NotNull;

public class CustomItemStackHandler extends ItemStackHandler
{
    private final int inputSlots;
    private final int energySlots;
    private final int outputSlots;
    private final int upgradeSlots;

    public CustomItemStackHandler(int inputSlots, int energySlots, int outputSlots, int upgradeSlots)
    {
        super(inputSlots + energySlots + outputSlots + upgradeSlots);
        this.inputSlots = inputSlots;
        this.energySlots = energySlots;
        this.outputSlots = outputSlots;
        this.upgradeSlots = upgradeSlots;
    }

    public int getInputSlots()
    {
        return this.inputSlots;
    }

    public int getEnergySlots()
    {
        return this.energySlots;
    }

    public int getOutputSlots()
    {
        return this.outputSlots;
    }

    public int getUpgradeSlots() { return this.upgradeSlots; }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot == this.inputSlots) return 1;
        if (slot >= this.inputSlots + this.energySlots + this.outputSlots) return 8;
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        if (slot < this.inputSlots)
        {
            return RecipesAlloyFurnace.getInstance().getLookupTable().isItemValid(this, 0, this.inputSlots, slot, stack);
        }
        else if (slot < this.inputSlots + this.energySlots)
        {
            return isEnergyItemValid(stack);
        }
        else if (slot >= this.inputSlots + this.energySlots + this.outputSlots)
        {
            return isUpgradeItemValid(slot, stack);
        }
        else
        {
            return false;
        }
    }

    public boolean isEnergyItemValid(ItemStack stack)
    {
        if (!stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

            return itemEnergy != null;
        }

        return false;
    }

    public boolean isUpgradeItemValid(int slot, ItemStack stack)
    {
        int upgradeSlotIndex = slot - (this.inputSlots + this.energySlots + this.outputSlots);
        switch (upgradeSlotIndex)
        {
            case 0:
                if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.MACHINE_UPGRADE))) return true;
                break;
            case 1:
                if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.SPEED_UPGRADE))) return true;
                break;
            case 2:
                if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.ENERGY_UPGRADE))) return true;
                break;
            case 3:
                if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.XP_UPGRADE))) return true;
        }

        return false;
    }

    public int isUpgradeItemValid(ItemStack stack)
    {
        if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.MACHINE_UPGRADE))) return 0;
        if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.SPEED_UPGRADE))) return 1;
        if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.ENERGY_UPGRADE))) return 2;
        if (ItemStack.areItemsEqual(stack, new ItemStack(ItemInit.XP_UPGRADE))) return 3;
        return -1;
    }
}