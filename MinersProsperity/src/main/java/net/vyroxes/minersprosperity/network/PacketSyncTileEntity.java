package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;

public class PacketSyncTileEntity implements IMessage
{
    private BlockPos pos;
    private EnumFacing facing;
    private int currentFace;

    public PacketSyncTileEntity() {}

    public PacketSyncTileEntity(BlockPos pos, EnumFacing facing, int currentFace)
    {
        this.pos = pos;
        this.facing = facing;
        this.currentFace = currentFace;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.facing = EnumFacing.byIndex(buf.readInt());
        this.currentFace = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.facing.getIndex());
        buf.writeInt(this.currentFace);
    }

    public static class Handler implements IMessageHandler<PacketSyncTileEntity, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSyncTileEntity message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te instanceof TileEntityAlloyFurnace)
                {
                    ((TileEntityAlloyFurnace) te).setFacing(message.facing);
                    ((TileEntityAlloyFurnace) te).setCurrentFace(message.currentFace);
                }
            });
            return null;
        }
    }
}