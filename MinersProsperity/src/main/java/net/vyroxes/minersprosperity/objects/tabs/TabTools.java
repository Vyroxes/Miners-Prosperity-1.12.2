package net.vyroxes.minersprosperity.objects.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ToolInit;
import org.jetbrains.annotations.NotNull;

public class TabTools extends CreativeTabs
{
	public TabTools(String label)
	{
		super("tools_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(ToolInit.EMERALD_PICKAXE);
	}
}