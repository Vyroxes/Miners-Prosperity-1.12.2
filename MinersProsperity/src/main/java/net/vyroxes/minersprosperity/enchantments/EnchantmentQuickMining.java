package net.vyroxes.minersprosperity.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.jetbrains.annotations.NotNull;

public class EnchantmentQuickMining extends Enchantment
{

    public EnchantmentQuickMining(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("quick_mining");
        this.setName("quick_mining");

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
        return super.canApply(stack) || stack.getItem() == Items.BOOK || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemShears || stack.getItem() instanceof ItemSword;
    }
}