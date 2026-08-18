package net.vyroxes.minersprosperity.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import org.jetbrains.annotations.NotNull;

public class EnchantmentFarReach extends Enchantment
{

    public EnchantmentFarReach(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("far_reach");
        this.setName("far_reach");
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 15;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return 100;
    }

    @Override
    public boolean canApply(@NotNull ItemStack stack) {
        return super.canApply(stack) || stack.getItem() == Items.BOOK || stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.LEGS;
    }
}