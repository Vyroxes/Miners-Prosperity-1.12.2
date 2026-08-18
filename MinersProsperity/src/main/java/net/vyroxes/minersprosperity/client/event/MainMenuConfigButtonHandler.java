package net.vyroxes.minersprosperity.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.client.gui.GuiModConfig;
import net.vyroxes.minersprosperity.config.ModConfig;

@Mod.EventBusSubscriber(modid = Tags.MODID, value = Side.CLIENT)
public class MainMenuConfigButtonHandler
{
    private static final int BUTTON_ID = 0x4D50;
    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 4;

    private MainMenuConfigButtonHandler() {}

    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof GuiMainMenu)
        {
            if (!ModConfig.GUI.showConfigButtonInMainMenu) return;

            GuiButton referenceButton = findButton(event, 6);
            if (referenceButton == null) return;

            event.getButtonList().add(new GuiButton(BUTTON_ID, referenceButton.x - BUTTON_WIDTH - BUTTON_GAP, referenceButton.y, BUTTON_WIDTH, BUTTON_HEIGHT, "MP"));
            return;
        }

        if (event.getGui() instanceof GuiIngameMenu)
        {
            if (!ModConfig.GUI.showConfigButtonInPauseMenu) return;

            GuiButton optionsButton = findButton(event, 0);
            if (optionsButton == null) return;

            event.getButtonList().add(new GuiButton(BUTTON_ID, optionsButton.x - BUTTON_WIDTH - BUTTON_GAP, optionsButton.y, BUTTON_WIDTH, BUTTON_HEIGHT, "MP"));
        }
    }

    @SubscribeEvent
    public static void onButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if (!(event.getGui() instanceof GuiMainMenu) && !(event.getGui() instanceof GuiIngameMenu)) return;
        if (event.getButton().id != BUTTON_ID) return;

        Minecraft.getMinecraft().displayGuiScreen(new GuiModConfig(event.getGui()));
    }

    private static GuiButton findButton(GuiScreenEvent.InitGuiEvent.Post event, int... ids)
    {
        for (GuiButton button : event.getButtonList()) for (int id : ids) if (button.id == id) return button;

        return null;
    }
}