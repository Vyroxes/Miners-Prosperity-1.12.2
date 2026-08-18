package net.vyroxes.minersprosperity.block.chest;

import net.minecraft.block.material.MapColor;

public enum ChestTier {
    VANILLA("vanilla_chest", MapColor.WOOD, 0, 2.5F, 12.5F, 27, 9),
    LEAD("lead_chest", MapColor.LIGHT_BLUE, 2, 7.0F, 10.0F, 36, 9),
    IRON("iron_chest", MapColor.IRON, 2, 6.0F, 12.0F, 45, 9),
    INCONEL("inconel_chest", MapColor.IRON, 2, 7.5F, 22.0F, 54, 9),
    GOLD("gold_chest", MapColor.YELLOW, 2, 9.5F, 30.0F, 66, 11),
    ELECTRUM("electrum_chest", MapColor.YELLOW, 2, 9.5F, 30.0F, 78, 13),
    DIAMOND("diamond_chest", MapColor.LIGHT_BLUE, 2, 9.0F, 32.0F, 90, 15),
    OBSIDIAN_CLUSTER("obsidian_cluster_chest", MapColor.PURPLE, 2, 70.0F, 1700.0F, 102, 17),
    EMERALD("emerald_chest", MapColor.LIME, 2, 8.0F, 26.0F, 114, 19),
    COBALTSTEEL("cobaltsteel_chest", MapColor.LIGHT_BLUE, 3, 8.0F, 28.0F, 126, 21),
    DARK_STEEL("dark_steel_chest", MapColor.GRAY, 3, 10.0F, 32.0F, 138, 23),
    BLACK_OPAL("black_opal_chest", MapColor.BLACK, 3, 13.0F, 40.0F, 150, 25);

    public final String translationKey;
    public final MapColor mapColor;
    public final int harvestLevel;
    public final float hardness;
    public final float resistance;
    public final int inventorySize;
    public final int inventoryColumns;

    ChestTier(String translationKey, MapColor mapColor, int harvestLevel, float hardness, float resistance, int inventorySize, int inventoryColumns) {
        this.translationKey = translationKey;
        this.mapColor = mapColor;
        this.harvestLevel = harvestLevel;
        this.hardness = hardness;
        this.resistance = resistance;
        this.inventorySize = inventorySize;
        this.inventoryColumns = inventoryColumns;
    }

    public ChestTier getNext() {
        int next = this.ordinal() + 1;
        return next < values().length ? values()[next] : null;
    }
}