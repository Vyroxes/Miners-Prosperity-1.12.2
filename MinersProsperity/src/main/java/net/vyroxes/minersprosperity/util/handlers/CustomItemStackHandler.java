package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import org.jetbrains.annotations.NotNull;

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
            ItemStack[] inputSlotStacks = new ItemStack[inputSlots];
            for (int i = 0; i < inputSlots; i++)
            {
                inputSlotStacks[i] = getStackInSlot(i);
            }

            return isValidInput(stack, inputSlotStacks);
        }
        else if (slot < inputSlots + energySlots)
        {
            return isValidEnergy(stack);
        }
        else
        {
            return false;
        }
    }


    public boolean isValidInput(ItemStack stack, ItemStack[] inputSlotStacks)
    {
        RecipesAlloyFurnace recipes = RecipesAlloyFurnace.getInstance();

        if (stack.isEmpty())
        {
            return false;
        }

        boolean allSlotsEmpty = true;
        for (ItemStack slot : inputSlotStacks)
        {
            if (!slot.isEmpty())
            {
                allSlotsEmpty = false;
                break;
            }
        }

        if (allSlotsEmpty && !recipes.findRecipes(stack).isEmpty())
        {
            return true;
        }

        for (ItemStack slot : inputSlotStacks)
        {
            if (!slot.isEmpty() && !recipes.findRecipes(stack, slot).isEmpty())
            {
                return true;
            }
        }

        for (ItemStack slot : inputSlotStacks)
        {
            if (!slot.isEmpty() && slot.getItem().equals(stack.getItem()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isValidEnergy(ItemStack stack)
    {
        if (!stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage itemEnergy = stack.getCapability(CapabilityEnergy.ENERGY, null);

            return itemEnergy != null;
        }

        return false;
    }
}