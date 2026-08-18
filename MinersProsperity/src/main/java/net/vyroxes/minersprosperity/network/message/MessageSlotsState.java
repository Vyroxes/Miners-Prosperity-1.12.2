package net.vyroxes.minersprosperity.network.message;

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
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;

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
        SlotState.SlotCondition slotCondition = SlotState.SlotCondition.values()[buf.readByte()];
        this.slotsState = new SlotState(slotType, ingredientType, slotMode, slotOutputMode, slotCondition);
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
        buf.writeByte(this.slotsState.getSlotCondition().ordinal());
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

            TileEntity tileEntity = world.getTileEntity(message.pos);

            if (!(tileEntity instanceof TileEntityMachine machine)) return;
            if (!machine.isUsableByPlayer(playerEntity)) return;

            SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) machine.getSidedIngredientHandler(message.side);

            if (sidedIngredientHandler == null) return;
            if (message.id < 0 || sidedIngredientHandler.getSlotState(message.id) == null) return;

            sidedIngredientHandler.setSlotState(message.id, message.slotsState);
        }
    }
}