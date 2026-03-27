package net.vyroxes.minersprosperity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.objects.items.ItemPortableCraftingTable;
import net.vyroxes.minersprosperity.util.compat.baubles.BaublesCompat;

public class MessageOpenPortableCraftingTable implements IMessage {
    public MessageOpenPortableCraftingTable() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<MessageOpenPortableCraftingTable, IMessage> {
        @Override
        public IMessage onMessage(MessageOpenPortableCraftingTable message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (Loader.isModLoaded("baubles"))
                {
                    ItemStack baubleStack = BaublesCompat.getStackInSlot(player, 6);
                    if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemPortableCraftingTable)
                    {
                        player.displayGui(new BlockWorkbench.InterfaceCraftingTable(player.world, new BlockPos(player)));
                    }
                }
            });
            return null;
        }
    }
}