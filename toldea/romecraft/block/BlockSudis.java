package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.tileentity.TileEntitySudis;

public class BlockSudis extends RomeCraftBlockContainer {

	public BlockSudis(int blockId, Material material) {
		super(blockId, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySudis();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}
}
