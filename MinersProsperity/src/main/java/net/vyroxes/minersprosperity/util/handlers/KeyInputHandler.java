package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler
{
    private static final KeyBinding toggleAutoCollectKey = new KeyBinding(
        "key.toggle_auto-collect", 
        KeyConflictContext.IN_GAME, 
        KeyModifier.NONE, 
        Keyboard.KEY_Z,
        "key.categories.backpacks"
    );

    public static void registerKeyBindings()
    {
        ClientRegistry.registerKeyBinding(toggleAutoCollectKey);
    }

    @SubscribeEvent
    public void onKeyInput(net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent event)
    {
        if (toggleAutoCollectKey.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                ItemStack itemStack = client.player.getHeldItemMainhand();

                if (!itemStack.isEmpty() && itemStack.getItem() instanceof Backpack)
                {
                    toggleAutoCollect(itemStack);
                }
            }
        }
    }

    private static void toggleAutoCollect(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        if (!compound.hasKey("BackpackData"))
        {
            NBTTagCompound backpackData = new NBTTagCompound();
            backpackData.setBoolean("AutoCollect", false);
            compound.setTag("BackpackData", backpackData);
        }
        else
        {
            NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");

            if (!backpackData.hasKey("AutoCollect"))
            {
                backpackData.setBoolean("AutoCollect", false);
            }

            compound.setTag("BackpackData", backpackData);
        }
        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
        boolean autoCollect = backpackData.getBoolean("AutoCollect");
        boolean newState = !autoCollect;
        NetworkHandler.sendAutoCollectUpdate(newState);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + I18n.format("tooltip.backpack.auto_collect") + " " + (newState ? TextFormatting.GREEN + I18n.format("tooltip.backpack.auto_collect.on") : TextFormatting.RED + I18n.format("tooltip.backpack.auto_collect.off"))));
    }
}