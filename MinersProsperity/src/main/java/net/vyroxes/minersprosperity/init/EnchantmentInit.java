package net.vyroxes.minersprosperity.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.vyroxes.minersprosperity.enchantments.EnchantmentGlow;
import net.vyroxes.minersprosperity.enchantments.EnchantmentQuickMining;
import net.vyroxes.minersprosperity.util.handlers.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentInit
{
    public static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();

    public static final Enchantment QUICK_MINING;
    public static final Enchantment GLOW;
//    public static final Enchantment UNRESTRICTED;
//    public static final Enchantment AUTO_COLLECT;
//    public static final Enchantment FAR_REACH;

    static
    {
        if (ConfigHandler.quickMining)
        {
            QUICK_MINING = new EnchantmentQuickMining(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
            ENCHANTMENTS.add(QUICK_MINING);
        }
        else
        {
            QUICK_MINING = null;
        }

        if (ConfigHandler.glow)
        {
            GLOW = new EnchantmentGlow(Enchantment.Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
        }
        else
        {
            GLOW = null;
        }

//        if (ConfigHandler.unrestricted)
//        {
//
//        }
//        else
//        {
//            UNRESTRICTED = null;
//        }
//
//        if (ConfigHandler.autoCollect)
//        {
//
//        }
//        else
//        {
//            AUTO_COLLECT = null;
//        }
//
//        if (ConfigHandler.farReach)
//        {
//
//        }
//        else
//        {
//            FAR_REACH = null;
//        }
    }
}