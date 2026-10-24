package net.vyroxes.minersprosperity.client.event;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpackBase;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class CraftingHandler
{
	@SubscribeEvent
	public static void onItemCrafted(ItemCraftedEvent event)
	{
		ItemStack crafted = event.crafting;

		if (crafted.getItem() instanceof ItemBackpackBase)
		{
			for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
			{
				ItemStack slotStack = event.craftMatrix.getStackInSlot(i);

				if (!slotStack.isEmpty() && slotStack.getItem() instanceof ItemBackpackBase && slotStack.hasTagCompound())
				{
					ItemStackHandler oldInventory = ItemBackpackBase.getBackpackInventory(slotStack);
					ItemStackHandler oldConfigurationInventory = ItemBackpackBase.getConfigurationInventory(slotStack);

					ItemStackHandler newInventory = ItemBackpackBase.getBackpackInventory(crafted);
					ItemStackHandler newConfigurationInventory = ItemBackpackBase.getConfigurationInventory(crafted);

					for (int slot = 0; slot < oldInventory.getSlots(); slot++)
					{
						ItemStack stackInSlot = oldInventory.extractItem(slot, oldInventory.getStackInSlot(slot).getCount(), true);
						if (!stackInSlot.isEmpty())
						{
							newInventory.insertItem(slot, stackInSlot.copy(), false);
						}
					}

					for (int slot = 0; slot < oldConfigurationInventory.getSlots(); slot++)
					{
						ItemStack stackInSlot = oldConfigurationInventory.extractItem(slot, oldConfigurationInventory.getStackInSlot(slot).getCount(), true);
						if (!stackInSlot.isEmpty())
						{
							newConfigurationInventory.insertItem(slot, stackInSlot.copy(), false);
						}
					}

					if (slotStack.hasTagCompound())
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

							if (slotStack.hasDisplayName())
							{
								crafted.setStackDisplayName(slotStack.getDisplayName());
							}

							break;
						}
					}
				}
			}
		}
	}
}