package net.vyroxes.minersprosperity.machine;

public enum FluidTankTier
{
    BASIC("container.basic_fluid_tank", 16000, 16000),
    IMPROVED("container.improved_fluid_tank", 32000, 32000),
    ADVANCED("container.advanced_fluid_tank", 64000, 64000),
    ELITE("container.elite_fluid_tank", 128000, 128000),
    ULTIMATE("container.ultimate_fluid_tank", 256000, 256000),
    SUPREME("container.supreme_fluid_tank", 512000, 512000),
    CREATIVE("container.creative_fluid_tank", Integer.MAX_VALUE, Integer.MAX_VALUE);

    public final String translationKey;
    public final int capacity;
    public final int transfer;

    FluidTankTier(String translationKey, int capacity, int transfer)
    {
        this.translationKey = translationKey;
        this.capacity = capacity;
        this.transfer = transfer;
    }
}