package toldea.romecraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSudisGhostBlock extends Block {

	public BlockSudisGhostBlock(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		BlockSudis.setSudisBlockBoundsBasedOnState(this, blockAccess, x, y - 1, z, -1);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
		// Remove the 'official' block.
		world.destroyBlock(x, y - 1, z, true);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
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

	@SideOnly(Side.CLIENT)
	@Override
	// Prevent vanilla break particles from spawning.
	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return true;
	}
}
