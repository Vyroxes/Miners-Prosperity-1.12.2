package net.vyroxes.minersprosperity.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModArmor;
import org.jetbrains.annotations.NotNull;

public class ItemHorseArmorCustom extends Item {
    private final HorseArmorTier tier;

    public ItemHorseArmorCustom(HorseArmorTier tier)
    {
        this.tier = tier;

        setTranslationKey(tier.registryName);
        setRegistryName(tier.registryName);
        setMaxStackSize(1);
        setCreativeTab(MinersProsperity.armour_tab);

        ModArmor.ARMORS.add(this);
    }

    public HorseArmorTier getTier()
    {
        return tier;
    }

    @Override
    public @NotNull HorseArmorType getHorseArmorType(@NotNull ItemStack stack)
    {
        return tier.armorType;
    }

    @Override
    public @NotNull String getHorseArmorTexture(@NotNull EntityLiving wearer, @NotNull ItemStack stack)
    {
        return tier.texture;
    }
}