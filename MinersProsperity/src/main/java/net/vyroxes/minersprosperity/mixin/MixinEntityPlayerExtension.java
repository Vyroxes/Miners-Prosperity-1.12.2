package net.vyroxes.minersprosperity.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.vyroxes.minersprosperity.util.GlowingPlayerExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayerExtension implements GlowingPlayerExtension
{
    @Unique
    private final BlockPos.MutableBlockPos minersProsperity$glowPos = new BlockPos.MutableBlockPos();

    @Unique
    private boolean minersProsperity$glowActive;

    @Unique
    private boolean minersProsperity$glowUpdated = true;

    @Override
    public BlockPos.MutableBlockPos minersProsperity$getGlowPos()
    {
        return minersProsperity$glowPos;
    }

    @Override
    public void minersProsperity$setGlowPos(BlockPos pos)
    {
        minersProsperity$glowPos.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean minersProsperity$isGlowActive()
    {
        return minersProsperity$glowActive;
    }

    @Override
    public void minersProsperity$setGlowActive(boolean active)
    {
        minersProsperity$glowActive = active;
    }

    @Override
    public boolean minersProsperity$isGlowUpdated()
    {
        return minersProsperity$glowUpdated;
    }

    @Override
    public void minersProsperity$setGlowUpdated(boolean updated)
    {
        minersProsperity$glowUpdated = updated;
    }
}