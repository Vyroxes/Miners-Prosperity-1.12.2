package net.vyroxes.minersprosperity.machine.recipe;

import com.google.gson.*;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class LiquidFuelGeneratorRecipeLoader
{
    public static void loadFromJSON()
    {
        File file = new File(Loader.instance().getConfigDir(), Tags.MODID + "/recipes/liquid_fuel_generator.json");
        LiquidFuelGeneratorRecipe recipes = LiquidFuelGeneratorRecipe.getInstance();
        recipes.clear();

        try
        {
            if (!file.exists()) createDefaultFile(file);

            try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
            {
                JsonArray array = new JsonParser().parse(reader).getAsJsonArray();
                for (JsonElement element : array)
                {
                    try
                    {
                        JsonObject recipe = element.getAsJsonObject();
                        Fluid fluid = FluidRegistry.getFluid(recipe.get("fluid").getAsString());
                        if (fluid == null) throw new IllegalArgumentException("Unknown fuel fluid");
                        int amount = recipe.get("amount").getAsInt();
                        if (amount > 16000) throw new IllegalArgumentException("Fuel batch exceeds the 16000 mB tank");
                        recipes.addRecipe(new FluidStack(fluid, amount), recipe.get("cookTime").getAsInt(), recipe.get("energyGeneration").getAsInt());
                    }
                    catch (RuntimeException exception)
                    {
                        MinersProsperity.logger.error("Skipping invalid Liquid Fuel Generator recipe: {}", element, exception);
                    }
                }
            }
        }
        catch (IOException | RuntimeException exception)
        {
            MinersProsperity.logger.error("Could not load Liquid Fuel Generator recipes from {}", file, exception);
        }
    }
    private static void createDefaultFile(File file) throws IOException
    {
        if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs())
        {
            throw new IOException("Cannot create liquid fuel recipe directory");
        }

        String exampleComment = """
                // Example:
                // {
                //   "fluid": "lava", // required Forge fluid registry name, e.g. "lava" or "crude_oil"
                //   "amount": 100, // mB consumed per batch; 1 to 16000, 1000 mB = one bucket
                //   "cookTime": 200, // fuel duration in ticks before upgrades; 20 ticks = one second
                //   "energyGeneration": 40 // FE per tick before upgrades; positive integer
                // }
                
                """;

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))
        {
            writer.write(exampleComment);
            new GsonBuilder().setPrettyPrinting().create().toJson(getJsonElements(), writer);
        }

        MinersProsperity.logger.info("Created default liquid_fuel_generator.json file in the config directory");
    }

    private static JsonArray getJsonElements()
    {
        JsonArray recipes = new JsonArray();

        recipes.add(createRecipe("lava", 100, 200, 40));
        recipes.add(createRecipe("crude_oil", 100, 200, 80));

        return recipes;
    }

    private static JsonObject createRecipe(String fluid, int amount, int cookTime, int energyGeneration)
    {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("fluid", fluid);
        recipe.addProperty("amount", amount);
        recipe.addProperty("cookTime", cookTime);
        recipe.addProperty("energyGeneration", energyGeneration);
        return recipe;
    }
}