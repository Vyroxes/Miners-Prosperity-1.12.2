package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.network.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("minersprosperity");

    public static void init(FMLInitializationEvent event)
    {
        int id = 0;
    	NETWORK.registerMessage(MessageVariable.Handler.class, MessageVariable.class, id++, Side.CLIENT);
    	NETWORK.registerMessage(MessageVariable.Handler.class, MessageVariable.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageOpenGui.Handler.class, MessageOpenGui.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageOpenGui.Handler.class, MessageOpenGui.class, id++, Side.SERVER);
    }

    public static void sendVariableUpdate(int id, int variable, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageVariable(id, variable, pos));
    }

    public static void sendSlotsStateUpdate(EnumFacing side, int id, SlotState slotsState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageSlotsState(side, id, slotsState, pos));
    }

    public static void sendOpenGuiUpdate(int guiId, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageOpenGui(guiId, pos));
    }
}