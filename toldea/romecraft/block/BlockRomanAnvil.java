package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class BlockRomanAnvil extends RomeCraftBlockContainer {
	public BlockRomanAnvil(int blockId, Material material) {
		super(blockId, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityRomanAnvil();
	}

	// This will tell minecraft not to render any side of our cube.
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	// And this tell it that you can see through this block, and neighbor blocks should be rendered.
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
