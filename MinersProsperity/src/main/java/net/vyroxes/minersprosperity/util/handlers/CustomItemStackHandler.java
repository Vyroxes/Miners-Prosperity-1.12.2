package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
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
            return isValidInput(stack);
        }
        else if (slot < inputSlots + energySlots)
        {
            return isItemEnergy(stack);
        }
        else
        {
            return false;
        }
    }

    private boolean isValidInput(ItemStack stack)
    {
        // Logic
        return true;
    }

    private boolean isItemEnergy(ItemStack stack)
    {
        // Logic
        return true;
    }
}