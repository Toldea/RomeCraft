package toldea.romecraft.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.CreativeTabsManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RomeCraftBlockWall extends BlockWall {

	public RomeCraftBlockWall(int par1, Block par2Block) {
		super(par1, par2Block);
		setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return BlockManager.blockMarbleMosaic.getBlockTextureFromSide(par1); // TODO: Make this dynamic
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
}
