package net.vyroxes.minersprosperity.objects.guis;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiRedstoneControlButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
	private final int buttonState;
	private List<String> currentTooltip = null;
	
	public GuiRedstoneControlButton(int buttonId, int x, int y, int width, int height, ResourceLocation texture, int textureX, int textureY, int buttonState) 
    {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.buttonState = buttonState;
    }

	public List<String> getCurrentTooltip()
	{
	    return currentTooltip;
	}

    public boolean isHovered(int mouseX, int mouseY)
    {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) 
    {
    	if (this.visible) 
        {
            boolean isHovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

            int yOffset = isHovered ? 18 : 0;

            mc.getTextureManager().bindTexture(this.texture);
            this.drawTexturedModalRect(this.x, this.y, this.textureX, this.textureY + yOffset, this.width, this.height); 
            
            if (isHovered)
            {	
            	 currentTooltip = new ArrayList<>();
                 if (this.buttonState == 0) currentTooltip.add("Ignored");
                 else if (this.buttonState == 1) currentTooltip.add("Low Signal");
                 else if (this.buttonState == 2) currentTooltip.add("High Signal");
            }
            else 
            {
            	currentTooltip = null;
            }
        }
    }
}