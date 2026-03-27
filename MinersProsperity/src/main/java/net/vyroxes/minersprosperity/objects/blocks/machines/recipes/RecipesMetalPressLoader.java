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

public class RecipesMetalPressLoader
{
    public static void loadFromJSON()
    {
        File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID + "/recipes");
        File jsonFile = new File(configDir, "metaL_press.json");

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

                RecipesMetalPress.DieType dieType = null;

                if (obj.has("dieType"))
                {
                    try
                    {
                        dieType = RecipesMetalPress.DieType.valueOf(obj.get("dieType").getAsString().toUpperCase());
                    }
                    catch (IllegalArgumentException e)
                    {
                        MinersProsperity.logger.error("Unknown dieType: {}", obj.get("dieType").getAsString());
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
                    MinersProsperity.logger.error("Item not found for result: {}", resultItemName);
                    continue;
                }

                ItemStack resultStack = new ItemStack(resultItem, resultCount, resultMeta);

                int cookTime = obj.get("cookTime").getAsInt();
                int energy = obj.get("energyUsage").getAsInt();
                float xp = obj.get("experience").getAsFloat();

                RecipesMetalPress.getInstance().addRecipe(input1Stack, oreDictInput1, quantity1, dieType, resultStack, cookTime, energy, xp);
            }
        }
        catch (Exception e)
        {
            MinersProsperity.logger.error("Error while loading metal press recipes from JSON", e);
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
                            //   "dieType": "PLATE", // required (or "GEAR")
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

            MinersProsperity.logger.info("Created default metal_press.json file in the config directory");
        }
        catch (IOException e)
        {
            MinersProsperity.logger.error("Failed to create default metal_press.json file in the config directory", e);
        }
    }

    private static @NotNull JsonArray getJsonElements()
    {
        JsonArray defaultRecipes = new JsonArray();

        defaultRecipes.add(createRecipe(null, null, "gemCharcoal", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:charcoal_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCoal", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:coal_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:copper_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTin", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:tin_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBronze", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:bronze_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSilver", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:silver_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotLead", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:lead_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotZinc", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:zinc_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBrass", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:brass_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotAluminum", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:aluminum_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDuraluminum", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:duraluminum_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIron", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:iron_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedIron", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:refined_iron_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSteel", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:steel_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotNickel", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:nickel_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotChromium", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:chromium_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardGlowstone", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:glowstone_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemQuartz", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:quartz_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotInconel", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:inconel_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotUranium", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:uranium_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTungsten", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:tungsten_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:gold_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotPlatinum", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:platinum_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotWhiteGold", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:white_gold_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotElectrum", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:electrum_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIridium", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:iridium_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTitanium", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:titanium_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemLapis", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:lapis_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRedstone", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:redstone_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemRuby", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:ruby_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemSapphire", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:sapphire_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemAmethyst", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:amethyst_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemTopaz", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:topaz_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemPeridot", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:peridot_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemDiamond", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:diamond_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedObsidian", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:refined_obsidian_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemEmerald", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:emerald_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobalt", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:cobalt_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobaltsteel", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:cobaltsteel_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemOnyx", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:onyx_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDarkSteel", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:dark_steel_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemBlackOpal", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:black_opal_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardAzure", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:azure_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRosalite", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:rosalite_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardVerdite", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:verdite_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemVoidium", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:voidium_gear", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardLuminosite", 1, RecipesMetalPress.DieType.GEAR, "minersprosperity:luminosite_gear", null, 1, 200, 20, 0.7f));

        defaultRecipes.add(createRecipe(null, null, "gemCharcoal", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:charcoal_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCoal", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:coal_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:copper_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTin", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:tin_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBronze", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:bronze_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSilver", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:silver_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotLead", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:lead_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotZinc", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:zinc_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBrass", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:brass_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotAluminum", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:aluminum_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDuraluminum", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:duraluminum_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIron", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:iron_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedIron", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:refined_iron_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSteel", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:steel_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotNickel", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:nickel_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotChromium", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:chromium_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardGlowstone", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:glowstone_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemQuartz", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:quartz_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotInconel", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:inconel_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotUranium", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:uranium_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTungsten", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:tungsten_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:gold_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotPlatinum", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:platinum_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotWhiteGold", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:white_gold_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotElectrum", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:electrum_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIridium", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:iridium_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTitanium", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:titanium_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemLapis", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:lapis_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRedstone", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:redstone_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemRuby", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:ruby_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemSapphire", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:sapphire_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemAmethyst", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:amethyst_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemTopaz", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:topaz_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemPeridot", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:peridot_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemDiamond", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:diamond_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedObsidian", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:refined_obsidian_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemEmerald", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:emerald_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobalt", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:cobalt_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobaltsteel", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:cobaltsteel_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemOnyx", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:onyx_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDarkSteel", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:dark_steel_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemBlackOpal", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:black_opal_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardAzure", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:azure_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRosalite", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:rosalite_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardVerdite", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:verdite_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemVoidium", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:voidium_plate", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardLuminosite", 1, RecipesMetalPress.DieType.PLATE, "minersprosperity:luminosite_plate", null, 1, 200, 20, 0.7f));

        defaultRecipes.add(createRecipe(null, null, "gemCharcoal", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:charcoal_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemCoal", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:coal_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCopper", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:copper_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTin", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:tin_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBronze", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:bronze_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSilver", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:silver_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotLead", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:lead_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotZinc", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:zinc_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotBrass", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:brass_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotAluminum", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:aluminum_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDuraluminum", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:duraluminum_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIron", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:iron_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedIron", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:refined_iron_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotSteel", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:steel_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotNickel", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:nickel_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotChromium", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:chromium_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardGlowstone", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:glowstone_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemQuartz", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:quartz_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotInconel", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:inconel_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotUranium", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:uranium_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTungsten", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:tungsten_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotGold", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:gold_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotPlatinum", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:platinum_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotWhiteGold", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:white_gold_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotElectrum", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:electrum_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotIridium", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:iridium_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotTitanium", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:titanium_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemLapis", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:lapis_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRedstone", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:redstone_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemRuby", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:ruby_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemSapphire", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:sapphire_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemAmethyst", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:amethyst_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemTopaz", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:topaz_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemPeridot", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:peridot_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemDiamond", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:diamond_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotRefinedObsidian", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:refined_obsidian_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemEmerald", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:emerald_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobalt", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:cobalt_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotCobaltsteel", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:cobaltsteel_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemOnyx", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:onyx_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "ingotDarkSteel", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:dark_steel_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemBlackOpal", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:black_opal_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardAzure", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:azure_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardRosalite", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:rosalite_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardVerdite", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:verdite_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "gemVoidium", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:voidium_rod", null, 1, 200, 20, 0.7f));
        defaultRecipes.add(createRecipe(null, null, "shardLuminosite", 1, RecipesMetalPress.DieType.ROD, "minersprosperity:luminosite_rod", null, 1, 200, 20, 0.7f));

        return defaultRecipes;
    }

    private static JsonObject createRecipe(String input1Item, Integer input1Meta, String input1OreDict, int quantity1, RecipesMetalPress.DieType dieType, String resultItem, Integer resultMeta, int resultCount, int cookTime, int energyUsage, float experience)
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

        recipe.add("input1", input1);

        if (dieType != null)
        {
            recipe.addProperty("dieType", dieType.name());
        }

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