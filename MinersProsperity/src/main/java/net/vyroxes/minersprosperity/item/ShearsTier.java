package net.vyroxes.minersprosperity.item;

public enum ShearsTier {
    WOODEN("wooden_shears", 120, 0.70F, 5, "plankWood"),
    STONE("stone_shears", 160, 0.80F, 5, "cobblestone"),
    LEAD("lead_shears", 200, 0.90F, 8, "ingotLead"),
    INCONEL("inconel_shears", 460, 1.10F, 16, "ingotInconel"),
    GOLDEN("golden_shears", 380, 1.30F, 25, "ingotGold"),
    ELECTRUM("electrum_shears", 640, 1.20F, 22, "ingotElectrum"),
    DIAMOND("diamond_shears", 960, 1.40F, 10, "gemDiamond"),
    OBSIDIAN_CLUSTER("obsidian_cluster_shears", 1280, 1.50F, 8, "clusterObsidian"),
    EMERALD("emerald_shears", 1536, 1.60F, 18, "gemEmerald"),
    COBALTSTEEL("cobaltsteel_shears", 2048, 1.75F, 15, "ingotCobaltsteel"),
    DARK_STEEL("dark_steel_shears", 2560, 1.90F, 12, "ingotDarkSteel"),
    BLACK_OPAL("black_opal_shears", 3072, 2.10F, 25, "gemBlackOpal");

    public final String registryName;
    public final int durability;
    public final float speedMultiplier;
    public final int enchantability;
    public final String repairMaterial;

    ShearsTier(String registryName, int durability, float speedMultiplier, int enchantability, String repairMaterial) {
        this.registryName = registryName;
        this.durability = durability;
        this.speedMultiplier = speedMultiplier;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
    }
}