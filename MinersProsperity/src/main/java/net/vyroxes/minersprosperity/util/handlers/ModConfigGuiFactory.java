package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModConfigGuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new ConfigGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    public static class ConfigGui extends GuiConfig
    {
        public ConfigGui(GuiScreen parent)
        {
            super(parent,
                    getConfigElements(),
                    "minersprosperity",
                    false,
                    false,
                    "Miner's Prosperity - Config");
        }

        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<>();
            list.add(new ConfigElement(ConfigHandler.config.getCategory("Ores")));
            list.add(new ConfigElement(ConfigHandler.config.getCategory("Backpacks")));
            list.add(new ConfigElement(ConfigHandler.config.getCategory("GUI")));
            list.add(new ConfigElement(ConfigHandler.config.getCategory("Enchantments")));
            list.add(new ConfigElement(ConfigHandler.config.getCategory("Tooltips")));
            return list;
        }
    }
}