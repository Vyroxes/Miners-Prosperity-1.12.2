package net.vyroxes.minersprosperity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class ItemShearsCustom extends ItemShears {
    private final ShearsTier tier;

    public ItemShearsCustom(ShearsTier tier) {
        this.tier = tier;

        setTranslationKey(tier.registryName);
        setRegistryName(tier.registryName);
        setMaxStackSize(1);
        setMaxDamage(tier.durability);
        setCreativeTab(MinersProsperity.tools_tab);

        ModTools.TOOLS.add(this);
    }

    public ShearsTier getTier() {
        return tier;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull IBlockState state) {
        float vanillaSpeed = super.getDestroySpeed(stack, state);

        if(vanillaSpeed <= 1.0F) return vanillaSpeed;

        return vanillaSpeed * tier.speedMultiplier;
    }

    @Override
    public int getItemEnchantability() {
        return tier.enchantability;
    }

    @Override
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
    {
        return OreDictionary.containsMatch(false, OreDictionary.getOres(tier.repairMaterial, false), repair);
    }
}