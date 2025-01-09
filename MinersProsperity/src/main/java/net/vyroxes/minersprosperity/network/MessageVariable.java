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
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;

public class MessageVariable implements IMessage
{
    private int id;
    private int variable;
    private BlockPos pos;

    public MessageVariable() {}

    public MessageVariable(int id, int variable, BlockPos pos)
    {
        this.id = id;
        this.variable = variable;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.variable = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        buf.writeInt(this.variable);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageVariable, IMessage>
    {
        @Override
        public IMessage onMessage(MessageVariable message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }
    }

    private static void handle(MessageVariable message, MessageContext ctx)
    {
        EntityPlayerMP playerEntity = ctx.getServerHandler().player;
        World world = playerEntity.getEntityWorld();

        if (!world.isBlockLoaded(message.pos)) return;

        TileEntity tileEntity = ctx.getServerHandler().player.world.getTileEntity(message.pos);
        if (tileEntity instanceof TileEntityMachine tileEntityMachine)
        {
            if (message.id == 0) tileEntityMachine.setRedstoneControlButtonState(message.variable);
            if (message.id == 1) tileEntityMachine.giveExperienceToPlayer(playerEntity);
        }
    }
}