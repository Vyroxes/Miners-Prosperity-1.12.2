package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.util.handlers.ConfigHandler;

public class SlotBackpack extends SlotItemHandler
{

    public SlotBackpack(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return false;
        }

        if (!ConfigHandler.allowBackpackLooping && stack.getItem() instanceof Backpack)
        {
            return false;
        }

        ResourceLocation id = Item.REGISTRY.getNameForObject(stack.getItem());
        if (id != null)
        {
            String baseId = id.toString();
            String fullId = baseId + "/" + stack.getMetadata();

            boolean baseBlacklisted = ConfigHandler.backpackItemBlacklist.contains(baseId);
            boolean fullBlacklisted = ConfigHandler.backpackItemBlacklist.contains(fullId);

            if (fullBlacklisted)
            {
                return false;
            }
            else return !baseBlacklisted || stack.getMetadata() != 0;
        }

        return true;
    }
}