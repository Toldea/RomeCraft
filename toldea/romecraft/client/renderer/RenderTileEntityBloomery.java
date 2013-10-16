package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.block.BlockHelper;
import toldea.romecraft.client.model.ModelBloomery;
import toldea.romecraft.client.model.ModelBloomeryBlock;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class RenderTileEntityBloomery extends TileEntitySpecialRenderer {
	private final ModelBloomery modelBloomery;
	private final ModelBloomeryBlock modelBloomeryBlock;

	private static final ResourceLocation bloomeryTexture = new ResourceLocation("romecraft", "textures/entity/bloomery.png");
	private static final ResourceLocation bloomeryBlockTexture = new ResourceLocation("romecraft", "textures/entity/bloomery_block.png");
	private static final ResourceLocation ironOreTexture = new ResourceLocation("textures/blocks/iron_ore.png");
	private static final ResourceLocation coalBlockTexture = new ResourceLocation("textures/blocks/coal_block.png");

	private static final float BLOOMERY_INSIDE_WIDTH = .375f;

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

					// Render the iron ore block if any are inside the bloomery.
					if (tileEntityBloomery.hasIronOre()) {
						renderOreInsideBloomery(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
								tileEntityBloomery.getSmeltingProgress(), tileEntityBloomery.getBellowsIdleFailureProgress());
					}
					// Render the fuel if there is any inside the bloomery.
					if (tileEntityBloomery.hasFuel()) {
						renderFuelInsideBloomery(tileEntityBloomery, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
					}
					// Render the smelted iron bloom if there is one inside the bloomery.
					if (tileEntityBloomery.hasIronBloom()) {
						//renderSmeltedIronBloomInsideBloomery(tileEntityBloomery, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
					}

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
		// Move and rotate the model to the right position and orientation.
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		GL11.glRotatef(180f, 0F, 0F, 1F);

		// Get the direction the block should be facing from the metadata and rotate the model appropriately.
		int dir = (tl.getBlockMetadata() & BlockHelper.MASK_DIR);
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
	public void renderBlockBloomery(TileEntityBloomery tl, World world, int x, int y, int z, Block block) {
		// Move and rotate the model to the right position and orientation.
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		GL11.glRotatef(180f, 0F, 0F, 1F);

		// Bind the texture and render the model.
		bindTexture(bloomeryBlockTexture);
		modelBloomeryBlock.renderBloomery(null, 0f, 0f, 0f, 0f, 0f, .0625f);

		GL11.glPopMatrix();
	}

	private void renderOreInsideBloomery(World world, int x, int y, int z, float smeltingProgress, float bellowsIdleFailureProgress) {
		float origin_x = .5f - BLOOMERY_INSIDE_WIDTH / 2f;
		float origin_y = .15f;
		float origin_z = origin_x;

		float width = BLOOMERY_INSIDE_WIDTH;
		float height = BLOOMERY_INSIDE_WIDTH;
		float length = BLOOMERY_INSIDE_WIDTH;

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		float tint = 1.0f;
		tint -= smeltingProgress;

		if (bellowsIdleFailureProgress > .8f) {
			bellowsIdleFailureProgress = (bellowsIdleFailureProgress - .8f) * 5f;
			tint += (bellowsIdleFailureProgress * (1 - tint));
		}

		RomeCraftRenderHelper.applyLightLevelsForRGB(1f, tint, tint, world, x, y, z, BlockManager.blockBloomery);
		RomeCraftRenderHelper.addVerticesForCube(origin_x, origin_y, origin_z, width, height, length);

		Minecraft.getMinecraft().renderEngine.bindTexture(ironOreTexture);

		tessellator.draw();

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderFuelInsideBloomery(TileEntityBloomery bloomery, World world, int x, int y, int z) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		int dir = (bloomery.getBlockMetadata() & BlockHelper.MASK_DIR);
		switch (dir) {
		case 0:
			GL11.glRotatef(90f, 0F, 1F, 0F); // East
			break;
		case 1:
			break; // South
		case 2:
			GL11.glRotatef(180f, 0F, 1F, 0F); // North
			break;
		case 3:
			GL11.glRotatef(-90f, 0F, 1F, 0F); // West
			break;
		}
		GL11.glTranslatef(-0.5F, -0.5f, -0.5F);

		float origin_x = .5f - BLOOMERY_INSIDE_WIDTH / 2f;
		float origin_y = .0f;
		float origin_z = .2f;

		float width = BLOOMERY_INSIDE_WIDTH;
		float height = .15f;
		float length = .48f;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		RomeCraftRenderHelper.addVerticesForCube(origin_x, origin_y, origin_z, width, height, length);
		Minecraft.getMinecraft().renderEngine.bindTexture(coalBlockTexture);

		tessellator.draw();

		GL11.glPopMatrix();
	}
}
