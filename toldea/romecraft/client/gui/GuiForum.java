package toldea.romecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.romanvillage.RomanVillage;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;

public class GuiForum extends GuiScreen {
	private static final ResourceLocation texture = new ResourceLocation("romecraft", "textures/gui/gui_forum.png");

	protected int xSize = 248;
	protected int ySize = 166;
	protected int guiLeft;
	protected int guiTop;

	private final TileEntityRomanVillageForum forum;
	private final RomanVillage village;

	public GuiForum(TileEntityRomanVillageForum forum) {
		this.forum = forum;
		village = forum.getRomanVillage();
	}

	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawBackground(0);
		drawForeground();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void drawBackground(int par1) {
		super.drawWorldBackground(0);

		GL11.glColor4f(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	public void drawForeground() {
		fontRenderer.drawString("Forum", guiLeft + 8, guiTop + 6, 0x404040);

		fontRenderer.drawString(
				"Village Forum: (" + village.getVillageForumLocation().posX + ", " + village.getVillageForumLocation().posY + ", "
						+ village.getVillageForumLocation().posZ + ")", guiLeft + 8, guiTop + 16, 0x404040);
		fontRenderer.drawString("Village Center: (" + village.getCenter().posX + ", " + village.getCenter().posY + ", " + village.getCenter().posZ + ")",
				guiLeft + 8, guiTop + 26, 0x404040);
		fontRenderer.drawString("Village Radius: " + village.getVillageRadius(), guiLeft + 8, guiTop + 36, 0x404040);
		fontRenderer.drawString("Number of Doors: " + village.getNumVillageDoors(), guiLeft + 8, guiTop + 46, 0x404040);
		fontRenderer.drawString("Number of Bloomeries: " + village.getNumBloomeries(), guiLeft + 8, guiTop + 56, 0x404040);
		fontRenderer.drawString("Number of Plebs: " + village.getNumPlebs() + " / " + village.getMaxNumberOfPlebs(), guiLeft + 8, guiTop + 66, 0x404040);

	}
}
