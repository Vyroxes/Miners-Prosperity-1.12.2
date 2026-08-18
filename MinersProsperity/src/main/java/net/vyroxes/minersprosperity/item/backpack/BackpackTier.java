package net.vyroxes.minersprosperity.item.backpack;

public enum BackpackTier
{
    BASIC("backpack", 27, 9),
    LEAD("lead_backpack", 36, 9),
    IRON("iron_backpack", 45, 9),
    INCONEL("inconel_backpack", 54, 9),
    GOLDEN("golden_backpack", 66, 11),
    ELECTRUM("electrum_backpack", 78, 13),
    DIAMOND("diamond_backpack", 90, 15),
    OBSIDIAN_CLUSTER("obsidian_cluster_backpack", 102, 17),
    EMERALD("emerald_backpack", 114, 19),
    COBALTSTEEL("cobaltsteel_backpack", 126, 21),
    DARK_STEEL("dark_steel_backpack", 138, 23),
    BLACK_OPAL("black_opal_backpack", 150, 25);

    public final String translationKey;
    public final int inventorySize;
    public final int inventoryColumns;

    BackpackTier(String translationKey, int inventorySize, int inventoryColumns)
    {
        this.translationKey = translationKey;
        this.inventorySize = inventorySize;
        this.inventoryColumns = inventoryColumns;
    }
}