package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.objects.containers.ContainerBackpack;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.containers.ContainerIronBackpack;
import net.vyroxes.minersprosperity.objects.guis.*;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class GuiHandler implements IGuiHandler
{

    public enum GuiTypes
    {
        CRUSHER,
        CRUSHER_SLOTS_CONFIGURATION,
        CRUSHER_SLOT_CONFIGURATION,
        BACKPACK,
        IRON_BACKPACK;

        public static GuiTypes fromId(int id)
        {
            if (id < 0 || id >= values().length)
            {
                throw new IllegalArgumentException("Invalid GUI ID: " + id);
            }
            return values()[id];
        }
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        return switch (GuiTypes.fromId(id))
        {
            case CRUSHER -> new ContainerCrusher(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(pos)));
            case CRUSHER_SLOTS_CONFIGURATION, CRUSHER_SLOT_CONFIGURATION -> new ContainerInventory(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(pos)));
            case BACKPACK -> new ContainerBackpack(player.inventory, player.getHeldItemMainhand());
            case IRON_BACKPACK -> new ContainerIronBackpack(player.inventory, player.getHeldItemMainhand());
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        return switch (GuiTypes.fromId(id))
        {
            case CRUSHER -> new GuiCrusher(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(pos)));
            case CRUSHER_SLOTS_CONFIGURATION -> new GuiCrusherSlotsConfiguration(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(pos)));
            case CRUSHER_SLOT_CONFIGURATION -> new GuiCrusherSlotConfiguration(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(pos)));
            case BACKPACK -> new GuiBackpack(new ContainerBackpack(player.inventory, player.getHeldItemMainhand()));
            case IRON_BACKPACK -> new GuiIronBackpack(new ContainerIronBackpack(player.inventory, player.getHeldItemMainhand()));
        };
    }
}