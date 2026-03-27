package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesMetalPress;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityElectricFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMetalPress;
import org.jetbrains.annotations.NotNull;

public class CustomItemStackHandler extends ItemStackHandler
{
    private final TileEntity tile;
    private final int inputSlots;
    private final int energySlots;
    private final int outputSlots;
    private final int upgradeSlots;
    public static final ItemStack[] DIES =
    {
            new ItemStack(ItemInit.IRON_GEAR_DIE),
            new ItemStack(ItemInit.IRON_PLATE_DIE),
            new ItemStack(ItemInit.IRON_ROD_DIE),
            new ItemStack(ItemInit.REFINED_OBSIDIAN_GEAR_DIE),
            new ItemStack(ItemInit.REFINED_OBSIDIAN_PLATE_DIE),
            new ItemStack(ItemInit.REFINED_OBSIDIAN_ROD_DIE)
    };

    public CustomItemStackHandler(TileEntity tile, int inputSlots, int energySlots, int outputSlots, int upgradeSlots)
    {
        super(inputSlots + energySlots + outputSlots + upgradeSlots);
        this.tile = tile;
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
    public int getSlots()
    {
        return super.getSlots();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        if (slot < this.inputSlots)
        {
            if (tile instanceof TileEntityAlloyFurnace)
            {
                return RecipesAlloyFurnace.getInstance().getLookupTable().isItemValid(this, 0, this.inputSlots, slot, stack);
            }
            else if (tile instanceof TileEntityMetalPress)
            {
                return RecipesMetalPress.getInstance().getLookupTable().isItemValid(this, slot, stack);
            }
            else if (tile instanceof TileEntityElectricFurnace)
            {
                return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
            }
            else
            {
                return false;
            }
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

    public boolean isDieItemValid(ItemStack stack)
    {
        for (ItemStack die : DIES)
        {
            if (ItemStack.areItemsEqualIgnoreDurability(stack, die))
            {
                return true;
            }
        }

        return false;
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