package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class ArmourTab extends CreativeTabs
{
	public ArmourTab(String label)
	{
		super("armour_tab");
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemInit.EMERALD_CHESTPLATE);
	}
}