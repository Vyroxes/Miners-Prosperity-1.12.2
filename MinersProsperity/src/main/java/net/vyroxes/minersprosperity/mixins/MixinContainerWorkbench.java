package net.vyroxes.minersprosperity.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraftforge.fml.common.Loader;
import net.vyroxes.minersprosperity.objects.items.ItemPortableCraftingTable;
import net.vyroxes.minersprosperity.util.compat.baubles.BaublesCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerWorkbench.class)
public abstract class MixinContainerWorkbench
{
    @Inject(method = "canInteractWith", at = @At("RETURN"), cancellable = true)
    private void onCanInteractWith(EntityPlayer player, CallbackInfoReturnable<Boolean> cir)
    {
        if (cir.getReturnValue()) return;

        if (player.getHeldItemMainhand().getItem() instanceof ItemPortableCraftingTable)
        {
            cir.setReturnValue(true);
        }
        else if (Loader.isModLoaded("baubles"))
        {
            cir.setReturnValue(BaublesCompat.canInteractWith(player));
        }
    }
}