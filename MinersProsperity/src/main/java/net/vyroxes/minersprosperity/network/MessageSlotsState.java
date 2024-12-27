package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.SidedItemStackHandler;

public class MessageSlotsState implements IMessage
{
    private EnumFacing side;
    private int id;
    private SidedItemStackHandler.SlotState slotsState;
    private BlockPos pos;

    public MessageSlotsState() {}

    public MessageSlotsState(EnumFacing side, int id, SidedItemStackHandler.SlotState slotsState, BlockPos pos)
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
        SidedItemStackHandler.SlotState.SlotType slotType = SidedItemStackHandler.SlotState.SlotType.values()[buf.readByte()];
        SidedItemStackHandler.SlotState.IngredientType ingredientType = SidedItemStackHandler.SlotState.IngredientType.values()[buf.readByte()];
        SidedItemStackHandler.SlotState.SlotMode slotMode = SidedItemStackHandler.SlotState.SlotMode.values()[buf.readByte()];
        SidedItemStackHandler.SlotState.SlotOutputMode slotOutputMode = SidedItemStackHandler.SlotState.SlotOutputMode.values()[buf.readByte()];
        this.slotsState = new SidedItemStackHandler.SlotState(slotType, ingredientType, slotMode, slotOutputMode);
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
            TileEntityAlloyFurnace tileEntity = (TileEntityAlloyFurnace) ctx.getServerHandler().player.world.getTileEntity(message.pos);
            if (tileEntity != null)
            {
                SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(message.side);
                if (sidedItemStackHandler != null)
                {
                    sidedItemStackHandler.setSlotState(message.id, message.slotsState);
                }
            }
            return null;
        }
    }
}