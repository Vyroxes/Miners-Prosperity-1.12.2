package net.vyroxes.minersprosperity.init;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.fluid.FluidBase;

public final class ModFluids
{
    public static Fluid LIQUID_EXPERIENCE;
    public static Fluid CRUDE_OIL;

    private static boolean initialized;

    private ModFluids() {}

    public static void register()
    {
        if (initialized) return;

        LIQUID_EXPERIENCE = registerFluid(createLiquidExperience());
        CRUDE_OIL = registerFluid(createCrudeOil());

        initialized = true;
    }

    private static Fluid createLiquidExperience()
    {
        return new FluidBase("liquid_experience", new ResourceLocation(Tags.MODID, "blocks/liquid_experience_still"), new ResourceLocation(Tags.MODID, "blocks/liquid_experience_flow"), new ResourceLocation(Tags.MODID, "blocks/liquid_experience_overlay")).setMaterial(Material.WATER).setColor(0xFF00FF22).setAlpha(1.0F).setLuminosity(10);
    }

    private static Fluid createCrudeOil()
    {
        return new FluidBase("crude_oil", new ResourceLocation(Tags.MODID, "blocks/crude_oil_still"), new ResourceLocation(Tags.MODID, "blocks/crude_oil_flow"), new ResourceLocation(Tags.MODID, "blocks/crude_oil_overlay")).setMaterial(Material.WATER).setColor(0xFF1A1A1A).setAlpha(1.0F).setLuminosity(0).setTemperature(300).setDensity(1500).setViscosity(3000);
    }

    private static Fluid registerFluid(Fluid candidate)
    {
        Fluid existing = FluidRegistry.getFluid(candidate.getName());

        if (existing != null)
        {
            MinersProsperity.logger.warn("Fluid '{}' is already registered. Using existing instance.", candidate.getName());

            FluidRegistry.addBucketForFluid(existing);
            return existing;
        }

        if (!FluidRegistry.registerFluid(candidate))
        {
            Fluid registered = FluidRegistry.getFluid(candidate.getName());

            if (registered == null) throw new IllegalStateException("Failed to register fluid: " + candidate.getName());

            FluidRegistry.addBucketForFluid(registered);
            return registered;
        }

        FluidRegistry.addBucketForFluid(candidate);

        return candidate;
    }

    public static boolean isInitialized() { return initialized; }
}