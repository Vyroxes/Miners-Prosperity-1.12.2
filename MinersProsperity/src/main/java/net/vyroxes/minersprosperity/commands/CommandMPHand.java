package net.vyroxes.minersprosperity.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.oredict.OreDictionary;

public class CommandMPHand extends CommandBase
{
    @Override
    public String getName()
    {
        return "mp";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/mp hand";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1 && args[0].equals("hand") && sender instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack held = player.getHeldItemMainhand();

            if (held.isEmpty())
            {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "You're not holding any item."));
                return;
            }

            ResourceLocation registryName = held.getItem().getRegistryName();
            int meta = held.getMetadata();
            NBTTagCompound tag = held.getTagCompound();

            TextComponentString idMessage = new TextComponentString(
                    TextFormatting.GOLD + "Item ID: " + TextFormatting.AQUA + registryName.toString()
            );
            idMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, registryName.toString()));
            idMessage.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy")));
            idMessage.getStyle().setUnderlined(true);
            sender.sendMessage(idMessage);

            sender.sendMessage(new TextComponentString(
                    TextFormatting.GOLD + "Meta: " + TextFormatting.AQUA + meta
            ));

            if (tag != null)
            {
                TextComponentString nbtMessage = new TextComponentString(
                        TextFormatting.GOLD + "NBT: " + TextFormatting.AQUA + tag.toString()
                );
                nbtMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, tag.toString()));
                nbtMessage.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy")));
                nbtMessage.getStyle().setUnderlined(true);
                sender.sendMessage(nbtMessage);
            }
            else
            {
                sender.sendMessage(new TextComponentString(
                        TextFormatting.GOLD + "NBT: " + TextFormatting.GRAY + "none"
                ));
            }

            int[] oreIDs = OreDictionary.getOreIDs(held);
            if (oreIDs.length > 0)
            {
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "OreDict:"));
                for (int id : oreIDs)
                {
                    String oreName = OreDictionary.getOreName(id);
                    TextComponentString oreMsg = new TextComponentString(" - " + TextFormatting.AQUA + oreName);
                    oreMsg.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, oreName));
                    oreMsg.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy")));
                    oreMsg.getStyle().setUnderlined(true);
                    sender.sendMessage(oreMsg);
                }
            }
            else
            {
                sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "OreDict: " + TextFormatting.GRAY + "none"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp hand"));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}