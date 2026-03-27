package net.vyroxes.minersprosperity.util.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class TOPCompat implements Function<ITheOneProbe, Void>
{

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(new TOPInfoProvider());
        return null;
    }

    @Override
    public @NotNull <V> Function<V, Void> compose(@NotNull Function<? super V, ? extends ITheOneProbe> before) {
        return Function.super.compose(before);
    }

    @Override
    public @NotNull <V> Function<ITheOneProbe, V> andThen(@NotNull Function<? super Void, ? extends V> after) {
        return Function.super.andThen(after);
    }
}