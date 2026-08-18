package net.vyroxes.minersprosperity.machine.upgrade;

public enum MachineUpgradeType {
    MACHINE(0),
    SPEED(1),
    ENERGY(2),
    EXPERIENCE(3);

    private final int index;

    MachineUpgradeType(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return this.index;
    }
}