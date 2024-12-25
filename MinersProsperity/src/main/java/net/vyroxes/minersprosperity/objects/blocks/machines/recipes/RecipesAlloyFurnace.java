package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;
import java.util.*;

public class RecipesAlloyFurnace
{
	private static final RecipesAlloyFurnace INSTANCE = new RecipesAlloyFurnace();

	private final LookupTable lookupTable;

	public RecipesAlloyFurnace()
	{
		this.lookupTable = new LookupTable();
		addRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND), 100, 20, 0.7f);
		addRecipe(new ItemStack(ItemInit.DIAMOND_DUST), new ItemStack(ItemInit.EMERALD_DUST), new ItemStack(Items.REDSTONE), 100, 20, 0.1F);
	}

	public static RecipesAlloyFurnace getInstance()
	{
		return INSTANCE;
	}

	public LookupTable getLookupTable()
	{
		return this.lookupTable;
	}

	public void addRecipe(ItemStack input1, ItemStack input2, ItemStack result, int totalCookTime, int energyUsage, float experience)
	{
		Recipe recipe = new Recipe(input1, input2, result, totalCookTime, energyUsage, experience);
		lookupTable.addRow(recipe);
	}

	public List<Recipe> findRecipes(ItemStack... inputs)
	{
		return lookupTable.findRecipes(inputs);
	}

	public static class Recipe
	{
		public ItemStack input1;
		public ItemStack input2;
		public ItemStack result;
		public int totalCookTime;
		public int energyUsage;
		public float experience;

		Recipe(ItemStack input1, ItemStack input2, ItemStack result, int totalCookTime, int energyUsage, float experience)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.result = result;
			this.totalCookTime = totalCookTime;
			this.energyUsage = energyUsage;
			this.experience = experience;
		}
	}

	public static class Row
	{
		private final ItemStack[] ingredients;
		private final Recipe recipe;

		public Row(ItemStack[] ingredients, Recipe recipe)
		{
			this.ingredients = ingredients;
			this.recipe = recipe;
		}

		public ItemStack[] getIngredients()
		{
			return ingredients;
		}

		public Recipe getRecipe()
		{
			return recipe;
		}
	}

	public static class LookupTable
	{
		public final List<Row> rows = new ArrayList<>();
		private final Map<ItemStack, Set<Integer>>[] ingredientIndices;

		@SuppressWarnings("unchecked")
		public LookupTable()
		{
			ingredientIndices = new Map[2];
			for (int i = 0; i < 2; i++)
			{
				ingredientIndices[i] = new Object2ObjectOpenCustomHashMap<>(new ItemStackHashStrategy());
			}
		}

		public void addRow(Recipe recipe) {
			int index = rows.size();
			Row row = new Row(new ItemStack[]{recipe.input1, recipe.input2}, recipe);
			rows.add(row);

			for (int i = 0; i < row.ingredients.length; i++)
			{
				ItemStack ingredient = row.ingredients[i];
				ingredientIndices[i]
						.computeIfAbsent(ingredient, k -> new IntOpenHashSet())
						.add(index);
			}
		}

		public List<Recipe> findRecipes(ItemStack... inputs)
		{
			if (inputs.length != 2)
			{
				throw new IllegalArgumentException("Exactly two inputs are required.");
			}

			Set<Integer> matchingIndices = new IntOpenHashSet();

			Set<Integer> matches1 = ingredientIndices[0].getOrDefault(inputs[0], Collections.emptySet());
			Set<Integer> matches2 = ingredientIndices[1].getOrDefault(inputs[1], Collections.emptySet());

			matchingIndices.addAll(matches1);
			matchingIndices.retainAll(matches2);

			if (matchingIndices.isEmpty())
			{
				Set<Integer> matches3 = ingredientIndices[0].getOrDefault(inputs[1], Collections.emptySet());
				Set<Integer> matches4 = ingredientIndices[1].getOrDefault(inputs[0], Collections.emptySet());

				matchingIndices.addAll(matches3);
				matchingIndices.retainAll(matches4);
			}

			List<Recipe> matchingRecipes = new ArrayList<>();
			for (int index : matchingIndices)
			{
				matchingRecipes.add(rows.get(index).recipe);
			}

			return matchingRecipes;
		}
	}

	public boolean isIngredientInAnyRecipe(ItemStack itemStack)
	{
		for (Row row : lookupTable.rows)
		{
			if (ItemStack.areItemStacksEqual(row.recipe.input1, itemStack) || ItemStack.areItemStacksEqual(row.recipe.input2, itemStack))
			{
				return true;
			}
		}
		return false;
	}

	public ItemStack getResult(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty())
		{
			return recipes.get(0).result;
		}
		return ItemStack.EMPTY;
	}

	public int getEnergyUsage(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty())
		{
			return recipes.get(0).energyUsage;
		}
		return 0;
	}

	public int getCookTime(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty())
		{
			return recipes.get(0).totalCookTime;
		}
		return 0;
	}

	public float getExperience(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty())
		{
			return recipes.get(0).experience;
		}
		return 0.0f;
	}

	public float getExperience(ItemStack result)
	{
		for (Row row : lookupTable.rows)
		{
			if (ItemStack.areItemStacksEqual(row.recipe.result, result))
			{
				return row.recipe.experience;
			}
		}
		return 0.0f;
	}

	private static class ItemStackHashStrategy implements Hash.Strategy<ItemStack>
	{
		@Override
		public int hashCode(ItemStack itemStack)
		{
			return Objects.hash(itemStack.getItem(), itemStack.getMetadata());
		}

		@Override
		public boolean equals(ItemStack a, ItemStack b)
		{
			if (a == null || b == null) return false;
			return a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata();
		}
	}
}