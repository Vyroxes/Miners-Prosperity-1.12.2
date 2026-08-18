package net.vyroxes.minersprosperity.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandMP extends CommandBase
{
    public static final String USAGE =
        TextFormatting.RED + "Usage:\n" +
        TextFormatting.RED + "- /mp hand\n" +
        TextFormatting.RED + "- /mp durability <durability>\n" +
        TextFormatting.RED + "- /mp health <health>\n" +
        TextFormatting.RED + "- /mp hunger <hunger>\n" +
        TextFormatting.RED + "- /mp xp [level]";

    @Override
    public @NotNull String getName()
    {
        return "mp";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender)
    {
        return USAGE;
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length >= 1 && args[0].equals("hand") && sender instanceof EntityPlayer player)
        {
            ItemStack held = player.getHeldItemMainhand();

            if (held.isEmpty())
            {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "You're not holding any item."));
                return;
            }

            ResourceLocation registryName = held.getItem().getRegistryName();
            int meta = held.getMetadata();
            NBTTagCompound tag = held.getTagCompound();

            assert registryName != null;
            TextComponentString idMessage = new TextComponentString(TextFormatting.GOLD + "Item ID: " + TextFormatting.AQUA + registryName.toString());
            idMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, registryName.toString()));
            idMessage.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy")));
            idMessage.getStyle().setUnderlined(true);
            sender.sendMessage(idMessage);

            sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Meta: " + TextFormatting.AQUA + meta));

            if (tag != null)
            {
                TextComponentString nbtMessage = new TextComponentString(TextFormatting.GOLD + "NBT: " + TextFormatting.AQUA + tag.toString());
                nbtMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, tag.toString()));
                nbtMessage.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy")));
                nbtMessage.getStyle().setUnderlined(true);
                sender.sendMessage(nbtMessage);
            }
            else sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "NBT: " + TextFormatting.GRAY + "none"));

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
            else sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "OreDict: " + TextFormatting.GRAY + "none"));
        }
        else if (args.length >= 1 && args[0].equals("durability") && sender instanceof EntityPlayer player)
        {
            if (args.length == 2) {
                ItemStack held = player.getHeldItemMainhand();

                if (held.isEmpty()) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "You're not holding any item."));
                    return;
                }

                if (!held.isItemStackDamageable()) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "You're not holding a damageable item."));
                    return;
                }

                try {
                    int durability = Integer.parseInt(args[1]);
                    int max = held.getMaxDamage() + 1;

                    if (durability > max)
                        durability = max;
                    else if (durability < 1)
                        durability = 1;

                    held.setItemDamage(max - durability);
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp durability <durability>"));
                }
            }
            else sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp durability <durability>"));
        }
        else if (args.length >= 1 && args[0].equals("health") && sender instanceof EntityPlayer player)
        {
            if (args.length == 2)
                try
                {
                    float health = Math.max(1, Float.parseFloat(args[1]));

                    player.setHealth(Math.min(health, player.getMaxHealth()));
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp health <0-20+>"));
                }
            else sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp health <0-20+>"));
        }
        else if (args.length >= 1 && args[0].equals("hunger") && sender instanceof EntityPlayer player)
        {
            if (args.length == 2)
                try
                {
                    int hunger = Integer.parseInt(args[1]);

                    hunger = Math.max(0, Math.min(20, hunger));

                    player.getFoodStats().setFoodLevel(hunger);
                    player.getFoodStats().setFoodSaturationLevel(hunger);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp hunger <0-20>"));
                }
            else sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp hunger <0-20>"));
        }
        else if (args.length >= 1 && args[0].equals("xp") && sender instanceof EntityPlayer player)
        {
            if (args.length == 1) sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "XP: " + TextFormatting.AQUA + player.experienceTotal));
            else if (args.length == 2)
            {
                try
                {
                    int level = Math.max(0, Integer.parseInt(args[1]));

                    player.experience = 0.0F;
                    player.experienceLevel = 0;
                    player.experienceTotal = 0;

                    player.addExperienceLevel(level);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp xp [level]"));
                }
            }
            else sender.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /mp xp [level]"));
        }
        else sender.sendMessage(new TextComponentString(USAGE));
    }

    @Override
    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, "hand", "durability", "health", "hunger", "xp");

        return Collections.emptyList();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
}