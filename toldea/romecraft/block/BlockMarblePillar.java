package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import toldea.romecraft.client.renderer.RenderBlockMarblePillar;

public class BlockMarblePillar extends RomeCraftBlock {

	public BlockMarblePillar(int id, Material material) {
		super(id, material);
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

	@Override
	public int getRenderType() {
		return RenderBlockMarblePillar.renderID;
	}
}
