package net.vyroxes.minersprosperity.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import net.vyroxes.minersprosperity.util.handlers.ModConfig;
import net.vyroxes.minersprosperity.util.interfaces.IGlowingPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;

@Mixin(World.class)
public abstract class MixinWorld
{
    @Final
    @Shadow
    public List<EntityPlayer> playerEntities;

    @Shadow
    public abstract boolean checkLightFor(EnumSkyBlock type, BlockPos pos);

    @ModifyReturnValue(method = "getRawLight", at = @At("RETURN"))
    private int modifyGetLightReturnValue(int originalReturn, BlockPos pos)
    {
        for (EntityPlayer player : playerEntities)
        {
            int px = (int) Math.floor(player.posX);
            int py = (int) Math.floor(player.posY + player.getEyeHeight());
            int pz = (int) Math.floor(player.posZ);

            if (px == pos.getX() && py == pos.getY() && pz == pos.getZ())
            {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                NBTTagCompound tag = helmet.getTagCompound();
                if (tag == null || !tag.getBoolean("Glow"))
                {
                    return originalReturn;
                }

                assert EnchantmentInit.GLOW != null;
                if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0)
                {
                    return Math.max(originalReturn, ModConfig.ENCHANTMENTS.glowLightValue);
                }
            }
        }

        return originalReturn;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci)
    {
        for (EntityPlayer player : playerEntities)
        {
            IGlowingPlayer glowing = (IGlowingPlayer) player;

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            assert EnchantmentInit.GLOW != null;
            boolean hasGlow = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0;
            BlockPos.MutableBlockPos glowPos = glowing.minersProsperity$getGlowPos();

            NBTTagCompound tag = helmet.getTagCompound();
            if (hasGlow && tag != null && tag.getBoolean("Glow"))
            {
                int newX = MathHelper.floor(player.posX);
                int newY = MathHelper.floor(player.posY + player.getEyeHeight());
                int newZ = MathHelper.floor(player.posZ);

                if (glowPos.getX() != newX || glowPos.getY() != newY || glowPos.getZ() != newZ)
                {
                    BlockPos oldPos = glowing.minersProsperity$getGlowPos().toImmutable();
                    checkLightFor(EnumSkyBlock.BLOCK, oldPos);
                    glowing.minersProsperity$setLastGlowPos(oldPos);

                    glowPos.setPos(newX, newY, newZ);
                    glowing.minersProsperity$setGlowUpdated(true);
                }

                if (glowing.minersProsperity$isGlowUpdated())
                {
                    checkLightFor(EnumSkyBlock.BLOCK, glowPos);
                    glowing.minersProsperity$setGlowUpdated(false);
                }
            }
            else
            {
                BlockPos last = glowing.minersProsperity$getLastGlowPos();

                if (last != null)
                {
                    checkLightFor(EnumSkyBlock.BLOCK, glowPos);
                    glowing.minersProsperity$setLastGlowPos(null);
                    glowPos.setPos(0, 0, 0);
                    glowing.minersProsperity$setGlowUpdated(true);
                }
            }
        }
    }
}