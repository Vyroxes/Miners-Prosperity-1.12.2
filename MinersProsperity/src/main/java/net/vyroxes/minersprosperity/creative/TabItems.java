package net.vyroxes.minersprosperity.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class TabItems extends CreativeTabs
{
	public TabItems(String label)
	{
		super("items_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(ModItems.DIAMOND_DUST);
	}
}