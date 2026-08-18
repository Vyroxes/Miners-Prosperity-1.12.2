package net.vyroxes.minersprosperity.machine.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.init.ModItems;
import javax.annotation.Nullable;
import java.util.*;

public class MetalPressRecipe
{
	private static final MetalPressRecipe INSTANCE = new MetalPressRecipe();
	private final LookupTable lookupTable;
	private static final Map<DieType, ItemStack[]> DIES_MAP = new HashMap<>();

	static
	{
		DIES_MAP.put(DieType.GEAR, new ItemStack[]
				{
						new ItemStack(ModItems.IRON_GEAR_DIE),
						new ItemStack(ModItems.REFINED_OBSIDIAN_GEAR_DIE)
				});

		DIES_MAP.put(DieType.PLATE, new ItemStack[]
				{
						new ItemStack(ModItems.IRON_PLATE_DIE),
						new ItemStack(ModItems.REFINED_OBSIDIAN_PLATE_DIE)
				});

		DIES_MAP.put(DieType.ROD, new ItemStack[]
				{
						new ItemStack(ModItems.IRON_ROD_DIE),
						new ItemStack(ModItems.REFINED_OBSIDIAN_ROD_DIE)
				});
	}

	public enum DieType
	{
		PLATE,
		GEAR,
		ROD
	}

	public MetalPressRecipe()
	{
		this.lookupTable = new LookupTable();
	}

	public static MetalPressRecipe getInstance()
	{
		return INSTANCE;
	}

	public LookupTable getLookupTable()
	{
		return this.lookupTable;
	}

	public void addRecipe(ItemStack input1, String oreDictInput1, int quantity1, DieType dieType, ItemStack result, int totalCookTime, int energyUsage, float experience)
	{
		Recipe recipe = new Recipe(input1, oreDictInput1, quantity1,dieType, result, totalCookTime, energyUsage, experience);
		lookupTable.addRow(recipe);
	}

	public List<Recipe> findRecipes(ItemStack input1)
	{
		return lookupTable.findRecipes(input1);
	}

	public List<Recipe> findRecipes(ItemStack input1, ItemStack die)
	{
		return lookupTable.findRecipes(input1, die);
	}

	public static class Recipe
	{
		public ItemStack input1;
		public String oreDict1;
		public int quantity1;
		public DieType dieType;
		public ItemStack result;
		public int totalCookTime;
		public int energyUsage;
		public float experience;

		Recipe(@Nullable ItemStack input1, @Nullable String oreDict1, int quantity1, DieType dieType, ItemStack result, int totalCookTime, int energyUsage, float experience)
		{
			this.input1 = input1;
			this.oreDict1 = oreDict1;
			this.quantity1 = quantity1;
			this.dieType = dieType;
			this.result = result;
			this.totalCookTime = totalCookTime;
			this.energyUsage = energyUsage;
			this.experience = experience;
		}

		public int getRequiredQuantity(ItemStack stack)
		{
			if (stack == null || stack.isEmpty()) return 0;

			if (detectDieType(stack) != null) return 1;

			if (this.input1 != null && OreDictionary.itemMatches(this.input1, stack, false)) return this.quantity1;

			if (this.oreDict1 != null && !this.oreDict1.isEmpty() && isOreDictMatch(stack, this.oreDict1)) return this.quantity1;

			return 0;
		}

		public ItemStack[] getDies(DieType dieType)
		{
			return DIES_MAP.getOrDefault(dieType, new ItemStack[0]);
		}
	}

	public static class LookupTable
	{
		private final List<Recipe> recipes;
		private final List<Recipe> unmodifiableRecipes;

		public LookupTable()
		{
			this.recipes = new ArrayList<>();
			this.unmodifiableRecipes = Collections.unmodifiableList(recipes);
		}

		public void addRow(Recipe recipe)
		{
			recipes.add(recipe);
		}

		public List<Recipe> getRecipes()
		{
			return unmodifiableRecipes;
		}

		public boolean isItemValid(IItemHandler itemHandler, int slot, ItemStack slotStack)
		{
			if (itemHandler == null || slotStack == null || slotStack.isEmpty()) return false;

			if (slot == 0) return !findRecipes(slotStack).isEmpty(); else if (slot == 1) return MetalPressRecipe.detectDieType(slotStack) != null;

			return false;
		}

		public List<Recipe> findRecipes(ItemStack input)
		{
			if (input == null || input.isEmpty()) return Collections.emptyList();

			List<Recipe> result = new ArrayList<>();
			for (Recipe recipe : this.recipes)
			{
				boolean matchesItem = recipe.input1 != null && OreDictionary.itemMatches(recipe.input1, input, false);
				boolean matchesOre = recipe.oreDict1 != null && isOreDictMatch(input, recipe.oreDict1);

				if ((matchesItem || matchesOre) && input.getCount() >= recipe.quantity1) result.add(recipe);
			}

			return result;
		}

		public List<Recipe> findRecipes(ItemStack input, ItemStack die)
		{
			if (input == null || input.isEmpty() || die == null || die.isEmpty()) return Collections.emptyList();

			DieType detectedDieType = detectDieType(die);
			if (detectedDieType == null) return Collections.emptyList();

			List<Recipe> result = new ArrayList<>();
			for (Recipe recipe : this.recipes)
			{
				if (recipe.dieType != detectedDieType) continue;

				boolean matchesItem = recipe.input1 != null && OreDictionary.itemMatches(recipe.input1, input, false);
				boolean matchesOre = recipe.oreDict1 != null && isOreDictMatch(input, recipe.oreDict1);

				if ((matchesItem || matchesOre) && input.getCount() >= recipe.quantity1) result.add(recipe);
			}

			return result;
		}
	}

	public static boolean isOreDictMatch(ItemStack stack, String oreDict)
	{
		if (stack == null || stack.isEmpty() || oreDict == null || oreDict.isEmpty()) return false;

		int[] oreIDs = OreDictionary.getOreIDs(stack);
		for (int oreID : oreIDs) if (OreDictionary.getOreName(oreID).equals(oreDict)) return true;

		return false;
	}

	public static DieType detectDieType(ItemStack stack)
	{
		if (stack.getItem() == ModItems.IRON_GEAR_DIE || stack.getItem() == ModItems.REFINED_OBSIDIAN_GEAR_DIE) return DieType.GEAR;
		if (stack.getItem() == ModItems.IRON_PLATE_DIE || stack.getItem() == ModItems.REFINED_OBSIDIAN_PLATE_DIE) return DieType.PLATE;
		if (stack.getItem() == ModItems.IRON_ROD_DIE || stack.getItem() == ModItems.REFINED_OBSIDIAN_ROD_DIE) return DieType.ROD;
		return null;
	}

	public ItemStack getResult(ItemStack input1, ItemStack die)
	{
		List<Recipe> recipes = findRecipes(input1, die);
		if (!recipes.isEmpty()) return recipes.get(0).result;

		return ItemStack.EMPTY;
	}

	public int getEnergyUsage(ItemStack input1, ItemStack die)
	{
		List<Recipe> recipes = findRecipes(input1, die);
		if (!recipes.isEmpty()) return recipes.get(0).energyUsage;
		return 0;
	}

	public int getCookTime(ItemStack input1, ItemStack die)
	{
		List<Recipe> recipes = findRecipes(input1, die);
		if (!recipes.isEmpty()) return recipes.get(0).totalCookTime;
		return 0;
	}

	public float getExperience(ItemStack input1, ItemStack die)
	{
		List<Recipe> recipes = findRecipes(input1, die);
		if (!recipes.isEmpty()) return recipes.get(0).experience;
		return 0.0f;
	}
}