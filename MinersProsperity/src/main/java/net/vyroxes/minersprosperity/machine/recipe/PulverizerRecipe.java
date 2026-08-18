package net.vyroxes.minersprosperity.machine.recipe;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import javax.annotation.Nullable;
import java.util.*;

public class PulverizerRecipe
{
	private static final PulverizerRecipe INSTANCE = new PulverizerRecipe();
	private final LookupTable lookupTable;

	public PulverizerRecipe()
	{
		this.lookupTable = new LookupTable();
	}

	public static PulverizerRecipe getInstance()
	{
		return INSTANCE;
	}

	public LookupTable getLookupTable()
	{
		return this.lookupTable;
	}

	public void addRecipe(@Nullable ItemStack input, @Nullable String oreDictInput, int quantity, ItemStack result, int totalCookTime, int energyUsage, float experience)
	{
		Recipe recipe = new Recipe(input, oreDictInput, quantity, result, totalCookTime, energyUsage, experience);
		this.lookupTable.addRow(recipe);
	}

	public List<Recipe> findRecipes(ItemStack input) { return this.lookupTable.findRecipes(input); }

	public boolean isInputValid(ItemStack input) { return this.lookupTable.isInputValid(input); }

	public static class Recipe
	{
		public ItemStack input;
		public String oreDict;
		public int quantity;
		public ItemStack result;
		public int totalCookTime;
		public int energyUsage;
		public float experience;

		Recipe(@Nullable ItemStack input, @Nullable String oreDict, int quantity, ItemStack result, int totalCookTime, int energyUsage, float experience)
		{
			this.input = input;
			this.oreDict = oreDict;
			this.quantity = quantity;
			this.result = result;
			this.totalCookTime = totalCookTime;
			this.energyUsage = energyUsage;
			this.experience = experience;
		}

		public int getRequiredQuantity(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return 0;

			if (this.input != null && OreDictionary.itemMatches(this.input, stack, false)) return this.quantity;
			if (this.oreDict != null && !this.oreDict.isEmpty() && isOreDictMatch(stack, this.oreDict)) return this.quantity;

			return 0;
		}
	}

	public static class LookupTable
	{
		private final Object2ObjectOpenCustomHashMap<ItemStack, IntOpenHashSet> recipeKeyToIndices;
		private final Map<String, IntOpenHashSet> oreDictToIndices;
		private final List<Recipe> recipes;
		private final List<Recipe> unmodifiableRecipes;

		public LookupTable()
		{
			this.recipeKeyToIndices = new Object2ObjectOpenCustomHashMap<>(new ItemStackHashStrategy());
			this.oreDictToIndices = new HashMap<>();
			this.recipes = new ArrayList<>();
			this.unmodifiableRecipes = Collections.unmodifiableList(this.recipes);
		}

		public void addRow(Recipe recipe)
		{
			int index = this.recipes.size();
			this.recipes.add(recipe);

			if (recipe.input != null && !recipe.input.isEmpty()) this.addItemToMap(recipe.input, index);
			if (recipe.oreDict != null && !recipe.oreDict.isEmpty()) this.oreDictToIndices.computeIfAbsent(recipe.oreDict, k -> new IntOpenHashSet()).add(index);
		}

		private void addItemToMap(ItemStack stack, int index)
		{
			if (stack != null && !stack.isEmpty()) this.recipeKeyToIndices.computeIfAbsent(stack, k -> new IntOpenHashSet()).add(index);
		}

		public List<Recipe> getRecipes() { return this.unmodifiableRecipes; }

		public boolean isInputValid(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return false;
			return !this.getMatchingIndices(stack).isEmpty();
		}

		public List<Recipe> findRecipes(ItemStack input)
		{
			if (input == null || input.isEmpty()) return Collections.emptyList();

			IntOpenHashSet matchingIndices = this.getMatchingIndices(input);
			if (matchingIndices.isEmpty()) return Collections.emptyList();

			List<Recipe> matchingRecipes = new ArrayList<>();

			for (int index : matchingIndices)
			{
				Recipe recipe = this.recipes.get(index);
				int requiredQuantity = recipe.getRequiredQuantity(input);

				if (requiredQuantity <= 0 || input.getCount() < requiredQuantity) continue;

				matchingRecipes.add(recipe);
			}

			return matchingRecipes;
		}

		private IntOpenHashSet getMatchingIndices(ItemStack input)
		{
			IntOpenHashSet indices = new IntOpenHashSet();

			IntOpenHashSet exactIndices = this.recipeKeyToIndices.get(input);
			if (exactIndices != null) indices.addAll(exactIndices);

			ItemStack wildcardStack = new ItemStack(input.getItem(), 1, OreDictionary.WILDCARD_VALUE);
			IntOpenHashSet wildcardIndices = this.recipeKeyToIndices.get(wildcardStack);
			if (wildcardIndices != null) indices.addAll(wildcardIndices);

			for (int oreID : OreDictionary.getOreIDs(input))
			{
				String oreName = OreDictionary.getOreName(oreID);
				IntOpenHashSet oreIndices = this.oreDictToIndices.get(oreName);
				if (oreIndices != null) indices.addAll(oreIndices);
			}

			return indices;
		}
	}

	public static boolean isOreDictMatch(ItemStack stack, String oreDict)
	{
		if (stack == null || stack.isEmpty() || oreDict == null || oreDict.isEmpty()) return false;

		for (int oreID : OreDictionary.getOreIDs(stack)) if (OreDictionary.getOreName(oreID).equals(oreDict)) return true;

		return false;
	}

	private static class ItemStackHashStrategy implements Hash.Strategy<ItemStack>
	{
		@Override
		public int hashCode(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return 0;
			return Objects.hash(stack.getItem(), stack.getMetadata());
		}

		@Override
		public boolean equals(ItemStack a, ItemStack b)
		{
			if (a == b) return true;
			if (a == null || b == null) return false;
			return ItemStack.areItemsEqual(a, b);
		}
	}

	public ItemStack getResult(ItemStack input)
	{
		List<Recipe> recipes = this.findRecipes(input);
		return !recipes.isEmpty() ? recipes.get(0).result : ItemStack.EMPTY;
	}

	public int getEnergyUsage(ItemStack input)
	{
		List<Recipe> recipes = this.findRecipes(input);
		return !recipes.isEmpty() ? recipes.get(0).energyUsage : 0;
	}

	public int getCookTime(ItemStack input)
	{
		List<Recipe> recipes = this.findRecipes(input);
		return !recipes.isEmpty() ? recipes.get(0).totalCookTime : 0;
	}

	public float getExperience(ItemStack input)
	{
		List<Recipe> recipes = this.findRecipes(input);
		return !recipes.isEmpty() ? recipes.get(0).experience : 0.0F;
	}
}