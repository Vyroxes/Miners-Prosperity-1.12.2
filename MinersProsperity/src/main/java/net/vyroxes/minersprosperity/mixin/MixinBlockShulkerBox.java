package net.vyroxes.minersprosperity.mixin;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockShulkerBox.class)
public class MixinBlockShulkerBox {
    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    public void allowOpenThroughBlocks(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.BLOCKS.allowOpeningVanillaChest) return;

        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityShulkerBox) {
            if (worldIn.isRemote) {
                cir.setReturnValue(true);
                return;
            }

            playerIn.displayGUIChest((IInventory)te);
            playerIn.addStat(StatList.OPEN_SHULKER_BOX);

            cir.setReturnValue(true);
        }
    }
}