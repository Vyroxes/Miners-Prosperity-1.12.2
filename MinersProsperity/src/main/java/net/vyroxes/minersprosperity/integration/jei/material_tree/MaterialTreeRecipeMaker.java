package net.vyroxes.minersprosperity.integration.jei.material_tree;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.Tags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MaterialTreeRecipeMaker
{
    private static final String[] PREFIXES = {"dustTiny", "ingot", "gem", "nugget", "dust", "gear", "plate", "stick", "rod", "material"};

    public static List<MaterialTreeRecipe> getRecipes()
    {
        Set<String> materials = new TreeSet<>();

        for (String name : OreDictionary.getOreNames())
        {
            for (String prefix : PREFIXES)
            {
                if (!name.startsWith(prefix) || name.length() == prefix.length()) continue;

                String material = name.substring(prefix.length());
                if (!Character.isUpperCase(material.charAt(0))) break;

                for (ItemStack stack : OreDictionary.getOres(name, false))
                {
                    if (isModItem(stack))
                    {
                        materials.add(material);
                        break;
                    }
                }

                break;
            }
        }

        List<MaterialTreeRecipe> recipes = new ArrayList<>();

        for (String material : materials)
        {
            if (material.equals("Carbon") || material.equals("Silicon")) continue;

            List<ItemStack> main = getForms("ingot" + material);
            if (main.isEmpty()) main = getForms("gem" + material);
            if (main.isEmpty()) main = getForms("material" + material);
            if (main.isEmpty()) main = getForms("dust" + material);
            if (main.isEmpty()) continue;

            List<ItemStack> nuggets = getForms("nugget" + material);
            List<ItemStack> dust = getForms("dust" + material);
            List<ItemStack> tinyDust = getForms("dustTiny" + material);
            List<ItemStack> gears = getForms("gear" + material);
            List<ItemStack> plates = getForms("plate" + material);
            List<ItemStack> rods = getForms("stick" + material, "rod" + material);

            boolean mainIsDust = false;
            for (ItemStack stack : dust)
            {
                if (ItemStack.areItemStacksEqual(main.get(0), stack))
                {
                    mainIsDust = true;
                    break;
                }
            }

            if (mainIsDust) dust = Collections.emptyList();

            List<List<ItemStack>> forms = Arrays.asList(main, nuggets, dust, tinyDust, gears, plates, rods);
            int available = 0;
            for (List<ItemStack> form : forms) if (!form.isEmpty()) available++;
            if (available <= 1) continue;

            boolean hasParts = !gears.isEmpty() || !plates.isEmpty() || !rods.isEmpty();
            int layout = 1;

            if (mainIsDust)
            {
                if (!nuggets.isEmpty()) layout = 3;
                else layout = hasParts ? 2 : 4;
            }
            else if (nuggets.isEmpty() && !hasParts) layout = 5;

            recipes.add(new MaterialTreeRecipe(forms, layout));
        }

        return recipes;
    }

    private static List<ItemStack> getForms(String... oreNames)
    {
        List<ItemStack> forms = new ArrayList<>();

        for (String name : oreNames)
        {
            for (ItemStack stack : OreDictionary.getOres(name, false))
            {
                if (stack.isEmpty()) continue;

                if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
                {
                    NonNullList<ItemStack> variants = NonNullList.create();
                    stack.getItem().getSubItems(CreativeTabs.SEARCH, variants);
                    for (ItemStack variant : variants) addForm(forms, variant);
                }
                else addForm(forms, stack);
            }
        }

        forms.sort((a, b) -> Boolean.compare(isModItem(b), isModItem(a)));
        return forms;
    }

    private static void addForm(List<ItemStack> forms, ItemStack stack)
    {
        if (stack.isEmpty()) return;

        ItemStack copy = stack.copy();
        copy.setCount(1);

        for (ItemStack existing : forms) if (ItemStack.areItemStacksEqual(existing, copy)) return;

        forms.add(copy);
    }

    private static boolean isModItem(ItemStack stack)
    {
        if (stack.isEmpty()) return false;

        ResourceLocation name = stack.getItem().getRegistryName();
        return name != null && Tags.MODID.equals(name.getNamespace());
    }
}