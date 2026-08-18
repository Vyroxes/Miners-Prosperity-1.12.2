package net.vyroxes.minersprosperity.client.particle;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.vyroxes.minersprosperity.fluid.FluidBase;
import org.jetbrains.annotations.NotNull;

public class FluidTintParticleFactory implements IParticleFactory {
    public enum Type
    {
        BUBBLE,
        SPLASH,
        WAKE
    }

    private final Type type;
    private final IParticleFactory parent;

    public FluidTintParticleFactory(Type type, IParticleFactory parent)
    {
        this.type = type;
        this.parent = parent;
    }

    @Override
    public Particle createParticle(int particleId, @NotNull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int @NotNull ... parameters)
    {
        FluidBase fluid = this.getFluid(world, x, y, z);

        if (fluid == null) return this.parent.createParticle(particleId, world, x, y, z, xSpeed, ySpeed, zSpeed, parameters);

        return switch (this.type)
        {
            case BUBBLE -> new ParticleFluidBubble(world, x, y, z, xSpeed, ySpeed, zSpeed, FluidParticleTextures.getBubble(), fluid);

            case SPLASH -> new ParticleFluidSplash(world, x, y, z, xSpeed, ySpeed, zSpeed, FluidParticleTextures.getSplash(), fluid);

            case WAKE -> new ParticleFluidWake(world, x, y, z, xSpeed, ySpeed, zSpeed, FluidParticleTextures.getWake(), fluid);
        };
    }

    private FluidBase getFluid(World world, double x, double y, double z)
    {
        BlockPos origin = new BlockPos(x, y, z);

        for (int depth = 0; depth <= 3; depth++)
        {
            FluidHit hit = this.getFluidAt(world, origin.down(depth));
            if (hit != null) return hit.fluid;
        }

        FluidHit nearestHit = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int offsetX = -1; offsetX <= 1; offsetX++)
        {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++)
            {
                if (offsetX == 0 && offsetZ == 0) continue;

                BlockPos column = origin.add(offsetX, 0, offsetZ);

                for (int depth = 0; depth <= 3; depth++)
                {
                    BlockPos checkPos = column.down(depth);

                    FluidHit hit = this.getFluidAt(world, checkPos);

                    if (hit == null) continue;

                    double centerX = checkPos.getX() + 0.5D;
                    double centerY = checkPos.getY() + 0.5D;
                    double centerZ = checkPos.getZ() + 0.5D;

                    double dx = x - centerX;
                    double dy = y - centerY;
                    double dz = z - centerZ;

                    double distance = dx * dx + dy * dy + dz * dz;

                    if (distance < nearestDistance)
                    {
                        nearestDistance = distance;
                        nearestHit = hit;
                    }

                    break;
                }
            }
        }

        return nearestHit != null ? nearestHit.fluid : null;
    }

    private FluidHit getFluidAt(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof IFluidBlock)
        {
            Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();
            if (fluid instanceof FluidBase) return new FluidHit((FluidBase) fluid);
            return new FluidHit(null);
        }

        if (state.getMaterial() == net.minecraft.block.material.Material.WATER) return new FluidHit(null);

        return null;
    }

    @Desugar
    private record FluidHit(FluidBase fluid) {}
}