package net.vyroxes.minersprosperity.fluid;

import net.minecraftforge.fluids.Fluid;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import org.jetbrains.annotations.Nullable;

public class FluidTankDefinition
{
    @Nullable
    private final Fluid fluid;

    private final int capacity;
    private final int storedFluid;
    private final boolean canDrain;
    private final boolean canFill;
    private final SlotState.SlotType slotType;
    private final SlotState.SlotMode defaultMode;
    private final boolean experienceTank;

    public FluidTankDefinition(@Nullable Fluid fluid, int capacity, int storedFluid, boolean canDrain, boolean canFill, SlotState.SlotType slotType, SlotState.SlotMode defaultMode, boolean experienceTank)
    {
        if (capacity <= 0) throw new IllegalArgumentException("Fluid tank capacity must be greater than 0");

        this.fluid = fluid;
        this.capacity = capacity;
        this.storedFluid = storedFluid;
        this.canDrain = canDrain;
        this.canFill = canFill;
        this.slotType = slotType;
        this.defaultMode = defaultMode;
        this.experienceTank = experienceTank;
    }

    @Nullable
    public Fluid getFluid() { return fluid; }

    public int getCapacity() { return capacity; }

    public int getStoredFluid() { return storedFluid; }

    public boolean canDrain() { return canDrain; }

    public boolean canFill() { return canFill; }

    public SlotState.SlotType getSlotType() { return slotType; }

    public SlotState.SlotMode getDefaultMode() { return defaultMode; }

    public boolean isExperienceTank() { return experienceTank; }

    public CustomFluidTank createTank() { return new CustomFluidTank(fluid, storedFluid, capacity, canDrain, canFill); }
}