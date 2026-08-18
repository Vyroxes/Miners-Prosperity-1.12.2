package net.vyroxes.minersprosperity.client.event;

import java.text.DecimalFormat;
import java.util.*;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModEnchantments;

public class TooltipHandler
{
    private static final int ICON_SIZE = 7;
    private static final int ICON_GAP = 2;
    private static final int PART_GAP = 2;
    private static final int LINE_HEIGHT = 10;
    private static final int ICON_Y_OFFSET = 0;

    private static final DecimalFormat DF = new DecimalFormat("0.##");
    private static final DecimalFormat DF_FOOD = new DecimalFormat("0.#");

    private static final ResourceLocation ATTACK_DAMAGE_ICON = icon("tooltip_attack_damage");
    private static final ResourceLocation ATTACK_SPEED_ICON = icon("tooltip_attack_speed");
    private static final ResourceLocation DURABILITY_ICON = icon("tooltip_durability");
    private static final ResourceLocation HARVEST_LEVEL_ICON = icon("tooltip_harvest_level");
    private static final ResourceLocation MINING_SPEED_ICON = icon("tooltip_mining_speed");
    private static final ResourceLocation FOOD_ICON = icon("tooltip_food");
    private static final ResourceLocation ARMOR_ICON = icon("tooltip_armor");
    private static final ResourceLocation ARMOR_TOUGHNESS_ICON = icon("tooltip_armor_toughness");
    private static final ResourceLocation FUEL_BURN_TIME_ICON = icon("tooltip_fuel_burn_time");

    private static final EntityEquipmentSlot[] ARMOR_SLOTS = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    private TooltipRenderData renderData;
    private CustomTooltipBlacklist blacklist = new CustomTooltipBlacklist(new String[0]);
    private int blacklistHash = Integer.MIN_VALUE;

    private static ResourceLocation icon(String name)
    {
        return new ResourceLocation(Tags.MODID, "textures/gui/" + name + ".png");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if (!ModConfig.TOOLTIPS.customTooltips || stack.isEmpty() || this.getBlacklist().isBlacklisted(stack))
        {
            this.renderData = null;
            return;
        }

        List<TooltipRow> rows = this.buildRows(stack, event.getFlags());
        this.renderData = new TooltipRenderData(rows);

        if (ModConfig.TOOLTIPS.hideVanillaAttributeTooltips) this.removeVanillaAttributeTooltips(stack, event.getToolTip());

        this.appendEnchantmentState(stack, event.getToolTip(), ModEnchantments.VACUUM, "Vacuum");
        this.appendEnchantmentState(stack, event.getToolTip(), ModEnchantments.GLOW, "Glow");

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int insertIndex = Math.min(1, event.getToolTip().size());

        for (TooltipRow row : rows) event.getToolTip().add(insertIndex++, this.createPlaceholder(row, font));

        this.addOreDictionaryTooltip(stack, event.getToolTip(), event.getFlags(), insertIndex);
    }

    @SubscribeEvent
    public void onTooltipRender(RenderTooltipEvent.PostText event)
    {
        if (!ModConfig.TOOLTIPS.customTooltips || this.renderData == null || this.renderData.rows.isEmpty()) return;

        int x = event.getX();
        int y = event.getY() + 12;

        for (TooltipRow row : this.renderData.rows)
        {
            this.drawRow(row, event.getFontRenderer(), x, y);
            y += LINE_HEIGHT;
        }

        this.renderData = null;
    }

    private List<TooltipRow> buildRows(ItemStack stack, ITooltipFlag flag)
    {
        List<TooltipRow> rows = new ArrayList<>();

        if (ModConfig.TOOLTIPS.foodStats) this.addFoodRow(stack, rows);
        if (ModConfig.TOOLTIPS.armorStats) this.addArmorRow(stack, rows);
        if (ModConfig.TOOLTIPS.combatStats) this.addCombatRow(stack, rows);
        if (ModConfig.TOOLTIPS.miningStats) this.addMiningRow(stack, rows);
        if (ModConfig.TOOLTIPS.durabilityStats) this.addDurabilityRow(stack, rows);
        if (ModConfig.TOOLTIPS.fuelBurnTime) this.addFuelBurnTimeRow(stack, flag, rows);

        return rows;
    }

    private void addFoodRow(ItemStack stack, List<TooltipRow> rows)
    {
        if (!(stack.getItem() instanceof ItemFood food)) return;

        float foodValue = (float) food.getHealAmount(stack) / 2.0F;
        float saturation = food.getSaturationModifier(stack);

        rows.add(new TooltipRow().add(FOOD_ICON, DF_FOOD.format(foodValue) + " (" + DF_FOOD.format(saturation) + ")", 0xFFFFFF));
    }

    private void addArmorRow(ItemStack stack, List<TooltipRow> rows)
    {
        ArmorStats stats = this.getArmorStats(stack);
        if (stats == null) return;

        TooltipRow row = new TooltipRow().add(ARMOR_ICON, DF.format(stats.armor), 0xFFFFFF);
        if (stats.toughness > 0.0D) row.add(ARMOR_TOUGHNESS_ICON, DF.format(stats.toughness), 0xFFFFFF);

        rows.add(row);
    }

    private ArmorStats getArmorStats(ItemStack stack)
    {
        ArmorStats best = null;

        for (EntityEquipmentSlot slot : ARMOR_SLOTS)
        {
            Multimap<String, AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(slot, stack);
            Collection<AttributeModifier> armorModifiers = modifiers.get(SharedMonsterAttributes.ARMOR.getName());
            Collection<AttributeModifier> toughnessModifiers = modifiers.get(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());

            if (armorModifiers.isEmpty() && toughnessModifiers.isEmpty()) continue;

            double armor = this.applyModifiers(0.0D, armorModifiers);
            double toughness = this.applyModifiers(0.0D, toughnessModifiers);

            if (best == null || armor + toughness > best.armor + best.toughness) best = new ArmorStats(armor, toughness);
        }

        return best;
    }

    private void addCombatRow(ItemStack stack, List<TooltipRow> rows)
    {
        Multimap<String, AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack);
        Collection<AttributeModifier> damageModifiers = modifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        Collection<AttributeModifier> speedModifiers = modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName());

        if (damageModifiers.isEmpty() && speedModifiers.isEmpty()) return;

        double attackDamage = this.applyModifiers(1.0D, damageModifiers);
        double attackSpeed = this.applyModifiers(4.0D, speedModifiers);

        int sharpnessLevel = Enchantments.SHARPNESS == null ? 0 : EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        if (sharpnessLevel > 0) attackDamage += 0.5D + 0.5D * sharpnessLevel;

        rows.add(new TooltipRow().add(ATTACK_DAMAGE_ICON, DF.format(attackDamage), 0xFFFFFF).add(ATTACK_SPEED_ICON, DF.format(attackSpeed), 0xFFFFFF));
    }

    private void addMiningRow(ItemStack stack, List<TooltipRow> rows)
    {
        Item item = stack.getItem();
        int harvestLevel = -1;

        for (String toolClass : item.getToolClasses(stack)) harvestLevel = Math.max(harvestLevel, item.getHarvestLevel(stack, toolClass, null, null));

        IBlockState stone = Blocks.STONE.getDefaultState();
        IBlockState wood = Blocks.PLANKS.getDefaultState();
        IBlockState dirt = Blocks.DIRT.getDefaultState();

        float miningSpeed = Math.max(item.getDestroySpeed(stack, stone), Math.max(item.getDestroySpeed(stack, wood), item.getDestroySpeed(stack, dirt)));

        int efficiencyLevel = Enchantments.EFFICIENCY == null ? 0 : EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        if (miningSpeed > 1.0F && efficiencyLevel > 0) miningSpeed += efficiencyLevel * efficiencyLevel + 1;

        if (harvestLevel < 0 && miningSpeed <= 1.0F) return;

        TooltipRow row = new TooltipRow();

        if (harvestLevel >= 0) row.add(HARVEST_LEVEL_ICON, String.valueOf(harvestLevel), 0xFFFFFF);
        if (miningSpeed > 1.0F) row.add(MINING_SPEED_ICON, DF.format(miningSpeed), 0xFFFFFF);

        if (!row.parts.isEmpty()) rows.add(row);
    }

    private void addDurabilityRow(ItemStack stack, List<TooltipRow> rows)
    {
        if (!stack.isItemStackDamageable()) return;

        int maxDurability = stack.getMaxDamage() + 1;
        int currentDurability = maxDurability - stack.getItemDamage();

        TooltipPart part = new TooltipPart(DURABILITY_ICON);

        if (currentDurability == maxDurability) part.add(String.valueOf(maxDurability), 0xFFFFFF);
        else
        {
            float percent = maxDurability <= 0 ? 0.0F : (float) currentDurability / maxDurability;
            int currentColor = percent > 0.66F ? 0xFFFFFF : percent > 0.33F ? 0xFFFF00 : 0xFF5555;

            part.add(String.valueOf(currentDurability), currentColor);
            part.add("/", 0xAAAAAA);
            part.add(String.valueOf(maxDurability), 0xFFFFFF);
        }

        rows.add(new TooltipRow().add(part));
    }

    private void addFuelBurnTimeRow(ItemStack stack, ITooltipFlag flag, List<TooltipRow> rows)
    {
        int burnTime = TileEntityFurnace.getItemBurnTime(stack);
        if (burnTime <= 0) return;

        String text = DF.format(burnTime);
        if (flag.isAdvanced()) text += " (" + DF.format(burnTime / 200.0F) + ")";

        rows.add(new TooltipRow().add(FUEL_BURN_TIME_ICON, text, 0xFFFFFF));
    }

    private double applyModifiers(double baseValue, Collection<AttributeModifier> modifiers)
    {
        double value = baseValue;

        for (AttributeModifier modifier : modifiers) if (modifier.getOperation() == 0) value += modifier.getAmount();
        for (AttributeModifier modifier : modifiers) if (modifier.getOperation() == 1) value += baseValue * modifier.getAmount();
        for (AttributeModifier modifier : modifiers) if (modifier.getOperation() == 2) value *= 1.0D + modifier.getAmount();

        return value;
    }

    private String createPlaceholder(TooltipRow row, FontRenderer font)
    {
        int spaceWidth = Math.max(1, font.getCharWidth(' '));
        int spaceCount = (int) Math.ceil((row.getWidth(font) + 1) / (double) spaceWidth);
        StringBuilder builder = new StringBuilder(spaceCount);

        for (int i = 0; i < spaceCount; i++) builder.append(' ');

        return builder.toString();
    }

    private void drawRow(TooltipRow row, FontRenderer font, int x, int y)
    {
        int currentX = x;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < row.parts.size(); i++)
        {
            TooltipPart part = row.parts.get(i);

            if (part.icon != null)
            {
                Minecraft.getMinecraft().getTextureManager().bindTexture(part.icon);
                Gui.drawModalRectWithCustomSizedTexture(currentX, y + ICON_Y_OFFSET, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
                currentX += ICON_SIZE + ICON_GAP;
            }

            for (TooltipText text : part.texts)
            {
                font.drawStringWithShadow(text.text, currentX, y, text.color);
                currentX += font.getStringWidth(text.text);
            }

            if (i < row.parts.size() - 1) currentX += PART_GAP;
        }

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void appendEnchantmentState(ItemStack stack, List<String> tooltip, Enchantment enchantment, String nbtKey)
    {
        if (enchantment == null) return;

        int level = EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
        if (level <= 0) return;

        String enchantmentName = enchantment.getTranslatedName(level);
        NBTTagCompound tag = stack.getTagCompound();
        boolean enabled = tag != null && tag.getBoolean(nbtKey);

        for (int i = 0; i < tooltip.size(); i++)
        {
            if (!tooltip.get(i).contains(enchantmentName)) continue;

            tooltip.set(i, tooltip.get(i) + TextFormatting.GRAY + ": " + (enabled ? TextFormatting.GREEN + I18n.format("tooltip.on") : TextFormatting.RED + I18n.format("tooltip.off")));
            return;
        }
    }

    private void addOreDictionaryTooltip(ItemStack stack, List<String> tooltip, ITooltipFlag flag, int insertIndex)
    {
        int mode = ModConfig.TOOLTIPS.oreDictInTooltips;
        if (mode == 0 || mode == 2 && !flag.isAdvanced()) return;

        int[] oreIds = OreDictionary.getOreIDs(stack);
        if (oreIds.length == 0) return;

        insertIndex = Math.min(insertIndex, tooltip.size());
        tooltip.add(insertIndex++, TextFormatting.GRAY + I18n.format("tooltip.ore_dict"));

        for (int oreId : oreIds) tooltip.add(insertIndex++, TextFormatting.DARK_GRAY + " - " + OreDictionary.getOreName(oreId));
    }

    private CustomTooltipBlacklist getBlacklist()
    {
        String[] entries = ModConfig.TOOLTIPS.customTooltipsBlacklist == null ? new String[0] : ModConfig.TOOLTIPS.customTooltipsBlacklist;
        int hash = Arrays.hashCode(entries);

        if (hash != this.blacklistHash)
        {
            this.blacklist = new CustomTooltipBlacklist(entries);
            this.blacklistHash = hash;
        }

        return this.blacklist;
    }

    private void removeVanillaAttributeTooltips(ItemStack stack, List<String> tooltip)
    {
        if (ModConfig.TOOLTIPS.armorStats) for (EntityEquipmentSlot slot : ARMOR_SLOTS) this.removeVanillaAttributeSection(stack, tooltip, slot, SharedMonsterAttributes.ARMOR.getName(), SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());

        if (ModConfig.TOOLTIPS.combatStats) this.removeVanillaAttributeSection(stack, tooltip, EntityEquipmentSlot.MAINHAND, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), SharedMonsterAttributes.ATTACK_SPEED.getName());
    }

    private void removeVanillaAttributeSection(ItemStack stack, List<String> tooltip, EntityEquipmentSlot slot, String... targetAttributeKeys)
    {
        Multimap<String, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
        if (modifiers.isEmpty()) return;

        Set<String> targetKeys = new HashSet<>(Arrays.asList(targetAttributeKeys));
        Map<String, String> attributeNames = new HashMap<>();

        int targetModifierCount = 0;
        boolean hasOtherAttributes = false;

        for (Map.Entry<String, AttributeModifier> entry : modifiers.entries())
        {
            String key = entry.getKey();
            attributeNames.put(key, I18n.format("attribute.name." + key));

            if (targetKeys.contains(key)) targetModifierCount++;
            else hasOtherAttributes = true;
        }

        if (targetModifierCount == 0) return;

        String header = I18n.format("item.modifiers." + slot.getName());
        int headerIndex = this.findTooltipLine(tooltip, header);
        if (headerIndex < 0) return;

        List<Integer> removeIndices = new ArrayList<>();
        int attributeLinesFound = 0;

        for (int i = headerIndex + 1; i < tooltip.size(); i++)
        {
            String clean = this.stripFormatting(tooltip.get(i));

            if (this.isVanillaAttributeHeader(clean)) break;

            String matchedKey = null;

            for (Map.Entry<String, String> attribute : attributeNames.entrySet())
            {
                if (clean.contains(attribute.getValue()))
                {
                    matchedKey = attribute.getKey();
                    break;
                }
            }

            if (matchedKey == null) continue;

            attributeLinesFound++;

            if (targetKeys.contains(matchedKey)) removeIndices.add(i);

            if (attributeLinesFound >= modifiers.size()) break;
        }

        int removedTargetLines = removeIndices.size();

        for (int i = removeIndices.size() - 1; i >= 0; i--) tooltip.remove((int) removeIndices.get(i));

        if (!hasOtherAttributes && removedTargetLines == targetModifierCount)
        {
            tooltip.remove(headerIndex);

            int previousIndex = headerIndex - 1;
            if (previousIndex >= 0 && this.stripFormatting(tooltip.get(previousIndex)).trim().isEmpty()) tooltip.remove(previousIndex);
        }
    }

    private int findTooltipLine(List<String> tooltip, String text)
    {
        for (int i = 0; i < tooltip.size(); i++) if (this.stripFormatting(tooltip.get(i)).equals(text)) return i;

        return -1;
    }

    private boolean isVanillaAttributeHeader(String line)
    {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) if (line.equals(I18n.format("item.modifiers." + slot.getName()))) return true;

        return false;
    }

    private String stripFormatting(String text)
    {
        String stripped = TextFormatting.getTextWithoutFormattingCodes(text);
        return stripped == null ? "" : stripped;
    }

    @Desugar
    private record TooltipRenderData(List<TooltipRow> rows) {}

    private static class TooltipRow
    {
        private final List<TooltipPart> parts = new ArrayList<>();

        private TooltipRow add(ResourceLocation icon, String text, int color)
        {
            this.parts.add(new TooltipPart(icon).add(text, color));
            return this;
        }

        private TooltipRow add(TooltipPart part)
        {
            this.parts.add(part);
            return this;
        }

        private int getWidth(FontRenderer font)
        {
            int width = 0;

            for (int i = 0; i < this.parts.size(); i++)
            {
                TooltipPart part = this.parts.get(i);

                if (part.icon != null) width += ICON_SIZE + ICON_GAP;
                for (TooltipText text : part.texts) width += font.getStringWidth(text.text);
                if (i < this.parts.size() - 1) width += PART_GAP;
            }

            return width;
        }
    }

    private static class TooltipPart
    {
        private final ResourceLocation icon;
        private final List<TooltipText> texts = new ArrayList<>();

        private TooltipPart(ResourceLocation icon)
        {
            this.icon = icon;
        }

        private TooltipPart add(String text, int color)
        {
            this.texts.add(new TooltipText(text, color));
            return this;
        }
    }

    @Desugar
    private record TooltipText(String text, int color) {}

    @Desugar
    private record ArmorStats(double armor, double toughness) {}

    public static class CustomTooltipBlacklist
    {
        private final Set<String> itemSet = new HashSet<>();
        private final Set<String> itemWildcard = new HashSet<>();
        private final Set<String> itemWithMeta = new HashSet<>();
        private final Set<String> modWildcard = new HashSet<>();
        private final Set<String> oreDictSet = new HashSet<>();
        private final Set<String> oreDictWildcard = new HashSet<>();

        public CustomTooltipBlacklist(String[] entries)
        {
            if (entries == null) return;

            for (String rawEntry : entries)
            {
                if (rawEntry == null || rawEntry.trim().isEmpty()) continue;

                String entry = rawEntry.trim().toLowerCase(Locale.ROOT);

                if (entry.endsWith(":*"))
                {
                    this.modWildcard.add(entry.substring(0, entry.length() - 2));
                    continue;
                }

                if (entry.contains(":"))
                {
                    int slash = entry.lastIndexOf('/');

                    if (slash > entry.indexOf(':'))
                    {
                        String suffix = entry.substring(slash + 1);
                        String itemId = entry.substring(0, slash);

                        if ("*".equals(suffix))
                        {
                            this.itemWildcard.add(itemId);
                            continue;
                        }

                        try
                        {
                            Integer.parseInt(suffix);
                            this.itemWithMeta.add(itemId + "/" + suffix);
                            continue;
                        }
                        catch (NumberFormatException ignored)
                        {
                        }
                    }

                    this.itemSet.add(entry);
                    continue;
                }

                if (entry.endsWith("*")) this.oreDictWildcard.add(entry.substring(0, entry.length() - 1));
                else this.oreDictSet.add(entry);
            }
        }

        public boolean isBlacklisted(ItemStack stack)
        {
            if (stack.isEmpty()) return false;

            ResourceLocation id = Item.REGISTRY.getNameForObject(stack.getItem());
            if (id == null) return false;

            String modid = id.getNamespace().toLowerCase(Locale.ROOT);
            String itemId = id.toString().toLowerCase(Locale.ROOT);
            int meta = stack.getMetadata();

            if (this.modWildcard.contains(modid)) return true;
            if (this.itemSet.contains(itemId) || this.itemWildcard.contains(itemId)) return true;
            if (this.itemWithMeta.contains(itemId + "/" + meta)) return true;

            for (int oreId : OreDictionary.getOreIDs(stack))
            {
                String oreName = OreDictionary.getOreName(oreId).toLowerCase(Locale.ROOT);

                if (this.oreDictSet.contains(oreName)) return true;
                for (String prefix : this.oreDictWildcard) if (oreName.startsWith(prefix)) return true;
            }

            return false;
        }
    }
}