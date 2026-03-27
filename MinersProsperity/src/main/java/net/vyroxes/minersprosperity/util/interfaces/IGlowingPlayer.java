package net.vyroxes.minersprosperity.util.interfaces;

import net.minecraft.util.math.BlockPos;

public interface IGlowingPlayer
{
    BlockPos.MutableBlockPos minersProsperity$getGlowPos();
    void minersProsperity$setGlowPos(BlockPos.MutableBlockPos pos);
    BlockPos minersProsperity$getLastGlowPos();
    void minersProsperity$setLastGlowPos(BlockPos pos);
    boolean minersProsperity$isGlowUpdated();
    void minersProsperity$setGlowUpdated(boolean glowUpdated);
}