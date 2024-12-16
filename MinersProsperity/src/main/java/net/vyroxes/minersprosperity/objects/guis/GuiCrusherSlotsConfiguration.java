package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GuiCrusherSlotsConfiguration extends GuiContainer
{
	private static final ResourceLocation CRUSHER_SLOTS_CONFIGURATION_TEXTURE = new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png");
	private final TileEntityCrusher tileEntity;

	public GuiCrusherSlotsConfiguration(InventoryPlayer player, TileEntityCrusher tileEntity)
	{
		super(new ContainerInventory(player, tileEntity));
		this.tileEntity = tileEntity;
	}
	
	@Override
	public void initGui() 
	{
	    super.initGui();

		int guiLeft = (this.width - this.xSize) / 2;
		int guiTop = (this.height - this.ySize) / 2;

		this.buttonList.clear();

		int northState = this.tileEntity.getFaceState(0);
		int southState = this.tileEntity.getFaceState(1);
		int eastState = this.tileEntity.getFaceState(2);
		int westState = this.tileEntity.getFaceState(3);
		int upState = this.tileEntity.getFaceState(4);
		int downState = this.tileEntity.getFaceState(5);

		if (northState == 0)
		{
			this.addButton(new GuiFaceButton(0, guiLeft + 132, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, northState));
		}
		else if (northState == 1)
		{
			this.addButton(new GuiFaceButton(0, guiLeft + 132, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, northState));
		}
		else if (northState == 2)
		{
			this.addButton(new GuiFaceButton(0, guiLeft + 132, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, northState));
		}
		else if (northState == 3)
		{
			this.addButton(new GuiFaceButton(0, guiLeft + 132, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, northState));
		}
		else if (northState == 4)
		{
			this.addButton(new GuiFaceButton(0, guiLeft + 132, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, northState));
		}

		if (southState == 0)
		{
			this.addButton(new GuiFaceButton(1, guiLeft + 151, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, southState));
		}
		else if (southState == 1)
		{
			this.addButton(new GuiFaceButton(1, guiLeft + 151, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, southState));
		}
		else if (southState == 2)
		{
			this.addButton(new GuiFaceButton(1, guiLeft + 151, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, southState));
		}
		else if (southState == 3)
		{
			this.addButton(new GuiFaceButton(1, guiLeft + 151, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, southState));
		}
		else if (southState == 4)
		{
			this.addButton(new GuiFaceButton(1, guiLeft + 151, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, southState));
		}

		if (eastState == 0)
		{
			this.addButton(new GuiFaceButton(2, guiLeft + 113, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, eastState));
		}
		else if (eastState == 1)
		{
			this.addButton(new GuiFaceButton(2, guiLeft + 113, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, eastState));
		}
		else if (eastState == 2)
		{
			this.addButton(new GuiFaceButton(2, guiLeft + 113, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, eastState));
		}
		else if (eastState == 3)
		{
			this.addButton(new GuiFaceButton(2, guiLeft + 113, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, eastState));
		}
		else if (eastState == 4)
		{
			this.addButton(new GuiFaceButton(2, guiLeft + 113, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, eastState));
		}

		if (westState == 0)
		{
			this.addButton(new GuiFaceButton(3, guiLeft + 151, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, westState));
		}
		else if (westState == 1)
		{
			this.addButton(new GuiFaceButton(3, guiLeft + 151, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, westState));
		}
		else if (westState == 2)
		{
			this.addButton(new GuiFaceButton(3, guiLeft + 151, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, westState));
		}
		else if (westState == 3)
		{
			this.addButton(new GuiFaceButton(3, guiLeft + 151, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, westState));
		}
		else if (westState == 4)
		{
			this.addButton(new GuiFaceButton(3, guiLeft + 151, guiTop + 34, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, westState));
		}

		if (upState == 0)
		{
			this.addButton(new GuiFaceButton(4, guiLeft + 132, guiTop + 15, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, upState));
		}
		else if (upState == 1)
		{
			this.addButton(new GuiFaceButton(4, guiLeft + 132, guiTop + 15, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, upState));
		}
		else if (upState == 2)
		{
			this.addButton(new GuiFaceButton(4, guiLeft + 132, guiTop + 15, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, upState));
		}
		else if (upState == 3)
		{
			this.addButton(new GuiFaceButton(4, guiLeft + 132, guiTop + 15, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, upState));
		}
		else if (upState == 4)
		{
			this.addButton(new GuiFaceButton(4, guiLeft + 132, guiTop + 15, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, upState));
		}

		if (downState == 0)
		{
			this.addButton(new GuiFaceButton(5, guiLeft + 132, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 0, downState));
		}
		else if (downState == 1)
		{
			this.addButton(new GuiFaceButton(5, guiLeft + 132, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 194, 0, downState));
		}
		else if (downState == 2)
		{
			this.addButton(new GuiFaceButton(5, guiLeft + 132, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 212, 0, downState));
		}
		else if (downState == 3)
		{
			this.addButton(new GuiFaceButton(5, guiLeft + 132, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 230, 0, downState));
		}
		else if (downState == 4)
		{
			this.addButton(new GuiFaceButton(5, guiLeft + 132, guiTop + 53, 18, 18, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 176, 36, downState));
		}
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			this.tileEntity.addFaceState(0);
			this.initGui();
		}
		else if (guiButton.id == 1)
		{
			this.tileEntity.addFaceState(1);
			this.initGui();
		}
		else if (guiButton.id == 2)
		{
			this.tileEntity.addFaceState(2);
			this.initGui();
		}
		else if (guiButton.id == 3)
		{
			this.tileEntity.addFaceState(3);
			this.initGui();
		}
		else if (guiButton.id == 4)
		{
			this.tileEntity.addFaceState(4);
			this.initGui();
		}
		else if (guiButton.id == 5)
		{
			this.tileEntity.addFaceState(5);
			this.initGui();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 1)
		{
			for (GuiButton guiButton : this.buttonList)
			{
				System.out.println("Sprawdzam przycisk ID: " + guiButton.id + ", czy mysz nad nim: " + guiButton.isMouseOver());

				if (guiButton.isMouseOver())
				{
					switch (guiButton.id)
					{
						case 0:
							this.tileEntity.subFaceState(0);
							break;
						case 1:
							this.tileEntity.subFaceState(1);
							break;
						case 2:
							this.tileEntity.subFaceState(2);
							break;
						case 3:
							this.tileEntity.subFaceState(3);
							break;
						case 4:
							this.tileEntity.subFaceState(4);
							break;
						case 5:
							this.tileEntity.subFaceState(5);
							break;
					}
					this.initGui();
					return;
				}
			}
		}
		else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

//	@Override
//	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
//	{
//		if (mouseButton == 1)
//		{
//			for (GuiButton guiButton : this.buttonList)
//			{
//				if (guiButton.id == 0)
//				{
//					this.tileEntity.subFaceState(0);
//					this.initGui();
//				}
//				else if (guiButton.id == 1)
//				{
//					this.tileEntity.subFaceState(1);
//					this.initGui();
//				}
//				else if (guiButton.id == 2)
//				{
//					this.tileEntity.subFaceState(2);
//					this.initGui();
//				}
//				else if (guiButton.id == 3)
//				{
//					this.tileEntity.subFaceState(3);
//					this.initGui();
//				}
//				else if (guiButton.id == 4)
//				{
//					this.tileEntity.subFaceState(4);
//					this.initGui();
//				}
//				else if (guiButton.id == 5)
//				{
//					this.tileEntity.subFaceState(5);
//					this.initGui();
//				}
//				return;
//			}
//		}
//		else
//		{
//			super.mouseClicked(mouseX, mouseY, mouseButton);
//		}
//	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);
	    this.renderHoveredToolTip(mouseX, mouseY);

		for (GuiButton button : this.buttonList)
		{
			if (button instanceof GuiFaceButton)
			{
				List<String> faceButtonTooltip = ((GuiFaceButton) button).getCurrentTooltip();
				if (faceButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(faceButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
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
		this.mc.getTextureManager().bindTexture(CRUSHER_SLOTS_CONFIGURATION_TEXTURE);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1)
		{
			this.tileEntity.setCurrentGuiId(GuiHandler.GUI_CRUSHER);

			this.mc.player.openGui(MinersProsperity.instance, GuiHandler.GUI_CRUSHER, this.mc.world, this.tileEntity.getPos().getX(),  this.tileEntity.getPos().getY(),  this.tileEntity.getPos().getZ());
		}
	}
}