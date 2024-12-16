package net.vyroxes.minersprosperity.util.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.network.MessageButtonState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.vyroxes.minersprosperity.network.MessageCurrentGuiId;
import net.vyroxes.minersprosperity.network.MessageFaceState;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("minersprosperity");

    public static void init(FMLInitializationEvent event)
    {
        int id = 0;
    	NETWORK.registerMessage(MessageButtonState.Handler.class, MessageButtonState.class, id++, Side.CLIENT);
    	NETWORK.registerMessage(MessageButtonState.Handler.class, MessageButtonState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageCurrentGuiId.Handler.class, MessageCurrentGuiId.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageCurrentGuiId.Handler.class, MessageCurrentGuiId.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageFaceState.Handler.class, MessageFaceState.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageFaceState.Handler.class, MessageFaceState.class, id++, Side.SERVER);
    }

    public static void sendButtonStateUpdate(int buttonState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageButtonState(buttonState, pos));
    }

    public static void sendCurrentGuiIdUpdate(int currentGuiId, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageCurrentGuiId(currentGuiId, pos));
    }

    public static void sendFaceStateUpdate(int face, int faceState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageFaceState(face, faceState, pos));
    }
}