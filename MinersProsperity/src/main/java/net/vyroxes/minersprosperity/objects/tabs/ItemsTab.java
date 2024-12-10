package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class ItemsTab extends CreativeTabs
{
	public ItemsTab(String label)
	{
		super("items_tab");
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemInit.BACKPACK);
	}
}