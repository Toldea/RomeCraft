package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.client.model.ModelBellows;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBellows;

public class RenderTileEntityBellows extends TileEntitySpecialRenderer {
	private final ModelBellows modelBellows;
	private static final ResourceLocation bellowsTexture = new ResourceLocation("romecraft", "textures/entity/bellows.png");

	public RenderTileEntityBellows() {
		modelBellows = new ModelBellows();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
		if (!(tileEntity instanceof TileEntityBellows)) {
			return;
		} else {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) d, (float) d1, (float) d2);

			// renderCube((TileEntityBellows) tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
			// BlockManager.blockBellows);

			renderTileEntityBellows((TileEntityBellows) tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
					BlockManager.blockBellows);

			GL11.glPopMatrix();
		}
	}

	private void renderTileEntityBellows(TileEntityBellows bellows, World world, int x, int y, int z, Block block) {
		// Add lighting to the model.
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(world, x, y, z);
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		// Move and rotate the model to the right position and orientation.
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		GL11.glRotatef(180f, 0F, 0F, 1F);

		// Get the direction the block should be facing from the metadata and rotate the model appropriately.
		int dir = (bellows.getBlockMetadata() & BlockBloomery.MASK_DIR);
		switch (dir) {
		case 0:
			// East
			GL11.glRotatef(180f, 0F, 1F, 0F);
			break;
		case 1:
			// South
			GL11.glRotatef(-90f, 0F, 1F, 0F);
			break;
		case 2:
			// North
			GL11.glRotatef(90f, 0F, 1F, 0F);
			break;
		case 3:
			// West
			break;
		}

		// Bind the texture and render the model.
		bindTexture(bellowsTexture);
		modelBellows.render(bellows, 0f, 0f, 0f, 0f, 0f, .0625f);

		GL11.glPopMatrix();
	}

	private static final ResourceLocation derp = new ResourceLocation("romecraft", "textures/entity/derp.png");

	private void renderCube(TileEntityBellows bellows, World world, int x, int y, int z, Block block) {
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(world, x, y, z);
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		tessellator.startDrawingQuads();

		// Front
		tessellator.addVertexWithUV(0, 0, 0, 0, 0);
		tessellator.addVertexWithUV(0, 1, 0, 0, 0);
		tessellator.addVertexWithUV(1, 1, 0, 0, 0);
		tessellator.addVertexWithUV(1, 0, 0, 0, 0);

		// Back
		tessellator.addVertexWithUV(0, 0, 1, 0, 0);
		tessellator.addVertexWithUV(1, 0, 1, 0, 0);
		tessellator.addVertexWithUV(1, 1, 1, 0, 0);
		tessellator.addVertexWithUV(0, 1, 1, 0, 0);

		// Bottom
		tessellator.addVertexWithUV(0, 0, 0, 0, 0);
		tessellator.addVertexWithUV(1, 0, 0, 0, 0);
		tessellator.addVertexWithUV(1, 0, 1, 0, 0);
		tessellator.addVertexWithUV(0, 0, 1, 0, 0);

		// Top
		tessellator.addVertexWithUV(0, 1, 0, 0, 0);
		tessellator.addVertexWithUV(0, 1, 1, 0, 0);
		tessellator.addVertexWithUV(1, 1, 1, 0, 0);
		tessellator.addVertexWithUV(1, 1, 0, 0, 0);

		// Right
		tessellator.addVertexWithUV(0, 0, 0, 0, 0);
		tessellator.addVertexWithUV(0, 0, 1, 0, 0);
		tessellator.addVertexWithUV(0, 1, 1, 0, 0);
		tessellator.addVertexWithUV(0, 1, 0, 0, 0);

		// Left
		tessellator.addVertexWithUV(1, 0, 0, 0, 0);
		tessellator.addVertexWithUV(1, 1, 0, 0, 0);
		tessellator.addVertexWithUV(1, 1, 1, 0, 0);
		tessellator.addVertexWithUV(1, 0, 1, 0, 0);

		Minecraft.getMinecraft().renderEngine.bindTexture(derp);
		tessellator.draw();
	}
}
