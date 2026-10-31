package net.vyroxes.minersprosperity.client.gui;

import java.math.BigDecimal;

public final class FluidFormatter
{
    private FluidFormatter() {}

    public static String capacity(int stored, int capacity)
    {
        int divisor = getDivisor(capacity);
        return format(stored, divisor) + "/" + format(capacity, divisor) + " " + getUnit(divisor)
                + " (" + EnergyFormatter.percentage(stored, capacity) + ")";
    }

    public static String transfer(int amount)
    {
        int divisor = getDivisor(amount);
        return format(amount, divisor) + " " + getUnit(divisor) + "/t";
    }

    private static int getDivisor(int amount)
    {
        if (amount >= 1_000_000_000) return 1_000_000_000;
        if (amount >= 1_000_000) return 1_000_000;
        if (amount >= 1_000) return 1_000;
        return 1;
    }

    private static String getUnit(int divisor)
    {
        return switch (divisor)
        {
            case 1_000_000_000 -> "MB";
            case 1_000_000 -> "kB";
            case 1_000 -> "B";
            default -> "mB";
        };
    }

    private static String format(int amount, int divisor)
    {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(divisor)).stripTrailingZeros().toPlainString();
    }
}