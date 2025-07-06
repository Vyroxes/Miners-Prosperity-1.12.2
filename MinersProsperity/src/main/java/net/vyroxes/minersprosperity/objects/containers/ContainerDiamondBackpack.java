package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.guis.GuiDiamondBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiGoldenBackpack;
import net.vyroxes.minersprosperity.objects.items.DiamondBackpack;
import net.vyroxes.minersprosperity.objects.items.GoldenBackpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;

public class ContainerDiamondBackpack extends ContainerBackpack
{
    public ContainerDiamondBackpack(InventoryPlayer playerInventory, ItemStack diamondBackpackItemStack)
    {
        super(playerInventory, diamondBackpackItemStack, 5);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        DiamondBackpack.saveBackpackInventory(backpackItemStack, backpackInventory);
        GuiDiamondBackpack.playBackpackOpenSound();
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