package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAutoCollect implements IMessage
{
    private boolean autoCollect;

    public MessageAutoCollect() {}

    public MessageAutoCollect(boolean autoCollect)
    {
        this.autoCollect = autoCollect;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.autoCollect = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.autoCollect);
    }

    public static class Handler implements IMessageHandler<MessageAutoCollect, IMessage>
    {
        @Override
        public IMessage onMessage(MessageAutoCollect message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack itemStack = player.getHeldItemMainhand();

                if (!itemStack.isEmpty() && itemStack.hasTagCompound())
                {
                    NBTTagCompound compound = itemStack.getTagCompound();
                    NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                    backpackData.setBoolean("AutoCollect", message.autoCollect);
                    compound.setTag("BackpackData", backpackData);
                    itemStack.setTagCompound(compound);
                }
            });
            return null;
        }
    }
}