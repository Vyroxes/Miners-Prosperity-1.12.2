package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;

import java.util.Arrays;
import java.util.List;

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

        if (!ModConfig.BACKPACKS.allowBackpackLooping && stack.getItem() instanceof ItemBackpackBase)
        {
            return false;
        }

        ResourceLocation id = Item.REGISTRY.getNameForObject(stack.getItem());
        if (id != null)
        {
            String baseId = id.toString();
            String fullId = baseId + "/" + stack.getMetadata();

            List<String> blacklist = Arrays.asList(ModConfig.BACKPACKS.backpackItemBlacklist);

            boolean baseBlacklisted = blacklist.contains(baseId);
            boolean fullBlacklisted = blacklist.contains(fullId);

            if (fullBlacklisted)
            {
                return false;
            }
            else return !baseBlacklisted || stack.getMetadata() != 0;
        }

        return true;
    }
}