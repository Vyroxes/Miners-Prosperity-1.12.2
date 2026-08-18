package net.vyroxes.minersprosperity.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.vyroxes.minersprosperity.Tags;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConfigOrderHandler {
    private ConfigOrderHandler() {}

    public static void apply()
    {
        try
        {
            Method method = ConfigManager.class.getDeclaredMethod("getConfiguration", String.class, String.class);
            method.setAccessible(true);

            Configuration config = (Configuration) method.invoke(null, Tags.MODID, "");
            if (config == null) return;

            for (Field field : ModConfig.class.getDeclaredFields())
            {
                if (field.isSynthetic() || !Modifier.isStatic(field.getModifiers()) || !isCategoryType(field.getType())) continue;

                String categoryName = getName(field);
                applyCategoryOrder(config, field.getType(), "general." + categoryName.toLowerCase(Locale.ENGLISH));
            }

            config.save();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to apply Miner's Prosperity config order: ", e);
        }
    }

    private static void applyCategoryOrder(Configuration config, Class<?> categoryClass, String categoryPath)
    {
        List<String> propertyOrder = new ArrayList<>();

        for (Field field : categoryClass.getDeclaredFields())
        {
            if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) continue;

            String name = getName(field);

            if (isCategoryType(field.getType()))
            {
                applyCategoryOrder(config, field.getType(), categoryPath + "." + name.toLowerCase(Locale.ENGLISH));
                continue;
            }

            propertyOrder.add(name);
        }

        if (!propertyOrder.isEmpty()) config.setCategoryPropertyOrder(categoryPath, propertyOrder);
    }

    private static String getName(Field field)
    {
        Config.Name annotation = field.getAnnotation(Config.Name.class);
        return annotation == null ? field.getName() : annotation.value();
    }

    private static boolean isCategoryType(Class<?> type)
    {
        return type.getName().startsWith(ModConfig.class.getName() + "$");
    }
}