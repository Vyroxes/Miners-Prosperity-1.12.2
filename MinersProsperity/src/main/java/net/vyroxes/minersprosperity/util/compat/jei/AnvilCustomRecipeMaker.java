package net.vyroxes.minersprosperity.util.compat.jei;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ArmorInit;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.init.ToolInit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnvilCustomRecipeMaker
{
    public static List<IRecipeWrapper> getRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory)
    {
        List<IRecipeWrapper> recipes = new ArrayList<>();

        Map<ItemStack, List<ItemStack>> repairMaterialsToItems = Maps.newHashMap();

        repairMaterialsToItems.put(new ItemStack(Blocks.PLANKS), Lists.newArrayList(
                new ItemStack(ArmorInit.WOODEN_BOOTS),
                new ItemStack(ArmorInit.WOODEN_LEGGINGS),
                new ItemStack(ArmorInit.WOODEN_CHESTPLATE),
                new ItemStack(ArmorInit.WOODEN_HELMET),
                new ItemStack(ToolInit.WOODEN_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(Blocks.STONE), Lists.newArrayList(
                new ItemStack(ArmorInit.STONE_BOOTS),
                new ItemStack(ArmorInit.STONE_LEGGINGS),
                new ItemStack(ArmorInit.STONE_CHESTPLATE),
                new ItemStack(ArmorInit.STONE_HELMET),
                new ItemStack(ToolInit.STONE_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.LEAD_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.LEAD_SWORD),
                new ItemStack(ToolInit.LEAD_PICKAXE),
                new ItemStack(ToolInit.LEAD_AXE),
                new ItemStack(ToolInit.LEAD_SHOVEL),
                new ItemStack(ToolInit.LEAD_HOE),
                new ItemStack(ArmorInit.LEAD_BOOTS),
                new ItemStack(ArmorInit.LEAD_LEGGINGS),
                new ItemStack(ArmorInit.LEAD_CHESTPLATE),
                new ItemStack(ArmorInit.LEAD_HELMET),
                new ItemStack(ToolInit.LEAD_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(Items.IRON_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.IRON_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.INCONEL_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.INCONEL_SWORD),
                new ItemStack(ToolInit.INCONEL_PICKAXE),
                new ItemStack(ToolInit.INCONEL_AXE),
                new ItemStack(ToolInit.INCONEL_SHOVEL),
                new ItemStack(ToolInit.INCONEL_HOE),
                new ItemStack(ArmorInit.INCONEL_BOOTS),
                new ItemStack(ArmorInit.INCONEL_LEGGINGS),
                new ItemStack(ArmorInit.INCONEL_CHESTPLATE),
                new ItemStack(ArmorInit.INCONEL_HELMET),
                new ItemStack(ToolInit.INCONEL_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(Items.GOLD_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.GOLDEN_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.ELECTRUM_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.ELECTRUM_SWORD),
                new ItemStack(ToolInit.ELECTRUM_PICKAXE),
                new ItemStack(ToolInit.ELECTRUM_AXE),
                new ItemStack(ToolInit.ELECTRUM_SHOVEL),
                new ItemStack(ToolInit.ELECTRUM_HOE),
                new ItemStack(ArmorInit.ELECTRUM_BOOTS),
                new ItemStack(ArmorInit.ELECTRUM_LEGGINGS),
                new ItemStack(ArmorInit.ELECTRUM_CHESTPLATE),
                new ItemStack(ArmorInit.ELECTRUM_HELMET),
                new ItemStack(ToolInit.ELECTRUM_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(Items.DIAMOND), Lists.newArrayList(
                new ItemStack(ToolInit.DIAMOND_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.OBSIDIAN_CLUSTER), Lists.newArrayList(
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_SWORD),
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_PICKAXE),
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_AXE),
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_SHOVEL),
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_HOE),
                new ItemStack(ArmorInit.OBSIDIAN_CLUSTER_BOOTS),
                new ItemStack(ArmorInit.OBSIDIAN_CLUSTER_LEGGINGS),
                new ItemStack(ArmorInit.OBSIDIAN_CLUSTER_CHESTPLATE),
                new ItemStack(ArmorInit.OBSIDIAN_CLUSTER_HELMET),
                new ItemStack(ToolInit.OBSIDIAN_CLUSTER_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(Items.EMERALD), Lists.newArrayList(
                new ItemStack(ToolInit.EMERALD_SWORD),
                new ItemStack(ToolInit.EMERALD_PICKAXE),
                new ItemStack(ToolInit.EMERALD_AXE),
                new ItemStack(ToolInit.EMERALD_SHOVEL),
                new ItemStack(ToolInit.EMERALD_HOE),
                new ItemStack(ArmorInit.EMERALD_BOOTS),
                new ItemStack(ArmorInit.EMERALD_LEGGINGS),
                new ItemStack(ArmorInit.EMERALD_CHESTPLATE),
                new ItemStack(ArmorInit.EMERALD_HELMET),
                new ItemStack(ToolInit.EMERALD_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.COBALTSTEEL_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.COBALTSTEEL_SWORD),
                new ItemStack(ToolInit.COBALTSTEEL_PICKAXE),
                new ItemStack(ToolInit.COBALTSTEEL_AXE),
                new ItemStack(ToolInit.COBALTSTEEL_SHOVEL),
                new ItemStack(ToolInit.COBALTSTEEL_HOE),
                new ItemStack(ArmorInit.COBALTSTEEL_BOOTS),
                new ItemStack(ArmorInit.COBALTSTEEL_LEGGINGS),
                new ItemStack(ArmorInit.COBALTSTEEL_CHESTPLATE),
                new ItemStack(ArmorInit.COBALTSTEEL_HELMET),
                new ItemStack(ToolInit.COBALTSTEEL_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.DARK_STEEL_INGOT), Lists.newArrayList(
                new ItemStack(ToolInit.DARK_STEEL_SWORD),
                new ItemStack(ToolInit.DARK_STEEL_PICKAXE),
                new ItemStack(ToolInit.DARK_STEEL_AXE),
                new ItemStack(ToolInit.DARK_STEEL_SHOVEL),
                new ItemStack(ToolInit.DARK_STEEL_HOE),
                new ItemStack(ArmorInit.DARK_STEEL_BOOTS),
                new ItemStack(ArmorInit.DARK_STEEL_LEGGINGS),
                new ItemStack(ArmorInit.DARK_STEEL_CHESTPLATE),
                new ItemStack(ArmorInit.DARK_STEEL_HELMET),
                new ItemStack(ToolInit.DARK_STEEL_PAXEL)
        ));

        repairMaterialsToItems.put(new ItemStack(ItemInit.BLACK_OPAL), Lists.newArrayList(
                new ItemStack(ToolInit.BLACK_OPAL_SWORD),
                new ItemStack(ToolInit.BLACK_OPAL_PICKAXE),
                new ItemStack(ToolInit.BLACK_OPAL_AXE),
                new ItemStack(ToolInit.BLACK_OPAL_SHOVEL),
                new ItemStack(ToolInit.BLACK_OPAL_HOE),
                new ItemStack(ArmorInit.BLACK_OPAL_BOOTS),
                new ItemStack(ArmorInit.BLACK_OPAL_LEGGINGS),
                new ItemStack(ArmorInit.BLACK_OPAL_CHESTPLATE),
                new ItemStack(ArmorInit.BLACK_OPAL_HELMET),
                new ItemStack(ToolInit.BLACK_OPAL_PAXEL)
        ));

        for (Map.Entry<ItemStack, List<ItemStack>> entry : repairMaterialsToItems.entrySet())
        {
            ItemStack repairMaterial = entry.getKey();

            for (ItemStack ingredient : entry.getValue())
            {
                ItemStack damaged1 = ingredient.copy();
                damaged1.setItemDamage(damaged1.getMaxDamage());

                ItemStack damaged2 = ingredient.copy();
                damaged2.setItemDamage(damaged2.getMaxDamage() * 3 / 4);

                ItemStack damaged3 = ingredient.copy();
                damaged3.setItemDamage(damaged3.getMaxDamage() / 2);

                IRecipeWrapper repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(damaged1, Collections.singletonList(repairMaterial), Collections.singletonList(damaged2));
                IRecipeWrapper repairWithSame = vanillaRecipeFactory.createAnvilRecipe(damaged2, Collections.singletonList(damaged2), Collections.singletonList(damaged3));

                recipes.add(repairWithMaterial);
                recipes.add(repairWithSame);
            }
        }
        return recipes;
    }
}