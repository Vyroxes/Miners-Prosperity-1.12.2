package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.vyroxes.minersprosperity.init.FluidInit;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;

public class TileEntityAlloyFurnace extends TileEntityMachine
{
    public TileEntityAlloyFurnace()
    {
        super(new Builder()
                .setItemInput(2)
                .setItemEnergy(1)
                .setItemOutput(1)
                .setItemUpgrade(4)
                .setEnergy(20000, 200, 0, 0)
                .setFluid(FluidInit.LIQUID_EXPERIENCE, Integer.MAX_VALUE, 0, true, false)
                .setSidedIngredientHandler(new SidedIngredientHandler.Builder()
                        .setInputs(2)
                        .setEnergySlots(1)
                        .setOutputs(1)
                        .setUpgradeSlots(4)));
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.alloy_furnace");
    }
}

