package net.vyroxes.minersprosperity.machine.recipe;

import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidFuelGeneratorRecipe
{
    private static final LiquidFuelGeneratorRecipe INSTANCE = new LiquidFuelGeneratorRecipe();
    private final List<Recipe> recipes = new ArrayList<>();

    public static LiquidFuelGeneratorRecipe getInstance() { return INSTANCE; }

    public List<Recipe> getRecipes() { return Collections.unmodifiableList(this.recipes); }

    public void clear() { this.recipes.clear(); }

    public void addRecipe(FluidStack input, int totalCookTime, int energyGeneration)
    {
        if (input == null || input.amount <= 0 || totalCookTime <= 0 || energyGeneration <= 0 || energyGeneration > Integer.MAX_VALUE / 10)
        {
            throw new IllegalArgumentException("Liquid fuel amount, duration and generation must be positive and fit an upgraded generator");
        }

        this.recipes.removeIf(recipe -> recipe.input.isFluidEqual(input));
        this.recipes.add(new Recipe(input.copy(), totalCookTime, energyGeneration));
    }

    @Nullable
    public Recipe findRecipe(@Nullable FluidStack fluid)
    {
        if (fluid == null) return null;
        for (Recipe recipe : this.recipes) if (recipe.input.isFluidEqual(fluid)) return recipe;
        return null;
    }

    public static class Recipe
    {
        public final FluidStack input;
        public final int totalCookTime;
        public final int energyGeneration;

        private Recipe(FluidStack input, int totalCookTime, int energyGeneration)
        {
            this.input = input;
            this.totalCookTime = totalCookTime;
            this.energyGeneration = energyGeneration;
        }
    }
}