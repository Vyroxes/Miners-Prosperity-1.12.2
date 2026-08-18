package net.vyroxes.minersprosperity.mixin;

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
import net.vyroxes.minersprosperity.init.ModEnchantments;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.util.GlowingPlayerExtension;
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
                if (tag == null || !tag.getBoolean("Glow")) return originalReturn;

                assert ModEnchantments.GLOW != null;
                if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.GLOW, helmet) > 0) return Math.max(originalReturn, ModConfig.ENCHANTMENTS.glowLightValue);
            }
        }

        return originalReturn;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        for (EntityPlayer player : playerEntities) {
            GlowingPlayerExtension glowing = (GlowingPlayerExtension) player;

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            boolean hasGlow = ModEnchantments.GLOW!=null && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.GLOW, helmet) > 0;

            NBTTagCompound tag = helmet.getTagCompound();

            boolean glowEnabled = hasGlow && tag != null && tag.getBoolean("Glow");

            BlockPos.MutableBlockPos glowPos = glowing.minersProsperity$getGlowPos();

            if (glowEnabled) {
                int newX = MathHelper.floor(player.posX);
                int newY = MathHelper.floor(player.posY + player.getEyeHeight());
                int newZ = MathHelper.floor(player.posZ);

                if (!glowing.minersProsperity$isGlowActive()) {
                    glowPos.setPos(newX, newY, newZ);

                    glowing.minersProsperity$setGlowActive(true);
                    glowing.minersProsperity$setGlowUpdated(true);
                }
                else if (glowPos.getX()!=newX || glowPos.getY()!=newY || glowPos.getZ()!=newZ) {
                    BlockPos oldPos = glowPos.toImmutable();

                    glowPos.setPos(newX, newY, newZ);

                    checkLightFor(EnumSkyBlock.BLOCK, oldPos);

                    glowing.minersProsperity$setGlowUpdated(true);
                }

                if (glowing.minersProsperity$isGlowUpdated()) {
                    checkLightFor(EnumSkyBlock.BLOCK, glowPos);

                    glowing.minersProsperity$setGlowUpdated(false);
                }
            }
            else if (glowing.minersProsperity$isGlowActive()) {
                BlockPos oldGlowPos = glowPos.toImmutable();

                glowing.minersProsperity$setGlowActive(false);
                glowing.minersProsperity$setGlowUpdated(true);

                glowPos.setPos(0, 0, 0);

                checkLightFor(EnumSkyBlock.BLOCK, oldGlowPos);
            }
        }
    }
}