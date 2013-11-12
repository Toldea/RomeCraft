package toldea.romecraft.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.block.BlockHelper;
import toldea.romecraft.block.BlockSudis;
import toldea.romecraft.client.model.ModelSudis;
import toldea.romecraft.tileentity.TileEntitySudis;

public class RenderTileEntitySudis extends TileEntitySpecialRenderer {
	private enum RenderLocation {
		CENTER, NORTH, SOUTH, WEST, EAST;
	}

	public final ModelSudis modelSudis;
	public static final ResourceLocation textureSudis = new ResourceLocation("romecraft", "textures/entity/sudis.png");

	public RenderTileEntitySudis() {
		modelSudis = new ModelSudis();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		GL11.glPushMatrix();

		GL11.glTranslated(d0, d1, d2);
		GL11.glTranslated(.5, .8, .5);

		TileEntitySudis tileEntitySudis = (TileEntitySudis) tileentity;
		if (tileEntitySudis.getHasMultipleSudes()) {
			renderMultipleSudes(tileEntitySudis.worldObj, tileEntitySudis.xCoord, tileEntitySudis.yCoord, tileEntitySudis.zCoord,
					tileEntitySudis.getNumberOfSudes());
		} else {
			renderSudis(RenderLocation.CENTER);
		}

		GL11.glPopMatrix();
	}

	private void renderSudis(RenderLocation renderLocation) {
		GL11.glPushMatrix();

		switch (renderLocation) {
		case CENTER:
			break;
		case NORTH:
			GL11.glTranslated(0, .0, -.25);
			break;
		case SOUTH:
			GL11.glTranslated(0, .0, .25);
			break;
		case WEST:
			GL11.glTranslated(-.25, .0, .0);
			break;
		case EAST:
			GL11.glTranslated(.25, .0, .0);
			break;
		default:
			break;
		}

		bindTexture(textureSudis);
		modelSudis.render(.0625f);

		GL11.glPopMatrix();
	}

	private void renderMultipleSudes(World world, int x, int y, int z, int numberOfSudes) {
		boolean flag = BlockSudis.canConnectSudisTo(world, x, y, z - 1);
		boolean flag1 = BlockSudis.canConnectSudisTo(world, x, y, z + 1);
		boolean flag2 = BlockSudis.canConnectSudisTo(world, x - 1, y, z);
		boolean flag3 = BlockSudis.canConnectSudisTo(world, x + 1, y, z);

		int connectCount = 0;
		if (flag) {
			connectCount++;
		}
		if (flag1) {
			connectCount++;
		}
		if (flag2) {
			connectCount++;
		}
		if (flag3) {
			connectCount++;
		}

		if (connectCount == 0) {
			// If none of the sides connect, draw in a direction depending on how the block was initially placed.
			int metadata = world.getBlockMetadata(x, y, z);
			int facing = metadata & BlockHelper.MASK_DIR;
			if (facing == BlockHelper.META_DIR_NORTH || facing == BlockHelper.META_DIR_SOUTH) {
				renderSudis(RenderLocation.WEST);
				renderSudis(RenderLocation.EAST);
			} else {
				renderSudis(RenderLocation.NORTH);
				renderSudis(RenderLocation.SOUTH);
			}
		} else {
			int sudesRemaining = numberOfSudes;
			if (flag) {
				renderSudis(RenderLocation.NORTH);
				sudesRemaining--;
				if (sudesRemaining > 0 && connectCount == 1) {
					renderSudis(RenderLocation.SOUTH);
					sudesRemaining--;
				}
			}
			if (sudesRemaining > 0 && flag1) {
				renderSudis(RenderLocation.SOUTH);
				sudesRemaining--;
				if (sudesRemaining > 0 && connectCount == 1) {
					renderSudis(RenderLocation.NORTH);
					sudesRemaining--;
				}
			}
			if (sudesRemaining > 0 && flag2) {
				renderSudis(RenderLocation.WEST);
				sudesRemaining--;
				if (sudesRemaining > 0 && connectCount == 1) {
					renderSudis(RenderLocation.EAST);
					sudesRemaining--;
				}
			}
			if (sudesRemaining > 0 && flag3) {
				renderSudis(RenderLocation.EAST);
				sudesRemaining--;
				if (sudesRemaining > 0 && connectCount == 1) {
					renderSudis(RenderLocation.WEST);
					sudesRemaining--;
				}
			}
		}
	}
}
