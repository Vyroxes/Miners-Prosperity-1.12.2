package net.vyroxes.minersprosperity.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.vyroxes.minersprosperity.fluid.FluidBase;

public class ParticleFluidSplash extends ParticleFluidBase
{
    public ParticleFluidSplash(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite[] textures, FluidBase fluid)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D, textures[0], fluid);

        this.setParticleTexture(textures[this.rand.nextInt(textures.length)]);

        this.motionX *= 0.30000001192092896D;
        this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        this.motionZ *= 0.30000001192092896D;

        this.setSize(0.01F, 0.01F);

        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));

        this.particleGravity = 0.04F;

        if (ySpeed == 0.0D && (xSpeed != 0.0D || zSpeed != 0.0D))
        {
            this.motionX = xSpeed;
            this.motionY = ySpeed + 0.1D;
            this.motionZ = zSpeed;
        }
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

        if (this.particleMaxAge-- <= 0) this.setExpired();

        if (this.onGround)
        {
            if (Math.random() < 0.5D) this.setExpired();

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);

        IBlockState state = this.world.getBlockState(pos);
        Material material = state.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double height;

            if (state.getBlock() instanceof BlockLiquid)
            {
                int level = state.getValue(BlockLiquid.LEVEL);
                height = 1.0F - BlockLiquid.getLiquidHeightPercent(level);
            }
            else if (state.getBlock() instanceof IFluidBlock) height = Math.abs(((IFluidBlock) state.getBlock()).getFilledPercentage(this.world, pos));
            else height = state.getBoundingBox(this.world, pos).maxY;

            double surfaceY = MathHelper.floor(this.posY) + height;

            if (this.posY < surfaceY) this.setExpired();
        }
    }
}