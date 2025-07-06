package net.vyroxes.minersprosperity.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

public class EnchantmentGlow extends Enchantment
{
    public EnchantmentGlow(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("glow");
        this.setName("glow");

        EnchantmentInit.ENCHANTMENTS.add(this);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD);
    }
}