package toldea.romecraft.client.gui;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


public class GuiBlacksmith extends RomeCraftGuiScreen {
	private static final ResourceLocation texture = new ResourceLocation("romecraft", "textures/gui/gui_blacksmith.png");
	
	public void initGui() {
		this.xSize = 248;
		this.ySize = 166;
		super.initGui();
	}

	@Override
	public void drawBackground(int par1) {
		super.drawWorldBackground(0);

		GL11.glColor4f(1, 1, 1, 1);

		minecraft.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void drawForeground() {
	}
}
