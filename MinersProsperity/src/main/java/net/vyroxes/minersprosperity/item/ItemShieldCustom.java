package net.vyroxes.minersprosperity.item;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class ItemShieldCustom extends ItemShield {
    private final ShieldTier tier;

    public ItemShieldCustom(ShieldTier tier)
    {
        this.tier = tier;

        setTranslationKey(tier.registryName);
        setRegistryName(tier.registryName);
        setMaxDamage(tier.durability);
        setCreativeTab(MinersProsperity.tools_tab);

        ModTools.TOOLS.add(this);
    }

    public ShieldTier getTier()
    {
        return tier;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        return I18n.translateToLocal("item." + tier.registryName + ".name").trim();
    }

    @Override
    public int getItemEnchantability()
    {
        return tier.enchantability;
    }

    @Override
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
    {
        return OreDictionary.containsMatch(false, OreDictionary.getOres(tier.repairMaterial, false), repair);
    }
}