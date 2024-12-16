package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class TabTools extends CreativeTabs
{
	public TabTools(String label)
	{
		super("tools_tab");
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemInit.EMERALD_PICKAXE);
	}
}