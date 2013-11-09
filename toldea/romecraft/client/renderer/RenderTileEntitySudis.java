package toldea.romecraft.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelSudis;

public class RenderTileEntitySudis extends TileEntitySpecialRenderer {
	public final ModelSudis modelSudis;
	public static final ResourceLocation textureSudis = new ResourceLocation("romecraft", "textures/entity/sudis.png");
	
	public RenderTileEntitySudis() {
		modelSudis = new ModelSudis();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		GL11.glPushMatrix();
		
		//GL11.glTranslated(d0 + .5, d1 + .5 + .3, d2 + .5);
		GL11.glTranslated(d0 + .25, d1 + .5 + .3, d2 + .5);
		
		bindTexture(textureSudis);
		modelSudis.render(.0625f);
		
		GL11.glTranslated(.50, .0, .0);
		modelSudis.render(.0625f);
		
		GL11.glPopMatrix();
	}

}
