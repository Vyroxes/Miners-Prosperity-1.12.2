package net.vyroxes.minersprosperity.machine;

public enum SolarPanelTier
{
    BASIC("container.basic_solar_panel", 6400, 64,64, 48, 0x828282),
    IMPROVED("container.improved_solar_panel", 25600, 256, 256, 192, 0x1b7097),
    ADVANCED("container.advanced_solar_panel", 102400, 1024, 1024, 768, 0x1f971b),
    ELITE("container.elite_solar_panel", 409600, 4096, 4096, 1200, 0x82862d),
    ULTIMATE("container.ultimate_solar_panel", 1638400, 16384, 16384, 12288, 0x862d2d),
    SUPREME("container.supreme_solar_panel", 6553600, 65536, 65536, 49152, 0x0c94b7),
    CREATIVE("container.creative_solar_panel", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, (int) (Integer.MAX_VALUE * 0.75), 0x8a00b3);

    public final String translationKey;
    public final int capacity;
    public final int maxExtract;
    public final int generationClear;
    public final int generationRain;
    public final int particleColor;

    SolarPanelTier(String translationKey, int capacity, int maxExtract, int genClear, int genRain, int particleColor)
    {
        this.translationKey = translationKey;
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.generationClear = genClear;
        this.generationRain = genRain;
        this.particleColor = particleColor;
    }

    public float getParticleRed() { return ((this.particleColor >> 16) & 0xFF) / 255.0F; }

    public float getParticleGreen() { return ((this.particleColor >> 8) & 0xFF) / 255.0F; }

    public float getParticleBlue() { return (this.particleColor & 0xFF) / 255.0F; }
}