package net.vyroxes.minersprosperity.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.fluid.FluidBase;

public class ParticleFluidBubble extends ParticleFluidBase
{
    public ParticleFluidBubble(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite texture, FluidBase fluid)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D, texture, fluid);

        this.setSize(0.02F, 0.02F);

        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;

        this.motionX = xSpeed * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        this.motionY = ySpeed * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        this.motionZ = zSpeed * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;

        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY += 0.002D;

        this.move(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.8500000238418579D;
        this.motionY *= 0.8500000238418579D;
        this.motionZ *= 0.8500000238418579D;

        if (!this.isInsideOwnFluid()) this.setExpired();

        if (this.particleMaxAge-- <= 0) this.setExpired();
    }
}