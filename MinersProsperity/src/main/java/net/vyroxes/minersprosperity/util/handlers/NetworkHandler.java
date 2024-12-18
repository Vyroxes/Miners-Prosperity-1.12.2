package net.vyroxes.minersprosperity.util.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.network.MessageButtonState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.vyroxes.minersprosperity.network.MessageCrusherVariables;
import net.vyroxes.minersprosperity.network.MessageSlotsState;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("minersprosperity");

    public static void init(FMLInitializationEvent event)
    {
        int id = 0;
    	NETWORK.registerMessage(MessageButtonState.Handler.class, MessageButtonState.class, id++, Side.CLIENT);
    	NETWORK.registerMessage(MessageButtonState.Handler.class, MessageButtonState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageCrusherVariables.Handler.class, MessageCrusherVariables.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageCrusherVariables.Handler.class, MessageCrusherVariables.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.SERVER);
    }

    public static void sendButtonStateUpdate(int buttonState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageButtonState(buttonState, pos));
    }

    public static void sendCrusherVariablesUpdate(int[] crusherVariables, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageCrusherVariables(crusherVariables, pos));
    }

    public static void sendSlotsStateUpdate(int[][] slotsState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageSlotsState(slotsState, pos));
    }
}