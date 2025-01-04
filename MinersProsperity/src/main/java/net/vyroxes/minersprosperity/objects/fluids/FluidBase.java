package net.vyroxes.minersprosperity.objects.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBase extends Fluid
{
    protected Material material;
    protected int mapColor;
    protected float overlayAlpha;

    public FluidBase(String fluidName, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay)
    {
        super(fluidName, still, flowing, overlay);
        setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY);
        setFillSound(SoundEvents.ITEM_BUCKET_FILL);
        setMaterial(this.material);
        setColor(this.mapColor);
        setAlpha(this.overlayAlpha);
    }

    @Override
    public int getColor()
    {
        return this.mapColor;
    }

    public FluidBase setColor(int mapColor)
    {
        this.mapColor = mapColor;
        return this;
    }

    public float getAlpha()
    {
        return this.overlayAlpha;
    }

    public FluidBase setAlpha(float overlayAlpha)
    {
        this.overlayAlpha = overlayAlpha;
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