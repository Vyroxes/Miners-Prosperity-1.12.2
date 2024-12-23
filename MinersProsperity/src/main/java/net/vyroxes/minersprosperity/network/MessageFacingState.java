package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;

public class MessageFacingState implements IMessage
{
    private EnumFacing facing;
    private BlockPos pos;

    public MessageFacingState() {}

    public MessageFacingState(EnumFacing facing, BlockPos pos)
    {
        this.facing = facing;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.facing = EnumFacing.byIndex(buf.readInt());
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.facing.getIndex());
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageFacingState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageFacingState message, MessageContext ctx)
        {
            TileEntityAlloyFurnace tileEntity = (TileEntityAlloyFurnace) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                tileEntity.setFacing(message.facing);
            }
            return null;
        }
    }
}