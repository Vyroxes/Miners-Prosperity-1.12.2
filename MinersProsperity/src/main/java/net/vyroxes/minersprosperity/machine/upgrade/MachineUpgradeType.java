package net.vyroxes.minersprosperity.machine.upgrade;

import net.minecraft.item.Item;
import net.vyroxes.minersprosperity.init.ModItems;

public enum MachineUpgradeType {
    MACHINE,
    SPEED,
    ENERGY,
    EXPERIENCE,
    POWER,
    EFFICIENCY;

    public Item getItem()
    {
        return switch (this)
        {
            case MACHINE -> ModItems.MACHINE_UPGRADE;
            case SPEED -> ModItems.SPEED_UPGRADE;
            case ENERGY -> ModItems.ENERGY_UPGRADE;
            case EXPERIENCE -> ModItems.XP_UPGRADE;
            case POWER -> ModItems.POWER_UPGRADE;
            case EFFICIENCY -> ModItems.EFFICIENCY_UPGRADE;
        };
    }
}