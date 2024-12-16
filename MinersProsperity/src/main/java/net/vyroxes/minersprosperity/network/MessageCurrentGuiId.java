package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;

public class MessageCurrentGuiId implements IMessage
{
    private int currentGuiId;
    private BlockPos pos;

    public MessageCurrentGuiId() {}

    public MessageCurrentGuiId(int currentGuiId, BlockPos pos)
    {
        this.currentGuiId = currentGuiId;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.currentGuiId = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.currentGuiId);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageCurrentGuiId, IMessage>
    {
        @Override
        public IMessage onMessage(MessageCurrentGuiId message, MessageContext ctx)
        {
            TileEntityCrusher tileEntity = (TileEntityCrusher) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                tileEntity.currentGuiId = message.currentGuiId;
                tileEntity.markDirty();
            }
            return null;
        }
    }
}