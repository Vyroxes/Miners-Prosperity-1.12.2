package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;

public class MessageSlotsState implements IMessage
{
    private int[][] slotsState;
    private BlockPos pos;

    public MessageSlotsState() {}

    public MessageSlotsState(int[][] slotsState, BlockPos pos)
    {
        this.slotsState = slotsState;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int rows = buf.readInt();

        this.slotsState = new int[rows][];

        for (int i = 0; i < rows; i++)
        {
            int cols = buf.readInt();

            this.slotsState[i] = new int[cols];

            for (int j = 0; j < cols; j++)
            {
                this.slotsState[i][j] = buf.readInt();
            }
        }
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(slotsState.length);

        for (int[] row : slotsState)
        {
            buf.writeInt(row.length);

            for (int value : row)
            {
                buf.writeInt(value);
            }
        }

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
                tileEntity.input1State = message.slotsState[0];
                tileEntity.input2State = message.slotsState[1];
                tileEntity.fuelState = message.slotsState[2];
                tileEntity.outputState = message.slotsState[3];

                tileEntity.markDirty();
            }
            return null;
        }
    }
}