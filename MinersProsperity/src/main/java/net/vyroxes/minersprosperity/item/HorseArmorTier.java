package net.vyroxes.minersprosperity.item;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.Tags;
import java.util.Locale;
import java.util.Objects;

public enum HorseArmorTier
{
    LEATHER("leather_horse_armor", 1, "leather"),
    WOODEN("wooden_horse_armor", 2, "plankWood"),
    STONE("stone_horse_armor", 3, "cobblestone"),
    LEAD("lead_horse_armor", 4, "ingotLead"),
    INCONEL("inconel_horse_armor", 6, "ingotInconel"),
    ELECTRUM("electrum_horse_armor", 8, "ingotElectrum"),
    OBSIDIAN_CLUSTER("obsidian_cluster_horse_armor", 12, "clusterObsidian"),
    EMERALD("emerald_horse_armor", 14, "gemEmerald"),
    COBALTSTEEL("cobaltsteel_horse_armor", 16, "ingotCobaltsteel"),
    DARK_STEEL("dark_steel_horse_armor", 18, "ingotDarkSteel"),
    BLACK_OPAL("black_opal_horse_armor", 20, "gemBlackOpal");

    public final String registryName;
    public final String texture;
    public final String repairMaterial;
    public final HorseArmorType armorType;

    HorseArmorTier(String registryName, int armorStrength, String repairOreName)
    {
        this.registryName = registryName;
        this.texture = Tags.MODID + ":textures/entity/horse/armor/" + registryName + ".png";
        this.repairMaterial = repairOreName;
        this.armorType = Objects.requireNonNull(EnumHelper.addHorseArmor(Tags.MODID + "_" + registryName.toUpperCase(Locale.ROOT), texture, armorStrength));
    }
}