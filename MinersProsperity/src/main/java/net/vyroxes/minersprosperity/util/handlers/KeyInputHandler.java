package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
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
                    boolean newState = toggleAutoCollect(itemStack);
                    
                    String statusMessage = TextFormatting.GRAY + "Auto-Collect: " + (newState ? TextFormatting.GREEN + "On" : TextFormatting.RED + "Off");
                    client.player.sendMessage(new TextComponentString(statusMessage));
                }
            }
        }
    }

    private static boolean toggleAutoCollect(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        if (!compound.hasKey("AutoCollect"))
        {
            compound.setBoolean("AutoCollect", false);
        }
        boolean autoCollect = compound.getBoolean("AutoCollect");
        boolean newState = !autoCollect;
        compound.setBoolean("AutoCollect", newState);
        itemStack.setTagCompound(compound);
        return newState;
    }
}