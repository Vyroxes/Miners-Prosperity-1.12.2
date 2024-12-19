package net.vyroxes.minersprosperity.util.compat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.init.ItemInit;

public class OreDictionaryCompat
{
	public static void registerOreDictionary()
    {
        OreDictionary.registerOre("gemCoal", Items.COAL);
        OreDictionary.registerOre("gemCharcoal", new ItemStack(Items.COAL, 1, 1));

        OreDictionary.registerOre("ingotCopper", ItemInit.COPPER_INGOT);
        OreDictionary.registerOre("ingotTin", ItemInit.TIN_INGOT);
        OreDictionary.registerOre("ingotLead", ItemInit.LEAD_INGOT);
        OreDictionary.registerOre("ingotSilver", ItemInit.SILVER_INGOT);

        OreDictionary.registerOre("nuggetCopper", ItemInit.COPPER_NUGGET);
        OreDictionary.registerOre("nuggetTin", ItemInit.TIN_NUGGET);
        OreDictionary.registerOre("nuggetLead", ItemInit.LEAD_NUGGET);
        OreDictionary.registerOre("nuggetSilver", ItemInit.SILVER_NUGGET);
        OreDictionary.registerOre("nuggetDiamond", ItemInit.DIAMOND_NUGGET);
        OreDictionary.registerOre("nuggetEmerald", ItemInit.EMERALD_NUGGET);
        
        OreDictionary.registerOre("dustCoal", ItemInit.COAL_DUST);
        OreDictionary.registerOre("dustCharcoal", ItemInit.CHARCOAL_DUST);
        OreDictionary.registerOre("dustCopper", ItemInit.COPPER_DUST);
        OreDictionary.registerOre("dustTin", ItemInit.TIN_DUST);
        OreDictionary.registerOre("dustLead", ItemInit.LEAD_DUST);
        OreDictionary.registerOre("dustSilver", ItemInit.SILVER_DUST);
        OreDictionary.registerOre("dustIron", ItemInit.IRON_DUST);
        OreDictionary.registerOre("dustGold", ItemInit.GOLD_DUST);
        OreDictionary.registerOre("dustDiamond", ItemInit.DIAMOND_DUST);
        OreDictionary.registerOre("dustEmerald", ItemInit.EMERALD_DUST);
        
        OreDictionary.registerOre("gearCoal", ItemInit.COAL_GEAR);
        OreDictionary.registerOre("gearCharcoal", ItemInit.CHARCOAL_GEAR);
        OreDictionary.registerOre("gearCopper", ItemInit.COPPER_GEAR);
        OreDictionary.registerOre("gearTin", ItemInit.TIN_GEAR);
        OreDictionary.registerOre("gearLead", ItemInit.LEAD_GEAR);
        OreDictionary.registerOre("gearSilver", ItemInit.SILVER_GEAR);
        OreDictionary.registerOre("gearIron", ItemInit.IRON_GEAR);
        OreDictionary.registerOre("gearGold", ItemInit.GOLD_GEAR);
        OreDictionary.registerOre("gearDiamond", ItemInit.DIAMOND_GEAR);
        OreDictionary.registerOre("gearEmerald", ItemInit.EMERALD_GEAR);
        OreDictionary.registerOre("gearRedstone", ItemInit.REDSTONE_GEAR);
        OreDictionary.registerOre("gearLapis", ItemInit.LAPIS_LAZULI_GEAR);
        
        OreDictionary.registerOre("plateCoal", ItemInit.COAL_PLATE);
        OreDictionary.registerOre("plateCharcoal", ItemInit.CHARCOAL_PLATE);
        OreDictionary.registerOre("plateCopper", ItemInit.COPPER_PLATE);
        OreDictionary.registerOre("plateTin", ItemInit.TIN_PLATE);
        OreDictionary.registerOre("plateLead", ItemInit.LEAD_PLATE);
        OreDictionary.registerOre("plateSilver", ItemInit.SILVER_PLATE);
        OreDictionary.registerOre("plateIron", ItemInit.IRON_PLATE);
        OreDictionary.registerOre("plateGold", ItemInit.GOLD_PLATE);
        OreDictionary.registerOre("plateDiamond", ItemInit.DIAMOND_PLATE);
        OreDictionary.registerOre("plateEmerald", ItemInit.EMERALD_PLATE);
        OreDictionary.registerOre("plateRedstone", ItemInit.REDSTONE_PLATE);
        OreDictionary.registerOre("plateLapis", ItemInit.LAPIS_LAZULI_PLATE);

        OreDictionary.registerOre("oreCopper", BlockInit.COPPER_ORE);
        OreDictionary.registerOre("oreTin", BlockInit.TIN_ORE);
        OreDictionary.registerOre("oreLead", BlockInit.LEAD_ORE);
        OreDictionary.registerOre("oreSilver", BlockInit.SILVER_ORE);

        OreDictionary.registerOre("blockCharcoal", BlockInit.CHARCOAL_BLOCK);
        OreDictionary.registerOre("blockCopper", BlockInit.COPPER_BLOCK);
        OreDictionary.registerOre("blockTin", BlockInit.TIN_BLOCK);
        OreDictionary.registerOre("blockLead", BlockInit.LEAD_BLOCK);
        OreDictionary.registerOre("blockSilver", BlockInit.SILVER_BLOCK);
    }
}