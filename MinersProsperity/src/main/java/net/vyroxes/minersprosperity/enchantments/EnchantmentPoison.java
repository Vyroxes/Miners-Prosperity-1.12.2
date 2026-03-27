package net.vyroxes.minersprosperity.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.jetbrains.annotations.NotNull;

public class EnchantmentPoison extends Enchantment
{

    public EnchantmentPoison(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("poison");
        this.setName("poison");

        EnchantmentInit.ENCHANTMENTS.add(this);
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 10 + (level - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack) || stack.getItem() == Items.BOOK || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemBow;
    }
}