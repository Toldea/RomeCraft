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
import toldea.romecraft.client.model.ModelBellows;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBellows;

public class RenderTileEntityBellows extends TileEntitySpecialRenderer {
	private final ModelBellows modelBellows;
	private static final ResourceLocation bellowsTexture = new ResourceLocation("romecraft", "textures/entity/bellows.png");
	private static final ResourceLocation bellowsBagTexture = new ResourceLocation("romecraft", "textures/entity/derp.png");

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

			renderTileEntityBellows((TileEntityBellows) tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
					BlockManager.blockBellows);

			renderBellowsBag((TileEntityBellows) tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
					BlockManager.blockBellows);

			GL11.glPopMatrix();
		}
	}

	private void renderTileEntityBellows(TileEntityBellows bellows, World world, int x, int y, int z, Block block) {
		// Move and rotate the model to the right position and orientation.
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		GL11.glRotatef(180f, 0F, 0F, 1F);

		// Get the direction the block should be facing from the metadata and rotate the model appropriately.
		int dir = (bellows.getBlockMetadata() & BlockHelper.MASK_DIR);
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

	private void renderBellowsBag(TileEntityBellows bellows, World world, int x, int y, int z, Block block) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5f, 0.5F);
		// Get the direction the block should be facing from the metadata and rotate the model appropriately.
		int dir = (bellows.getBlockMetadata() & BlockHelper.MASK_DIR);
		switch (dir) {
		case 0:
			// East
			GL11.glRotatef(90f, 0F, 1F, 0F);
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
			GL11.glRotatef(-90f, 0F, 1F, 0F);
			break;
		}
		GL11.glTranslatef(-0.5F, -0.5f, -0.5F);
		
		int numBellowsBagFolds = 1;
		float bellowsRotation = bellows.getBellowsRotation();
		float individualFoldRotation = TileEntityBellows.MAX_ROTATION / (bellowsRotation / numBellowsBagFolds);
		
		for (int i = 0; i < numBellowsBagFolds; i++) {
			renderSingleBellowsBagFold(individualFoldRotation);
		}

		GL11.glPopMatrix();
	}

	private void renderSingleBellowsBagFold(float bellowsRotation) {
		Tessellator tessellator = Tessellator.instance;
		
		if (bellowsRotation == 0f) {
			bellowsRotation = .001f;
		}

		float wf = 6f / 16f; // Width front
		wf /= 2f;
		float wb = 2f / 16f; // Width back
		wb /= 2f;
		float l = 11f / 16f; // Length
		l /= 2f;
		float yo = 4f / 16f; // Y Origin
		float yt = yo + ((6f / 16f) / bellowsRotation); // Y Top
		float lt = l - ((2f / 16f) / bellowsRotation); // Length Top

		tessellator.startDrawingQuads();

		// Front
		tessellator.addVertexWithUV(.5 - wf, yo, .5 - l, 1, 1);
		tessellator.addVertexWithUV(.5 - wf, yt, .5 - lt, 1, 0);
		tessellator.addVertexWithUV(.5 + wf, yt, .5 - lt, 0, 0);
		tessellator.addVertexWithUV(.5 + wf, yo, .5 - l, 0, 1);

		// Bottom
		tessellator.addVertexWithUV(.5 - wf, yo, .5 - l, 0, 0);
		tessellator.addVertexWithUV(.5 + wf, yo, .5 - l, 1, 0);
		tessellator.addVertexWithUV(.5 + wb, yo, .5 + l, 1, 1);
		tessellator.addVertexWithUV(.5 - wb, yo, .5 + l, 0, 1);

		// Top
		tessellator.addVertexWithUV(.5 - wf, yt, .5 - lt, 0, 0);
		tessellator.addVertexWithUV(.5 - wb, yo, .5 + l, 0, 1);
		tessellator.addVertexWithUV(.5 + wb, yo, .5 + l, 1, 1);
		tessellator.addVertexWithUV(.5 + wf, yt, .5 - lt, 1, 0);

		// Right
		tessellator.addVertexWithUV(.5 - wf, yo, .5 - l, 0, 1);
		tessellator.addVertexWithUV(.5 - wb, yo, .5 + l, 1, 0);
		tessellator.addVertexWithUV(.5 - wb, yo, .5 + l, 1, 0);
		tessellator.addVertexWithUV(.5 - wf, yt, .5 - lt, 0, 0);

		// Left
		tessellator.addVertexWithUV(.5 + wf, yo, .5 - l, 1, 1);
		tessellator.addVertexWithUV(.5 + wf, yt, .5 - lt, 1, 0);
		tessellator.addVertexWithUV(.5 + wb, yo, .5 + l, 0, 0);
		tessellator.addVertexWithUV(.5 + wb, yo, .5 + l, 0, 0);

		Minecraft.getMinecraft().renderEngine.bindTexture(bellowsBagTexture);
		tessellator.draw();
	}
}
