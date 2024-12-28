package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAlloyFurnace extends TileEntityMachine
{
    public TileEntityAlloyFurnace()
    {
        super(new Builder()
                .setItemInput(2)
                .setItemEnergy(1)
                .setItemOutput(1)
                .setEnergy(20000, 200, 0, 0));
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.alloy_furnace");
    }

    @SideOnly(Side.CLIENT)
    public static boolean isPowered(TileEntityAlloyFurnace tileEntity)
    {
        return tileEntity.cookTime > 0;
    }
}

