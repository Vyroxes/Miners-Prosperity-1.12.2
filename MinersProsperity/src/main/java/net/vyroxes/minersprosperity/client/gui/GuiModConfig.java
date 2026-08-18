package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.config.ModConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class GuiModConfig extends GuiConfig {
    public GuiModConfig(GuiScreen parentScreen)
    {
        super(parentScreen, getOrderedConfigElements(), Tags.MODID, false, false, "Miner's Prosperity Config");
    }

    private static List<IConfigElement> getOrderedConfigElements()
    {
        List<IConfigElement> original = ConfigElement.from(ModConfig.class).getChildElements();
        List<IConfigElement> ordered = new ArrayList<>();
        Map<String, IConfigElement> elements = new HashMap<>();

        for (IConfigElement element : original) elements.put(element.getName().toLowerCase(Locale.ENGLISH), element);

        for (Field field : ModConfig.class.getDeclaredFields())
        {
            if (field.isSynthetic() || !Modifier.isStatic(field.getModifiers())) continue;

            Config.Name annotation = field.getAnnotation(Config.Name.class);
            if (annotation == null) continue;

            IConfigElement element = elements.remove(annotation.value().toLowerCase(Locale.ENGLISH));
            if (element != null) ordered.add(element);
        }

        ordered.addAll(elements.values());

        return ordered;
    }
}