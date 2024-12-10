package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.vyroxes.minersprosperity.objects.containers.BackpackContainer;
import net.vyroxes.minersprosperity.objects.containers.CrusherContainer;
import net.vyroxes.minersprosperity.objects.containers.IronBackpackContainer;
import net.vyroxes.minersprosperity.objects.guis.BackpackGui;
import net.vyroxes.minersprosperity.objects.guis.CrusherGui;
import net.vyroxes.minersprosperity.objects.guis.IronBackpackGui;
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
    		return new CrusherContainer(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));
    	}
        if (ID == BACKPACK_GUI)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new BackpackContainer(player.inventory, backpackItemStack);
        }
        if (ID == IRON_BACKPACK_GUI)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new IronBackpackContainer(player.inventory, IronBackpackItemStack);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
    	if (ID == CRUSHER_GUI)
    	{
    		return new CrusherGui(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x,y,z)));

    	}
        if (ID == BACKPACK_GUI)
        {
            ItemStack backpackItemStack = player.getHeldItemMainhand();
            return new BackpackGui(new BackpackContainer(player.inventory, backpackItemStack));
        }
        if (ID == IRON_BACKPACK_GUI)
        {
            ItemStack IronBackpackItemStack = player.getHeldItemMainhand();
            return new IronBackpackGui(new IronBackpackContainer(player.inventory, IronBackpackItemStack));
        }
        return null;
    }
}