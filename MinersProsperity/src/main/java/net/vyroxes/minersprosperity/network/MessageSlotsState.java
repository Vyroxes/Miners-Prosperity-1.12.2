package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.SidedItemStackHandler;
import net.vyroxes.minersprosperity.util.handlers.SlotState;

public class MessageSlotsState implements IMessage
{
    private EnumFacing side;
    private int id;
    private SlotState slotsState;
    private BlockPos pos;

    public MessageSlotsState() {}

    public MessageSlotsState(EnumFacing side, int id, SlotState slotsState, BlockPos pos)
    {
        this.side = side;
        this.id = id;
        this.slotsState = slotsState;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.side = EnumFacing.values()[buf.readByte()];
        this.id = buf.readInt();
        SlotState.SlotType slotType = SlotState.SlotType.values()[buf.readByte()];
        SlotState.IngredientType ingredientType = SlotState.IngredientType.values()[buf.readByte()];
        SlotState.SlotMode slotMode = SlotState.SlotMode.values()[buf.readByte()];
        SlotState.SlotOutputMode slotOutputMode = SlotState.SlotOutputMode.values()[buf.readByte()];
        this.slotsState = new SlotState(slotType, ingredientType, slotMode, slotOutputMode);
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.side.ordinal());
        buf.writeInt(this.id);
        buf.writeByte(this.slotsState.getSlotType().ordinal());
        buf.writeByte(this.slotsState.getIngredientType().ordinal());
        buf.writeByte(this.slotsState.getSlotMode().ordinal());
        buf.writeByte(this.slotsState.getSlotOutputMode().ordinal());
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static class Handler implements IMessageHandler<MessageSlotsState, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSlotsState message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(MessageSlotsState message, MessageContext ctx)
        {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();

            if (!world.isBlockLoaded(message.pos)) return;

            TileEntity tileEntity = ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity instanceof TileEntityAlloyFurnace tileEntityAlloyFurnace)
            {
                SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntityAlloyFurnace.getSidedItemHandler(message.side);
                if (sidedItemStackHandler != null)
                {
                    sidedItemStackHandler.setSlotState(message.id, message.slotsState);
                }
            }
        }
    }
}