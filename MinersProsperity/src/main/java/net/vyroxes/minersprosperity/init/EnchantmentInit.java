package net.vyroxes.minersprosperity.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.vyroxes.minersprosperity.enchantments.*;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentInit
{
    public static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();

    public static final Enchantment QUICK_MINING;
    public static final Enchantment GLOW;
    public static final Enchantment UNRESTRICTED;
    public static final Enchantment VACUUM;
    public static final Enchantment FAR_REACH;
    public static final Enchantment FLAMEPROOF;
    public static final Enchantment POISONPROOF;
    public static final Enchantment WITHERPROOF;
    public static final Enchantment LIGHTNINGPROOF;
    public static final Enchantment INSIGHT;
    public static final Enchantment VOID_GUARD;
    public static final Enchantment FARMERS_BLESSING;
    public static final Enchantment IMMOVABLE;
    public static final Enchantment POISON;
    public static final Enchantment WITHERING;
    public static final Enchantment LIGHTNING;
    public static final Enchantment BLOODTHIRST;

    static
    {
        if (ModConfig.ENCHANTMENTS.quickMining)
        {
            QUICK_MINING = new EnchantmentQuickMining(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(QUICK_MINING);
        }
        else
        {
            QUICK_MINING = null;
        }

        if (ModConfig.ENCHANTMENTS.glow)
        {
            GLOW = new EnchantmentGlow(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
            ENCHANTMENTS.add(GLOW);
        }
        else
        {
            GLOW = null;
        }

        if (ModConfig.ENCHANTMENTS.unrestricted)
        {
            UNRESTRICTED = new EnchantmentUnrestricted(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST });
            ENCHANTMENTS.add(UNRESTRICTED);
        }
        else
        {
            UNRESTRICTED = null;
        }

        if (ModConfig.ENCHANTMENTS.vacuum)
        {
            VACUUM = new EnchantmentVacuum(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
            ENCHANTMENTS.add(VACUUM);
        }
        else
        {
            VACUUM = null;
        }

        if (ModConfig.ENCHANTMENTS.farReach)
        {
            FAR_REACH = new EnchantmentFarReach(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_LEGS, new EntityEquipmentSlot[] { EntityEquipmentSlot.LEGS });
            ENCHANTMENTS.add(FAR_REACH);
        }
        else
        {
            FAR_REACH = null;
        }

        if (ModConfig.ENCHANTMENTS.flameproof)
        {
            FLAMEPROOF = new EnchantmentFlameproof(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST });
            ENCHANTMENTS.add(FLAMEPROOF);
        }
        else
        {
            FLAMEPROOF = null;
        }

        if (ModConfig.ENCHANTMENTS.poisonproof)
        {
            POISONPROOF = new EnchantmentPoisonproof(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
            ENCHANTMENTS.add(POISONPROOF);
        }
        else
        {
            POISONPROOF = null;
        }

        if (ModConfig.ENCHANTMENTS.witherproof)
        {
            WITHERPROOF = new EnchantmentWitherproof(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_LEGS, new EntityEquipmentSlot[] { EntityEquipmentSlot.LEGS });
            ENCHANTMENTS.add(WITHERPROOF);
        }
        else
        {
            WITHERPROOF = null;
        }

        if (ModConfig.ENCHANTMENTS.lightningproof)
        {
            LIGHTNINGPROOF = new EnchantmentLightningproof(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
            ENCHANTMENTS.add(LIGHTNINGPROOF);
        }
        else
        {
            LIGHTNINGPROOF = null;
        }

        if (ModConfig.ENCHANTMENTS.insight)
        {
            INSIGHT = new EnchantmentInsight(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(INSIGHT);
        }
        else
        {
            INSIGHT = null;
        }

        if (ModConfig.ENCHANTMENTS.voidGuard)
        {
            VOID_GUARD = new EnchantmentVoidGuard(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
            ENCHANTMENTS.add(VOID_GUARD);
        }
        else
        {
            VOID_GUARD = null;
        }

        if (ModConfig.ENCHANTMENTS.farmersBlessing)
        {
            FARMERS_BLESSING = new EnchantmentFarmersBlessing(Enchantment.Rarity.RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(FARMERS_BLESSING);
        }
        else
        {
            FARMERS_BLESSING = null;
        }

        if (ModConfig.ENCHANTMENTS.immovable)
        {
            IMMOVABLE = new EnchantmentImmovable(Enchantment.Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST });
            ENCHANTMENTS.add(IMMOVABLE);
        }
        else
        {
            IMMOVABLE = null;
        }

        if (ModConfig.ENCHANTMENTS.poison)
        {
            POISON = new EnchantmentPoison(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(POISON);
        }
        else
        {
            POISON = null;
        }

        if (ModConfig.ENCHANTMENTS.withering)
        {
            WITHERING = new EnchantmentWithering(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(WITHERING);
        }
        else
        {
            WITHERING = null;
        }

        if (ModConfig.ENCHANTMENTS.lightning)
        {
            LIGHTNING = new EnchantmentLightning(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(LIGHTNING);
        }
        else
        {
            LIGHTNING = null;
        }

        if (ModConfig.ENCHANTMENTS.bloodthirst)
        {
            BLOODTHIRST = new EnchantmentBloodthirst(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(BLOODTHIRST);
        }
        else
        {
            BLOODTHIRST = null;
        }
    }
}