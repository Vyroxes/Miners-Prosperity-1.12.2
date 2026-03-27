package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.containers.ContainerUpgrades;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityElectricFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMetalPress;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

import java.util.List;
import java.util.Objects;

public class GuiMachineUpgrades extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation MACHINE_UPGRADES = new ResourceLocation(Tags.MODID, "textures/gui/machine_upgrades.png");
	private final TileEntityMachine tileEntity;

	public GuiMachineUpgrades(InventoryPlayer player, TileEntityMachine tileEntity)
	{
		super(new ContainerUpgrades(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	public TileEntityMachine getTileEntity()
	{
		return this.tileEntity;
	}

	@Override
	public void initGui() 
	{
	    super.initGui();
	    int guiLeft = (this.width - this.xSize) / 2;
	    int guiTop = (this.height - this.ySize) / 2;
	    
	    this.buttonList.clear();

		this.addButton(new GuiDefaultButton(0, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 36, 9, I18n.format("gui.back.name")));
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			if (tileEntity instanceof TileEntityAlloyFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityMetalPress)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityElectricFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
			}
		}
		this.initGui();
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
	            List<String> guiButtonTooltip = ((GuiDefaultButton) button).getCurrentTooltip();
	            if (guiButtonTooltip != null)
	            {
	                GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
	            }
	        }
	    }

	    this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String title = Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText();
		this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8, 73, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(MACHINE_UPGRADES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

			if (tileEntity instanceof TileEntityAlloyFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityMetalPress)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityElectricFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
			}
		}
	}
}