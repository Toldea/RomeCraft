package toldea.romecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class RomeCraftGuiScreen extends GuiScreen {
	protected static final Minecraft minecraft = Minecraft.getMinecraft();
	
	protected int xSize;
	protected int ySize;
	protected int guiLeft;
	protected int guiTop;
	
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		drawBackground(0);
		drawForeground();
		super.drawScreen(par1, par2, par3);
	}
	
	public void drawForeground() {}
}
