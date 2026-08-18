package net.vyroxes.minersprosperity.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ModArmor;
import org.jetbrains.annotations.NotNull;

public class TabArmor extends CreativeTabs
{
	public TabArmor(String label)
	{
		super("armor_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(ModArmor.EMERALD_CHESTPLATE);
	}
}