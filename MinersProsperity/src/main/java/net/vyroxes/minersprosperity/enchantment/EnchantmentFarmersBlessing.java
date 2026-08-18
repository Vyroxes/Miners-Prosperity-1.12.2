package net.vyroxes.minersprosperity.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import org.jetbrains.annotations.NotNull;

public class EnchantmentFarmersBlessing extends Enchantment
{

    public EnchantmentFarmersBlessing(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("farmers_blessing");
        this.setName("farmers_blessing");
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
        return 3;
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return stack.getItem() == Items.BOOK || stack.getItem() instanceof ItemHoe;
    }
}