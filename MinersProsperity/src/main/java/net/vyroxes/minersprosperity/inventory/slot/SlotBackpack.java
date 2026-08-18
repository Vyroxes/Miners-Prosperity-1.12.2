package net.vyroxes.minersprosperity.inventory.slot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpackBase;
import net.vyroxes.minersprosperity.config.ModConfig;
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
        if (stack.isEmpty()) return false;

        if (!ModConfig.ITEMS.allowBackpackLooping && stack.getItem() instanceof ItemBackpackBase) return false;

        List<String> blacklist = Arrays.asList(ModConfig.ITEMS.backpackItemBlacklist);

        ResourceLocation id = Item.REGISTRY.getNameForObject(stack.getItem());

        if (id != null)
        {
            String modWildcard = id.getNamespace() + ":*";
            String baseId = id.toString();
            String fullId = baseId + "/" + stack.getMetadata();
            String itemWildcard = baseId + "/*";

            if (blacklist.contains(modWildcard)) return false;

            if (blacklist.contains(itemWildcard)) return false;

            if (blacklist.contains(fullId)) return false;

            if (blacklist.contains(baseId) && stack.getMetadata() == 0) return false;
        }

        int[] oreIds = OreDictionary.getOreIDs(stack);

        for (int oreId : oreIds)
        {
            String oreName = OreDictionary.getOreName(oreId);

            for (String entry : blacklist)
            {
                if (entry == null || entry.isEmpty() || entry.contains(":")) continue;

                if (entry.endsWith("*"))
                {
                    String prefix = entry.substring(0, entry.length() - 1);

                    if (oreName.startsWith(prefix)) return false;
                }
                else if (oreName.equals(entry)) return false;
            }
        }

        return true;
    }
}