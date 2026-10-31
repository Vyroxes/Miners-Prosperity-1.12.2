package net.vyroxes.minersprosperity.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.inventory.container.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.inventory.container.ContainerElectricFurnace;
import net.vyroxes.minersprosperity.inventory.container.ContainerMetalPress;
import net.vyroxes.minersprosperity.inventory.container.ContainerPulverizer;
import net.vyroxes.minersprosperity.client.gui.GuiAlloyFurnace;
import net.vyroxes.minersprosperity.client.gui.GuiElectricFurnace;
import net.vyroxes.minersprosperity.client.gui.GuiMetalPress;
import net.vyroxes.minersprosperity.client.gui.GuiPulverizer;
import net.vyroxes.minersprosperity.integration.jei.alloy_furnace.AlloyFurnaceRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.alloy_furnace.AlloyFurnaceRecipeMaker;
import net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipeMaker;
import net.vyroxes.minersprosperity.integration.jei.pulverizer.PulverizerRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.pulverizer.PulverizerRecipeMaker;
import net.vyroxes.minersprosperity.integration.jei.material_tree.MaterialTreeRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.material_tree.MaterialTreeRecipeMaker;
import net.vyroxes.minersprosperity.client.gui.GuiLiquidFuelGenerator;
import net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator.LiquidFuelGeneratorRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator.LiquidFuelGeneratorRecipeTransferHandler;
import java.util.IllegalFormatException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.tileentity.TileEntityFurnace;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.machine.upgrade.MachineUpgradeType;
import net.vyroxes.minersprosperity.client.gui.GuiSolidFuelGenerator;
import net.vyroxes.minersprosperity.inventory.container.ContainerSolidFuelGenerator;
import net.vyroxes.minersprosperity.integration.jei.solid_fuel_generator.SolidFuelGeneratorRecipe;
import net.vyroxes.minersprosperity.integration.jei.solid_fuel_generator.SolidFuelGeneratorRecipeCategory;

@JEIPlugin
public class JEICompat implements IModPlugin

{
    @Override
    public void registerItemSubtypes(mezz.jei.api.ISubtypeRegistry registry)
    {
        for (net.minecraft.item.Item item : net.vyroxes.minersprosperity.init.ModItems.ITEMS)
        {
            if (!(item instanceof net.vyroxes.minersprosperity.item.battery.ItemBattery)
                    && !(item instanceof net.vyroxes.minersprosperity.block.machine.ItemBlockEnergyStorage)) continue;
            if (!net.vyroxes.minersprosperity.init.ModItems.isEnabled(item)) continue;
            registry.registerSubtypeInterpreter(item, stack ->
            {
                net.minecraftforge.energy.IEnergyStorage energy = stack.getCapability(net.minecraftforge.energy.CapabilityEnergy.ENERGY, null);
                return energy != null && energy.getEnergyStored() > 0 ? "charged" : "empty";
            });
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new AlloyFurnaceRecipeCategory(gui));
        registry.addRecipeCategories(new PulverizerRecipeCategory(gui));
        registry.addRecipeCategories(new MetalPressRecipeCategory(gui));
        registry.addRecipeCategories(new MaterialTreeRecipeCategory(gui));
        if (ModConfig.MACHINES.enableLiquidFuelGenerator) registry.addRecipeCategories(new LiquidFuelGeneratorRecipeCategory(gui));
        if (ModConfig.MACHINES.enableSolidFuelGenerator) registry.addRecipeCategories(new SolidFuelGeneratorRecipeCategory(gui));
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RESIN_BLOCK));
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RESIN_BLOCK_EMPTY));

        for (MachineUpgradeType type : MachineUpgradeType.values())
        {
            ItemStack upgrade = new ItemStack(type.getItem());
            String key = upgrade.getTranslationKey() + ".tooltip";
            List<String> descriptions = new ArrayList<>();
            descriptions.add(key);

            for (int line = 1; net.minecraft.client.resources.I18n.hasKey(key + "." + line); line++)
            {
                descriptions.add(key + "." + line);
            }

            descriptions.add("tooltip.upgrade.limit");
            registry.addIngredientInfo(upgrade, ItemStack.class, descriptions.toArray(new String[0]));
        }

        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        if (ModConfig.MACHINES.enableSolidFuelGenerator)
        {
            List<SolidFuelGeneratorRecipe> fuels = new ArrayList<>();
            for (ItemStack fuel : ingredientRegistry.getAllIngredients(ItemStack.class))
            {
                int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
                if (burnTime > 0) fuels.add(new SolidFuelGeneratorRecipe(fuel, burnTime));
            }
            registry.addRecipes(fuels, RecipeCategories.SOLID_FUEL_GENERATOR);
            registry.addRecipeClickArea(GuiSolidFuelGenerator.class, 80, 27, 16, 15, RecipeCategories.SOLID_FUEL_GENERATOR);
            recipeTransfer.addRecipeTransferHandler(ContainerSolidFuelGenerator.class, RecipeCategories.SOLID_FUEL_GENERATOR, 0, 1, 2, 36);
            registry.addRecipeCatalyst(new ItemStack(ModBlocks.SOLID_FUEL_GENERATOR), RecipeCategories.SOLID_FUEL_GENERATOR);
        }

        if (ModConfig.MACHINES.enableLiquidFuelGenerator)
        {
            List<net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator.LiquidFuelGeneratorRecipe> fuels = new ArrayList<>();
            for (net.vyroxes.minersprosperity.machine.recipe.LiquidFuelGeneratorRecipe.Recipe recipe : net.vyroxes.minersprosperity.machine.recipe.LiquidFuelGeneratorRecipe.getInstance().getRecipes())
            {
                fuels.add(new net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator.LiquidFuelGeneratorRecipe(recipe));
            }
            registry.addRecipes(fuels, RecipeCategories.LIQUID_FUEL_GENERATOR);
            registry.addRecipeCatalyst(new ItemStack(ModBlocks.LIQUID_FUEL_GENERATOR), RecipeCategories.LIQUID_FUEL_GENERATOR);
            registry.addRecipeClickArea(GuiLiquidFuelGenerator.class, 80, 36, 16, 15, RecipeCategories.LIQUID_FUEL_GENERATOR);
            recipeTransfer.addRecipeTransferHandler(new LiquidFuelGeneratorRecipeTransferHandler(jeiHelpers.recipeTransferHandlerHelper()), RecipeCategories.LIQUID_FUEL_GENERATOR);
        }

        registry.addRecipes(MaterialTreeRecipeMaker.getRecipes(), RecipeCategories.MATERIAL_TREE);

        registry.addRecipes(AnvilCustomRecipeMaker.getRepairRecipes(jeiHelpers.getVanillaRecipeFactory()), RecipeCategories.ANVIL);

        registry.addRecipes(AlloyFurnaceRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.ALLOY_FURNACE);
        registry.addRecipeClickArea(GuiAlloyFurnace.class, 79, 36, 21, 15, RecipeCategories.ALLOY_FURNACE);
        recipeTransfer.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategories.ALLOY_FURNACE, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_FURNACE), RecipeCategories.ALLOY_FURNACE);

        registry.addRecipes(PulverizerRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.PULVERIZER);
        registry.addRecipeClickArea(GuiPulverizer.class, 69, 36, 21, 15, RecipeCategories.PULVERIZER);
        recipeTransfer.addRecipeTransferHandler(ContainerPulverizer.class, RecipeCategories.PULVERIZER, 0, 1, 3, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PULVERIZER), RecipeCategories.PULVERIZER);

        registry.addRecipes(MetalPressRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.METAL_PRESS);
        registry.addRecipeClickArea(GuiMetalPress.class, 79, 36, 21, 15, RecipeCategories.METAL_PRESS);
        recipeTransfer.addRecipeTransferHandler(ContainerMetalPress.class, RecipeCategories.METAL_PRESS, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.METAL_PRESS), RecipeCategories.METAL_PRESS);

        registry.addRecipeClickArea(GuiElectricFurnace.class, 69, 36, 21, 15, RecipeCategories.SMELTING);
        recipeTransfer.addRecipeTransferHandler(ContainerElectricFurnace.class, RecipeCategories.SMELTING, 0, 1, 3, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_FURNACE), RecipeCategories.SMELTING);
    }

    @SuppressWarnings("deprecation")
    public static String translateToLocal(String key)
    {
        if(I18n.canTranslate(key)) return I18n.translateToLocal(key);
        else return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format)
    {
        String s = translateToLocal(key);
        try
        {
            return String.format(s, format);
        }
        catch (IllegalFormatException e)
        {
            return "Format error " + s;
        }
    }
}