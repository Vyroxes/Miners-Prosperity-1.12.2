package net.vyroxes.minersprosperity.util.handlers;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipHandler
{
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();
        ITooltipFlag flag = event.getFlags();

        if (ConfigHandler.showOreDictInTooltips == 1 && !stack.isEmpty())
        {
			int[] oreIDs = OreDictionary.getOreIDs(stack);

			if (oreIDs.length > 0)
			{
				tooltip.add(1, TextFormatting.GRAY + "OreDict:");
				for (int id : oreIDs)
				{
					String oreName = OreDictionary.getOreName(id);
					tooltip.add(2, TextFormatting.DARK_GRAY + " - " + oreName);
				}
			}
		}
		else if (ConfigHandler.showOreDictInTooltips == 2 && !stack.isEmpty() && flag.isAdvanced())
		{
			int[] oreIDs = OreDictionary.getOreIDs(stack);

			if (oreIDs.length > 0)
			{
				tooltip.add(1, TextFormatting.GRAY + "OreDict:");
				for (int id : oreIDs)
				{
					String oreName = OreDictionary.getOreName(id);
					tooltip.add(2, TextFormatting.DARK_GRAY + " - " + oreName);
				}
			}
		}

		if (ConfigHandler.showHarvestLevelInTooltips == 1 && !stack.isEmpty())
		{
			Item item = stack.getItem();
			if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemShears || item instanceof ItemPickaxe || item instanceof ItemSpade || item instanceof ItemAxe)
			{
				String[] toolClasses = {"pickaxe", "shovel", "axe"};

				for (String toolClass : toolClasses)
				{
					int harvestLevel = item.getHarvestLevel(stack, toolClass, null, null);
					if (harvestLevel >= 0)
					{
						tooltip.add(5, TextFormatting.GRAY + " " + harvestLevel + " Harvest Level");
					}
				}
			}
		}
		else if (ConfigHandler.showHarvestLevelInTooltips == 2 && !stack.isEmpty() && flag.isAdvanced())
		{
			Item item = stack.getItem();
			if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemShears || item instanceof ItemPickaxe || item instanceof ItemSpade || item instanceof ItemAxe)
			{
				String[] toolClasses = {"pickaxe", "shovel", "axe"};

				for (String toolClass : toolClasses)
				{
					int harvestLevel = item.getHarvestLevel(stack, toolClass, null, null);
					if (harvestLevel >= 0)
					{
						tooltip.add(5, TextFormatting.GRAY + " " + harvestLevel + " Harvest Level");
					}
				}
			}
		}
    }
}