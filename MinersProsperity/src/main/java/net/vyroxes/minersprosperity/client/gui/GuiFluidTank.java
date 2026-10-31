package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.inventory.container.ContainerFluidTank;
import net.vyroxes.minersprosperity.machine.FluidTankTier;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.tile.TileEntityFluidTank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiFluidTank extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/fluid_tank.png");
    private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
    private final TileEntityFluidTank tileEntity;

    public GuiFluidTank(InventoryPlayer inventory, TileEntityFluidTank tileEntity)
    {
        super(new ContainerFluidTank(inventory, tileEntity));
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
            FluidStack fluid = Objects.requireNonNull(this.tileEntity.getFluidTank(0)).getFluid();
            int amount = Objects.requireNonNull(this.tileEntity.getFluidTank(0)).getFluidAmount();
            int capacity = Objects.requireNonNull(this.tileEntity.getFluidTank(0)).getCapacity();
            if (fluid != null) tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid") + TextFormatting.GRAY + ": " + fluid.getLocalizedName());
            if (this.tileEntity.getTier() == FluidTankTier.CREATIVE) {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_transfer") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
                tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.fluid_tank.creative"));
            }
            else
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + FluidFormatter.capacity(amount, capacity));
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_transfer") + TextFormatting.GRAY + ": " + FluidFormatter.transfer(this.tileEntity.getTier().transfer));
            }
        }
        else if (this.isPointInRegion(50, 25, 18, 18, mouseX, mouseY) && this.tileEntity.getInputStack(0).isEmpty()) tooltip.add(I18n.format("gui.fluid_tank.empty_container"));
        else if (this.isPointInRegion(108, 25, 18, 18, mouseX, mouseY) && this.tileEntity.getInputStack(1).isEmpty()) tooltip.add(I18n.format("gui.fluid_tank.fill_container"));
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
        drawRect(this.guiLeft + 7, this.guiTop + 6, this.guiLeft + 25, this.guiTop + 70, 0xFFC6C6C6);
        GlStateManager.color(1, 1, 1, 1);
        FluidStack fluid = Objects.requireNonNull(this.tileEntity.getFluidTank(0)).getFluid();
        if (fluid == null || fluid.amount <= 0) return;
        int height = Math.max(1, (int) ((long) fluid.amount * 36 / Objects.requireNonNull(this.tileEntity.getFluidTank(0)).getCapacity()));
        int color = fluid.getFluid().getColor(fluid);
        TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
        for (int offset = 0; offset < height; offset += 16)
        {
            int segment = Math.min(16, height - offset);
            this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 62 - offset - segment, sprite, 16, segment);
        }
        GlStateManager.color(1, 1, 1, 1);
    }
}