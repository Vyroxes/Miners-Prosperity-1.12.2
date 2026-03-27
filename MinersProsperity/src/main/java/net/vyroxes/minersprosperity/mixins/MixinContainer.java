package net.vyroxes.minersprosperity.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.items.ItemPortableCraftingTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public class MixinContainer
{
    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir)
    {
        if (!(player.openContainer instanceof ContainerWorkbench))
        {
            return;
        }

        if (slotId < 0 || slotId >= player.openContainer.inventorySlots.size())
        {
            return;
        }

        Slot slot = player.openContainer.getSlot(slotId);
        ItemStack clickedStack = slot.getStack();
        ItemStack mainHandStack = player.getHeldItemMainhand();

        if (!clickedStack.isEmpty() && clickedStack == mainHandStack && clickedStack.getItem() instanceof ItemPortableCraftingTable)
        {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}