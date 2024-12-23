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
    	NETWORK.registerMessage(MessageRedstoneControlButtonState.Handler.class, MessageRedstoneControlButtonState.class, id++, Side.CLIENT);
    	NETWORK.registerMessage(MessageRedstoneControlButtonState.Handler.class, MessageRedstoneControlButtonState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageSlotsState.Handler.class, MessageSlotsState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageFacingState.Handler.class, MessageFacingState.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageFacingState.Handler.class, MessageFacingState.class, id++, Side.SERVER);
        NETWORK.registerMessage(MessageOpenGui.Handler.class, MessageOpenGui.class, id++, Side.CLIENT);
        NETWORK.registerMessage(MessageOpenGui.Handler.class, MessageOpenGui.class, id++, Side.SERVER);
        NETWORK.registerMessage(PacketSyncTileEntity.Handler.class, PacketSyncTileEntity.class, id++, Side.CLIENT);
    }

    public static void sendButtonStateUpdate(int redstoneControlButtonState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageRedstoneControlButtonState(redstoneControlButtonState, pos));
    }

    public static void sendFacingState(EnumFacing facing, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageFacingState(facing, pos));
    }

    public static void sendTileEntitySync(BlockPos pos, EnumFacing facing, int currentFace)
    {
        NETWORK.sendToAll(new PacketSyncTileEntity(pos, facing, currentFace));
    }

    public static void sendSlotsStateUpdate(int[][] slotsState, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageSlotsState(slotsState, pos));
    }

    public static void sendOpenGuiUpdate(int guiId, BlockPos pos)
    {
        NETWORK.sendToServer(new MessageOpenGui(guiId, pos));
    }
}