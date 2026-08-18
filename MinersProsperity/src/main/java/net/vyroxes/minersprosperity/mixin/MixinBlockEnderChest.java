package net.vyroxes.minersprosperity.mixin;

import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEnderChest.class)
public class MixinBlockEnderChest {
    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    public void allowOpenThroughBlocks(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.BLOCKS.allowOpeningVanillaChest) return;

        InventoryEnderChest inv = playerIn.getInventoryEnderChest();
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityEnderChest) {
            if (worldIn.isRemote) {
                cir.setReturnValue(true);
                return;
            }

            inv.setChestTileEntity((TileEntityEnderChest) te);
            playerIn.displayGUIChest(inv);
            playerIn.addStat(StatList.ENDERCHEST_OPENED);

            cir.setReturnValue(true);
        }
    }
}