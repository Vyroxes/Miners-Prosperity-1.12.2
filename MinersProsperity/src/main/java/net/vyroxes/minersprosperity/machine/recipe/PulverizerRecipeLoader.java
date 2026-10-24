package net.vyroxes.minersprosperity.machine.recipe;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PulverizerRecipeLoader
{
    public static void loadFromJSON()
    {
        File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID + "/recipes");
        File jsonFile = new File(configDir, "pulverizer.json");

        if (!jsonFile.exists()) createDefaultFile(jsonFile);

        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))
        {
            JsonArray array = new JsonParser().parse(reader).getAsJsonArray();

            for (JsonElement el : array)
            {
                JsonObject obj = el.getAsJsonObject();

                if (!obj.has("input"))
                {
                    MinersProsperity.logger.error("Pulverizer recipe has no input: {}", obj);
                    continue;
                }

                if (!obj.has("result"))
                {
                    MinersProsperity.logger.error("Pulverizer recipe has no result: {}", obj);
                    continue;
                }

                ItemStack inputStack = null;
                String oreDictInput = null;
                int quantity = 1;

                if (obj.has("input"))
                {
                    JsonObject input = obj.getAsJsonObject("input");
                    if (input.has("item"))
                    {
                        String itemName = input.get("item").getAsString();
                        int meta = getMetadata(input);
                        int count = input.has("count") ? input.get("count").getAsInt() : 1;

                        Item item = Item.getByNameOrId(itemName);
                        if (item == null)
                        {
                            MinersProsperity.logger.error("Pulverizer recipe has no item for input: {}", itemName);
                            continue;
                        }
                        inputStack = new ItemStack(item, count, meta);
                        quantity = count;
                    }
                    else if (input.has("oreDict"))
                    {
                        oreDictInput = input.get("oreDict").getAsString();
                        quantity = input.has("count") ? input.get("count").getAsInt() : 1;
                    }
                    else
                    {
                        MinersProsperity.logger.error("Pulverizer input has neither item nor oreDict: {}", input);
                        continue;
                    }
                }

                JsonObject resultObj = obj.getAsJsonObject("result");
                String resultItemName = resultObj.get("item").getAsString();
                int resultMeta = resultObj.has("meta") ? resultObj.get("meta").getAsInt() : 0;
                int resultCount = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

                Item resultItem = Item.getByNameOrId(resultItemName);
                if (resultItem == null)
                {
                    MinersProsperity.logger.error("Pulverizer recipe has no item for result: {}", resultItemName);
                    continue;
                }

                ItemStack resultStack = new ItemStack(resultItem, resultCount, resultMeta);

                int cookTime = obj.get("cookTime").getAsInt();
                int energy = obj.get("energyUsage").getAsInt();
                float xp = obj.get("experience").getAsFloat();

                PulverizerRecipe.getInstance().addRecipe(inputStack, oreDictInput, quantity, resultStack, cookTime, energy, xp);
            }

            MinersProsperity.logger.info("Loaded {} Pulverizer recipes", PulverizerRecipe.getInstance().getLookupTable().getRecipes().size());
        }
        catch (Exception e)
        {
            MinersProsperity.logger.error("Error while loading pulverizer recipes from JSON", e);
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
                            //   "input": {
                            //     "item": "minecraft:coal", // required (or "oreDict")
                            //     "meta": "*", // optional, number or "*" for all metadata
                            //     "count": 2 // optional, default: 1
                            //   },
                            //   "result": {
                            //     "item": "modid:itemid", // required
                            //     "meta": 2, // optional, default: 0
                            //     "count": 4 // optional, default: 1
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

            MinersProsperity.logger.info("Created default pulverizer.json file in the config directory");
        }
        catch (IOException e)
        {
            MinersProsperity.logger.error("Failed to create default pulverizer.json file in the config directory", e);
        }
    }

    private static @NotNull JsonArray getJsonElements()
    {
        JsonArray defaultRecipes = new JsonArray();

        defaultRecipes.add(createRecipe(null, null, "materialClay", 1, "minersprosperity:clay_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "flint", 1, "minersprosperity:flint_dust", null, 1, 200, 20, 0.7f));

        defaultRecipes.add(createRecipe(null, null, "gemGraphite", 1, "minersprosperity:graphite_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCharcoal", 1, "minersprosperity:charcoal_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCoal", 1, "minersprosperity:coal_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemGraphite", 1, "minersprosperity:graphite_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCoke", 1, "minersprosperity:coke_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", 1, "minersprosperity:copper_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTin", 1, "minersprosperity:tin_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBronze", 1, "minersprosperity:bronze_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSilver", 1, "minersprosperity:silver_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotLead", 1, "minersprosperity:lead_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotZinc", 1, "minersprosperity:zinc_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBrass", 1, "minersprosperity:brass_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotAluminum", 1, "minersprosperity:aluminum_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDuraluminum", 1, "minersprosperity:duraluminum_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIron", 1, "minersprosperity:iron_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedIron", 1, "minersprosperity:refined_iron_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSteel", 1, "minersprosperity:steel_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotNickel", 1, "minersprosperity:nickel_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotChromium", 1, "minersprosperity:chromium_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardGlowstone", 1, "minecraft:glowstone_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemQuartz", 1, "minersprosperity:quartz_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotInconel", 1, "minersprosperity:inconel_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotUranium", 1, "minersprosperity:uranium_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTungsten", 1, "minersprosperity:tungsten_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", 1, "minersprosperity:gold_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotPlatinum", 1, "minersprosperity:platinum_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotWhiteGold", 1, "minersprosperity:white_gold_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotElectrum", 1, "minersprosperity:electrum_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIridium", 1, "minersprosperity:iridium_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTitanium", 1, "minersprosperity:titanium_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemLapis", 1, "minersprosperity:lapis_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRedstone", 1, "minecraft:redstone", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemRuby", 1, "minersprosperity:ruby_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemSapphire", 1, "minersprosperity:sapphire_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemAmethyst", 1, "minersprosperity:amethyst_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemTopaz", 1, "minersprosperity:topaz_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemPeridot", 1, "minersprosperity:peridot_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemDiamond", 1, "minersprosperity:diamond_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedObsidian", 1, "minersprosperity:refined_obsidian_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemEmerald", 1, "minersprosperity:emerald_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobalt", 1, "minersprosperity:cobalt_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobaltsteel", 1, "minersprosperity:cobaltsteel_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemOnyx", 1, "minersprosperity:onyx_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDarkSteel", 1, "minersprosperity:dark_steel_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemBlackOpal", 1, "minersprosperity:black_opal_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardAzure", 1, "minersprosperity:azure_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRosalite", 1, "minersprosperity:rosalite_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardVerdite", 1, "minersprosperity:verdite_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemVoidium", 1, "minersprosperity:voidium_dust", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardLuminosite", 1, "minersprosperity:luminosite_dust", null, 1, 200, 20, 0.7f));

        defaultRecipes.add(createRecipe(null, null, "plankWood", 1, "minersprosperity:wood_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "logWood", 1, "minersprosperity:wood_dust", null, 4, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "stone", 1, "minersprosperity:stone_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "cobblestone", 1, "minersprosperity:stone_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "netherrack", 1, "minersprosperity:netherrack_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "endstone", 1, "minersprosperity:end_stone_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "enderpearl", 1, "minersprosperity:ender_dust", null, 1, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "endereye", 1, "minersprosperity:ender_dust", null, 1, 300, 30, 1.0f));

        defaultRecipes.add(createRecipe(null, null, "oreSulfur", 1, "minersprosperity:sulfur", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreCoal", 1, "minersprosperity:coal_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreGraphite", 1, "minersprosperity:graphite_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreCopper", 1, "minersprosperity:copper_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreTin", 1, "minersprosperity:tin_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreSilver", 1, "minersprosperity:silver_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreLead", 1, "minersprosperity:lead_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreZinc", 1, "minersprosperity:zinc_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreAluminum", 1, "minersprosperity:aluminum_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreIron", 1, "minersprosperity:iron_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreNickel", 1, "minersprosperity:nickel_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreChromium", 1, "minersprosperity:chromium_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreQuartz", 1, "minersprosperity:quartz_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreUranium", 1, "minersprosperity:uranium_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreTungsten", 1, "minersprosperity:tungsten_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreGold", 1, "minersprosperity:gold_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "orePlatinum", 1, "minersprosperity:platinum_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreIridium", 1, "minersprosperity:iridium_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreTitanium", 1, "minersprosperity:titanium_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreLapis", 1, "minersprosperity:lapis_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreRedstone", 1, "minecraft:redstone", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreRuby", 1, "minersprosperity:ruby_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreSapphire", 1, "minersprosperity:sapphire_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreAmethyst", 1, "minersprosperity:amethyst_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreTopaz", 1, "minersprosperity:topaz_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "orePeridot", 1, "minersprosperity:peridot_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreDiamond", 1, "minersprosperity:diamond_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreEmerald", 1, "minersprosperity:emerald_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreCobalt", 1, "minersprosperity:cobalt_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreOnyx", 1, "minersprosperity:onyx_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreBlackOpal", 1, "minersprosperity:black_opal_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreAzure", 1, "minersprosperity:azure_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreRosalite", 1, "minersprosperity:rosalite_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreVerdite", 1, "minersprosperity:verdite_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreVoidium", 1, "minersprosperity:voidium_dust", null, 2, 300, 30, 1.0f));
        defaultRecipes.add(createRecipe(null, null, "oreLuminosite", 1, "minersprosperity:luminosite_dust", null, 2, 300, 30, 1.0f));

        return defaultRecipes;
    }

    private static JsonObject createRecipe(String inputItem, Object inputMeta, String inputOreDict, int quantity, String resultItem, Integer resultMeta, int resultCount, int cookTime, int energyUsage, float experience)
    {
        JsonObject recipe = new JsonObject();

        JsonObject input = new JsonObject();
        if (inputItem != null)
        {
            input.addProperty("item", inputItem);
            addMeta(input, inputMeta);
        }
        else if (inputOreDict != null) input.addProperty("oreDict", inputOreDict);
        input.addProperty("count", quantity);

        recipe.add("input", input);

        JsonObject result = new JsonObject();
        result.addProperty("item", resultItem);
        if (resultMeta != null) result.addProperty("meta", resultMeta);
        result.addProperty("count", resultCount);
        recipe.add("result", result);

        recipe.addProperty("cookTime", cookTime);
        recipe.addProperty("energyUsage", energyUsage);
        recipe.addProperty("experience", experience);

        return recipe;
    }

    private static void addMeta(JsonObject object, Object meta)
    {
        if (meta == null) return;

        if (meta instanceof Number) object.addProperty("meta", (Number) meta);
        else if (meta instanceof String && meta.equals("*")) object.addProperty("meta", "*");
        else throw new IllegalArgumentException("Pulverizer recipe has invalid metadata: " + meta);
    }

    private static int getMetadata(JsonObject object)
    {
        if (!object.has("meta")) return 0;

        JsonElement element = object.get("meta");

        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
        {
            String value = element.getAsString().trim();

            if (value.equals("*")) return OreDictionary.WILDCARD_VALUE;
        }

        return element.getAsInt();
    }
}