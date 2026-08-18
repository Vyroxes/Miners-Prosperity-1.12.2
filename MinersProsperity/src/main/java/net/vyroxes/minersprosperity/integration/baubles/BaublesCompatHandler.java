package net.vyroxes.minersprosperity.integration.baubles;

import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpack;
import net.vyroxes.minersprosperity.item.ItemPortableCraftingTable;
import net.vyroxes.minersprosperity.item.ItemTotemOfUndying;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class BaublesCompatHandler
{
    public static final ResourceLocation BAUBLE_CAP_ID = new ResourceLocation(Tags.MODID, "bauble_cap");

    @SubscribeEvent
    public void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();

        if (stack.getItem() instanceof ItemBackpack)
        {
            event.addCapability(BAUBLE_CAP_ID, new IBaubleCapabilityProvider(new BaublesCapabilityProvider(BaubleType.BODY)));
        }
        else if (stack.getItem() instanceof ItemTotemOfUndying)
        {
            event.addCapability(BAUBLE_CAP_ID, new IBaubleCapabilityProvider(new BaublesCapabilityProvider(BaubleType.CHARM)));
        }
        else if (stack.getItem() instanceof ItemPortableCraftingTable)
        {
            event.addCapability(BAUBLE_CAP_ID, new IBaubleCapabilityProvider(new BaublesCapabilityProvider(BaubleType.CHARM)));
        }
    }
}