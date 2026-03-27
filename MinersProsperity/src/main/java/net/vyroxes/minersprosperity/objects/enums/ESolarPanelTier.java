package net.vyroxes.minersprosperity.objects.enums;

public enum ESolarPanelTier
{
    BASIC("container.basic_solar_panel", 6400, 64,64, 48),
    IMPROVED("container.improved_solar_panel", 25600, 256, 256, 192),
    ADVANCED("container.advanced_solar_panel", 102400, 1024, 1024, 768),
    ELITE("container.elite_solar_panel", 409600, 4096, 4096, 1200),
    ULTIMATE("container.ultimate_solar_panel", 1638400, 16384, 16384, 12288),
    SUPREME("container.supreme_solar_panel", 6553600, 65536, 65536, 49152),
    CREATIVE("container.creative_solar_panel", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, (int) (Integer.MAX_VALUE * 0.75));

    public final String translationKey;
    public final int capacity;
    public final int maxExtract;
    public final int generationClear;
    public final int generationRain;

    ESolarPanelTier(String translationKey, int capacity, int maxExtract, int genClear, int genRain)
    {
        this.translationKey = translationKey;
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.generationClear = genClear;
        this.generationRain = genRain;
    }
}