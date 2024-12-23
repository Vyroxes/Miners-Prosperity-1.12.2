package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiSlotsConfigurationButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
    private final String tooltip;
	private List<String> currentTooltip = null;

	public GuiSlotsConfigurationButton(int buttonId, int x, int y, int width, int height, ResourceLocation texture, int textureX, int textureY, String tooltip)
    {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.tooltip = tooltip;
    }

	public List<String> getCurrentTooltip()
	{
	    return currentTooltip;
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
                currentTooltip.add(tooltip);
            }
            else 
            {
            	currentTooltip = null;
            }
        }
    }
}