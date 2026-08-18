package net.vyroxes.minersprosperity.item.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import java.util.Objects;

public enum ToolTier
{
    WOODEN(Item.ToolMaterial.WOOD, "plankWood"),
    STONE(Item.ToolMaterial.STONE, "cobblestone"),
    LEAD("lead tool", 1, 96, 6.0F, 1.5F, 14, "ingotLead"),
    IRON(Item.ToolMaterial.IRON, "ingotIron"),
    INCONEL("inconel tool", 2, 502, 6.5F, 2.0F, 12, "ingotInconel"),
    GOLDEN(Item.ToolMaterial.GOLD, "ingotGold"),
    ELECTRUM("electrum tool", 2, 1004, 7.0F, 2.5F, 10, "ingotElectrum"),
    DIAMOND(Item.ToolMaterial.DIAMOND, "gemDiamond"),
    OBSIDIAN_CLUSTER("obsidian cluster tool", 3, 1892, 10.0F, 4.0F, 12, "clusterObsidian"),
    EMERALD("emerald tool", 4, 2223, 12.0F, 5.0F, 14, "gemEmerald"),
    COBALTSTEEL("cobaltsteel tool", 4, 2611, 14.0F, 6.0F, 16, "ingotCobaltsteel"),
    DARK_STEEL("dark steel tool", 5, 3067, 16.0F, 7.0F, 18, "ingotDarkSteel"),
    BLACK_OPAL("black opal tool", 5, 3603, 18.0F, 8.0F, 20, "gemBlackOpal");

    public final Item.ToolMaterial material;
    public final String repairMaterial;
    public static final int PAXEL_DURABILITY_MULTIPLIER = 5;

    ToolTier(Item.ToolMaterial material, String repairOreName)
    {
        this.material = material;
        this.repairMaterial = repairOreName;
    }

    ToolTier(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability, String repairOreName)
    {
        this.material = Objects.requireNonNull(EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability));
        this.repairMaterial = repairOreName;
    }

    public boolean isRepairMaterial(ItemStack repair)
    {
        if (repair.isEmpty()) return false;

        if (OreDictionary.containsMatch(false, OreDictionary.getOres(repairMaterial, false), repair)) return true;

        ItemStack repairItem = material.getRepairItemStack();

        return !repairItem.isEmpty() && OreDictionary.itemMatches(repairItem, repair, false);
    }
}