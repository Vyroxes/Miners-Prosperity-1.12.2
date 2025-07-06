package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.guis.GuiIronBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiLeadBackpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;
import net.vyroxes.minersprosperity.objects.items.LeadBackpack;

public class ContainerLeadBackpack extends ContainerBackpack
{
    public ContainerLeadBackpack(InventoryPlayer playerInventory, ItemStack leadBackpackItemStack)
    {
        super(playerInventory, leadBackpackItemStack, 2);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        LeadBackpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        GuiLeadBackpack.playBackpackOpenSound();
        ItemStack held = playerIn.getHeldItemMainhand();
        if (!ItemStack.areItemsEqual(held, backpackItemStack))
        {
            held = playerIn.getHeldItemOffhand();
        }

        if (held.getItem() instanceof IronBackpack)
        {
            held.getOrCreateSubCompound("BackpackData").setBoolean("IsOpen", false);
        }
    }
}