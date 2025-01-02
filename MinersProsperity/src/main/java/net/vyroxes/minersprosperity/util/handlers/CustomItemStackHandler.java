package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomItemStackHandler extends ItemStackHandler
{
    private final int inputSlots;
    private final int energySlots;
    private final int outputSlots;

    public CustomItemStackHandler(int inputSlots, int energySlots, int outputSlots)
    {
        super(inputSlots + energySlots + outputSlots);
        this.inputSlots = inputSlots;
        this.energySlots = energySlots;
        this.outputSlots = outputSlots;
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

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        if (slot < inputSlots)
        {
            return RecipesAlloyFurnace.getInstance().getLookupTable().isItemValid(this, 0, inputSlots, slot, stack);
        }
        else if (slot < inputSlots + energySlots)
        {
            return isEnergyItemValid(stack);
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
}