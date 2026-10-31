package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.inventory.container.ContainerEnergyStorage;
import net.vyroxes.minersprosperity.machine.EnergyStorageTier;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.tile.TileEntityEnergyStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiEnergyStorage extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/energy_storage.png");
    private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
    private final TileEntityEnergyStorage tileEntity;

    public GuiEnergyStorage(InventoryPlayer inventory, TileEntityEnergyStorage tileEntity)
    {
        super(new ContainerEnergyStorage(inventory, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        int state = this.tileEntity.getRedstoneControlButtonState();
        String key = state == 1 ? "gui.redstone_low_signal.name" : state == 2 ? "gui.redstone_high_signal.name" : "gui.redstone_ignored.name";
        this.addButton(new GuiDefaultButton(0, this.guiLeft + 151, this.guiTop + 6, 18, 18, GUI_ELEMENTS, 16 + state * 18, 0, 0, 18, I18n.format(key)));
        this.addButton(new GuiDefaultButton(1, this.guiLeft + 151, this.guiTop + 27, 18, 18, GUI_ELEMENTS, 70, 0, 0, 18, I18n.format("gui.configuration.name")));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.tileEntity.setRedstoneControlButtonState((this.tileEntity.getRedstoneControlButtonState() + 1) % 3);
            this.initGui();
        }
        else if (button.id == 1) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_CONFIGURATION.ordinal(), this.tileEntity.getPos());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiButton button : this.buttonList)
        {
            if (button instanceof GuiDefaultButton)
            {
                List<String> tooltip = ((GuiDefaultButton) button).getCurrentTooltip();
                if (tooltip != null) GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
            }
        }
        List<String> tooltip = new ArrayList<>();
        if (this.isPointInRegion(79, 25, 18, 38, mouseX, mouseY))
        {
            boolean creative = this.tileEntity.getTier() == EnergyStorageTier.CREATIVE;
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + EnergyFormatter.capacity(this.tileEntity.getEnergyStored(), this.tileEntity.getMaxEnergyStored(), creative));
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_input") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(this.tileEntity.getMaxReceive(), creative));
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(this.tileEntity.getMaxExtract(), creative));
        }
        else if (this.isPointInRegion(50, 35, 18, 18, mouseX, mouseY) && this.tileEntity.getEnergyStack(0).isEmpty()) tooltip.add(I18n.format("gui.energy_storage.discharge_item"));
        else if (this.isPointInRegion(108, 35, 18, 18, mouseX, mouseY) && this.tileEntity.getEnergyStack(1).isEmpty()) tooltip.add(I18n.format("gui.energy_storage.charge_item"));
        if (!tooltip.isEmpty()) GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
        this.renderHoveredToolTip(mouseX, mouseY);
        GuiMachineTitle.drawTooltip(this.fontRenderer, Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText(), this.xSize, this.guiLeft, this.guiTop, this.buttonList, mouseX, mouseY, this.width, this.height);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String title = Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText();
        GuiMachineTitle.draw(this.fontRenderer, title, this.xSize, this.guiLeft, this.guiTop, this.buttonList);
        this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8, 73, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int capacity = this.tileEntity.getMaxEnergyStored();
        int height = capacity > 0 ? (int) ((long) this.tileEntity.getEnergyStored() * 36 / capacity) : 0;
        this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
        this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 26, 0, 0, 16, 36 - height);
        GlStateManager.color(1, 1, 1, 1);
    }
}