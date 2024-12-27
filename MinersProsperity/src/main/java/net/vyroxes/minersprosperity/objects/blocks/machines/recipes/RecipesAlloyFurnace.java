package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import com.github.bsideup.jabel.Desugar;
import it.unimi.dsi.fastutil.Hash;
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
		private final Map<ItemStack, Set<Integer>> itemStackToRecipeIndices;
		private final List<Recipe> recipes;

		public LookupTable()
		{
			this.itemStackToRecipeIndices = new HashMap<>();
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
			itemStackToRecipeIndices.computeIfAbsent(stack, k -> new HashSet<>()).add(index);
		}

		public List<Recipe> getRecipes()
		{
			return new ArrayList<>(recipes);
		}

		public List<Recipe> findRecipes(ItemStack... inputs)
		{
			if (inputs.length == 0)
			{
				return Collections.emptyList();
			}

			Set<Integer> validIndices = new HashSet<>(itemStackToRecipeIndices.getOrDefault(inputs[0], Collections.emptySet()));

			for (int i = 1; i < inputs.length; i++)
			{
				if (inputs[i] == null || inputs[i].isEmpty())
				{
					continue;
				}

				validIndices.retainAll(itemStackToRecipeIndices.getOrDefault(inputs[i], Collections.emptySet()));
			}

			List<Recipe> matchingRecipes = new ArrayList<>();
			for (int index : validIndices)
			{
				matchingRecipes.add(recipes.get(index));
			}

			return matchingRecipes;
		}
	}

	@Desugar
	private record RecipeKey(ItemStack ingredient1, ItemStack ingredient2)
	{
		private RecipeKey(ItemStack ingredient1, ItemStack ingredient2)
		{
			if (compareStacks(ingredient1, ingredient2) <= 0)
			{
				this.ingredient1 = ingredient1;
				this.ingredient2 = ingredient2;
			}
			else
			{
				this.ingredient1 = ingredient2;
				this.ingredient2 = ingredient1;
			}
		}

		private static int compareStacks(ItemStack a, ItemStack b)
		{
			return Objects.requireNonNull(a.getItem().getRegistryName()).compareTo(Objects.requireNonNull(b.getItem().getRegistryName()));
		}
	}

	private static class RecipeKeyHashStrategy implements Hash.Strategy<RecipeKey>
	{
		@Override
		public int hashCode(RecipeKey key)
		{
			return Objects.hash(key.ingredient1().getItem(), key.ingredient1().getMetadata(), key.ingredient2().getItem(), key.ingredient2().getMetadata());
		}

		@Override
		public boolean equals(RecipeKey a, RecipeKey b)
		{
			if (a == b) return true;
			if (a == null || b == null) return false;
			return ItemStack.areItemsEqual(a.ingredient1(), b.ingredient1()) && ItemStack.areItemsEqual(a.ingredient2(), b.ingredient2());
		}
	}

	public boolean isIngredientInAnyRecipe(ItemStack itemStack)
	{
		return !this.lookupTable.itemStackToRecipeIndices.getOrDefault(itemStack, Collections.emptySet()).isEmpty();
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