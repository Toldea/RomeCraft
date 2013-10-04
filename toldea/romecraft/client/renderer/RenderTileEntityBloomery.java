package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.client.model.ModelBloomery;
import toldea.romecraft.client.model.ModelBloomeryBlock;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class RenderTileEntityBloomery extends TileEntitySpecialRenderer {
	private final ModelBloomery modelBloomery;
	private final ModelBloomeryBlock modelBloomeryBlock;
	private static final ResourceLocation bloomeryTexture = new ResourceLocation("romecraft", "textures/entity/bloomery.png");
	private static final ResourceLocation bloomeryBlockTexture = new ResourceLocation("romecraft", "textures/entity/bloomery_block.png");

	public RenderTileEntityBloomery() {
		modelBloomery = new ModelBloomery();
		modelBloomeryBlock = new ModelBloomeryBlock();
	}

	// This method is called when minecraft renders a tile entity
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
		if (!(tileEntity instanceof TileEntityBloomery)) {
			return;
		} else {
			TileEntityBloomery tileEntityBloomery = (TileEntityBloomery) tileEntity;

			if (tileEntityBloomery.getIsValid()) {
				if (tileEntityBloomery.getIsMaster()) {
					// Only render for a valid Bloomery, and only for the master block (as to not render twice).
					GL11.glPushMatrix();
					// Translate the matrix towards our location.
					GL11.glTranslatef((float) d, (float) d1, (float) d2);
					renderTileEntityBloomery(tileEntityBloomery, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
							BlockManager.blockBloomery);
					GL11.glPopMatrix();
				}
			} else {
				// If the model isn't valid yet, render a 'block version'.
				GL11.glPushMatrix();
				// Translate the matrix towards our location.
				GL11.glTranslatef((float) d, (float) d1, (float) d2);
				renderBlockBloomery(tileEntityBloomery, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
						BlockManager.blockBloomery);
				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Renders the fully formed Bloomery model.
	 */
	public void renderTileEntityBloomery(TileEntityBloomery tl, World world, int x, int y, int z, Block block) {
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
		int dir = (tl.getBlockMetadata() & BlockBloomery.MASK_DIR);
		switch (dir) {
		case 0:
			// East
			GL11.glRotatef(-90f, 0F, 1F, 0F);
			break;
		case 1:
			// South
			break;
		case 2:
			// North
			GL11.glRotatef(180f, 0F, 1F, 0F);
			break;
		case 3:
			// West
			GL11.glRotatef(90f, 0F, 1F, 0F);
			break;
		}

		// Bind the texture and render the model.
		bindTexture(bloomeryTexture);
		modelBloomery.renderBloomery(null, 0f, 0f, 0f, 0f, 0f, .0625f);

		GL11.glPopMatrix();
	}

	/**
	 * Renders the incomplete 'block form' of the Bloomery.
	 */
	public void renderBlockBloomery(TileEntityBloomery tl, World world, int i, int j, int k, Block block) {
		// Add lighting to the model.
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(world, i, j, k);
		int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		// Move and rotate the model to the right position and orientation.
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		GL11.glRotatef(180f, 0F, 0F, 1F);

		// Bind the texture and render the model.
		bindTexture(bloomeryBlockTexture);
		modelBloomeryBlock.renderBloomery(null, 0f, 0f, 0f, 0f, 0f, .0625f);

		GL11.glPopMatrix();
	}
}
