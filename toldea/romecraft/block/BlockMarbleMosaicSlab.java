package toldea.romecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import toldea.romecraft.managers.BlockManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMarbleMosaicSlab extends RomeCraftBlockHalfSlab {

	/** The type of tree this slab came from. */
	public static final String[] slabType = new String[] { "marbleMosaic" };

	public BlockMarbleMosaicSlab(int par1, boolean par2) {
		super(par1, par2, Material.rock);

	}

	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return BlockManager.blockMarbleMosaic.getIcon(par1, par2 & 7);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, Random par2Random, int par3) {
		return BlockManager.blockMarbleMosaicHalfSlab.blockID;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(BlockManager.blockMarbleMosaicHalfSlab.blockID, 2, par1 & 7);
	}

	/**
	 * Returns the slab block name with step type.
	 */
	public String getFullSlabName(int par1) {
		if (par1 < 0 || par1 >= slabType.length) {
			par1 = 0;
		}

		return super.getUnlocalizedName() + "." + slabType[par1];
	}

	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		if (par1 != BlockManager.blockMarbleMosaicDoubleSlab.blockID) {
			for (int j = 0; j < slabType.length; ++j) {
				par3List.add(new ItemStack(par1, 1, j));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockSingleSlab(int blockID) {
		return blockID == BlockManager.blockMarbleMosaicHalfSlab.blockID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return isBlockSingleSlab(this.blockID) ? this.blockID : BlockManager.blockMarbleMosaicHalfSlab.blockID;
	}
}
