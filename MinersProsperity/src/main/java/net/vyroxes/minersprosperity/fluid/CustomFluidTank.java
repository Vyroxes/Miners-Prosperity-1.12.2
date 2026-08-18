package net.vyroxes.minersprosperity.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import javax.annotation.Nullable;

public class CustomFluidTank extends FluidTank
{
    @Nullable
    private final Fluid allowedFluid;

    public CustomFluidTank(@Nullable Fluid fluid, int amount, int capacity, boolean canDrain, boolean canFill) {
        super(capacity);

        this.allowedFluid = fluid;

        if (fluid != null && amount > 0) this.setFluid(new FluidStack(fluid, Math.min(amount, capacity)));

        this.setCanDrain(canDrain);
        this.setCanFill(canFill);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid)
    {
        if (fluid == null || fluid.getFluid() == null) return false;

        return this.allowedFluid == null || fluid.getFluid() == this.allowedFluid;
    }

    public void setFluidStored(@Nullable Fluid fluid, int fluidStored)
    {
        if (fluid == null || fluidStored <= 0)
        {
            this.setFluid(null);
            return;
        }

        this.setFluid(new FluidStack(fluid, Math.min(fluidStored, this.getCapacity())));
    }

    @Nullable
    public Fluid getAllowedFluid() { return this.allowedFluid; }
}