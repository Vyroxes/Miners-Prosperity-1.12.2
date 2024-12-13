package net.vyroxes.minersprosperity.init;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.armour.ArmourEmerald;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;
import net.vyroxes.minersprosperity.objects.items.ItemBase;
import net.vyroxes.minersprosperity.objects.tools.AxeEmerald;
import net.vyroxes.minersprosperity.objects.tools.HoeEmerald;
import net.vyroxes.minersprosperity.objects.tools.PickaxeEmerald;
import net.vyroxes.minersprosperity.objects.tools.ShovelEmerald;
import net.vyroxes.minersprosperity.objects.tools.SwordEmerald;

public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//MATERIALS
	public static final ToolMaterial EMERALD_TOOL = EnumHelper.addToolMaterial("emerald tool", 4, 2300, 10.0F, 4.0F, 8);
	public static final ArmorMaterial EMERALD_ARMOUR = EnumHelper.addArmorMaterial("emerald armor", Reference.MODID + ":emerald", 40, new int[]{4, 7, 9, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F);

	//TOOLS
	public static final Item EMERALD_SWORD = new SwordEmerald("emerald_sword", EMERALD_TOOL);
	public static final Item EMERALD_PICKAXE = new PickaxeEmerald("emerald_pickaxe", EMERALD_TOOL);
	public static final Item EMERALD_AXE = new AxeEmerald("emerald_axe", EMERALD_TOOL);
	public static final Item EMERALD_SHOVEL = new ShovelEmerald("emerald_shovel", EMERALD_TOOL);
	public static final Item EMERALD_HOE = new HoeEmerald("emerald_hoe", EMERALD_TOOL);
	
	//ARMOUR
	public static final Item EMERALD_HELMET = new ArmourEmerald("emerald_helmet", EMERALD_ARMOUR, 1, EntityEquipmentSlot.HEAD);
	public static final Item EMERALD_CHESTPLATE = new ArmourEmerald("emerald_chestplate", EMERALD_ARMOUR, 1, EntityEquipmentSlot.CHEST);
	public static final Item EMERALD_LEGGINGS = new ArmourEmerald("emerald_leggings", EMERALD_ARMOUR, 2, EntityEquipmentSlot.LEGS);
	public static final Item EMERALD_BOOTS = new ArmourEmerald("emerald_boots", EMERALD_ARMOUR, 1, EntityEquipmentSlot.FEET);

	//ITEMS
	public static final Item BACKPACK = new Backpack("backpack");
	public static final Item IRON_BACKPACK = new IronBackpack("iron_backpack");
	
	public static final Item DIAMOND_NUGGET = new ItemBase("diamond_nugget");
	public static final Item EMERALD_NUGGET = new ItemBase("emerald_nugget");
	public static final Item COAL_DUST = new ItemBase("coal_dust");
	public static final Item CHARCOAL_DUST = new ItemBase("charcoal_dust");
	public static final Item IRON_DUST = new ItemBase("iron_dust");
	public static final Item GOLD_DUST = new ItemBase("gold_dust");
	public static final Item DIAMOND_DUST = new ItemBase("diamond_dust");
	public static final Item EMERALD_DUST = new ItemBase("emerald_dust");
	
	public static void registerOreDictionary()
    {
        OreDictionary.registerOre("nuggetDiamond", DIAMOND_NUGGET);
        OreDictionary.registerOre("nuggetEmerald", EMERALD_NUGGET);
        OreDictionary.registerOre("dustCoal", COAL_DUST);
        OreDictionary.registerOre("dustCharcoal", CHARCOAL_DUST);
        OreDictionary.registerOre("dustIron", IRON_DUST);
        OreDictionary.registerOre("dustGold", GOLD_DUST);
        OreDictionary.registerOre("dustDiamond", DIAMOND_DUST);
        OreDictionary.registerOre("dustEmerald", EMERALD_DUST);
    }
}