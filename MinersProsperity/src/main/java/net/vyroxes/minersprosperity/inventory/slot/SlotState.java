package net.vyroxes.minersprosperity.inventory.slot;

import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

public class SlotState {
    public enum SlotType {
        INPUT,
        ENERGY,
        UPGRADE,
        OUTPUT
    }

    public enum IngredientType {
        ITEM,
        FLUID,
        ENERGY
    }

    public enum SlotMode {
        DISABLED,
        INPUT,
        AUTO_INPUT,
        OUTPUT,
        AUTO_OUTPUT,
        INPUT_OUTPUT
    }

    public enum SlotOutputMode {
        DEFAULT,
        VOID_EXCESS,
        VOID_ALL
    }

    public enum SlotCondition
    {
        DEFAULT,
        WHEN_EMPTY,
        WHEN_FULL
    }

    private SlotType slotType;
    private IngredientType ingredientType;
    private SlotMode slotMode;
    private SlotOutputMode slotOutputMode;
    private SlotCondition slotCondition;

    public SlotState(SlotType slotType, IngredientType ingredientType, SlotMode slotMode, SlotOutputMode slotOutputMode, SlotCondition slotCondition) {
        this.slotType = slotType;
        this.ingredientType = ingredientType;
        this.slotMode = slotMode;
        this.slotOutputMode = slotOutputMode;
        this.slotCondition = slotCondition;
    }

    public SlotType getSlotType() {
        return this.slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public IngredientType getIngredientType() {
        return this.ingredientType;
    }

    public void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    public SlotMode getSlotMode() {
        return this.slotMode;
    }

    public void setSlotMode(SlotMode slotMode) {
        this.slotMode = slotMode;
    }

    public SlotOutputMode getSlotOutputMode() {
        return this.slotOutputMode;
    }

    public void setSlotOutputMode(SlotOutputMode slotOutputMode) {
        this.slotOutputMode = slotOutputMode;
    }

    public SlotCondition getSlotCondition() { return this.slotCondition;}

    public void setSlotCondition(SlotCondition slotCondition) { this.slotCondition = slotCondition;}

    public void writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setString("SlotType", getSlotType().name());
        tag.setString("IngredientType", getIngredientType().name());
        tag.setString("SlotMode", getSlotMode().name());
        tag.setString("SlotOutputMode", getSlotOutputMode().name());
        tag.setString("SlotCondition", getSlotCondition().name());
    }

    public void readFromNBT(@NotNull NBTTagCompound tag) {
        if (tag.hasKey("SlotType")) this.slotType = SlotType.valueOf(tag.getString("SlotType"));
        if (tag.hasKey("IngredientType")) this.ingredientType = IngredientType.valueOf(tag.getString("IngredientType"));
        if (tag.hasKey("SlotMode")) this.slotMode = SlotMode.valueOf(tag.getString("SlotMode"));
        if (tag.hasKey("SlotOutputMode")) this.slotOutputMode = SlotOutputMode.valueOf(tag.getString("SlotOutputMode"));
        if (tag.hasKey("SlotCondition")) this.slotCondition = SlotCondition.valueOf(tag.getString("SlotCondition"));
        else slotCondition = SlotCondition.DEFAULT;
    }
}
