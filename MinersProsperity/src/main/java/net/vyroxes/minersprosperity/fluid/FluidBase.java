package net.vyroxes.minersprosperity.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;

public class FluidBase extends Fluid
{
    protected Material material = Material.WATER;
    protected float overlayAlpha = 1.0F;

    public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay)
    {
        super(fluidName, still, flowing, overlay);

        setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY);
        setFillSound(SoundEvents.ITEM_BUCKET_FILL);
    }

    public FluidBase setColor(int color)
    {
        super.setColor(color);
        return this;
    }

    public float getAlpha()
    {
        return this.overlayAlpha;
    }

    public FluidBase setAlpha(float alpha)
    {
        this.overlayAlpha = MathHelper.clamp(alpha, 0.0F, 1.0F);
        return this;
    }

    public Material getMaterial()
    {
        return this.material;
    }

    public FluidBase setMaterial(Material material)
    {
        this.material = material;
        return this;
    }
}