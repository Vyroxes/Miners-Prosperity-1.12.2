package net.vyroxes.minersprosperity.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld
{
    @Shadow
    public List<EntityPlayer> playerEntities;

    @ModifyReturnValue(method = "getRawLight", at = @At("RETURN"))
    private int modifyGetLightReturnValue(int originalReturn, BlockPos pos)
    {
        BlockPos.MutableBlockPos playerHead = new BlockPos.MutableBlockPos();

        for (EntityPlayer player : playerEntities)
        {
            playerHead.setPos(player.posX, player.posY + player.getEyeHeight(), player.posZ);

            if (playerHead.equals(pos))
            {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0)
                {
                    if (originalReturn < 15)
                    {
                        return 15;
                    }
                    break;
                }
            }
        }

        return originalReturn;
    }
}