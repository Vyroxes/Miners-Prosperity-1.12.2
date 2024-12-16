package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.vyroxes.minersprosperity.objects.containers.ContainerBackpack;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.objects.containers.ContainerIronBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiCrusher;
import net.vyroxes.minersprosperity.objects.guis.GuiCrusherSlotsConfiguration;
import net.vyroxes.minersprosperity.objects.guis.GuiIronBackpack;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class GuiHandler implements IGuiHandler
{
	public static final int GUI_CRUSHER = ConfigHandler.GUI_CRUSHER;
    public static final int GUI_CRUSHER_SLOTS_CONFIGURATION = ConfigHandler.GUI_CRUSHER_SLOTS_CONFIGURATION;
    public static final int GUI_BACKPACK = ConfigHandler.GUI_BACKPACK;
    public static final int GUI_IRON_BACKPACK = ConfigHandler.GUI_IRON_BACKPACK;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
    	if (ID == GUI_CRUSHER)
    	{
    		return new ContainerCrusher(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
    	}
        if (ID == GUI_CRUSHER_SLOTS_CONFIGURATION)
        {
            return new ContainerCrusher(player.inventory, (TileEntityCrusher) Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
        }
        if (ID == GUI_BACKPACK)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new ContainerBackpack(player.inventory, backpackItemStack);
        }
        if (ID == GUI_IRON_BACKPACK)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new ContainerIronBackpack(player.inventory, IronBackpackItemStack);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
    	if (ID == GUI_CRUSHER)
    	{
    		return new GuiCrusher(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));
    	}
        if (ID == GUI_CRUSHER_SLOTS_CONFIGURATION)
        {
            return new GuiCrusherSlotsConfiguration(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));
        }
        if (ID == GUI_BACKPACK)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new GuiBackpack(new ContainerBackpack(player.inventory, backpackItemStack));
        }
        if (ID == GUI_IRON_BACKPACK)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new GuiIronBackpack(new ContainerIronBackpack(player.inventory, IronBackpackItemStack));
        }
        return null;
    }
}