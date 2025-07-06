package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.vyroxes.minersprosperity.objects.items.Backpack;

public class CraftingHandler
{
	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event)
	{
		ItemStack crafted = event.crafting;

		if (crafted.getItem() instanceof Backpack)
		{
			for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
			{
				ItemStack slotStack = event.craftMatrix.getStackInSlot(i);

				if (!slotStack.isEmpty() && slotStack.getItem() instanceof Backpack && slotStack.hasTagCompound())
				{
					NBTTagCompound originalNBT = slotStack.getTagCompound();

					if (originalNBT != null)
					{
						NBTTagCompound newNBT = originalNBT.copy();

						if (originalNBT.hasKey("BackpackData"))
						{
							newNBT.setTag("BackpackData", originalNBT.getTag("BackpackData"));
						}

						crafted.setTagCompound(newNBT);
						crafted.setStackDisplayName(slotStack.getDisplayName());
						break;
					}
				}
			}
		}
	}
}