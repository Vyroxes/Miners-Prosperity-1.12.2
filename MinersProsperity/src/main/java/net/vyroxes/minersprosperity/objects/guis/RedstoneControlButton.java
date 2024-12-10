package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class RedstoneControlButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
	
	public RedstoneControlButton(int buttonId, int x, int y, int width, int height, ResourceLocation texture, int textureX, int textureY) 
    {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) 
    {
    	if (this.visible) 
        {
            boolean isHovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

            int yOffset = isHovered ? 20 : 0;

            mc.getTextureManager().bindTexture(this.texture);
            this.drawTexturedModalRect(this.x, this.y, this.textureX, this.textureY + yOffset, this.width, this.height); 
            
            if (isHovered)
            {
//                String hoverText = "Ignored";
//                this.drawHoveringText(hoverText, mouseX, mouseY, mc.fontRenderer);
            	
//            	List<String> tooltipText = new ArrayList<>();
//                tooltipText.add("Ignored");
//                
//                RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(
//                        null,
//                        tooltipText,
//                        mouseX,
//                        mouseY,
//                        mc.displayWidth,
//                        mc.displayHeight,
//                        -1,
//                        mc.fontRenderer
//                );
//                
//                MinecraftForge.EVENT_BUS.post(event);
//                
//                int tooltipWidth = 0;
//                for (String line : event.getLines()) {
//                    int lineWidth = mc.fontRenderer.getStringWidth(line);
//                    tooltipWidth = Math.max(tooltipWidth, lineWidth);
//                }
//                
//                FontRenderer fontRenderer = mc.fontRenderer;
//                int offsetX = 12;
//                int offsetY = -12;
//                int tooltipHeight = event.getLines().size() * mc.fontRenderer.FONT_HEIGHT;
//
//
//                drawGradientRect(mouseX + offsetX - 3, mouseY + offsetY - 4, mouseX + offsetX + tooltipWidth + 3, mouseY + offsetY + tooltipHeight + 3, 0xF0100010, 0xF0100010);
//                
//                for (String line : event.getLines())
//                {
//                    fontRenderer.drawStringWithShadow(line, (float)(mouseX + offsetX), (float)(mouseY + offsetY), 0xFFFFFF);
//                    offsetY += fontRenderer.FONT_HEIGHT;
//                }
            }
        }
    }

//    private void drawHoveringText(String text, int mouseX, int mouseY, FontRenderer fontRenderer)
//    {
//    	Minecraft mc = Minecraft.getMinecraft();
//        mc.getRenderItem().zLevel = 300.0F;
//    	
//        List<String> lines = fontRenderer.listFormattedStringToWidth(text, 200);
//        int x = mouseX + 12;
//        int y = mouseY - 12;
//
//        int width = 0;
//        for (String line : lines)
//        {
//            int lineWidth = fontRenderer.getStringWidth(line);
//            if (lineWidth > width)
//            {
//                width = lineWidth;
//            }
//        }
//
//        int height = 8 * lines.size();
//
//        this.drawGradientRect(x - 4, y - 5, x + width + 4, y + height + 5, 0xFF100010, 0xFF100010);
//        this.drawGradientRect(x - 5, y - 4, x + width + 5, y + height + 4, 0xFF100010, 0xFF100010);
//        this.drawGradientRect(x - 4, y - 4, x + width + 4, y + height + 4, 0x505000FF, 0x505000FF);
//        this.drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, 0xFF100010, 0xFF100010);
//
//        for (int i = 0; i < lines.size(); i++)
//        {
//            fontRenderer.drawString(lines.get(i), x, y + i * 8, 16777215);
//        }
//        
//        mc.getRenderItem().zLevel = 0.0F;
//    }
}