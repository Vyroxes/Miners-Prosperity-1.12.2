package net.vyroxes.minersprosperity.item;

public enum BowTier
{
    STONE("stone_bow", 512, 1.08D, 5, "cobblestone"),
    LEAD("lead_bow", 640, 1.16D, 8, "rodLead"),
    IRON("iron_bow", 768, 1.25D, 14, "rodIron"),
    INCONEL("inconel_bow", 896, 1.38D, 16, "rodInconel"),
    GOLDEN("golden_bow", 800, 1.60D, 25, "rodGold"),
    ELECTRUM("electrum_bow", 1004, 1.50D, 22, "rodElectrum"),
    DIAMOND("diamond_bow", 2048, 1.70D, 10, "rodDiamond"),
    OBSIDIAN_CLUSTER("obsidian_cluster_bow", 1892, 1.90D, 8, "rodObsidianCluster"),
    EMERALD("emerald_bow", 2223, 2.10D, 18, "rodEmerald"),
    COBALTSTEEL("cobaltsteel_bow", 2611, 2.35D, 15, "rodCobaltsteel"),
    DARK_STEEL("dark_steel_bow", 3067, 2.65D, 12, "rodDarkSteel"),
    BLACK_OPAL("black_opal_bow", 3603, 3.00D, 25, "rodBlackOpal");

    public final String registryName;
    public final int durability;
    public final double damageMultiplier;
    public final int enchantability;
    public final String repairMaterial;

    BowTier(String registryName, int durability, double damageMultiplier, int enchantability, String repairMaterial)
    {
        this.registryName = registryName;
        this.durability = durability;
        this.damageMultiplier = damageMultiplier;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }
}