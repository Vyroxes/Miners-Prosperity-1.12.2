package net.vyroxes.minersprosperity.machine.recipe;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import javax.annotation.Nullable;
import java.util.*;

public class AlloyFurnaceRecipe
{
	private static final AlloyFurnaceRecipe INSTANCE = new AlloyFurnaceRecipe();
	private final LookupTable lookupTable;

	public AlloyFurnaceRecipe()
	{
		this.lookupTable = new LookupTable();
	}

	public static AlloyFurnaceRecipe getInstance()
	{
		return INSTANCE;
	}

	public LookupTable getLookupTable()
	{
		return this.lookupTable;
	}

	public void addRecipe(ItemStack input1, ItemStack input2, String oreDictInput1, String oreDictInput2, int quantity1, int quantity2, ItemStack result, int totalCookTime, int energyUsage, float experience)
	{
		Recipe recipe = new Recipe(input1, input2, oreDictInput1, oreDictInput2, quantity1, quantity2, result, totalCookTime, energyUsage, experience);
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
		public String oreDict1;
		public String oreDict2;
		public int quantity1;
		public int quantity2;
		public ItemStack result;
		public int totalCookTime;
		public int energyUsage;
		public float experience;

		Recipe(@Nullable ItemStack input1, @Nullable ItemStack input2, @Nullable String oreDict1, @Nullable String oreDict2, int quantity1, int quantity2, ItemStack result, int totalCookTime, int energyUsage, float experience)
		{
			this.input1 = input1;
			this.input2 = input2;
			this.oreDict1 = oreDict1;
			this.oreDict2 = oreDict2;
			this.quantity1 = quantity1;
			this.quantity2 = quantity2;
			this.result = result;
			this.totalCookTime = totalCookTime;
			this.energyUsage = energyUsage;
			this.experience = experience;
		}

		public int getRequiredQuantity(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return 0;

			if (this.input1 != null && OreDictionary.itemMatches(this.input1, stack, false)) return this.quantity1;

			if (this.input2 != null && OreDictionary.itemMatches(this.input2, stack, false)) return this.quantity2;

			if (this.oreDict1 != null && !this.oreDict1.isEmpty() && isOreDictMatch(stack, this.oreDict1)) return this.quantity1;

			if (this.oreDict2 != null && !this.oreDict2.isEmpty() && isOreDictMatch(stack, this.oreDict2)) return this.quantity2;

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
			this.unmodifiableRecipes = Collections.unmodifiableList(recipes);
		}

		public void addRow(Recipe recipe)
		{
			int index = recipes.size();
			recipes.add(recipe);

			if (recipe.input1 != null && !recipe.input1.isEmpty()) addItemToMap(recipe.input1, index);
			if (recipe.input2 != null && !recipe.input2.isEmpty()) addItemToMap(recipe.input2, index);
			if (recipe.oreDict1 != null && !recipe.oreDict1.isEmpty()) oreDictToIndices.computeIfAbsent(recipe.oreDict1, k -> new IntOpenHashSet()).add(index);
			if (recipe.oreDict2 != null && !recipe.oreDict2.isEmpty()) oreDictToIndices.computeIfAbsent(recipe.oreDict2, k -> new IntOpenHashSet()).add(index);
		}

		private void addItemToMap(ItemStack stack, int index)
		{
			if (stack != null && !stack.isEmpty()) recipeKeyToIndices.computeIfAbsent(stack, k -> new IntOpenHashSet()).add(index);
		}

		public List<Recipe> getRecipes()
		{
			return unmodifiableRecipes;
		}

		public boolean isItemValid(IItemHandler itemHandler, int start, int len, int slot, ItemStack slotStack)
		{
			if (itemHandler == null || slotStack == null || slotStack.isEmpty()) return false;

			for (int i = start; i < start + len; i++)
			{
				if (i == slot) continue;

				ItemStack stackInSlot = itemHandler.getStackInSlot(i);
				if (!stackInSlot.isEmpty() && ItemStack.areItemsEqual(stackInSlot, slotStack)) return false;

				if (!stackInSlot.isEmpty())
				{
					int[] oreIDs1 = OreDictionary.getOreIDs(stackInSlot);
					int[] oreIDs2 = OreDictionary.getOreIDs(slotStack);

					for (int oreID1 : oreIDs1) for (int oreID2 : oreIDs2) if (oreID1 == oreID2) return false;
				}
			}

			IntOpenHashSet matchingIndices = null;

			for (int i = start; i < start + len; i++)
			{
				final ItemStack input = i == slot ? slotStack : itemHandler.getStackInSlot(i);

				if (input.isEmpty()) continue;

				IntOpenHashSet indices = getMatchingIndices(input);

				if (matchingIndices == null) matchingIndices = new IntOpenHashSet(indices);
				else matchingIndices.retainAll(indices);

				if (matchingIndices.isEmpty()) return false;
			}

			return matchingIndices != null && !matchingIndices.isEmpty();
		}

		public List<Recipe> findRecipes(ItemStack... inputs)
		{
			if (inputs == null || inputs.length == 0) return Collections.emptyList();

			for (int i = 0; i < inputs.length; i++)
			{
				if (inputs[i] == null || inputs[i].isEmpty()) continue;

				for (int j = i + 1; j < inputs.length; j++)
				{
					if (inputs[j] == null || inputs[j].isEmpty()) continue;

					if (ItemStack.areItemsEqual(inputs[i], inputs[j])) return Collections.emptyList();

					int[] oreIDs1 = OreDictionary.getOreIDs(inputs[i]);
					int[] oreIDs2 = OreDictionary.getOreIDs(inputs[j]);

					for (int oreID1 : oreIDs1) for (int oreID2 : oreIDs2) if (oreID1 == oreID2) return Collections.emptyList();
				}
			}

			IntOpenHashSet matchingIndices = null;

			for (ItemStack input : inputs)
			{
				if (input == null || input.isEmpty()) continue;

				IntOpenHashSet indices = getMatchingIndices(input);

				if (indices.isEmpty()) return Collections.emptyList();

				if (matchingIndices == null) matchingIndices = new IntOpenHashSet(indices);
				else matchingIndices.retainAll(indices);

				if (matchingIndices.isEmpty()) return Collections.emptyList();
			}

			if (matchingIndices == null || matchingIndices.isEmpty()) return Collections.emptyList();

			List<Recipe> matchingRecipes = new ArrayList<>();
			for (int index : matchingIndices)
			{
				Recipe recipe = recipes.get(index);

				for (ItemStack input : inputs)
				{
					if (input == null || input.isEmpty()) continue;

					int requiredQuantity = recipe.getRequiredQuantity(input);
					if (requiredQuantity > 0 && input.getCount() < requiredQuantity) return Collections.emptyList();
				}

				matchingRecipes.add(recipes.get(index));
			}

			return matchingRecipes;
		}

		private IntOpenHashSet getMatchingIndices(ItemStack input)
		{
			IntOpenHashSet indices = new IntOpenHashSet();

			IntOpenHashSet exactIndices = recipeKeyToIndices.get(input);

			if (exactIndices != null) indices.addAll(exactIndices);

			ItemStack wildcardStack = new ItemStack(input.getItem(), 1, OreDictionary.WILDCARD_VALUE);
			IntOpenHashSet wildcardIndices = recipeKeyToIndices.get(wildcardStack);

			if (wildcardIndices != null) indices.addAll(wildcardIndices);

			for (int oreID : OreDictionary.getOreIDs(input))
			{
				String oreName = OreDictionary.getOreName(oreID);
				IntOpenHashSet oreIndices = oreDictToIndices.get(oreName);

				if (oreIndices != null) indices.addAll(oreIndices);
			}

			return indices;
		}
	}

	public static boolean isOreDictMatch(ItemStack stack, String oreDict)
	{
		if (stack == null || stack.isEmpty() || oreDict == null || oreDict.isEmpty()) return false;

		int[] oreIDs = OreDictionary.getOreIDs(stack);
		for (int oreID : oreIDs)
		{
			if (OreDictionary.getOreName(oreID).equals(oreDict)) return true;
		}

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

	public ItemStack getResult(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty()) return recipes.get(0).result;

		return ItemStack.EMPTY;
	}

	public int getEnergyUsage(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty()) return recipes.get(0).energyUsage;
		return 0;
	}

	public int getCookTime(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty()) return recipes.get(0).totalCookTime;
		return 0;
	}

	public float getExperience(ItemStack input1, ItemStack input2)
	{
		List<Recipe> recipes = findRecipes(input1, input2);
		if (!recipes.isEmpty()) return recipes.get(0).experience;
		return 0.0f;
	}
}