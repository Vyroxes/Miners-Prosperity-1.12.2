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

	public static class LookupTable
	{
		private final Object2ObjectOpenCustomHashMap<ItemStack, IntOpenHashSet> recipeKeyToIndices;
		private final List<Recipe> recipes;

		public LookupTable()
		{
			this.recipeKeyToIndices = new Object2ObjectOpenCustomHashMap<>(new ItemStackHashStrategy());
			this.recipes = new ArrayList<>();
		}

		public void addRow(Recipe recipe)
		{
			int index = recipes.size();
			recipes.add(recipe);

			addItemToMap(recipe.input1, index);
			addItemToMap(recipe.input2, index);
		}

		private void addItemToMap(ItemStack stack, int index)
		{
			recipeKeyToIndices.computeIfAbsent(stack, k -> new IntOpenHashSet()).add(index);
		}

		public List<Recipe> getRecipes()
		{
			return new ArrayList<>(recipes);
		}

		public List<Recipe> findRecipes(ItemStack... inputs)
		{
			if (inputs == null || inputs.length == 0)
			{
				return Collections.emptyList();
			}

			IntOpenHashSet matchingIndices = new IntOpenHashSet();
			for (ItemStack input : inputs)
			{
				if (input == null || input.isEmpty()) continue;

				IntOpenHashSet indices = recipeKeyToIndices.getOrDefault(input, new IntOpenHashSet());
				if (matchingIndices.isEmpty())
				{
					matchingIndices.addAll(indices);
				}
				else
				{
					matchingIndices.retainAll(indices);
				}
			}

			if (matchingIndices.isEmpty())
			{
				return Collections.emptyList();
			}

			List<Recipe> matchingRecipes = new ArrayList<>();
			for (int index : matchingIndices)
			{
				Recipe recipe = recipes.get(index);
				if (matchesRecipe(recipe, inputs))
				{
					matchingRecipes.add(recipe);
				}
			}

			return matchingRecipes;
		}

		private boolean matchesRecipe(Recipe recipe, ItemStack... inputs)
		{
			Set<ItemStack> recipeInputs = new HashSet<>(Arrays.asList(recipe.input1, recipe.input2));

			for (ItemStack input : inputs)
			{
				if (input == null || input.isEmpty()) continue;

				boolean foundMatch = recipeInputs.stream().anyMatch(r -> ItemStack.areItemsEqual(r, input));
				if (!foundMatch)
				{
					return false;
				}

				recipeInputs.removeIf(r -> ItemStack.areItemsEqual(r, input));
			}

			return true;
		}
	}

	private static class ItemStackHashStrategy implements Hash.Strategy<ItemStack>
	{
		@Override
		public int hashCode(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return 0;
			return Objects.hash(stack.getItem(), stack.getMetadata(), stack.getTagCompound());
		}

		@Override
		public boolean equals(ItemStack a, ItemStack b)
		{
			if (a == b) return true;
			if (a == null || b == null) return false;
			return ItemStack.areItemsEqual(a, b);
		}
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
		for (Recipe recipe : this.lookupTable.recipes)
		{
			if (ItemStack.areItemsEqual(recipe.result, result))
			{
				return recipe.experience;
			}
		}
		return 0.0f;
	}
}