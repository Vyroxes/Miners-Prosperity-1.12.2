package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.guis.GuiIronBackpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;

public class ContainerIronBackpack extends ContainerBackpack
{
    public ContainerIronBackpack(InventoryPlayer playerInventory, ItemStack ironBackpackItemStack)
    {
        super(playerInventory, ironBackpackItemStack, 3);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        IronBackpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        GuiIronBackpack.playBackpackOpenSound();
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