package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class RecipesAlloyFurnace
{
	private static final RecipesAlloyFurnace INSTANCE = new RecipesAlloyFurnace();

	private final Table<ItemStack, ItemStack, RecipeData> recipesList = HashBasedTable.create();

	public static RecipesAlloyFurnace getInstance()
	{
		return INSTANCE;
	}

	public Table<ItemStack, ItemStack, RecipeData> getRecipesList()
	{
		return recipesList;
	}

	public static class RecipeData
	{
		private final ItemStack result;
		private final int cookTime;
		private final int energyUsage;
		private final float experience;
		
		public RecipeData(ItemStack result, int cookTime, int energyUsage, float experience)
		{
			this.result = result;
			this.cookTime = cookTime;
			this.energyUsage = energyUsage;
			this.experience = experience;
		}

		public ItemStack getResult()
		{
			return this.result;
		}
		
		public int getCookTime()
		{
			return this.cookTime;
		}

		public int getEnergyUsage()
		{
			return this.energyUsage;
		}

		public float getExperience()
		{
			return this.experience;
		}
	}
	
	private RecipesAlloyFurnace()
	{
		addRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND), 100, 20, 0.7F);
		addRecipe(new ItemStack(ItemInit.DIAMOND_DUST), new ItemStack(ItemInit.EMERALD_DUST), new ItemStack(Items.REDSTONE), 100, 20, 0.1F);
	}
	
	public void addRecipe(ItemStack input1, ItemStack input2, ItemStack result, int cookTime, int energyUsage, float experience)
	{
		if (getResult(input1, input2) != ItemStack.EMPTY) return;
		this.recipesList.put(input1, input2, new RecipeData(result, cookTime, energyUsage, experience));
	}
	
	public ItemStack getResult(ItemStack input1, ItemStack input2)
	{
	    for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
	    {
	        if (compareItemStacks(input1, entry.getKey())) 
	        {
	            for (Entry<ItemStack, RecipeData> ent : entry.getValue().entrySet()) 
	            {
	                if (compareItemStacks(input2, ent.getKey())) 
	                {
	                    return ent.getValue().getResult();
	                }
	            }
	        }
	    }

	    for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
	    {
	        if (compareItemStacks(input2, entry.getKey())) 
	        {
	            for (Entry<ItemStack, RecipeData> ent : entry.getValue().entrySet()) 
	            {
	                if (compareItemStacks(input1, ent.getKey())) 
	                {
	                    return ent.getValue().getResult();
	                }
	            }
	        }
	    }

	    return ItemStack.EMPTY;
	}
	
	public boolean isInputInAnyRecipe(ItemStack input) 
	{
	    for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
	    {
	        if (compareItemStacks(input, entry.getKey())) 
	        {
	            return true;
	        }

	        for (Entry<ItemStack, RecipeData> ent : entry.getValue().entrySet()) 
	        {
	            if (compareItemStacks(input, ent.getKey())) 
	            {
	                return true;
	            }
	        }
	    }

	    return false;
	}
	
	private RecipeData getRecipeData(ItemStack input1, ItemStack input2)
	{
	    for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
	    {
	        if (compareItemStacks(input1, entry.getKey()))
	        {
	            for (Entry<ItemStack, RecipeData> ent : entry.getValue().entrySet())
	            {
	                if (compareItemStacks(input2, ent.getKey()))
	                {
	                    return ent.getValue();
	                }
	            }
	        }
	    }
	    return null;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

	public int getCookTime(ItemStack input1, ItemStack input2)
	{
		RecipeData recipeData = getRecipeData(input1, input2);
		if (recipeData != null)
		{
			return recipeData.getCookTime();
		}

		recipeData = getRecipeData(input2, input1);
		if (recipeData != null)
		{
			return recipeData.getCookTime();
		}

		return 200;
	}

	public int getCooktime(ItemStack result)
	{
		for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
		{
			for (RecipeData recipeData : entry.getValue().values())
			{
				if (compareItemStacks(recipeData.getResult(), result))
				{
					return recipeData.getCookTime();
				}
			}
		}
		return 200;
	}

	public int getEnergyUsage(ItemStack input1, ItemStack input2)
	{
		RecipeData recipeData = getRecipeData(input1, input2);
		if (recipeData != null)
		{
			return recipeData.getEnergyUsage();
		}

		recipeData = getRecipeData(input2, input1);
		if (recipeData != null)
		{
			return recipeData.getEnergyUsage();
		}

		return 1000;
	}

	public int getEnergyUsage(ItemStack result)
	{
		for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
		{
			for (RecipeData recipeData : entry.getValue().values())
			{
				if (compareItemStacks(recipeData.getResult(), result))
				{
					return recipeData.getEnergyUsage();
				}
			}
		}
		return 1000;
	}

	public float getExperience(ItemStack input1, ItemStack input2)
	{
		RecipeData recipeData = getRecipeData(input1, input2);
		if (recipeData != null)
		{
			return recipeData.getExperience();
		}

		recipeData = getRecipeData(input2, input1);
		if (recipeData != null)
		{
			return recipeData.getExperience();
		}
		return 1.0F;
	}

	public float getExperience(ItemStack result)
	{
		for (Entry<ItemStack, Map<ItemStack, RecipeData>> entry : this.recipesList.columnMap().entrySet())
		{
			for (RecipeData recipeData : entry.getValue().values())
			{
				if (compareItemStacks(recipeData.getResult(), result))
				{
					return recipeData.getExperience();
				}
			}
		}
		return 1.0F;
	}
}