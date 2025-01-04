package net.vyroxes.minersprosperity.init;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.fluids.FluidBase;

public class FluidInit
{
    //FLUIDS
    public static final Fluid LIQUID_EXPERIENCE = new FluidBase("liquid_experience", new ResourceLocation(Tags.MODID, "blocks/liquid_experience_still"), new ResourceLocation(Tags.MODID, "blocks/liquid_experience_flow"), new ResourceLocation(Tags.MODID, "blocks/liquid_experience_overlay")).setMaterial(Material.WATER).setColor(0xFFFFFFFF).setAlpha(0.2F).setLuminosity(10);
}