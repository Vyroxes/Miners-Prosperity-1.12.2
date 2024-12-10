package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.vyroxes.minersprosperity.init.ItemInit;

public class CraftingHandler 
{
	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event)
	{
	    if (event.crafting.getItem() == ItemInit.IRON_BACKPACK) 
	    {
	        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) 
	        {
	            ItemStack slotStack = event.craftMatrix.getStackInSlot(i);
	            
	            if (!slotStack.isEmpty() && slotStack.getItem() == ItemInit.BACKPACK && slotStack.hasTagCompound()) 
	            {
	                NBTTagCompound nbt = slotStack.getTagCompound();
	                if (nbt != null) 
	                {
	                    event.crafting.setTagCompound(nbt.copy());
	                    break;
	                }
	            }
	        }
	    }
	}
}