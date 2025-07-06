package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.guis.GuiDiamondBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiEmeraldBackpack;
import net.vyroxes.minersprosperity.objects.items.DiamondBackpack;
import net.vyroxes.minersprosperity.objects.items.EmeraldBackpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;

public class ContainerEmeraldBackpack extends ContainerBackpack
{
    public ContainerEmeraldBackpack(InventoryPlayer playerInventory, ItemStack emeraldBackpackItemStack)
    {
        super(playerInventory, emeraldBackpackItemStack, 6);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        EmeraldBackpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        GuiEmeraldBackpack.playBackpackOpenSound();
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