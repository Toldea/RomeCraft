package toldea.romecraft.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelRomanAnvil;

public class RenderTileEntityRomanAnvil extends TileEntitySpecialRenderer {
	private final ModelRomanAnvil modelRomanAnvil;
	private static final ResourceLocation romanAnvilTexture = new ResourceLocation("romecraft", "textures/entity/romananvil.png");

	public RenderTileEntityRomanAnvil() {
		modelRomanAnvil = new ModelRomanAnvil();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d0 + .5f, (float) d1 + .5f, (float) d2 + .5f);
		GL11.glRotatef(180f, 0F, 0F, 1F);
		
		bindTexture(romanAnvilTexture);
		modelRomanAnvil.renderAnvil(.0625f);
		
		GL11.glPopMatrix();
	}

}
