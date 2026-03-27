package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.BlockInit;
import org.jetbrains.annotations.NotNull;

public class TabBlocks extends CreativeTabs
{
	public TabBlocks(String label)
	{
		super("blocks_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(BlockInit.ALLOY_FURNACE);
	}
}