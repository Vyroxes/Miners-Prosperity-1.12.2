package net.vyroxes.minersprosperity.machine;

public class MachineSlotLayout {
    private final int inputCount;
    private final int energyCount;
    private final int outputCount;
    private final int upgradeCount;

    private final int inputStart;
    private final int energyStart;
    private final int outputStart;
    private final int upgradeStart;

    public MachineSlotLayout(int inputCount, int energyCount, int outputCount, int upgradeCount)
    {
        if (inputCount < 0) throw new IllegalArgumentException("inputCount cannot be negative");
        if (energyCount < 0) throw new IllegalArgumentException("energyCount cannot be negative");
        if (outputCount < 0) throw new IllegalArgumentException("outputCount cannot be negative");
        if (upgradeCount < 0) throw new IllegalArgumentException("upgradeCount cannot be negative");

        this.inputCount = inputCount;
        this.energyCount = energyCount;
        this.outputCount = outputCount;
        this.upgradeCount = upgradeCount;

        this.inputStart = 0;
        this.energyStart = this.inputStart + inputCount;
        this.outputStart = this.energyStart + energyCount;
        this.upgradeStart = this.outputStart + outputCount;
    }

    public int getInputSlot(int index)
    {
        checkIndex(index, this.inputCount, "input");
        return this.inputStart + index;
    }

    public int getEnergySlot(int index)
    {
        checkIndex(index, this.energyCount, "energy");
        return this.energyStart + index;
    }

    public int getOutputSlot(int index)
    {
        checkIndex(index, this.outputCount, "output");
        return this.outputStart + index;
    }

    public int getUpgradeSlot(int index)
    {
        checkIndex(index, this.upgradeCount, "upgrade");
        return this.upgradeStart + index;
    }

    public int[] getInputSlots()
    {
        return createRange(this.inputStart, this.inputCount);
    }

    public int[] getEnergySlots()
    {
        return createRange(this.energyStart, this.energyCount);
    }

    public int[] getOutputSlots()
    {
        return createRange(this.outputStart, this.outputCount);
    }

    public int[] getUpgradeSlots()
    {
        return createRange(this.upgradeStart, this.upgradeCount);
    }

    public int getInputCount()
    {
        return this.inputCount;
    }

    public int getEnergyCount()
    {
        return this.energyCount;
    }

    public int getOutputCount()
    {
        return this.outputCount;
    }

    public int getUpgradeCount()
    {
        return this.upgradeCount;
    }

    public int getTotalSlots()
    {
        return this.inputCount + this.energyCount + this.outputCount + this.upgradeCount;
    }

    private static int[] createRange(int start, int count)
    {
        int[] slots = new int[count];

        for (int i = 0; i < count; i++) slots[i] = start + i;

        return slots;
    }

    private static void checkIndex(int index, int count, String type)
    {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException("Invalid " + type + " slot index: " + index + ", count: " + count);
    }
}