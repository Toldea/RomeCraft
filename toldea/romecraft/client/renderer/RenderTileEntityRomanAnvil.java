package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelRomanAnvil;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class RenderTileEntityRomanAnvil extends TileEntitySpecialRenderer {
	private final ModelRomanAnvil modelRomanAnvil;
	private static final ResourceLocation romanAnvilTexture = new ResourceLocation("romecraft", "textures/entity/romananvil.png");
	private static final ResourceLocation ironBloomTexture = new ResourceLocation("romecraft", "textures/items/ironbloom.png");

	public RenderTileEntityRomanAnvil() {
		modelRomanAnvil = new ModelRomanAnvil();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
		if (tileEntity instanceof TileEntityRomanAnvil) {
			TileEntityRomanAnvil romanAnvil = (TileEntityRomanAnvil)tileEntity;
			
			GL11.glPushMatrix();
			GL11.glTranslatef((float) d0, (float) d1, (float) d2);
			
			if (romanAnvil.hasIronBloom()) {
				renderIronBloom();
			}
			
			GL11.glTranslatef(.5f, .5f, .5f);
			GL11.glRotatef(180f, 0F, 0F, 1F);
			
			bindTexture(romanAnvilTexture);
			modelRomanAnvil.renderAnvil(.0625f);
			
			GL11.glPopMatrix();
		}
	}

	private void renderIronBloom() {
		GL11.glDisable(GL11.GL_LIGHTING);
		
		float width = .5f; 
		float height = .5f;

		float origin_x = .5f - width / 2f;
		float origin_y = .01f;
		float origin_z = origin_x;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		RomeCraftRenderHelper.addVerticesForPlaneWithDirection(4, origin_x, origin_y + height, origin_z, width, height);
		Minecraft.getMinecraft().renderEngine.bindTexture(ironBloomTexture);
		tessellator.draw();

		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
