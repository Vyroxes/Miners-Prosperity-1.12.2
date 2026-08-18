package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.inventory.container.ContainerCustomChest;
import net.vyroxes.minersprosperity.tile.TileEntityCustomChest;
import java.util.List;

public class GuiCustomChest extends GuiContainer {
    private final TileEntityCustomChest te;
    private final ResourceLocation texture;
    private final ResourceLocation texture2;
    private final int chestRows;
    private final int chestColumns;

    public GuiCustomChest(ContainerCustomChest container, TileEntityCustomChest te) {
        super(container);
        this.te = te;
        this.texture = te.getGuiTexture();
        this.texture2 = te.getGuiTexture2();
        this.chestRows = container.getRows();
        this.chestColumns = container.getColumns();

        int slotSize = 18;
        this.xSize = 8 + chestColumns * slotSize + 8;
        this.ySize = 17 + chestRows * slotSize + 97;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiButton button : this.buttonList) {
            if (button instanceof GuiDefaultButton) {
                List<String> guiButtonTooltip = ((GuiDefaultButton) button).getCurrentTooltip();
                if (guiButtonTooltip != null) GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
            }
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title;
        if (this.te.hasCustomName()) title = this.te.getCustomName();
        else title = I18n.format("tile." + this.te.getTier().translationKey + ".name");
        this.fontRenderer.drawString(title, 8, 5, 4210752);
        int inventoryLabelY = 19 + this.chestRows * 18;
        int slotSize = 18;
        int textOffsetX = Math.max(0, (chestColumns - 9) / 2) * slotSize;
        this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8 + textOffsetX, inventoryLabelY, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        if (this.chestColumns > 13) {
            int width = (this.chestColumns - 13) * 18 + 7;
            this.drawTexturedModalRect(this.guiLeft, this.guiTop - 1, 0, 0, 248, this.ySize);
            this.mc.getTextureManager().bindTexture(texture2);
            this.drawTexturedModalRect(this.guiLeft + 241, this.guiTop - 1, 0, 0, width, 221);
        }
        else this.drawTexturedModalRect(this.guiLeft, this.guiTop - 1, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode) { return true; }
}