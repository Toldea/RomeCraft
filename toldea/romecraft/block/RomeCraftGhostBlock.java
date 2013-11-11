package toldea.romecraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class RomeCraftGhostBlock extends Block {

	public RomeCraftGhostBlock(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
		// super.breakBlock(world, x, y, z, blockID, metadata);
		// Remove the 'official' block.
		world.destroyBlock(x, y - 1, z, true);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}
}
