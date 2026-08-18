package net.vyroxes.minersprosperity.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.integration.baubles.BaublesCompat;

public class MessageBackpackState implements IMessage
{
    private boolean value;
    private String state;
    private boolean fromBaubles;

    public MessageBackpackState() {}

    public MessageBackpackState(boolean value, String state, boolean fromBaubles)
    {
        this.value = value;
        this.state = state;
        this.fromBaubles = fromBaubles;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.value = buf.readBoolean();
        this.state = ByteBufUtils.readUTF8String(buf);
        this.fromBaubles = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.value);
        ByteBufUtils.writeUTF8String(buf, this.state);
        buf.writeBoolean(this.fromBaubles);
    }

    public static class Handler implements IMessageHandler<MessageBackpackState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageBackpackState message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (message.fromBaubles)
                {
                    if (Loader.isModLoaded("baubles"))
                    {
                        ItemStack itemStack = BaublesCompat.getStackInSlot(player, 5);
                        if (!itemStack.isEmpty() && itemStack.hasTagCompound())
                        {
                            NBTTagCompound compound = itemStack.getTagCompound();
                            assert compound != null;
                            NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                            backpackData.setBoolean(message.state, message.value);
                            compound.setTag("BackpackData", backpackData);
                            itemStack.setTagCompound(compound);
                            BaublesCompat.setChanged(player, 5);
                        }
                    }
                }
                else
                {
                    ItemStack itemStack = player.getHeldItemMainhand();
                    if (!itemStack.isEmpty() && itemStack.hasTagCompound())
                    {
                        NBTTagCompound compound = itemStack.getTagCompound();
                        assert compound != null;
                        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                        backpackData.setBoolean(message.state, message.value);
                        compound.setTag("BackpackData", backpackData);
                        itemStack.setTagCompound(compound);
                    }
                }
            });

            return null;
        }
    }
}