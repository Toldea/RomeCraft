package toldea.romecraft.tileentity;

import toldea.romecraft.BlockManager;
import toldea.romecraft.block.BlockMultiFurnaceCore;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMultiFurnaceCore extends TileEntity {
	private boolean isValidMultiblock = false;

	public boolean getIsValid() {
		return isValidMultiblock;
	}

	public void invalidateMultiblock() {
		isValidMultiblock = false;

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		metadata = metadata & BlockMultiFurnaceCore.MASK_DIR;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);

		// furnaceBurnTime = 0;
		// currentItemBurnTime = 0;
		// furnaceCookTime = 0;

		revertDummies();
	}

	public boolean checkIfProperlyFormed() {
		int dir = (getBlockMetadata() & BlockMultiFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH || dir == BlockMultiFurnaceCore.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH) || (dir == BlockMultiFurnaceCore.META_DIR_SOUTH));

		/*
		 * FORWARD BACKWARD North: -z +z South: +z -z East: +x -x West: -x +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		for (int horiz = -1; horiz <= 1; horiz++) // Horizontal (X or Z)
		{
			for (int vert = -1; vert <= 1; vert++) // Vertical (Y)
			{
				for (int depth = 0; depth <= 2; depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					int blockId = worldObj.getBlockId(x, y, z);

					if (horiz == 0 && vert == -1) {
						if (depth == 0 || depth == 1) { // Bottom first and middle block must be air.
							if (blockId != 0) {
								return false;
							} else {
								continue;
							}
						}
					}
					if (horiz == 0 && vert == 0) {
						if (depth == 0) { // Looking at self, move on!
							continue;
						}
						if (depth == 1) { // Center must be air!
							if (blockId != 0)
								return false;
							else
								continue;
						}
					}
					if (horiz == 0 && vert == 1) {
						if (depth == 1) { // Top middle block must be air.
							if (blockId != 0) {
								return false;
							} else {
								continue;
							}
						}
					}

					if (blockId != Block.brick.blockID)
						return false;
				}
			}
		}
		return true;
	}

	public void convertDummies() {
		int dir = (getBlockMetadata() & BlockMultiFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH || dir == BlockMultiFurnaceCore.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH) || (dir == BlockMultiFurnaceCore.META_DIR_SOUTH));

		/*
		 * FORWARD BACKWARD North: -z +z South: +z -z East: +x -x West: -x +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		for (int horiz = -1; horiz <= 1; horiz++) // Horizontal (X or Z)
		{
			for (int vert = -1; vert <= 1; vert++) // Vertical (Y)
			{
				for (int depth = 0; depth <= 2; depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == -1 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == 1 && depth == 1)
						continue;

					worldObj.setBlock(x, y, z, BlockManager.multiFurnaceDummy.blockID);
					worldObj.markBlockForUpdate(x, y, z);
					TileEntityMultiFurnaceDummy dummyTE = (TileEntityMultiFurnaceDummy) worldObj.getBlockTileEntity(x, y, z);
					dummyTE.setCore(this);
				}
			}
		}

		isValidMultiblock = true;
	}

	private void revertDummies() {
		int dir = (getBlockMetadata() & BlockMultiFurnaceCore.MASK_DIR);

		int depthMultiplier = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH || dir == BlockMultiFurnaceCore.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH) || (dir == BlockMultiFurnaceCore.META_DIR_SOUTH));

		/*
		 * FORWARD BACKWARD North: -z +z South: +z -z East: +x -x West: -x +x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */

		for (int horiz = -1; horiz <= 1; horiz++) { // Horizontal (X or Z)
			for (int vert = -1; vert <= 1; vert++) { // Vertical (Y)
				for (int depth = 0; depth <= 2; depth++) { // Depth (Z or X)
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					int blockId = worldObj.getBlockId(x, y, z);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == -1 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == 1 && depth == 1)
						continue;

					if (blockId != BlockManager.multiFurnaceDummy.blockID)
						continue;

					worldObj.setBlock(x, y, z, Block.brick.blockID);
					worldObj.markBlockForUpdate(x, y, z);
				}
			}
		}

		isValidMultiblock = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		par1.setBoolean("isValidMultiblock", isValidMultiblock);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		isValidMultiblock = par1.getBoolean("isValidMultiblock");
	}
}
