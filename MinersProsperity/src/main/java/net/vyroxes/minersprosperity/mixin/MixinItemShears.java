package net.vyroxes.minersprosperity.mixin;

import net.minecraft.item.*;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemShears.class)
public abstract class MixinItemShears extends Item
{
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
    {
        return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotIron"), repair);
    }
}