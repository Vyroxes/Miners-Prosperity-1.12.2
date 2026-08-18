package net.vyroxes.minersprosperity.util;

import net.minecraft.util.math.BlockPos;

public interface GlowingPlayerExtension
{
    BlockPos.MutableBlockPos minersProsperity$getGlowPos();

    void minersProsperity$setGlowPos(BlockPos pos);

    boolean minersProsperity$isGlowActive();

    void minersProsperity$setGlowActive(boolean active);

    boolean minersProsperity$isGlowUpdated();

    void minersProsperity$setGlowUpdated(boolean glowUpdated);
}