package net.vyroxes.minersprosperity.util.compat.baubles;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Optional;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.objects.items.ItemPortableCraftingTable;
import net.vyroxes.minersprosperity.util.handlers.AutoCollectHandler;
import net.vyroxes.minersprosperity.util.handlers.KeyInputHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "baubles.api.BaublesApi", modid = "baubles", striprefs = true)
public class BaublesCompat
{
    public static ItemStack tryGetTotemFromBaubles(EntityPlayer player)
    {
        ItemStack stack = baubles.api.BaublesApi.getBaublesHandler(player).getStackInSlot(6);
        if (!stack.isEmpty() && stack.getItem() == net.minecraft.init.Items.TOTEM_OF_UNDYING)
        {
            baubles.api.BaublesApi.getBaublesHandler(player).setStackInSlot(6, ItemStack.EMPTY);
            return stack.copy();
        }
        return ItemStack.EMPTY;
    }

    public static void tryEquipBauble(EntityPlayer player, ItemStack itemStack, boolean isRemote)
    {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        ItemStack stack = handler.getStackInSlot(6);
        ItemStack count = handler.insertItem(6, itemStack.copy(), true);

        if (!isRemote)
        {
            if (stack.isEmpty())
            {
                handler.insertItem(6, itemStack.copy(), false);
                itemStack.setCount(count.getCount());
            }
        }
        else if (stack.isEmpty())
        {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
        }
    }

    public static List<ItemStack> getBackpackFromBaubles(EntityPlayer player)
    {
        List<ItemStack> backpacks = new ArrayList<>();

        ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(5);
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBackpackBase)
        {
            backpacks.add(stack);
        }

        return backpacks;
    }

    public static ItemStack getBackpackStackFromBaubles(EntityPlayer player)
    {
        ItemStack bauble = BaublesApi.getBaublesHandler(player).getStackInSlot(5);
        if (!bauble.isEmpty() && bauble.getItem() instanceof ItemBackpackBase)
        {
            return bauble;
        }

        return ItemStack.EMPTY;
    }

    public static void toggleAutoCollect(EntityPlayerSP player)
    {
        ItemStack baubleStack = BaublesApi.getBaublesHandler(player).getStackInSlot(5);
        if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemBackpackBase)
        {
            KeyInputHandler.toggleAutoCollect(baubleStack);
        }
    }

    public static void sendOpenPortableCraftingTableUpdate(EntityPlayerSP player)
    {
        ItemStack baubleStack = BaublesApi.getBaublesHandler(player).getStackInSlot(6);
        if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemPortableCraftingTable)
        {
            NetworkHandler.sendOpenPortableCraftingTableUpdate();
        }
    }

    public static void sendOpenBackpackUpdate(EntityPlayerSP player)
    {
        ItemStack baubleStack = BaublesApi.getBaublesHandler(player).getStackInSlot(5);
        if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemBackpackBase)
        {
            NetworkHandler.sendOpenBackpackUpdate();
        }
    }

    public static boolean canInteractWith(EntityPlayer player)
    {
        ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(6);
        return !stack.isEmpty() && stack.getItem() instanceof ItemPortableCraftingTable;
    }

    public static ItemStack getStackInSlot(EntityPlayerMP player, int slot)
    {
        return BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
    }

    public static void setChanged(EntityPlayerMP player, int slot)
    {
        BaublesApi.getBaublesHandler(player).setChanged(slot, true);
    }

    public static boolean tryPickupToBackpackFromBaubles(EntityPlayer player, EntityItemPickupEvent event)
    {
        ItemStack baubleStack = BaublesApi.getBaublesHandler(player).getStackInSlot(5);
        if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemBackpackBase)
        {
            return AutoCollectHandler.tryPickupToBackpack(player, baubleStack, event);
        }

        return false;
    }
}