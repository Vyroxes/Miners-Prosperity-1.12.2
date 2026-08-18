package net.vyroxes.minersprosperity.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;

public class SlotMachineOutput extends SlotItemHandler
{
    private final TileEntityMachine tileEntity;

    public SlotMachineOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition, TileEntityMachine tileEntity)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) { return false; }

    @Override
    public @NotNull ItemStack onTake(@NotNull EntityPlayer entityPlayer, @NotNull ItemStack stack)
    {
        this.tileEntity.giveExperienceToPlayer(entityPlayer);
        return super.onTake(entityPlayer, stack);
    }
}