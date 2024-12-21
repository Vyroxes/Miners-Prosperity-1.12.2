package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;

public class MessageRedstoneControlButtonState implements IMessage
{
    private int redstoneControlButtonState;
    private BlockPos pos;

    public MessageRedstoneControlButtonState() {}

    public MessageRedstoneControlButtonState(int redstoneControlButtonState, BlockPos pos)
    {
        this.redstoneControlButtonState = redstoneControlButtonState;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.redstoneControlButtonState = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.redstoneControlButtonState);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageRedstoneControlButtonState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageRedstoneControlButtonState message, MessageContext ctx)
        {
            TileEntityAlloyFurnace tileEntity = (TileEntityAlloyFurnace) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                tileEntity.setRedstoneControlButtonState(message.redstoneControlButtonState);
                tileEntity.markDirty();
            }
            return null;
        }
    }
}