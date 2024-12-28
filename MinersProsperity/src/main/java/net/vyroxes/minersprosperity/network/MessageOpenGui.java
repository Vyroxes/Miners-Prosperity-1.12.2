package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.SidedItemStackHandler;

public class MessageOpenGui implements IMessage
{
    private int openGui;
    private BlockPos pos;

    public MessageOpenGui() {}

    public MessageOpenGui(int openGui, BlockPos pos)
    {
        this.openGui = openGui;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.openGui = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.openGui);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageOpenGui, IMessage>
    {
        @Override
        public IMessage onMessage(MessageOpenGui message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                        player.openGui(MinersProsperity.INSTANCE,
                        message.openGui,
                        player.world,
                        message.pos.getX(),
                        message.pos.getY(),
                        message.pos.getZ()
                );
            });
            return null;
        }
    }
}