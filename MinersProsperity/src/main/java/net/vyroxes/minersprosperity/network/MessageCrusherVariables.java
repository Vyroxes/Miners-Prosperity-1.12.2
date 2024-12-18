package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;

public class MessageCrusherVariables implements IMessage
{
    private int[] crusherVariables;
    private BlockPos pos;

    public MessageCrusherVariables() {}

    public MessageCrusherVariables(int[] crusherVariables, BlockPos pos)
    {
        this.crusherVariables = crusherVariables;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int length = buf.readInt();
        this.crusherVariables = new int[length];

        for (int i = 0; i < length; i++)
        {
            this.crusherVariables[i] = buf.readInt();
        }

        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.crusherVariables.length);

        for (int value : this.crusherVariables)
        {
            buf.writeInt(value);
        }

        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageCrusherVariables, IMessage>
    {
        @Override
        public IMessage onMessage(MessageCrusherVariables message, MessageContext ctx)
        {
            TileEntityCrusher tileEntity = (TileEntityCrusher) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                tileEntity.crusherBurnTime = message.crusherVariables[0];
                tileEntity.currentItemBurnTime = message.crusherVariables[1];
                tileEntity.cookTime = message.crusherVariables[2];
                tileEntity.totalCookTime = message.crusherVariables[3];

                tileEntity.markDirty();
            }
            return null;
        }
    }
}