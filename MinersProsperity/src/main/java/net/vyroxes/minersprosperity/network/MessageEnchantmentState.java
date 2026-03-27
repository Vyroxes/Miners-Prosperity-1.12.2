package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

import java.util.Objects;

public class MessageEnchantmentState implements IMessage
{
    private boolean value;
    private String state;
    private int enchantmentID;

    public MessageEnchantmentState() {}

    public MessageEnchantmentState(boolean value, String state, int enchantmentID)
    {
        this.value = value;
        this.state = state;
        this.enchantmentID = enchantmentID;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.value = buf.readBoolean();
        this.state = ByteBufUtils.readUTF8String(buf);
        this.enchantmentID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.value);
        ByteBufUtils.writeUTF8String(buf, this.state);
        buf.writeInt(this.enchantmentID);
    }

    public static class Handler implements IMessageHandler<MessageEnchantmentState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageEnchantmentState message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack held = player.getHeldItemMainhand();
                ItemStack stack = null;

                if (Enchantment.getEnchantmentByID(message.enchantmentID) == EnchantmentInit.VACUUM)
                {
                    stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                }
                else if (Enchantment.getEnchantmentByID(message.enchantmentID) == EnchantmentInit.GLOW)
                {
                    stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                }

                if (!held.isEmpty() && EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(message.enchantmentID)), held) > 0)
                {
                    NBTTagCompound compound = held.getTagCompound();
                    compound.setBoolean(message.state, message.value);
                    held.setTagCompound(compound);
                }
                else if (stack != null && !stack.isEmpty() && EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(message.enchantmentID)), stack) > 0)
                {
                    NBTTagCompound compound = stack.getTagCompound();
                    compound.setBoolean(message.state, message.value);
                    stack.setTagCompound(compound);
                }

            });
            return null;
        }
    }
}