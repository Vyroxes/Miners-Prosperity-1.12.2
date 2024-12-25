package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.SidedItemHandler;

public class MessageSlotsState implements IMessage
{
    private EnumFacing side;
    private int id;
    private SidedItemHandler.SlotState slotsState;
    private BlockPos pos;

    public MessageSlotsState() {}

    public MessageSlotsState(EnumFacing side, int id, SidedItemHandler.SlotState slotsState, BlockPos pos)
    {
        this.side = side;
        this.id = id;
        this.slotsState = slotsState;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.side = EnumFacing.values()[buf.readInt()];
        this.id = buf.readInt();
        this.slotsState = SidedItemHandler.SlotState.values()[buf.readInt()];
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.side.ordinal());
        buf.writeInt(this.id);
        buf.writeInt(this.slotsState.ordinal());
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageSlotsState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSlotsState message, MessageContext ctx)
        {
            TileEntityAlloyFurnace tileEntity = (TileEntityAlloyFurnace) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                SidedItemHandler sidedItemHandler = (SidedItemHandler) tileEntity.getSidedItemHandler(message.side);
                if (sidedItemHandler != null)
                {
                    sidedItemHandler.setSlotState(message.id, message.slotsState);
                }
            }
            return null;
        }
    }
}