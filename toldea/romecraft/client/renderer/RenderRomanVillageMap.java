package toldea.romecraft.client.renderer;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import toldea.romecraft.romanvillage.RomanVillageMap;

public class RenderRomanVillageMap {
	private static final ResourceLocation mapIconsTexture = new ResourceLocation("textures/map/map_icons.png");
	private final DynamicTexture bufferedImage;
	private int[] intArray;
	private final TextureManager textureManager;
	private final ResourceLocation mapImageTexture;
	private final int size;

	public RenderRomanVillageMap(TextureManager par2TextureManager, int size) {
		this.textureManager = par2TextureManager;
		this.bufferedImage = new DynamicTexture(size, size);
		this.mapImageTexture = par2TextureManager.getDynamicTextureLocation("romanvillagemap", this.bufferedImage);
		this.size = size;
		this.intArray = this.bufferedImage.getTextureData();
		for (int i = 0; i < this.intArray.length; ++i) {
			this.intArray[i] = 0;
		}
	}
	
	public ResourceLocation getMapImage() {
		return mapImageTexture;
	}

	public void renderMapToImage(RomanVillageMap mapData) {
		for (int i = 0; i < size * size; ++i) {
			byte b0 = mapData.colors[i];
			if (b0 / 4 == 0) {
				this.intArray[i] = (i + i / size & 1) * 8 + 16 << 24;
			} else {
				int j = MapColor.mapColorArray[b0 / 4].colorValue;
				
				int k = b0 & 3;
				short short1 = 220;
				
				if (k == 2) {
					short1 = 255;
				}

				if (k == 0) {
					short1 = 180;
				}

				int l = (j >> 16 & 255) * short1 / 255;
				int i1 = (j >> 8 & 255) * short1 / 255;
				int j1 = (j & 255) * short1 / 255;
				this.intArray[i] = -16777216 | l << 16 | i1 << 8 | j1;
			}
		}

		this.bufferedImage.updateDynamicTexture();
		/*
		byte b1 = 0;
		byte b2 = 0;
		Tessellator tessellator = Tessellator.instance;
		float f = 0.0F;
		textureManager.bindTexture(this.field_111276_e);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double) ((float) (b1 + 0) + f), (double) ((float) (b2 + 128) - f), -0.009999999776482582D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double) ((float) (b1 + 128) - f), (double) ((float) (b2 + 128) - f), -0.009999999776482582D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double) ((float) (b1 + 128) - f), (double) ((float) (b2 + 0) + f), -0.009999999776482582D, 1.0D, 0.0D);
		tessellator.addVertexWithUV((double) ((float) (b1 + 0) + f), (double) ((float) (b2 + 0) + f), -0.009999999776482582D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		textureManager.bindTexture(mapIconsTexture);
		int k1 = 0;

		for (Iterator iterator = mapData.playersVisibleOnMap.values().iterator(); iterator.hasNext(); ++k1) {
			MapCoord mapcoord = (MapCoord) iterator.next();
			GL11.glPushMatrix();
			GL11.glTranslatef((float) b1 + (float) mapcoord.centerX / 2.0F + 64.0F, (float) b2 + (float) mapcoord.centerZ / 2.0F + 64.0F, -0.02F);
			GL11.glRotatef((float) (mapcoord.iconRotation * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(4.0F, 4.0F, 3.0F);
			GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
			float f1 = (float) (mapcoord.iconSize % 4 + 0) / 4.0F;
			float f2 = (float) (mapcoord.iconSize / 4 + 0) / 4.0F;
			float f3 = (float) (mapcoord.iconSize % 4 + 1) / 4.0F;
			float f4 = (float) (mapcoord.iconSize / 4 + 1) / 4.0F;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(-1.0D, 1.0D, (double) ((float) k1 * 0.001F), (double) f1, (double) f2);
			tessellator.addVertexWithUV(1.0D, 1.0D, (double) ((float) k1 * 0.001F), (double) f3, (double) f2);
			tessellator.addVertexWithUV(1.0D, -1.0D, (double) ((float) k1 * 0.001F), (double) f3, (double) f4);
			tessellator.addVertexWithUV(-1.0D, -1.0D, (double) ((float) k1 * 0.001F), (double) f1, (double) f4);
			tessellator.draw();
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -0.04F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		*/
	}
}
