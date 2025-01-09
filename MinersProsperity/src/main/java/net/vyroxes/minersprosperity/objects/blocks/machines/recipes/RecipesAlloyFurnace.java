package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.init.ItemInit;
import javax.annotation.Nullable;
import java.util.*;

public class RecipesAlloyFurnace
{
	private static final RecipesAlloyFurnace INSTANCE = new RecipesAlloyFurnace();
	private final LookupTable lookupTable;

	public RecipesAlloyFurnace()
	{
		this.lookupTable = new LookupTable();
		addRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), null, null, 2, 1, new ItemStack(Items.DIAMOND, 1), 200, 20, 0.7f);
		addRecipe(new ItemStack(ItemInit.DIAMOND_DUST), new ItemStack(ItemInit.EMERALD_DUST), null, null, 1, 1, new ItemStack(Items.REDSTONE, 1), 200, 20, 0.1F);
		addRecipe(null, null, "ingotCopper", "ingotTin", 2, 1, new ItemStack(Items.COAL, 1), 200, 20, 0.7f);
	}

	public static RecipesAlloyFurnace getInstance()
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
			if (stack == null || stack.isEmpty())
			{
				return 0;
			}

			if (this.input1 != null && ItemStack.areItemsEqual(stack, this.input1))
			{
				return this.quantity1;
			}

			if (this.input2 != null && ItemStack.areItemsEqual(stack, this.input2))
			{
				return this.quantity2;
			}

			if (this.oreDict1 != null && !this.oreDict1.isEmpty() && isOreDictMatch(stack, this.oreDict1))
			{
				return this.quantity1;
			}

			if (this.oreDict2 != null && !this.oreDict2.isEmpty() && isOreDictMatch(stack, this.oreDict2))
			{
				return this.quantity2;
			}

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
			if (itemHandler == null || slotStack == null || slotStack.isEmpty())
			{
				return false;
			}

			for (int i = start; i < start + len; i++)
			{
				if (i == slot) continue;

				ItemStack stackInSlot = itemHandler.getStackInSlot(i);
				if (!stackInSlot.isEmpty() && ItemStack.areItemsEqual(stackInSlot, slotStack))
				{
					return false;
				}

				if (!stackInSlot.isEmpty())
				{
					int[] oreIDs1 = OreDictionary.getOreIDs(stackInSlot);
					int[] oreIDs2 = OreDictionary.getOreIDs(slotStack);

					for (int oreID1 : oreIDs1)
					{
						for (int oreID2 : oreIDs2)
						{
							if (oreID1 == oreID2)
							{
								return false;
							}
						}
					}
				}
			}

			IntOpenHashSet matchingIndices = null;

			for (int i = start; i < start + len; i++)
			{
				final ItemStack input = i == slot ? slotStack : itemHandler.getStackInSlot(i);

				if (input.isEmpty()) continue;

				IntOpenHashSet indices = recipeKeyToIndices.get(input);

				if (indices == null)
				{
					int[] oreIDs = OreDictionary.getOreIDs(input);
					indices = new IntOpenHashSet();

					for (int oreID : oreIDs)
					{
						String oreName = OreDictionary.getOreName(oreID);
						IntOpenHashSet oreIndices = oreDictToIndices.get(oreName);
						if (oreIndices != null)
						{
							indices.addAll(oreIndices);
						}
					}
				}

				if (matchingIndices == null)
				{
					matchingIndices = new IntOpenHashSet(indices);
				}
				else
				{
					matchingIndices.retainAll(indices);
				}

				if (matchingIndices.isEmpty())
				{
					return false;
				}
			}

			return matchingIndices != null && !matchingIndices.isEmpty();
		}

		public List<Recipe> findRecipes(ItemStack... inputs)
		{
			if (inputs == null || inputs.length == 0)
			{
				return Collections.emptyList();
			}

			for (int i = 0; i < inputs.length; i++)
			{
				if (inputs[i] == null || inputs[i].isEmpty()) continue;

				for (int j = i + 1; j < inputs.length; j++)
				{
					if (inputs[j] == null || inputs[j].isEmpty()) continue;

					if (ItemStack.areItemsEqual(inputs[i], inputs[j]))
					{
						return Collections.emptyList();
					}

					int[] oreIDs1 = OreDictionary.getOreIDs(inputs[i]);
					int[] oreIDs2 = OreDictionary.getOreIDs(inputs[j]);

					for (int oreID1 : oreIDs1)
					{
						for (int oreID2 : oreIDs2)
						{
							if (oreID1 == oreID2)
							{
								return Collections.emptyList();
							}
						}
					}
				}
			}

			IntOpenHashSet matchingIndices = null;

			for (ItemStack input : inputs)
			{
				if (input == null || input.isEmpty()) continue;

				IntOpenHashSet indices = recipeKeyToIndices.get(input);

				if (indices == null)
				{
					int[] oreIDs = OreDictionary.getOreIDs(input);
					indices = new IntOpenHashSet();

					for (int oreID : oreIDs)
					{
						String oreName = OreDictionary.getOreName(oreID);
						IntOpenHashSet oreIndices = oreDictToIndices.get(oreName);
						if (oreIndices != null)
						{
							indices.addAll(oreIndices);
						}
					}
				}

				if (indices.isEmpty())
				{
					return Collections.emptyList();
				}

				if (matchingIndices == null)
				{
					matchingIndices = new IntOpenHashSet(indices);
				}
				else
				{
					matchingIndices.retainAll(indices);
				}

				if (matchingIndices.isEmpty())
				{
					return Collections.emptyList();
				}
			}

			if (matchingIndices == null || matchingIndices.isEmpty())
			{
				return Collections.emptyList();
			}

			List<Recipe> matchingRecipes = new ArrayList<>();
			for (int index : matchingIndices)
			{
				Recipe recipe = recipes.get(index);

				for (ItemStack input : inputs)
				{
					if (input == null || input.isEmpty()) continue;

					int requiredQuantity = recipe.getRequiredQuantity(input);
					if (requiredQuantity > 0 && input.getCount() < requiredQuantity)
					{
						return Collections.emptyList();
					}
				}

				matchingRecipes.add(recipes.get(index));
			}

			return matchingRecipes;
		}
	}

	public static boolean isOreDictMatch(ItemStack stack, String oreDict)
	{
		if (stack == null || stack.isEmpty() || oreDict == null || oreDict.isEmpty())
		{
			return false;
		}

		int[] oreIDs = OreDictionary.getOreIDs(stack);
		for (int oreID : oreIDs)
		{
			if (OreDictionary.getOreName(oreID).equals(oreDict))
			{
				return true;
			}
		}

		return false;
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