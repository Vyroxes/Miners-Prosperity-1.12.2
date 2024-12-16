package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiFaceButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
    private final int faceState;
    private List<String> currentTooltip = null;

	public GuiFaceButton(int buttonId, int x, int y, int width, int height, ResourceLocation texture, int textureX, int textureY, int faceState)
    {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.faceState = faceState;
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
                if (this.faceState == 0) currentTooltip.add("Ignored");
                else if (this.faceState == 1) currentTooltip.add("Input 1");
                else if (this.faceState == 2) currentTooltip.add("Input 2");
                else if (this.faceState == 3) currentTooltip.add("Fuel");
                else if (this.faceState == 4) currentTooltip.add("Output");
            }
            else
            {
                currentTooltip = null;
            }
        }
    }
}