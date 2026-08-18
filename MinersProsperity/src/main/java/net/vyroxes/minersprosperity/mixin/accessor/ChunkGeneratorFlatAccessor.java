package net.vyroxes.minersprosperity.mixin.accessor;

import net.minecraft.world.gen.ChunkGeneratorFlat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGeneratorFlat.class)
public interface ChunkGeneratorFlatAccessor
{
    @Accessor("hasDecoration")
    boolean getHasDecoration();
}