package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.util.compat.baubles.BaublesCompat;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class KeyInputHandler
{
    private static final KeyBinding toggleAutoCollectKey = new KeyBinding(
        "key.toggle_auto-collect", 
        KeyConflictContext.IN_GAME, 
        KeyModifier.NONE, 
        Keyboard.KEY_Z,
        "key.categories.miners_prosperity"
    );

    private static final KeyBinding toggleVaccum = new KeyBinding(
            "key.toggle_vacuum",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            Keyboard.KEY_V,
            "key.categories.miners_prosperity"
    );

    private static final KeyBinding toggleGlow = new KeyBinding(
            "key.toggle_glow",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            Keyboard.KEY_G,
            "key.categories.miners_prosperity"
    );

    private static final KeyBinding openPortableCraftingTable = new KeyBinding(
            "key.open_portable_crafting_table",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            Keyboard.KEY_C,
            "key.categories.miners_prosperity"
    );

    private static final KeyBinding openBackpack = new KeyBinding(
            "key.open_backpack",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            Keyboard.KEY_B,
            "key.categories.miners_prosperity"
    );

    public static void registerKeyBindings()
    {
        ClientRegistry.registerKeyBinding(toggleAutoCollectKey);
        ClientRegistry.registerKeyBinding(toggleVaccum);
        ClientRegistry.registerKeyBinding(toggleGlow);
        ClientRegistry.registerKeyBinding(openPortableCraftingTable);
        ClientRegistry.registerKeyBinding(openBackpack);
    }

    @SubscribeEvent
    public static void onKeyInput(net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent event)
    {
        if (toggleAutoCollectKey.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                ItemStack itemStack = client.player.getHeldItemMainhand();

                if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBackpackBase)
                {
                    toggleAutoCollect(itemStack);
                }
                else if ((itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemBackpackBase)))
                {
                    if (Loader.isModLoaded("baubles"))
                    {
                        BaublesCompat.toggleAutoCollect(client.player);
                    }
                }
            }
        }
        else if (toggleVaccum.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                EntityPlayer player = client.player;

                ItemStack held = client.player.getHeldItemMainhand();
                ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (!held.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, held) > 0)
                {
                    toggleVacuum(held);
                }
                else if (!boots.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) > 0)
                {
                    toggleVacuum(boots);
                }
            }
        }
        else if (toggleGlow.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                EntityPlayer player = client.player;

                ItemStack held = client.player.getHeldItemMainhand();
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!held.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, held) > 0)
                {
                    toggleGlow(held);
                }
                else if (!helmet.isEmpty() && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0)
                {
                    toggleGlow(helmet);
                }
            }
        }
        else if (openPortableCraftingTable.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                if (Loader.isModLoaded("baubles"))
                {
                    BaublesCompat.sendOpenPortableCraftingTableUpdate(client.player);
                }
            }
        }
        else if (openBackpack.isPressed())
        {
            Minecraft client = Minecraft.getMinecraft();
            if (client.player != null)
            {
                if (Loader.isModLoaded("baubles"))
                {
                    BaublesCompat.sendOpenBackpackUpdate(client.player);
                }
            }
        }
    }

    public static void toggleAutoCollect(ItemStack itemStack)
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
        NetworkHandler.sendBackpackStateUpdate(newState, "AutoCollect", true);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + I18n.format("tooltip.backpack.auto_collect") + " " + (newState ? TextFormatting.GREEN + I18n.format("tooltip.on") : TextFormatting.RED + I18n.format("tooltip.off"))));
    }

    private static void toggleVacuum(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        if (!compound.hasKey("Vacuum"))
        {
            compound.setBoolean("Vacuum", true);
            itemStack.setTagCompound(compound);
        }
        boolean vacuum = compound.getBoolean("Vacuum");
        boolean newState = !vacuum;
        NetworkHandler.sendVacuumStateUpdate(newState, "Vacuum", Enchantment.getEnchantmentID(EnchantmentInit.VACUUM));
        int vacuumLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, itemStack);
        String vacuumName = EnchantmentInit.VACUUM.getTranslatedName(vacuumLevel);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + vacuumName + ": " + (newState ? TextFormatting.GREEN + I18n.format("tooltip.on") : TextFormatting.RED + I18n.format("tooltip.off"))));
    }

    private static void toggleGlow(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        if (!compound.hasKey("Glow"))
        {
            compound.setBoolean("Glow", true);
            itemStack.setTagCompound(compound);
        }
        boolean glow = compound.getBoolean("Glow");
        boolean newState = !glow;
        NetworkHandler.sendVacuumStateUpdate(newState, "Glow", Enchantment.getEnchantmentID(EnchantmentInit.GLOW));
        int glowLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, itemStack);
        String glowName = EnchantmentInit.GLOW.getTranslatedName(glowLevel);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GRAY + glowName + ": " + (newState ? TextFormatting.GREEN + I18n.format("tooltip.on") : TextFormatting.RED + I18n.format("tooltip.off"))));
    }
}