package net.vyroxes.minersprosperity.item.armor;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.Tags;
import java.util.Objects;

public enum ArmorTier
{
    WOODEN("wooden armor", "wooden", 5, new int[]{1, 2, 3, 1}, 22, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F, "plankWood"),
    STONE("stone armor", "stone", 8, new int[]{2, 3, 4, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, "cobblestone"),
    LEAD("lead armor", "lead", 12, new int[]{2, 4, 5, 2}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, "ingotLead"),
    INCONEL("inconel armor", "inconel", 21, new int[]{2, 5, 6, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, "ingotInconel"),
    ELECTRUM("electrum armor", "electrum", 27, new int[]{3, 5, 6, 3}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, "ingotElectrum"),
    OBSIDIAN_CLUSTER("obsidian cluster armor", "obsidian_cluster", 40, new int[]{4, 7, 9, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F, "clusterObsidian"),
    EMERALD("emerald armor", "emerald", 47, new int[]{5, 8, 10, 5}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4.0F, "gemEmerald"),
    COBALTSTEEL("cobaltsteel armor", "cobaltsteel", 54, new int[]{5, 10, 12, 5}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 5.0F, "ingotCobaltsteel"),
    DARK_STEEL("dark steel armor", "dark_steel", 61, new int[]{6, 10, 12, 6}, 14, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 6.0F, "ingotDarkSteel"),
    BLACK_OPAL("black opal armor", "black_opal", 68, new int[]{7, 12, 14, 7}, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 7.0F, "gemBlackOpal");

    public final ItemArmor.ArmorMaterial material;
    public final String repairMaterial;

    ArmorTier(String materialName, String textureName, int durability, int[] damageReductionAmounts, int enchantability, SoundEvent equipSound, float toughness, String repairOreName)
    {
        this.material = Objects.requireNonNull(EnumHelper.addArmorMaterial(materialName, Tags.MODID + ":" + textureName, durability, damageReductionAmounts, enchantability, equipSound, toughness));
        this.repairMaterial = repairOreName;
    }
}