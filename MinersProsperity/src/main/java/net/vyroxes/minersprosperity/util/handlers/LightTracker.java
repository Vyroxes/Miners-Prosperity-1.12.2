package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LightTracker
{
    private static final Set<BlockPos> activeLights = ConcurrentHashMap.newKeySet();

    public static void add(BlockPos pos)
    {
        activeLights.add(pos.toImmutable());
    }

    public static Set<BlockPos> getAll()
    {
        return activeLights;
    }
}