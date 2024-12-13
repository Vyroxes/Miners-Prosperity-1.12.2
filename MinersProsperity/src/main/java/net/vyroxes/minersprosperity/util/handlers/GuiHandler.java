package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.vyroxes.minersprosperity.objects.containers.ContainerBackpack;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.objects.containers.ContainerIronBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiBackpack;
import net.vyroxes.minersprosperity.objects.guis.GuiCrusher;
import net.vyroxes.minersprosperity.objects.guis.GuiIronBackpack;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class GuiHandler implements IGuiHandler
{
	public static final int CRUSHER_GUI = 1;
    public static final int BACKPACK_GUI = 2;
    public static final int IRON_BACKPACK_GUI = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
    	if (ID == CRUSHER_GUI)
    	{
    		return new ContainerCrusher(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));
    	}
        if (ID == BACKPACK_GUI)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new ContainerBackpack(player.inventory, backpackItemStack);
        }
        if (ID == IRON_BACKPACK_GUI)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new ContainerIronBackpack(player.inventory, IronBackpackItemStack);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
    	if (ID == CRUSHER_GUI)
    	{
    		return new GuiCrusher(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));

    	}
        if (ID == BACKPACK_GUI)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new GuiBackpack(new ContainerBackpack(player.inventory, backpackItemStack));
        }
        if (ID == IRON_BACKPACK_GUI)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new GuiIronBackpack(new ContainerIronBackpack(player.inventory, IronBackpackItemStack));
        }
        return null;
    }
}