package net.vyroxes.minersprosperity.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemFlintAndSteel.class)
public abstract class MixinItemFlintAndSteel extends Item
{
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
    {
        return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotIron"), repair);
    }
}