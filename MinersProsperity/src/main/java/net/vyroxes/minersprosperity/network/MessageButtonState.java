package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;

public class MessageButtonState implements IMessage
{
    private int buttonState;
    private BlockPos pos;

    public MessageButtonState() {}

    public MessageButtonState(int buttonState, BlockPos pos)
    {
        this.buttonState = buttonState;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.buttonState = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.buttonState);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageButtonState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageButtonState message, MessageContext ctx)
        {
            TileEntityCrusher tileEntity = (TileEntityCrusher) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                tileEntity.redstoneControlButtonState = message.buttonState;
                tileEntity.markDirty();
            }
            return null;
        }
    }
}