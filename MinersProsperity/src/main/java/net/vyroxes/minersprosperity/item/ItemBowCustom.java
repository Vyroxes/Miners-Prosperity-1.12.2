package net.vyroxes.minersprosperity.item;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class ItemBowCustom extends ItemBow
{
    private final BowTier tier;

    public ItemBowCustom(BowTier tier)
    {
        this.tier = tier;

        setTranslationKey(tier.registryName);
        setRegistryName(tier.registryName);
        setMaxStackSize(1);
        setMaxDamage(tier.durability);
        setCreativeTab(MinersProsperity.tools_tab);

        addPropertyOverride(new ResourceLocation(Tags.MODID, "pull"), (stack, world, entity) -> {
            if (entity == null) return 0.0F;
            if (entity.getActiveItemStack().getItem() != this) return 0.0F;

            return (float) (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20.0F;
        });

        addPropertyOverride(new ResourceLocation(Tags.MODID, "pulling"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack().getItem() == this ? 1.0F : 0.0F);

        ModTools.TOOLS.add(this);
    }

    @Override
    public @NotNull EntityArrow customizeArrow(@NotNull EntityArrow arrow)
    {
        EntityArrow customizedArrow = super.customizeArrow(arrow);
        customizedArrow.setDamage(customizedArrow.getDamage() * tier.damageMultiplier);

        return customizedArrow;
    }

    @Override
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
    {
        return OreDictionary.containsMatch(false, OreDictionary.getOres(tier.repairMaterial, false), repair);
    }

    @Override
    public int getItemEnchantability()
    {
        return tier.enchantability;
    }

    public BowTier getTier()
    {
        return tier;
    }
}