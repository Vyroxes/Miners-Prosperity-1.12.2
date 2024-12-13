package net.vyroxes.minersprosperity.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.armour.ArmourBase;
import net.vyroxes.minersprosperity.objects.items.Backpack;
import net.vyroxes.minersprosperity.objects.items.IronBackpack;
import net.vyroxes.minersprosperity.objects.items.ItemBase;
import net.vyroxes.minersprosperity.objects.tools.ToolAxe;
import net.vyroxes.minersprosperity.objects.tools.ToolHoe;
import net.vyroxes.minersprosperity.objects.tools.ToolPickaxe;
import net.vyroxes.minersprosperity.objects.tools.ToolShovel;
import net.vyroxes.minersprosperity.objects.tools.ToolSword;

public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//MATERIALS
	public static final ToolMaterial EMERALD_TOOL = EnumHelper.addToolMaterial("emerald tool", 4, 2300, 10.0F, 4.0F, 8);
	public static final ArmorMaterial EMERALD_ARMOUR = EnumHelper.addArmorMaterial("emerald armor", Reference.MODID + ":emerald", 40, new int[]{4, 7, 9, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F);

	//TOOLS
	public static final Item EMERALD_SWORD = new ToolSword("emerald_sword", EMERALD_TOOL);
	public static final Item EMERALD_PICKAXE = new ToolPickaxe("emerald_pickaxe", EMERALD_TOOL);
	public static final Item EMERALD_AXE = new ToolAxe("emerald_axe", EMERALD_TOOL);
	public static final Item EMERALD_SHOVEL = new ToolShovel("emerald_shovel", EMERALD_TOOL);
	public static final Item EMERALD_HOE = new ToolHoe("emerald_hoe", EMERALD_TOOL);
	
	//ARMOUR
	public static final Item EMERALD_HELMET = new ArmourBase("emerald_helmet", EMERALD_ARMOUR, 1, EntityEquipmentSlot.HEAD);
	public static final Item EMERALD_CHESTPLATE = new ArmourBase("emerald_chestplate", EMERALD_ARMOUR, 1, EntityEquipmentSlot.CHEST);
	public static final Item EMERALD_LEGGINGS = new ArmourBase("emerald_leggings", EMERALD_ARMOUR, 2, EntityEquipmentSlot.LEGS);
	public static final Item EMERALD_BOOTS = new ArmourBase("emerald_boots", EMERALD_ARMOUR, 1, EntityEquipmentSlot.FEET);

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
}