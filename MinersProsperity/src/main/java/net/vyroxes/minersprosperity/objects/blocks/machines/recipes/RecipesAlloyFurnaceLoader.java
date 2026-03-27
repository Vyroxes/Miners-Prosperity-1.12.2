package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class RecipesAlloyFurnaceLoader
{
    public static void loadFromJSON()
    {
        File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID + "/recipes");
        File jsonFile = new File(configDir, "alloy_furnace.json");

        if (!jsonFile.exists())
        {
            createDefaultFile(jsonFile);
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))
        {
            JsonArray array = new JsonParser().parse(reader).getAsJsonArray();

            for (JsonElement el : array)
            {
                JsonObject obj = el.getAsJsonObject();

                ItemStack input1Stack = null;
                String oreDictInput1 = null;
                int quantity1 = 1;

                if (obj.has("input1"))
                {
                    JsonObject input1 = obj.getAsJsonObject("input1");
                    if (input1.has("item"))
                    {
                        String itemName = input1.get("item").getAsString();
                        int meta = input1.has("meta") ? input1.get("meta").getAsInt() : 0;
                        int count = input1.has("count") ? input1.get("count").getAsInt() : 1;

                        Item item = Item.getByNameOrId(itemName);
                        if (item == null)
                        {
                            MinersProsperity.logger.error("Item not found for input1: {}", itemName);
                            continue;
                        }
                        input1Stack = new ItemStack(item, count, meta);
                        quantity1 = count;
                    }
                    else if (input1.has("oreDict"))
                    {
                        oreDictInput1 = input1.get("oreDict").getAsString();
                        quantity1 = input1.has("count") ? input1.get("count").getAsInt() : 1;
                    }
                }

                ItemStack input2Stack = null;
                String oreDictInput2 = null;
                int quantity2 = 1;

                if (obj.has("input2"))
                {
                    JsonObject input2 = obj.getAsJsonObject("input2");
                    if (input2.has("item"))
                    {
                        String itemName = input2.get("item").getAsString();
                        int meta = input2.has("meta") ? input2.get("meta").getAsInt() : 0;
                        int count = input2.has("count") ? input2.get("count").getAsInt() : 1;

                        Item item = Item.getByNameOrId(itemName);
                        if (item == null)
                        {
                            MinersProsperity.logger.error("Item not found for input2: {}", itemName);
                            continue;
                        }
                        input2Stack = new ItemStack(item, count, meta);
                        quantity2 = count;
                    }
                    else if (input2.has("oreDict"))
                    {
                        oreDictInput2 = input2.get("oreDict").getAsString();
                        quantity2 = input2.has("count") ? input2.get("count").getAsInt() : 1;
                    }
                }

                JsonObject resultObj = obj.getAsJsonObject("result");
                String resultItemName = resultObj.get("item").getAsString();
                int resultMeta = resultObj.has("meta") ? resultObj.get("meta").getAsInt() : 0;
                int resultCount = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

                Item resultItem = Item.getByNameOrId(resultItemName);
                if (resultItem == null)
                {
                    MinersProsperity.logger.error("Item not found for result: {}", resultItemName);
                    continue;
                }

                ItemStack resultStack = new ItemStack(resultItem, resultCount, resultMeta);

                int cookTime = obj.get("cookTime").getAsInt();
                int energy = obj.get("energyUsage").getAsInt();
                float xp = obj.get("experience").getAsFloat();

                RecipesAlloyFurnace.getInstance().addRecipe(input1Stack, input2Stack, oreDictInput1, oreDictInput2, quantity1, quantity2, resultStack, cookTime, energy, xp);
            }
        }
        catch (Exception e)
        {
            MinersProsperity.logger.error("Error while loading alloy furnace recipes from JSON", e);
        }
    }

    private static void createDefaultFile(File file)
    {
        try
        {
            File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID + "/recipes");
            if (!configDir.exists() && !configDir.mkdirs())
            {
                MinersProsperity.logger.error("Could not create config directory: {}", configDir.getAbsolutePath());
                return;
            }

            JsonArray defaultRecipes = getJsonElements();

            String exampleComment =
                    """
                            // Example:
                            // {
                            //   "input1": {
                            //     "item": "minecraft:coal", // required (or "oreDict")
                            //     "meta": 1, // optional
                            //     "count": 2 // required
                            //   },
                            //   "input2": {
                            //     "oreDict": "blockDiamond", // required (or "item")
                            //     "count": 3 // required
                            //   },
                            //   "result": {
                            //     "item": "modid:itemid", // required
                            //     "meta": 2, // optional
                            //     "count": 4 // required
                            //   },
                            //   "cookTime": 100, // in ticks, 20 ticks is 1 second
                            //   "energyUsage": 40, // FE per tick
                            //   "experience": 0.3 // XP gained when taken from output
                            // }
                            
                            """;

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))
            {
                writer.write(exampleComment);
                new GsonBuilder().setPrettyPrinting().create().toJson(defaultRecipes, writer);
            }

            MinersProsperity.logger.info("Created default alloy_furnace.json file in the config directory");
        }
        catch (IOException e)
        {
            MinersProsperity.logger.error("Failed to create default alloy_furnace.json file in the config directory", e);
        }
    }

    private static @NotNull JsonArray getJsonElements()
    {
        JsonArray defaultRecipes = new JsonArray();

        defaultRecipes.add(createRecipe(null, null, "ingotCopper", null, null, "ingotTin", 3, 1, "minersprosperity:bronze_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", null, null, "ingotZinc", 2, 1, "minersprosperity:brass_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", null, null, "ingotAluminum", 1, 3, "minersprosperity:duraluminum_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIron", null, null, "gemCoal", 1, 1, "minersprosperity:steel_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotNickel", null, null, "ingotChromium", 2, 1, "minersprosperity:inconel_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", null, null, "ingotPlatinum", 2, 1, "minersprosperity:white_gold_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", null, null, "ingotSilver", 1, 1, "minersprosperity:electrum_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemAmethyst", null, null, "ingotRefinedObsidian", 1, 2, "minersprosperity:obsidian_cluster", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobalt", null, null, "ingotSteel", 1, 2, "minersprosperity:cobaltsteel_ingot", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSteel", null, null, "gemOnyx", 1, 1, "minersprosperity:dark_steel_ingot", null, 1, 200, 20, 0.7f));

        return defaultRecipes;
    }

    private static JsonObject createRecipe(String input1Item, Integer input1Meta, String input1OreDict, String input2Item, Integer input2Meta, String input2OreDict, int quantity1, int quantity2, String resultItem, Integer resultMeta, int resultCount, int cookTime, int energyUsage, float experience)
    {
        JsonObject recipe = new JsonObject();

        JsonObject input1 = new JsonObject();
        if (input1Item != null)
        {
            input1.addProperty("item", input1Item);
            if (input1Meta != null)
                input1.addProperty("meta", input1Meta);
        }
        else if (input1OreDict != null)
        {
            input1.addProperty("oreDict", input1OreDict);
        }
        input1.addProperty("count", quantity1);

        JsonObject input2 = new JsonObject();
        if (input2Item != null)
        {
            input2.addProperty("item", input2Item);
            if (input2Meta != null)
                input2.addProperty("meta", input2Meta);
        }
        else if (input2OreDict != null)
        {
            input2.addProperty("oreDict", input2OreDict);
        }
        input2.addProperty("count", quantity2);

        recipe.add("input1", input1);
        recipe.add("input2", input2);

        JsonObject result = new JsonObject();
        result.addProperty("item", resultItem);
        if (resultMeta != null)
            result.addProperty("meta", resultMeta);
        result.addProperty("count", resultCount);
        recipe.add("result", result);

        recipe.addProperty("cookTime", cookTime);
        recipe.addProperty("energyUsage", energyUsage);
        recipe.addProperty("experience", experience);

        return recipe;
    }
}