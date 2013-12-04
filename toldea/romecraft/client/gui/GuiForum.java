package toldea.romecraft.client.gui;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.renderer.RenderRomanVillageMap;
import toldea.romecraft.romanvillage.RomanVillage;
import toldea.romecraft.romanvillage.RomanVillageMap;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;

public class GuiForum extends RomeCraftGuiScreen {
	private static final ResourceLocation texture = new ResourceLocation("romecraft", "textures/gui/gui_forum.png");
	private static final RenderRomanVillageMap mapRenderer = new RenderRomanVillageMap(minecraft.getTextureManager(), RomanVillageMap.DEFAULT_ROMAN_MAP_SIZE);

	private final TileEntityRomanVillageForum forum;
	private final RomanVillage village;
	private int mapSize = 0;

	public GuiForum(TileEntityRomanVillageForum forum) {
		this.forum = forum;
		village = forum.getRomanVillage();
		if (village != null) {
			village.updateMapData(forum.worldObj);
			RomanVillageMap mapData = village.getVillageMapData();
			mapRenderer.renderMapToImage(mapData);
			// For some reason we have to draw the map twice as large to get all the pixels.
			mapSize = mapData.mapSize * 2;
		}
	}

	public void initGui() {
		this.xSize = 248;
		this.ySize = 166;
		super.initGui();
	}
	
	@Override
	public void initGuiComponents() {
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
		if (village == null) {
			return;
		}

		fontRenderer.drawStringWithShadow("Forum", guiLeft + 8, guiTop + 6, 0xFFFFFF);

		int x = guiLeft + 14;
		int y = guiTop + 20;
		int i = 0;
		int d = 12;

		fontRenderer.drawString(
				"Village Forum: (" + village.getVillageForumLocation().posX + ", " + village.getVillageForumLocation().posY + ", "
						+ village.getVillageForumLocation().posZ + ")", x, y + i++ * d, 0x404040);
		fontRenderer.drawString("Village Center: (" + village.getCenter().posX + ", " + village.getCenter().posY + ", " + village.getCenter().posZ + ")", x, y
				+ i++ * d, 0x404040);
		fontRenderer.drawString("Village Radius: " + village.getVillageRadius(), x, y + i++ * d, 0x404040);
		fontRenderer.drawString("Number of Doors: " + village.getNumVillageDoors(), x, y + i++ * d, 0x404040);
		fontRenderer.drawString("Number of Bloomeries: " + village.getNumBloomeries(), x, y + i++ * d, 0x404040);
		fontRenderer.drawString("Number of Anvils: " + village.getNumRomanAnvils(), x, y + i++ * d, 0x404040);
		fontRenderer.drawString("Number of Plebs: " + village.getNumPlebs() + " / " + village.getMaxNumberOfPlebs(), x, y + i++ * d, 0x404040);

		drawVillageMap();
	}

	private void drawVillageMap() {
		if (mapSize > 0) {
			minecraft.getTextureManager().bindTexture(mapRenderer.getMapImage());

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1, 1, 1, 1);

			GL11.glScalef(.25F, .25F, .25F);

			// Draw the map twice as large as normal, but scale it down 50%. For some reason the source image is twice too large.
			drawTexturedModalRect((guiLeft + 238) * 4 - mapSize, (guiTop + 10) * 4, 0, 0, mapSize, mapSize);

			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
