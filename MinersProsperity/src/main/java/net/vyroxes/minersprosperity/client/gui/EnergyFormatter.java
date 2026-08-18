package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.resources.I18n;
import java.text.DecimalFormat;

public final class EnergyFormatter
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.##");

    private EnergyFormatter() {}

    public static String capacity(int stored, int capacity, boolean creative)
    {
        if (creative) return I18n.format("gui.energy_infinite");

        Scale scale = getScale(capacity);

        return FORMAT.format(stored / scale.divisor) + "/" + FORMAT.format(capacity / scale.divisor) + " " + scale.unit;
    }

    public static String usage(int energyPerTick, boolean creative)
    {
        if (creative) return I18n.format("gui.energy_infinite");

        Scale scale = getScale(energyPerTick);

        return FORMAT.format(energyPerTick / scale.divisor) + " " + scale.unit + "/t";
    }

    private static Scale getScale(int value)
    {
        long absolute = Math.abs((long) value);

        if (absolute >= 1_000_000L) return Scale.MEGA;

        if (absolute >= 1_000L) return Scale.KILO;

        return Scale.BASE;
    }

    private enum Scale
    {
        BASE(1.0D, "FE"),
        KILO(1_000.0D, "kFE"),
        MEGA(1_000_000.0D, "MFE");

        private final double divisor;
        private final String unit;

        Scale(double divisor, String unit)
        {
            this.divisor = divisor;
            this.unit = unit;
        }
    }
}