package net.vyroxes.minersprosperity.item;

import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;

public enum ShieldTier
{
    STONE("stone_shield", 512, 5, "cobblestone"),
    LEAD("lead_shield", 640, 8, "ingotLead"),
    IRON("iron_shield", 768, 14, "ingotIron"),
    INCONEL("inconel_shield", 896, 16, "ingotInconel"),
    GOLDEN("golden_shield", 800, 25, "ingotGold"),
    ELECTRUM("electrum_shield", 1004, 22, "ingotElectrum"),
    DIAMOND("diamond_shield", 1536, 10, "gemDiamond"),
    OBSIDIAN_CLUSTER("obsidian_cluster_shield", 1892, 8, "clusterObsidian"),
    EMERALD("emerald_shield", 2223, 18, "gemEmerald"),
    COBALTSTEEL("cobaltsteel_shield", 2611, 15, "ingotCobaltsteel"),
    DARK_STEEL("dark_steel_shield", 3067, 12, "ingotDarkSteel"),
    BLACK_OPAL("black_opal_shield", 3603, 25, "gemBlackOpal");

    public final String registryName;
    public final int durability;
    public final int enchantability;
    public final String repairMaterial;
    public final ResourceLocation texture;

    ShieldTier(String registryName, int durability, int enchantability, String repairMaterial)
    {
        this.registryName = registryName;
        this.durability = durability;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
        this.texture = new ResourceLocation(Tags.MODID, "textures/entity/shield/" + registryName + ".png");
    }
}