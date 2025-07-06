package net.vyroxes.minersprosperity.util.handlers;

import java.text.DecimalFormat;
import java.util.List;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.Tags;

public class TooltipHandler
{
	private String addLeadingSpaces(int iconCount, String text)
	{
		int width = 0;

		width += iconCount * 8;
		int gapCount = 2 * iconCount;
		width += gapCount * 2;

		for (char ch : text.toCharArray())
		{
			if (ch >= '0' && ch <= '9')
			{
				width += 6;
			}
			else if (ch == '.')
			{
				width += 2;
			}
			else if (ch == ' ')
			{
				width += 4;
			}
		}

		int spaceCount = (int) Math.ceil(width / 4.0);

		StringBuilder spaces = new StringBuilder();
		for (int i = 0; i < spaceCount; i++)
		{
			spaces.append(" ");
		}

        return spaces + " ";
    }

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		List<String> tooltip = event.getToolTip();
		ITooltipFlag flag = event.getFlags();
		Item item = stack.getItem();

		if (!ConfigHandler.customTooltips)
			return;

		if (stack.isEmpty())
			return;

		DecimalFormat df = new DecimalFormat("0.##");
		int maxDurability = 0;
		int currentDurability = 0;

		if (stack.isItemStackDamageable())
		{
			maxDurability = stack.getMaxDamage();
			currentDurability = stack.getMaxDamage() - stack.getItemDamage();
		}

		if (item instanceof ItemFood foodItem)
		{
            int food = foodItem.getHealAmount(stack);

			int icons = 1;

			String text = String.valueOf(food);
			String line = addLeadingSpaces(icons, text);
			tooltip.add(1, line);
		}
		else if (item instanceof ItemArmor armor)
		{
			int armorPoints = armor.damageReduceAmount;
			float armorToughness = armor.toughness;

			int icons = armorToughness > 0 ? 2 : 1;
			String text;
			if (armorToughness > 0)
			{
				text = armorPoints + df.format(armorToughness);
				String line = addLeadingSpaces(icons, text);
				tooltip.add(1, line);
			}
			else
			{
				text = String.valueOf(armorPoints);
				String line = addLeadingSpaces(icons, text);
				tooltip.add(1, line);
			}

			icons = 1;

			if (maxDurability != currentDurability)
			{
				text = currentDurability + "/" + maxDurability;
				String line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}
			else
			{
				text = String.valueOf(maxDurability);
				String line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}

			if (flag.isAdvanced())
			{
				int linesToRemove = Math.min(maxDurability == currentDurability ? armorToughness > 0 ? 4 : 3 : armorToughness > 0 ? 5 : 4, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - (stack.hasTagCompound() ? 4 : 3));
				}
			}
			else
			{
				int linesToRemove = Math.min(armorToughness > 0 ? 4 : 3, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - 2);
				}
			}
		}
		else if (item instanceof ItemSword || item instanceof ItemHoe)
		{
			Multimap<String, AttributeModifier> modifiers = item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);

			double attackDamage = 1;
			double attackSpeed = 4;

			for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			{
				attackDamage += mod.getAmount();
			}

			int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
			if (sharpnessLevel > 0)
			{
				attackDamage += 1.0 + 0.5 * sharpnessLevel;
			}

			for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName()))
			{
				attackSpeed += mod.getAmount();
			}

			int icons = 2;

			String text = df.format(attackDamage) + df.format(attackSpeed);
			String line = addLeadingSpaces(icons, text);
			tooltip.add(1, line);

			icons = 1;

			if (maxDurability != currentDurability)
			{
				text = currentDurability + "/" + maxDurability;
				line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}
			else
			{
				text = String.valueOf(maxDurability);
				line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}

			if (flag.isAdvanced())
			{
				int linesToRemove = Math.min(maxDurability == currentDurability ? 4 : 5, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - (stack.hasTagCompound() ? 4 : 3));
				}
			}
			else
			{
				int linesToRemove = Math.min(4, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - 2);
				}
			}
		}
		else if (item instanceof ItemTool)
		{
			Multimap<String, AttributeModifier> modifiers = item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);

			double attackDamage = 1;
			double attackSpeed = 4;

			for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			{
				attackDamage += mod.getAmount();
			}

			int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
			if (sharpnessLevel > 0)
			{
				attackDamage += 1.0 + 0.5 * sharpnessLevel;
			}

			for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName()))
			{
				attackSpeed += mod.getAmount();
			}

			int harvestLevel = -1;

			String[] toolClasses = {"pickaxe", "shovel", "axe"};
			for (String toolClass : toolClasses)
			{
				int level = item.getHarvestLevel(stack, toolClass, null, null);
				if (level > harvestLevel)
				{
					harvestLevel = level;
					break;
				}
			}

			IBlockState testBlock = Blocks.STONE.getDefaultState();
			if (item instanceof ItemAxe)
				testBlock = Blocks.PLANKS.getDefaultState();
			else if (item instanceof ItemSpade)
				testBlock = Blocks.DIRT.getDefaultState();

			float miningSpeed = item.getDestroySpeed(stack, testBlock);
			int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);

			if (miningSpeed > 1.0F && efficiencyLevel > 0)
			{
				miningSpeed += (efficiencyLevel * efficiencyLevel) + 1;
			}

			int icons = 4;

			String text = df.format(attackDamage) + df.format(attackSpeed) + harvestLevel + df.format(miningSpeed);
			String line = addLeadingSpaces(icons, text);
			tooltip.add(1, line);

			icons = 1;

			if (maxDurability != currentDurability)
			{
				text =currentDurability + "/" + maxDurability;
				line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}
			else
			{
				text = String.valueOf(maxDurability);
				line = addLeadingSpaces(icons, text);
				tooltip.add(2, line);
			}

			if (flag.isAdvanced())
			{
				int linesToRemove = Math.min(maxDurability == currentDurability ? 4 : 5, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - (stack.hasTagCompound() ? 4 : 3));
				}
			}
			else if (!flag.isAdvanced())
			{
				int linesToRemove = Math.min(4, tooltip.size());
				for (int i = 0; i < linesToRemove; i++)
				{
					tooltip.remove(tooltip.size() - 2);
				}
			}
		}
		else if (stack.isItemStackDamageable())
		{
			int icons = 1;

			if (maxDurability != currentDurability)
			{
				String text = currentDurability + "/" + maxDurability;
				String line = addLeadingSpaces(icons, text);
				tooltip.add(1, line);
			}
			else
			{
				String text = String.valueOf(maxDurability);
				String line = addLeadingSpaces(icons, text);
				tooltip.add(1, line);
			}

			if (flag.isAdvanced() && maxDurability != currentDurability)
			{
				if (stack.hasTagCompound())
				{
					int linesToRemove = Math.min(1, tooltip.size());
					for (int i = 0; i < linesToRemove; i++)
					{
						tooltip.remove(tooltip.size() - 4);
					}
				}
				else
				{
					int linesToRemove = Math.min(1, tooltip.size());
					for (int i = 0; i < linesToRemove; i++)
					{
						tooltip.remove(tooltip.size() - 3);
					}
				}
			}
		}

		if (ConfigHandler.oreDictInTooltips == 1 && !stack.isEmpty())
        {
			int[] oreIDs = OreDictionary.getOreIDs(stack);

			if (oreIDs.length > 0)
			{
				if (item instanceof ItemFood)
				{
					tooltip.add(2, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));
					for (int id : oreIDs)
					{
						String oreName = OreDictionary.getOreName(id);
						tooltip.add(3, TextFormatting.DARK_GRAY + " - " + oreName);
					}
				}
				else if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemHoe)
				{
					tooltip.add(3, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));
					for (int id : oreIDs)
					{
						String oreName = OreDictionary.getOreName(id);
						tooltip.add(4, TextFormatting.DARK_GRAY + " - " + oreName);
					}
				}
				else if (stack.isItemStackDamageable())
				{
					tooltip.add(2, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));
					for (int id : oreIDs)
					{
						String oreName = OreDictionary.getOreName(id);
						tooltip.add(3, TextFormatting.DARK_GRAY + " - " + oreName);
					}
				}
				else {
					tooltip.add(1, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));
					for (int id : oreIDs)
					{
						String oreName = OreDictionary.getOreName(id);
						tooltip.add(2, TextFormatting.DARK_GRAY + " - " + oreName);
					}
				}
			}
		}
		else if (ConfigHandler.oreDictInTooltips == 2 && !stack.isEmpty() && flag.isAdvanced())
		{
			int[] oreIDs = OreDictionary.getOreIDs(stack);

			if (oreIDs.length > 0)
			{
				tooltip.add(1, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));
				for (int id : oreIDs)
				{
					String oreName = OreDictionary.getOreName(id);
					tooltip.add(2, TextFormatting.DARK_GRAY + " - " + oreName);
				}
			}
		}
	}

	@SubscribeEvent
	public void onTooltipRender(RenderTooltipEvent.PostText event)
	{
		ItemStack stack = event.getStack();
		Item item = stack.getItem();

		if (!ConfigHandler.customTooltips)
			return;

		if (!stack.isEmpty())
		{
			Minecraft mc = Minecraft.getMinecraft();
			ResourceLocation attackDamageIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_attack_damage.png");
			ResourceLocation attackSpeedIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_attack_speed.png");
			ResourceLocation durabilityIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_durability.png");
			ResourceLocation harvestLevelIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_harvest_level.png");
			ResourceLocation miningSpeedIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_mining_speed.png");
			ResourceLocation foodIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_food.png");
			ResourceLocation armorIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_armor.png");
			ResourceLocation armorToughnessIcon = new ResourceLocation(Tags.MODID, "textures/gui/tooltip_armor_toughness.png");

			DecimalFormat df = new DecimalFormat("0.##");

			int width = 8;
			int height = 8;
			int space = 2;
			int x = event.getX();
			int y = event.getY() + 12;
			int ySecondLine = y + height + space;

			if (item instanceof ItemFood foodItem)
			{
                int food = foodItem.getHealAmount(stack);

				mc.getTextureManager().bindTexture(foodIcon);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, 1f);
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();
				mc.fontRenderer.drawStringWithShadow(String.valueOf(food), x + width + space, y, 0xFFFFFF);
			}

			int maxDurability = 0;
			int currentDurability = 0;
			int color = 0xFFFFFF;

			if (stack.isItemStackDamageable())
			{
				maxDurability = stack.getMaxDamage();
				currentDurability = stack.getMaxDamage() - stack.getItemDamage();
				float percent = (float) currentDurability / (float) maxDurability;

				if (percent >= 0.33f)
				{
					color = 0xFFFF00;
				}
				else
				{
					color = 0xFF5555;
				}
			}

			if (item instanceof ItemArmor armor)
			{
				int armorPoints = armor.damageReduceAmount;
				float armorToughness = armor.toughness;

				mc.getTextureManager().bindTexture(armorIcon);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, 1f);
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();
				mc.fontRenderer.drawStringWithShadow(String.valueOf(armorPoints), x + width + space, y, 0xFFFFFF);

				if (armorToughness > 0)
				{
					mc.getTextureManager().bindTexture(armorToughnessIcon);
					GlStateManager.enableBlend();
					Gui.drawModalRectWithCustomSizedTexture(x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(armorPoints)) + space, y, 0, 0, width, height, width, height);
					GlStateManager.disableBlend();
					mc.fontRenderer.drawStringWithShadow(String.valueOf(df.format(armorToughness)), x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(armorPoints)) + space + width + space, y, 0xFFFFFF);
				}

				mc.getTextureManager().bindTexture(durabilityIcon);
				GlStateManager.enableBlend();
				Gui.drawModalRectWithCustomSizedTexture(x, ySecondLine, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();

				if (currentDurability == maxDurability)
				{
					mc.fontRenderer.drawStringWithShadow(String.valueOf(maxDurability), x + width + space, ySecondLine, 0xFFFFFF);
				}
				else
				{
					mc.fontRenderer.drawStringWithShadow(String.valueOf(currentDurability), x + width + space, ySecondLine, color);
					mc.fontRenderer.drawStringWithShadow("/", x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(currentDurability)), ySecondLine, 0xAAAAAA);
					mc.fontRenderer.drawStringWithShadow(String.valueOf(maxDurability), x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(currentDurability)) + mc.fontRenderer.getStringWidth("/"), ySecondLine, 0xFFFFFF);
				}
			}
			else if (item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemHoe)
			{
				Multimap<String, AttributeModifier> modifiers = item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);

				double attackDamage = 1;
				double attackSpeed = 4;

				for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
				{
					attackDamage += mod.getAmount();
				}

				int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
				if (sharpnessLevel > 0)
				{
					attackDamage += 1.0 + 0.5 * sharpnessLevel;
				}

				for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName()))
				{
					attackSpeed += mod.getAmount();
				}

				mc.getTextureManager().bindTexture(attackDamageIcon);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, 1f);
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();
				mc.fontRenderer.drawStringWithShadow(String.valueOf(df.format(attackDamage)), x + width + space, y , 0xFFFFFF);

				mc.getTextureManager().bindTexture(attackSpeedIcon);
				GlStateManager.enableBlend();
				Gui.drawModalRectWithCustomSizedTexture(x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space, y, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();
				mc.fontRenderer.drawStringWithShadow(String.valueOf(df.format(attackSpeed)), x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space + width + space, y, 0xFFFFFF);

				int harvestLevel = -1;

				if (item instanceof ItemTool)
				{
					String[] toolClasses = {"pickaxe", "shovel", "axe"};

					for (String toolClass : toolClasses)
					{
						int level = item.getHarvestLevel(stack, toolClass, null, null);
						if (level > harvestLevel)
						{
							harvestLevel = level;
							break;
						}
					}

					if (harvestLevel >= 0)
					{
						GlStateManager.enableBlend();
						mc.getTextureManager().bindTexture(harvestLevelIcon);
						Gui.drawModalRectWithCustomSizedTexture(x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackSpeed))) + space, y, 0, 0, width, height, width, height);
						GlStateManager.disableBlend();
						mc.fontRenderer.drawStringWithShadow(String.valueOf(harvestLevel), x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackSpeed))) + space + width + space, y, 0xFFFFFF);
					}

					IBlockState testBlock = Blocks.STONE.getDefaultState();
					if (item instanceof ItemAxe)
						testBlock = Blocks.PLANKS.getDefaultState();
					else if (item instanceof ItemSpade)
						testBlock = Blocks.DIRT.getDefaultState();

					float miningSpeed = item.getDestroySpeed(stack, testBlock);
					int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);

					if (miningSpeed > 1.0F && efficiencyLevel > 0)
					{
						miningSpeed += (efficiencyLevel * efficiencyLevel) + 1;
					}

					if (miningSpeed > 1.0F)
					{
						GlStateManager.enableBlend();
						mc.getTextureManager().bindTexture(miningSpeedIcon);
						Gui.drawModalRectWithCustomSizedTexture(x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackSpeed))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(harvestLevel)) + space, y, 0, 0, width, height, width, height);
						GlStateManager.disableBlend();
						mc.fontRenderer.drawStringWithShadow(String.valueOf(df.format(miningSpeed)), x + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackDamage))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(df.format(attackSpeed))) + space + width + space + mc.fontRenderer.getStringWidth(String.valueOf(harvestLevel)) + space + width + space, y, 0xFFFFFF);
					}
				}

				mc.getTextureManager().bindTexture(durabilityIcon);
				GlStateManager.enableBlend();
				Gui.drawModalRectWithCustomSizedTexture(x, ySecondLine, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();

				if (currentDurability == maxDurability)
				{
					mc.fontRenderer.drawStringWithShadow(String.valueOf(maxDurability), x + width + space, ySecondLine, 0xFFFFFF);
				}
				else
				{
					mc.fontRenderer.drawStringWithShadow(currentDurability + "/" + maxDurability, x + width + space, ySecondLine, 0xFFFFFF);
				}
			}
			else if (stack.isItemStackDamageable())
			{
				mc.getTextureManager().bindTexture(durabilityIcon);
				GlStateManager.enableBlend();
				GlStateManager.color(1f, 1f, 1f, 1f);
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
				GlStateManager.disableBlend();

				if (currentDurability == maxDurability)
				{
					mc.fontRenderer.drawStringWithShadow(String.valueOf(maxDurability), x + width + space, y, 0xFFFFFF);
				}
				else
				{
					mc.fontRenderer.drawStringWithShadow(currentDurability + "/" + maxDurability, x + width + space, y, 0xFFFFFF);
				}
			}
		}
	}

}