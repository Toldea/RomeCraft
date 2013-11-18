package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.client.renderer.RenderMarblePillar;
import toldea.romecraft.tileentity.TileEntityMarblePillar;

public class BlockMarblePillar extends RomeCraftBlockContainer {

	public BlockMarblePillar(int id, Material material) {
		super(id, material);
	}

	// This will tell minecraft not to render any side of our cube.
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
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

	@Override
	public int getRenderType() {
		return RenderMarblePillar.renderID;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMarblePillar();
	}
}
