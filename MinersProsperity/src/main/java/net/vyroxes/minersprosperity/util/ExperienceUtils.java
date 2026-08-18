package net.vyroxes.minersprosperity.util;

public final class ExperienceUtils {
    private ExperienceUtils() {}

    public static float calculateLevelFromXp(int xp)
    {
        if (xp <= 0) return 0.0F;

        int remainingXp = xp;
        int level = 0;

        while (remainingXp > 0)
        {
            int xpToNextLevel = getXpToNextLevel(level);

            if (remainingXp < xpToNextLevel) return level + (float) remainingXp / xpToNextLevel;

            remainingXp -= xpToNextLevel;
            level++;
        }

        return level;
    }

    private static int getXpToNextLevel(int level)
    {
        if (level < 16) return 2 * level + 7;

        if (level < 31) return 5 * level - 38;

        return 9 * level - 158;
    }
}