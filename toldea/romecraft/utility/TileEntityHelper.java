package toldea.romecraft.utility;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityHelper {
	private TileEntityHelper() {
	}

	/**
	 * Returns the tile entity located one block away in the specified direction.
	 */
	public static TileEntity getNeighbouringTileEntityForDirection(int direction, World world, int x, int y, int z) {
		int dx = 0;
		int dz = 0;

		switch (direction) {
		case 0:
			dx = 1;
			break;
		case 1:
			dz = 1;
			break;
		case 2:
			dz = -1;
			break;
		case 3:
			dx = -1;
			break;
		}

		if (dx == 0 && dz == 0) {
			return null;
		} else {
			return world.getBlockTileEntity(x + dx, y, z + dz);
		}
	}
}
