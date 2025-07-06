package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.guis.GuiGoldenBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiLeadBackpack;
import net.vyroxes.minersprosperity.objects.items.GoldenBackpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;
import net.vyroxes.minersprosperity.objects.items.LeadBackpack;

public class ContainerGoldenBackpack extends ContainerBackpack
{
    public ContainerGoldenBackpack(InventoryPlayer playerInventory, ItemStack goldenBackpackItemStack)
    {
        super(playerInventory, goldenBackpackItemStack, 4);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        GoldenBackpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        GuiGoldenBackpack.playBackpackOpenSound();
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