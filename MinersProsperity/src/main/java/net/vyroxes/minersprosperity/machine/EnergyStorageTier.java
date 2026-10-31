package net.vyroxes.minersprosperity.machine;

public enum EnergyStorageTier
{
    BASIC("container.basic_energy_storage", 200000, 2000, 0xFFBA8E),
    IMPROVED("container.improved_energy_storage", 800000, 8000, 0x2D91E0),
    ADVANCED("container.advanced_energy_storage", 3200000, 32000, 0x38CF49),
    ELITE("container.elite_energy_storage", 12800000, 128000, 0xD0D54B),
    ULTIMATE("container.ultimate_energy_storage", 51200000, 512000, 0xD94A4A),
    SUPREME("container.supreme_energy_storage", 204800000, 2048000, 0x16C6EF),
    CREATIVE("container.creative_energy_storage", Integer.MAX_VALUE, Integer.MAX_VALUE, 0xB73BEB);

    public final String translationKey;
    public final int capacity;
    public final int transfer;
    public final int coreColor;

    EnergyStorageTier(String translationKey, int capacity, int transfer, int coreColor)
    {
        this.translationKey = translationKey;
        this.capacity = capacity;
        this.transfer = transfer;
        this.coreColor = coreColor;
    }
}