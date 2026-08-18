package net.vyroxes.minersprosperity.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpackBase;
import net.vyroxes.minersprosperity.integration.baubles.BaublesCompat;
import net.vyroxes.minersprosperity.gui.GuiHandler;

public class MessageOpenBackpack implements IMessage {
    public MessageOpenBackpack() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<MessageOpenBackpack, IMessage> {
        @Override
        public IMessage onMessage(MessageOpenBackpack message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                if (Loader.isModLoaded("baubles"))
                {
                    ItemStack baubleStack = BaublesCompat.getStackInSlot(player, 5);
                    if (!baubleStack.isEmpty() && baubleStack.getItem() instanceof ItemBackpackBase)
                    {
                        player.openGui(MinersProsperity.INSTANCE, GuiHandler.GuiTypes.BACKPACK.ordinal(), player.world, (int) player.posX, 1, (int) player.posZ);
                    }
                }
            });
            return null;
        }
    }
}