package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpackBase;
import net.vyroxes.minersprosperity.integration.baubles.BaublesCompat;

@Mod.EventBusSubscriber
public class AutoCollectHandler
{
	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event)
	{
	    EntityPlayer player = event.getEntityPlayer();

        if (Loader.isModLoaded("baubles"))
        {
            if (BaublesCompat.tryPickupToBackpackFromBaubles(player, event))
                return;
        }

	    for (int i = 0; i < player.inventory.getSizeInventory(); i++) 
	    {
	        ItemStack itemStack = player.inventory.getStackInSlot(i);

            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBackpackBase)
            {
                if (tryPickupToBackpack(player, itemStack, event))
                {
                    return;
                }
            }
	    }
	}

    public static boolean tryPickupToBackpack(EntityPlayer player, ItemStack backpackStack, EntityItemPickupEvent event)
    {
        NBTTagCompound compound = backpackStack.getTagCompound();
        if (compound == null)
        {
            return false;
        }
        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
        boolean autoCollect = backpackData.getBoolean("AutoCollect");

        if (autoCollect)
        {
            EntityItem entityItem = event.getItem();
            ItemStack itemStackToPickup = entityItem.getItem();

            if (addToBackpack(player, backpackStack, itemStackToPickup))
            {
                event.setCanceled(true);
                entityItem.setDead();
                return true;
            }
        }
        return false;
    }

    private static boolean matchesFilter(ItemStack filterStack, ItemStack input, boolean useMetadata, boolean useNBT, boolean useDamage, boolean useOreDict, boolean useModID)
    {
        if (filterStack.isEmpty() || input.isEmpty())
            return false;

        boolean itemMatches = filterStack.getItem() == input.getItem();

        if (!useOreDict && !useModID && !itemMatches)
            return false;

        if (useMetadata && filterStack.getMetadata() != input.getMetadata())
            return false;

        if (useDamage && filterStack.getItemDamage() != input.getItemDamage())
            return false;

        if (useNBT && !ItemStack.areItemStackTagsEqual(filterStack, input))
            return false;

        if (useOreDict)
        {
            int[] filterIDs = OreDictionary.getOreIDs(filterStack);
            int[] inputIDs = OreDictionary.getOreIDs(input);

            boolean anyMatch = false;
            for (int id1 : filterIDs)
            {
                for (int id2 : inputIDs)
                {
                    if (id1 == id2)
                    {
                        anyMatch = true;
                        break;
                    }
                }
                if (anyMatch)
                    break;
            }
            if (!anyMatch)
                return false;
        }

        if (useModID)
        {
            String modFilter = filterStack.getItem().getRegistryName().getNamespace();
            String modInput = input.getItem().getRegistryName().getNamespace();
            return modFilter.equals(modInput);
        }

        return true;
    }

    public static boolean addToBackpack(EntityPlayer player, ItemStack backpack, ItemStack itemStackToAdd)
    {
        NBTTagCompound backpackData = backpack.getTagCompound().getCompoundTag("BackpackData");

        boolean whitelist = backpackData.getBoolean("WhitelistOrBlacklist");
        boolean autoCollectMode = backpackData.getBoolean("AutoCollectMode");
        boolean useMetadata = backpackData.getBoolean("Metadata");
        boolean useNBT = backpackData.getBoolean("NBT");
        boolean useDamage = backpackData.getBoolean("Damage");
        boolean useOreDict = backpackData.getBoolean("OreDict");
        boolean useModID = backpackData.getBoolean("ModID");

        ItemStackHandler backpackInventory = ItemBackpackBase.getBackpackInventory(backpack);

        if (autoCollectMode)
        {
            boolean matchedExisting = false;

            for (int i = 0; i < backpackInventory.getSlots(); i++)
            {
                ItemStack existing = backpackInventory.getStackInSlot(i);
                if (matchesFilter(existing, itemStackToAdd, useMetadata, useNBT, useDamage, useOreDict, useModID))
                {
                    matchedExisting = true;
                    break;
                }
            }

            if (!matchedExisting)
                return false;
        }

        ItemStackHandler configurationInventory = ItemBackpackBase.getConfigurationInventory(backpack);
        boolean matchedFilter = false;

        for (int i = 0; i < configurationInventory.getSlots(); i++)
        {
            ItemStack filterStack = configurationInventory.getStackInSlot(i);
            if (matchesFilter(filterStack, itemStackToAdd, useMetadata, useNBT, useDamage, useOreDict, useModID))
            {
                matchedFilter = true;
                break;
            }
        }

        if (whitelist && !matchedFilter)
            return false;

        if (!whitelist && matchedFilter)
            return false;

        for (int i = 0; i < backpackInventory.getSlots(); i++)
        {
            ItemStack slotStack = backpackInventory.getStackInSlot(i);

            if (slotStack.isEmpty())
            {
                backpackInventory.setStackInSlot(i, itemStackToAdd.copy());
                itemStackToAdd.setCount(0);
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
        return itemStackToAdd.isEmpty();
    }
}