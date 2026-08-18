package net.vyroxes.minersprosperity.mixin;

import net.minecraft.block.BlockChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockChest.class)
public class MixinBlockChest {
    @Inject(method = "isBelowSolidBlock", at = @At("HEAD"), cancellable = true)
    public void isBelowSolidBlock(World worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.BLOCKS.allowOpeningVanillaChest) cir.setReturnValue(false);
    }
}