package net.vyroxes.minersprosperity.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.jetbrains.annotations.NotNull;

public class EnchantmentVacuum extends Enchantment
{
    public EnchantmentVacuum(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("vacuum");
        this.setName("vacuum");

        EnchantmentInit.ENCHANTMENTS.add(this);
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
        return super.canApply(stack) || stack.getItem() == Items.BOOK || stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    }
}