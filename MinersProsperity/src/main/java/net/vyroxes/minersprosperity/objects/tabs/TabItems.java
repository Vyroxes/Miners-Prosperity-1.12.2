package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;
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
		return new ItemStack(ItemInit.DIAMOND_DUST);
	}
}