package net.vyroxes.minersprosperity.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiFluidHandler implements IFluidHandler
{
    private final CustomFluidTank[] tanks;

    public MultiFluidHandler(CustomFluidTank[] tanks)
    {
        this.tanks = tanks;
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        List<IFluidTankProperties> properties = new ArrayList<>();

        for (CustomFluidTank tank : tanks) Collections.addAll(properties, tank.getTankProperties());

        return properties.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource == null || resource.amount <= 0) return 0;

        int remaining = resource.amount;
        int filledTotal = 0;

        for (CustomFluidTank tank : tanks)
        {
            if (remaining <= 0) break;

            FluidStack toFill = resource.copy();
            toFill.amount = remaining;

            int filled = tank.fill(toFill, doFill);

            if (filled > 0)
            {
                filledTotal += filled;
                remaining -= filled;
            }
        }

        return filledTotal;
    }

    @Override
    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (resource == null || resource.amount <= 0) return null;

        FluidStack drainedTotal = null;
        int remaining = resource.amount;

        for (CustomFluidTank tank : tanks)
        {
            if (remaining <= 0) break;

            FluidStack request = resource.copy();
            request.amount = remaining;

            FluidStack drained = tank.drain(request, doDrain);

            if (drained == null || drained.amount <= 0) continue;

            if (drainedTotal == null) drainedTotal = drained.copy();
            else drainedTotal.amount += drained.amount;

            remaining -= drained.amount;
        }

        return drainedTotal;
    }

    @Override
    @Nullable
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (maxDrain <= 0) return null;

        FluidStack drainedTotal = null;
        int remaining = maxDrain;

        for (CustomFluidTank tank : tanks)
        {
            if (remaining <= 0) break;

            FluidStack drained;

            if (drainedTotal == null) drained = tank.drain(remaining, doDrain);
            else
            {
                FluidStack request = drainedTotal.copy();
                request.amount = remaining;

                drained = tank.drain(request, doDrain);
            }

            if (drained == null || drained.amount <= 0) continue;

            if (drainedTotal == null) drainedTotal = drained.copy();
            else drainedTotal.amount += drained.amount;

            remaining -= drained.amount;
        }

        return drainedTotal;
    }
}