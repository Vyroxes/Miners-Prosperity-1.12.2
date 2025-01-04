package net.vyroxes.minersprosperity.objects.fluids;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class CustomFluidTank extends FluidTank
{
    public CustomFluidTank(Fluid fluid, int amount, int capacity, boolean canDrain, boolean canFill) {
        super(fluid, amount, capacity);
        this.setCanDrain(canDrain);
        this.setCanFill(canFill);
    }

    public void setFluidStored(Fluid fluid, int fluidStored)
    {
        this.setFluid(new FluidStack(fluid, fluidStored));
    }
}