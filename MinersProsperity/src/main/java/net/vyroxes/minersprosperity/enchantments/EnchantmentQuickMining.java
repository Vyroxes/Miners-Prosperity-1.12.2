package net.vyroxes.minersprosperity.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

public class EnchantmentQuickMining extends Enchantment
{

    public EnchantmentQuickMining(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.setRegistryName("quick_mining");
        this.setName("quick_mining");

        EnchantmentInit.ENCHANTMENTS.add(this);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemShears || stack.getItem() instanceof ItemSword;
    }
}