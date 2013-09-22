package toldea.romecraft.block;

import toldea.romecraft.tileentity.TileEntityBloomery;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBloomery extends BlockContainer {

	public BlockBloomery(int id, Material material) {
		super(id, material);
	}

	// This will tell minecraft not to render any side of our cube.
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	// And this tell it that you can see through this block, and neighbor blocks should be rendered.
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBloomery();
	}
}
