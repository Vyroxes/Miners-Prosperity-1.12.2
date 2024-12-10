package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.BlockInit;

public class BlocksTab extends CreativeTabs
{
	public BlocksTab(String label)
	{
		super("blocks_tab");
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(BlockInit.CRUSHER);
	}
}