package net.vyroxes.minersprosperity.client.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.fluid.FluidBase;

public class ParticleFluidWake extends ParticleFluidBase
{
    private final TextureAtlasSprite[] frames;

    public ParticleFluidWake(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite[] frames, FluidBase fluid)
    {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed, frames[0], fluid);

        this.frames = frames;

        this.setSize(0.01F, 0.01F);

        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));

        this.particleGravity = 0.0F;

        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= this.particleGravity;

        this.move(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        int i = 60 - this.particleMaxAge;

        float size = i * 0.001F;
        this.setSize(size, size);

        this.setParticleTexture(this.frames[i % 4]);

        if (this.particleMaxAge-- <= 0) this.setExpired();
    }
}