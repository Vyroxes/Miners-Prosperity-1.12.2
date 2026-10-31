package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiUtils;
import java.util.Collections;
import java.util.List;

public final class GuiMachineTitle
{
    private GuiMachineTitle() {}

    public static void draw(FontRenderer font, String title, int width, int guiLeft, int guiTop, List<GuiButton> buttons)
    {
        int[] bounds = getBounds(font, width, guiLeft, guiTop, buttons);
        String text = fit(font, title, bounds[1] - bounds[0]);
        int x = Math.max(bounds[0], Math.min((width - font.getStringWidth(text)) / 2, bounds[1] - font.getStringWidth(text)));
        font.drawString(text, x, 6, 4210752);
    }

    public static void drawTooltip(FontRenderer font, String title, int width, int guiLeft, int guiTop, List<GuiButton> buttons, int mouseX, int mouseY, int screenWidth, int screenHeight)
    {
        int[] bounds = getBounds(font, width, guiLeft, guiTop, buttons);
        int available = bounds[1] - bounds[0];
        if (font.getStringWidth(title) <= available) return;
        String text = fit(font, title, available);
        int textWidth = font.getStringWidth(text);
        int x = guiLeft + Math.max(bounds[0], Math.min((width - textWidth) / 2, bounds[1] - textWidth));
        if (mouseX >= x && mouseX < x + textWidth && mouseY >= guiTop + 6 && mouseY < guiTop + 6 + font.FONT_HEIGHT)
        {
            GuiUtils.drawHoveringText(Collections.singletonList(title), mouseX, mouseY, screenWidth, screenHeight, screenWidth / 2, font);
        }
    }

    private static String fit(FontRenderer font, String title, int width)
    {
        if (font.getStringWidth(title) <= width) return title;
        String suffix = "...";
        int suffixWidth = font.getStringWidth(suffix);
        if (width < suffixWidth) return font.trimStringToWidth(suffix, Math.max(0, width));
        return font.trimStringToWidth(title, width - suffixWidth) + suffix;
    }

    private static int[] getBounds(FontRenderer font, int width, int guiLeft, int guiTop, List<GuiButton> buttons)
    {
        int left = 8;
        int right = width - 8;
        for (GuiButton button : buttons)
        {
            if (!button.visible || button.y >= guiTop + 6 + font.FONT_HEIGHT || button.y + button.height <= guiTop + 6) continue;
            int buttonLeft = button.x - guiLeft;
            int buttonRight = buttonLeft + button.width;
            if (buttonRight <= 0 || buttonLeft >= width) continue;
            if (buttonLeft + button.width / 2 < width / 2) left = Math.max(left, buttonRight + 4);
            else right = Math.min(right, buttonLeft - 4);
        }
        return new int[]{left, Math.max(left, right)};
    }
}