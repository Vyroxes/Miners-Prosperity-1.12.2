package net.vyroxes.minersprosperity.util;

import net.minecraft.client.resources.I18n;
import java.text.DecimalFormat;

public class UtilHelper
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.##");

    public static String energy(int energyStored, int energyMaxStored, boolean isCreative, boolean capacity, boolean usage)
    {
        if (isCreative)
            return I18n.format("gui.energy_infinite");

        if (capacity)
        {
            return formatEnergyCapacity(energyStored, energyMaxStored);
        }

        if (usage)
        {
            return formatEnergyPerTick(energyStored);
        }

        return "";
    }

    private static String formatEnergyCapacity(int stored, int max)
    {
        String unit = getUnit(max);
        double scaledStored = scaleEnergy(stored);
        double scaledMax = scaleEnergy(max);
        return FORMAT.format(scaledStored) + "/" + FORMAT.format(scaledMax) + " " + unit;
    }

    private static String formatEnergyPerTick(int value)
    {
        String unit = getUnit(value);
        double scaled = scaleEnergy(value);
        return FORMAT.format(scaled) + " " + unit + "/t";
    }

    private static double scaleEnergy(int value)
    {
        if (value >= 1_000_000)
            return value / 1_000_000.0;
        else if (value >= 1_000)
            return value / 1_000.0;
        else
            return value;
    }

    private static String getUnit(int value)
    {
        if (value >= 1_000_000)
            return "MFE";
        else if (value >= 1_000)
            return "kFE";
        else
            return "FE";
    }

    public static float calculateLevelsFromXP(int xp)
    {
        int level = 0;

        while (xp > 0)
        {
            int xpToNextLevel;

            if (level < 16)
            {
                xpToNextLevel = 2 * level + 7;
            }
            else if (level < 31)
            {
                xpToNextLevel = 5 * level - 38;
            }
            else
            {
                xpToNextLevel = 9 * level - 158;
            }

            if (xp >= xpToNextLevel)
            {
                xp -= xpToNextLevel;
                level++;
            }
            else
            {
                return level + (float) xp / xpToNextLevel;
            }
        }

        return level;
    }
}