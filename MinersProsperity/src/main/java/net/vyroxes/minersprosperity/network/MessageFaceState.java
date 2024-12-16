package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;

public class MessageFaceState implements IMessage
{
    private int faceState;
    private BlockPos pos;
    private int face;

    public MessageFaceState() {}

    public MessageFaceState(int face, int faceState, BlockPos pos)
    {
        this.faceState = faceState;
        this.pos = pos;
        this.face = face;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.faceState = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.faceState);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public int getFace()
    {
        return this.face;
    }

    public static class Handler implements IMessageHandler<MessageFaceState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageFaceState message, MessageContext ctx)
        {
            TileEntityCrusher tileEntity = (TileEntityCrusher) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                if (message.getFace() == 0)
                {
                    tileEntity.northState = message.faceState;
                }
                else if (message.getFace() == 1)
                {
                    tileEntity.southState = message.faceState;
                }
                else if (message.getFace() == 2)
                {
                    tileEntity.eastState = message.faceState;
                }
                else if (message.getFace() == 3)
                {
                    tileEntity.westState = message.faceState;
                }
                else if (message.getFace() == 4)
                {
                    tileEntity.upState = message.faceState;
                }
                else if (message.getFace() == 5)
                {
                    tileEntity.downState = message.faceState;
                }

                tileEntity.markDirty();
            }
            return null;
        }
    }
}