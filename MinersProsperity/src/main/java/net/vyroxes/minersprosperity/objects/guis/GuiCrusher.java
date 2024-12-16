package net.vyroxes.minersprosperity.objects.guis;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;


public class GuiCrusher extends GuiContainer
{
	private static final ResourceLocation CRUSHER_TEXTURE = new ResourceLocation("minersprosperity", "textures/gui/crusher.png");
	private final TileEntityCrusher tileEntity;
	
	public GuiCrusher(InventoryPlayer player, TileEntityCrusher tileEntity)
	{
		super(new ContainerCrusher(player, tileEntity));
		this.tileEntity = tileEntity;
	}
	
	@Override
	public void initGui() 
	{
	    super.initGui();
	    int guiLeft = (this.width - this.xSize) / 2;
	    int guiTop = (this.height - this.ySize) / 2;
	    
	    this.buttonList.clear();
	    
	    int buttonState = this.tileEntity.getButtonState();
	    
	    if (buttonState == 0)
	    {
		    this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 15, 22, 20, new ResourceLocation("minersprosperity", "textures/gui/crusher.png"), 176, 31, buttonState));
	    }
	    else if (buttonState == 1)
	    {
		    this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 15, 22, 20, new ResourceLocation("minersprosperity", "textures/gui/crusher.png"), 198, 31, buttonState));
	    }
	    else if (buttonState == 2)
	    {
		    this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 15, 22, 20, new ResourceLocation("minersprosperity", "textures/gui/crusher.png"), 220, 31, buttonState));
	    }

		this.addButton(new GuiSlotsConfigurationButton(1, guiLeft + 7, guiTop + 38, 22, 20, new ResourceLocation("minersprosperity", "textures/gui/crusher.png"), 176, 71));
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			this.tileEntity.addButtonState();
			this.initGui();
		}
		if (guiButton.id == 1)
		{
			this.tileEntity.setCurrentGuiId(GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION);

			this.mc.player.openGui(MinersProsperity.instance, GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION, this.mc.world, this.tileEntity.getPos().getX(),  this.tileEntity.getPos().getY(),  this.tileEntity.getPos().getZ());
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);

	    for (GuiButton button : this.buttonList) 
	    {
	        if (button instanceof GuiRedstoneControlButton) 
	        {
	            List<String> redstoneControlButtonTooltip = ((GuiRedstoneControlButton) button).getCurrentTooltip();
	            if (redstoneControlButtonTooltip != null)
	            {
	                GuiUtils.drawHoveringText(redstoneControlButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
	            }
	        }
			if (button instanceof GuiSlotsConfigurationButton)
			{
				List<String> slotsConfigurationButtonTooltip = ((GuiSlotsConfigurationButton) button).getCurrentTooltip();
				if (slotsConfigurationButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(slotsConfigurationButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
				}
			}
	    }

	    this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String title = this.tileEntity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8, 73, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(CRUSHER_TEXTURE);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		 
		if (TileEntityCrusher.isActive(tileEntity))
		{
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}
	
	private int getCookProgressScaled(int pixels)
	{
		int i = this.tileEntity.getField(2);
		int j = this.tileEntity.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private int getBurnLeftScaled(int pixels)
	{
		int i = this.tileEntity.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileEntity.getField(0) * pixels / i;
	}
}