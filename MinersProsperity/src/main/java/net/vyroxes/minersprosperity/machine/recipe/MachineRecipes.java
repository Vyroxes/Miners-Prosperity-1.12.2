package net.vyroxes.minersprosperity.machine.recipe;

public final class MachineRecipes {
    private MachineRecipes() {}

    public static void load() {
        AlloyFurnaceRecipeLoader.loadFromJSON();
        PulverizerRecipeLoader.loadFromJSON();
        MetalPressRecipeLoader.loadFromJSON();
        LiquidFuelGeneratorRecipeLoader.loadFromJSON();
    }
}