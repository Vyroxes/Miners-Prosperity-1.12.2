package net.vyroxes.minersprosperity.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.vyroxes.minersprosperity.fluid.FluidBase;

public abstract class ParticleFluidBase extends Particle
{
    protected final FluidBase fluid;

    protected ParticleFluidBase(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite texture, FluidBase fluid)
    {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);

        this.fluid = fluid;
        this.setParticleTexture(texture);

        int color = fluid.getColor();

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        this.setRBGColorF(red, green, blue);
        this.setAlphaF(fluid.getAlpha());
    }

    protected boolean isInsideOwnFluid()
    {
        IBlockState state = this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));

        if (!(state.getBlock() instanceof IFluidBlock)) return false;

        return ((IFluidBlock) state.getBlock()).getFluid() == this.fluid;
    }

    @Override
    public int getFXLayer() { return 1; }
}