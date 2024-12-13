package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.InventoryBackpack;

public class PickupHandler 
{
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
	    EntityPlayer player = event.getEntityPlayer();

	    for (int i = 0; i < player.inventory.getSizeInventory(); i++) 
	    {
	        ItemStack itemStack = player.inventory.getStackInSlot(i);

	        if (!itemStack.isEmpty() && itemStack.getItem() instanceof Backpack) 
	        {
	            NBTTagCompound compound = itemStack.getTagCompound();
	            if (compound != null && compound.getBoolean("AutoCollect")) 
	            {
	                EntityItem entityItem = event.getItem();
	                ItemStack itemStackToPickup = entityItem.getItem();

	                if (addToBackpack(player, itemStack, itemStackToPickup)) 
	                {
	                    event.setCanceled(true);
	                    entityItem.setDead();
	                    return;
	                }
	            }
	        }
	    }
	}


    private boolean addToBackpack(EntityPlayer player, ItemStack backpack, ItemStack itemStackToAdd) 
    {
        InventoryBackpack backpackInventory = Backpack.getBackpackInventory(backpack);

        //for (int i = 0; i < backpackInventory.getSizeInventory(); i++) 
        for (int i = 0; i < backpackInventory.getSlots(); i++) 
        {
            ItemStack slotStack = backpackInventory.getStackInSlot(i);

            if (slotStack.isEmpty()) 
            {
                //backpackInventory.setInventorySlotContents(i, itemStackToAdd.copy());
                backpackInventory.setStackInSlot(i, itemStackToAdd.copy());
                itemStackToAdd.setCount(0);
                Backpack.saveBackpackInventory(backpack, backpackInventory);
                return true;
            } 
            else if (ItemStack.areItemsEqual(slotStack, itemStackToAdd) && ItemStack.areItemStackTagsEqual(slotStack, itemStackToAdd)) 
            {
                int maxStackSize = slotStack.getMaxStackSize();
                int spaceLeft = maxStackSize - slotStack.getCount();

                if (spaceLeft >= itemStackToAdd.getCount()) 
                {
                    slotStack.grow(itemStackToAdd.getCount());
                    itemStackToAdd.setCount(0);
                    Backpack.saveBackpackInventory(backpack, backpackInventory);
                    return true;
                } 
                else 
                {
                    slotStack.grow(spaceLeft);
                    itemStackToAdd.shrink(spaceLeft);
                }
            }
        }
        
        player.sendMessage(new TextComponentString(TextFormatting.RED + "Your backpack is full!"));
        Backpack.saveBackpackInventory(backpack, backpackInventory);
        return itemStackToAdd.isEmpty();
    }
}