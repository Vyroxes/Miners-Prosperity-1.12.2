package net.vyroxes.minersprosperity.objects.blocks.machines.recipes;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class RecipesCrusher
{
	private static final RecipesCrusher INSTANCE = new RecipesCrusher();

	private final Table<ItemStack, ItemStack, RecipeData> recipesList = HashBasedTable.create();
	private final Map<ItemStack, Float> experienceList = Maps.newHashMap();

	public static RecipesCrusher getInstance()
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
		
		public RecipeData(ItemStack result, float experience, int cookTime)
		{
			this.result = result;
			this.cookTime = cookTime;
		}
		
		public ItemStack getResult()
		{
			return result;
		}
		
		public int getCookTime()
		{
			return cookTime;
		}
	}
	
	private RecipesCrusher() 
	{
		addCrusherRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND), 0.7F, 20);
		addCrusherRecipe(new ItemStack(ItemInit.DIAMOND_DUST), new ItemStack(ItemInit.EMERALD_DUST), new ItemStack(Items.REDSTONE), 0.1F, 20);
		addCrusherRecipe(new ItemStack(ItemInit.CHAIN), new ItemStack(ItemInit.COAL_GEAR), new ItemStack(Items.APPLE), 1.0F, 20);
	}
	
	public void addCrusherRecipe(ItemStack input1, ItemStack input2, ItemStack result, float experience, int cookTime) 
	{
		if (getCrusherResult(input1, input2) != ItemStack.EMPTY) return;
		this.recipesList.put(input1, input2, new RecipeData(result, experience, cookTime));
		this.experienceList.put(result, experience);
	}
	
	public ItemStack getCrusherResult(ItemStack input1, ItemStack input2) 
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
	
	public float getCrusherExperience(ItemStack stack)
	{
		for (Entry<ItemStack, Float> entry : this.experienceList.entrySet()) 
		{
			if (compareItemStacks(stack, entry.getKey())) 
			{
				return entry.getValue();
			}
		}
		return 0.0F;
	}
}