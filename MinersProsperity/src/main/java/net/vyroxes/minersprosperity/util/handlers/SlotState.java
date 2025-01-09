package net.vyroxes.minersprosperity.util.handlers;

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
        FLUID
    }

    public enum SlotMode {
        DISABLED,
        INPUT,
        AUTO_INPUT,
        OUTPUT,
        AUTO_OUTPUT
    }

    public enum SlotOutputMode {
        DEFAULT,
        VOID_EXCESS,
        VOID_ALL
    }

    private SlotType slotType;
    private IngredientType ingredientType;
    private SlotMode slotMode;
    private SlotOutputMode slotOutputMode;

    public SlotState(SlotType slotType, IngredientType ingredientType, SlotMode slotMode, SlotOutputMode slotOutputMode) {
        this.slotType = slotType;
        this.ingredientType = ingredientType;
        this.slotMode = slotMode;
        this.slotOutputMode = slotOutputMode;
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

    public void writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setString("SlotType", getSlotType().name());
        tag.setString("IngredientType", getIngredientType().name());
        tag.setString("SlotMode", getSlotMode().name());
        tag.setString("SlotOutputMode", getSlotOutputMode().name());
    }

    public void readFromNBT(@NotNull NBTTagCompound tag) {
        if (tag.hasKey("SlotType")) {
            this.slotType = SlotType.valueOf(tag.getString("SlotType"));
        }
        if (tag.hasKey("IngredientType")) {
            this.ingredientType = IngredientType.valueOf(tag.getString("IngredientType"));
        }
        if (tag.hasKey("SlotMode")) {
            this.slotMode = SlotMode.valueOf(tag.getString("SlotMode"));
        }
        if (tag.hasKey("SlotOutputMode")) {
            this.slotOutputMode = SlotOutputMode.valueOf(tag.getString("SlotOutputMode"));
        }
    }
}
