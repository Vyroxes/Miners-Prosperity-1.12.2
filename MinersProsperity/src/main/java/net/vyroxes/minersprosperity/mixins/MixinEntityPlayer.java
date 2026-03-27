package net.vyroxes.minersprosperity.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.vyroxes.minersprosperity.util.interfaces.IGlowingPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer implements IGlowingPlayer
{
    @Unique
    private BlockPos.MutableBlockPos minersProsperity$glowPos = new BlockPos.MutableBlockPos();

    @Unique
    private final BlockPos minersProsperity$lastGlowPos = new BlockPos.MutableBlockPos();

    @Unique
    private boolean minersProsperity$glowUpdated = true;

    @Override
    public BlockPos.MutableBlockPos minersProsperity$getGlowPos()
    {
        return minersProsperity$glowPos;
    }

    @Override
    public void minersProsperity$setGlowPos(BlockPos.MutableBlockPos pos)
    {
        this.minersProsperity$glowPos = pos;
    }

    @Override
    public BlockPos minersProsperity$getLastGlowPos()
    {
        return minersProsperity$lastGlowPos.toImmutable();
    }

    @Override
    public void minersProsperity$setLastGlowPos(BlockPos pos) {

    }

    @Override
    public boolean minersProsperity$isGlowUpdated()
    {
        return minersProsperity$glowUpdated;
    }

    @Override
    public void minersProsperity$setGlowUpdated(boolean updated)
    {
        this.minersProsperity$glowUpdated = updated;
    }
}