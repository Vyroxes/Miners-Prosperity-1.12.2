package net.vyroxes.minersprosperity.integration.jei;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.init.ModArmor;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.init.ModTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnvilCustomRecipeMaker
{
    public static List<IRecipeWrapper> getRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory)
    {
        List<IRecipeWrapper> recipes = new ArrayList<>();

        Map<Object, List<ItemStack>> repairMaterialsToItems = Maps.newLinkedHashMap();

        repairMaterialsToItems.put("stickWood", Lists.newArrayList(
                new ItemStack(Items.BOW),
                new ItemStack(Items.FISHING_ROD)
        ));

        repairMaterialsToItems.put("rodStone", Lists.newArrayList(
                new ItemStack(ModTools.STONE_BOW)
        ));

        repairMaterialsToItems.put("rodLead", Lists.newArrayList(
                new ItemStack(ModTools.LEAD_BOW)
        ));

        repairMaterialsToItems.put("rodIron", Lists.newArrayList(
                new ItemStack(ModTools.IRON_BOW)
        ));

        repairMaterialsToItems.put("rodInconel", Lists.newArrayList(
                new ItemStack(ModTools.INCONEL_BOW)
        ));

        repairMaterialsToItems.put("rodGold", Lists.newArrayList(
                new ItemStack(ModTools.GOLDEN_BOW)
        ));

        repairMaterialsToItems.put("rodElectrum", Lists.newArrayList(
                new ItemStack(ModTools.ELECTRUM_BOW)
        ));

        repairMaterialsToItems.put("rodDiamond", Lists.newArrayList(
                new ItemStack(ModTools.DIAMOND_BOW)
        ));

        repairMaterialsToItems.put("rodObsidianCluster", Lists.newArrayList(
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_BOW)
        ));

        repairMaterialsToItems.put("rodEmerald", Lists.newArrayList(
                new ItemStack(ModTools.EMERALD_BOW)
        ));

        repairMaterialsToItems.put("rodCobaltsteel", Lists.newArrayList(
                new ItemStack(ModTools.COBALTSTEEL_BOW)
        ));

        repairMaterialsToItems.put("rodDarkSteel", Lists.newArrayList(
                new ItemStack(ModTools.DARK_STEEL_BOW)
        ));

        repairMaterialsToItems.put("rodBlackOpal", Lists.newArrayList(
                new ItemStack(ModTools.BLACK_OPAL_BOW)
        ));

        repairMaterialsToItems.put("plankWood", Lists.newArrayList(
                new ItemStack(ModItems.WOODEN_TREETAP),
                new ItemStack(ModArmor.WOODEN_BOOTS),
                new ItemStack(ModArmor.WOODEN_LEGGINGS),
                new ItemStack(ModArmor.WOODEN_CHESTPLATE),
                new ItemStack(ModArmor.WOODEN_HELMET),
                new ItemStack(ModTools.WOODEN_PAXEL),
                new ItemStack(ModTools.WOODEN_SHEARS)
        ));

        repairMaterialsToItems.put("cobblestone", Lists.newArrayList(
                new ItemStack(ModArmor.STONE_BOOTS),
                new ItemStack(ModArmor.STONE_LEGGINGS),
                new ItemStack(ModArmor.STONE_CHESTPLATE),
                new ItemStack(ModArmor.STONE_HELMET),
                new ItemStack(ModTools.STONE_PAXEL),
                new ItemStack(ModTools.STONE_SHIELD),
                new ItemStack(ModTools.STONE_SHEARS)
        ));

        repairMaterialsToItems.put("ingotRefinedObsidian", Lists.newArrayList(
                new ItemStack(ModItems.REFINED_OBSIDIAN_TREETAP)
        ));

        repairMaterialsToItems.put("ingotSteel", Lists.newArrayList(
                new ItemStack(ModItems.WRENCH)
        ));

        repairMaterialsToItems.put("ingotLead", Lists.newArrayList(
                new ItemStack(ModTools.LEAD_SWORD),
                new ItemStack(ModTools.LEAD_PICKAXE),
                new ItemStack(ModTools.LEAD_AXE),
                new ItemStack(ModTools.LEAD_SHOVEL),
                new ItemStack(ModTools.LEAD_HOE),
                new ItemStack(ModArmor.LEAD_BOOTS),
                new ItemStack(ModArmor.LEAD_LEGGINGS),
                new ItemStack(ModArmor.LEAD_CHESTPLATE),
                new ItemStack(ModArmor.LEAD_HELMET),
                new ItemStack(ModTools.LEAD_PAXEL),
                new ItemStack(ModTools.LEAD_SHIELD),
                new ItemStack(ModTools.LEAD_SHEARS)
        ));

        repairMaterialsToItems.put("ingotIron", Lists.newArrayList(
                new ItemStack(Items.SHEARS),
                new ItemStack(Items.FLINT_AND_STEEL),
                new ItemStack(ModItems.IRON_TREETAP),
                new ItemStack(ModTools.IRON_PAXEL),
                new ItemStack(ModTools.IRON_SHIELD)
        ));

        repairMaterialsToItems.put("ingotInconel", Lists.newArrayList(
                new ItemStack(ModTools.INCONEL_SWORD),
                new ItemStack(ModTools.INCONEL_PICKAXE),
                new ItemStack(ModTools.INCONEL_AXE),
                new ItemStack(ModTools.INCONEL_SHOVEL),
                new ItemStack(ModTools.INCONEL_HOE),
                new ItemStack(ModArmor.INCONEL_BOOTS),
                new ItemStack(ModArmor.INCONEL_LEGGINGS),
                new ItemStack(ModArmor.INCONEL_CHESTPLATE),
                new ItemStack(ModArmor.INCONEL_HELMET),
                new ItemStack(ModTools.INCONEL_PAXEL),
                new ItemStack(ModTools.INCONEL_SHIELD),
                new ItemStack(ModTools.INCONEL_SHEARS)
        ));

        repairMaterialsToItems.put("ingotGold", Lists.newArrayList(
                new ItemStack(ModTools.GOLDEN_PAXEL),
                new ItemStack(ModTools.GOLDEN_SHIELD),
                new ItemStack(ModTools.GOLDEN_SHEARS)
        ));

        repairMaterialsToItems.put("ingotElectrum", Lists.newArrayList(
                new ItemStack(ModTools.ELECTRUM_SWORD),
                new ItemStack(ModTools.ELECTRUM_PICKAXE),
                new ItemStack(ModTools.ELECTRUM_AXE),
                new ItemStack(ModTools.ELECTRUM_SHOVEL),
                new ItemStack(ModTools.ELECTRUM_HOE),
                new ItemStack(ModArmor.ELECTRUM_BOOTS),
                new ItemStack(ModArmor.ELECTRUM_LEGGINGS),
                new ItemStack(ModArmor.ELECTRUM_CHESTPLATE),
                new ItemStack(ModArmor.ELECTRUM_HELMET),
                new ItemStack(ModTools.ELECTRUM_PAXEL),
                new ItemStack(ModTools.ELECTRUM_SHIELD),
                new ItemStack(ModTools.ELECTRUM_SHEARS)
        ));

        repairMaterialsToItems.put("gemDiamond", Lists.newArrayList(
                new ItemStack(ModTools.DIAMOND_PAXEL),
                new ItemStack(ModTools.DIAMOND_SHIELD),
                new ItemStack(ModTools.DIAMOND_SHEARS)
        ));

        repairMaterialsToItems.put("clusterObsidian", Lists.newArrayList(
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_SWORD),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_PICKAXE),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_AXE),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_SHOVEL),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_HOE),
                new ItemStack(ModArmor.OBSIDIAN_CLUSTER_BOOTS),
                new ItemStack(ModArmor.OBSIDIAN_CLUSTER_LEGGINGS),
                new ItemStack(ModArmor.OBSIDIAN_CLUSTER_CHESTPLATE),
                new ItemStack(ModArmor.OBSIDIAN_CLUSTER_HELMET),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_PAXEL),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_SHIELD),
                new ItemStack(ModTools.OBSIDIAN_CLUSTER_SHEARS)
        ));

        repairMaterialsToItems.put("gemEmerald", Lists.newArrayList(
                new ItemStack(ModTools.EMERALD_SWORD),
                new ItemStack(ModTools.EMERALD_PICKAXE),
                new ItemStack(ModTools.EMERALD_AXE),
                new ItemStack(ModTools.EMERALD_SHOVEL),
                new ItemStack(ModTools.EMERALD_HOE),
                new ItemStack(ModArmor.EMERALD_BOOTS),
                new ItemStack(ModArmor.EMERALD_LEGGINGS),
                new ItemStack(ModArmor.EMERALD_CHESTPLATE),
                new ItemStack(ModArmor.EMERALD_HELMET),
                new ItemStack(ModTools.EMERALD_PAXEL),
                new ItemStack(ModTools.EMERALD_SHIELD),
                new ItemStack(ModTools.EMERALD_SHEARS)
        ));

        repairMaterialsToItems.put("ingotCobaltsteel", Lists.newArrayList(
                new ItemStack(ModTools.COBALTSTEEL_SWORD),
                new ItemStack(ModTools.COBALTSTEEL_PICKAXE),
                new ItemStack(ModTools.COBALTSTEEL_AXE),
                new ItemStack(ModTools.COBALTSTEEL_SHOVEL),
                new ItemStack(ModTools.COBALTSTEEL_HOE),
                new ItemStack(ModArmor.COBALTSTEEL_BOOTS),
                new ItemStack(ModArmor.COBALTSTEEL_LEGGINGS),
                new ItemStack(ModArmor.COBALTSTEEL_CHESTPLATE),
                new ItemStack(ModArmor.COBALTSTEEL_HELMET),
                new ItemStack(ModTools.COBALTSTEEL_PAXEL),
                new ItemStack(ModTools.COBALTSTEEL_SHIELD),
                new ItemStack(ModTools.COBALTSTEEL_SHEARS)
        ));

        repairMaterialsToItems.put("ingotDarkSteel", Lists.newArrayList(
                new ItemStack(ModTools.DARK_STEEL_SWORD),
                new ItemStack(ModTools.DARK_STEEL_PICKAXE),
                new ItemStack(ModTools.DARK_STEEL_AXE),
                new ItemStack(ModTools.DARK_STEEL_SHOVEL),
                new ItemStack(ModTools.DARK_STEEL_HOE),
                new ItemStack(ModArmor.DARK_STEEL_BOOTS),
                new ItemStack(ModArmor.DARK_STEEL_LEGGINGS),
                new ItemStack(ModArmor.DARK_STEEL_CHESTPLATE),
                new ItemStack(ModArmor.DARK_STEEL_HELMET),
                new ItemStack(ModTools.DARK_STEEL_PAXEL),
                new ItemStack(ModTools.DARK_STEEL_SHIELD),
                new ItemStack(ModTools.DARK_STEEL_SHEARS)
        ));

        repairMaterialsToItems.put("gemBlackOpal", Lists.newArrayList(
                new ItemStack(ModTools.BLACK_OPAL_SWORD),
                new ItemStack(ModTools.BLACK_OPAL_PICKAXE),
                new ItemStack(ModTools.BLACK_OPAL_AXE),
                new ItemStack(ModTools.BLACK_OPAL_SHOVEL),
                new ItemStack(ModTools.BLACK_OPAL_HOE),
                new ItemStack(ModArmor.BLACK_OPAL_BOOTS),
                new ItemStack(ModArmor.BLACK_OPAL_LEGGINGS),
                new ItemStack(ModArmor.BLACK_OPAL_CHESTPLATE),
                new ItemStack(ModArmor.BLACK_OPAL_HELMET),
                new ItemStack(ModTools.BLACK_OPAL_PAXEL),
                new ItemStack(ModTools.BLACK_OPAL_SHIELD),
                new ItemStack(ModTools.BLACK_OPAL_SHEARS)
        ));

        for (Map.Entry<Object, List<ItemStack>> entry : repairMaterialsToItems.entrySet())
        {
            List<ItemStack> repairMaterials;

            if (entry.getKey() instanceof String) repairMaterials = OreDictionary.getOres((String) entry.getKey(), false);
            else if (entry.getKey() instanceof ItemStack) repairMaterials = Collections.singletonList(((ItemStack) entry.getKey()).copy());
            else continue;

            if (repairMaterials.isEmpty()) continue;

            for (ItemStack ingredient : entry.getValue())
            {
                ItemStack damaged1 = ingredient.copy();
                damaged1.setItemDamage(damaged1.getMaxDamage());

                ItemStack damaged2 = ingredient.copy();
                damaged2.setItemDamage(damaged2.getMaxDamage() * 3 / 4);

                ItemStack damaged3 = ingredient.copy();
                damaged3.setItemDamage(damaged3.getMaxDamage() / 2);

                List<ItemStack> repairOutputs = new ArrayList<>();

                for (ItemStack ignored : repairMaterials) repairOutputs.add(damaged2.copy());

                IRecipeWrapper repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(damaged1, repairMaterials, repairOutputs);
                IRecipeWrapper repairWithSame = vanillaRecipeFactory.createAnvilRecipe(damaged2, Collections.singletonList(damaged2), Collections.singletonList(damaged3));

                recipes.add(repairWithMaterial);
                recipes.add(repairWithSame);
            }
        }
        return recipes;
    }
}