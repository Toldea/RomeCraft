package toldea.romecraft.utility;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class AdjacentTileEntityCache<T extends TileEntity> {
	private final ChunkCoordinates[] adjacentTileEntityLocations;

	public AdjacentTileEntityCache() {
		adjacentTileEntityLocations = new ChunkCoordinates[4];
	}

	public T getAdjacentTileEntityOfTypeForDirection(Class<T> type, int direction, World world, int x, int y, int z) {
		if (adjacentTileEntityLocations[direction] == null) {
			TileEntity tileEntity = TileEntityHelper.getNeighbouringTileEntityForDirection(direction, world, x, y, z);
			if (tileEntity != null && type.isAssignableFrom(tileEntity.getClass())) {
				adjacentTileEntityLocations[direction] = new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
			}
		}
		if (adjacentTileEntityLocations[direction] != null) {
			ChunkCoordinates pos = adjacentTileEntityLocations[direction];
			TileEntity tileEntity = world.getBlockTileEntity(pos.posX, pos.posY, pos.posZ);
			if (tileEntity != null && type.isAssignableFrom(tileEntity.getClass())) {
				return (T) tileEntity;
			}
		}
		adjacentTileEntityLocations[direction] = null;
		return null;
	}
}
