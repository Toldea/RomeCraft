package toldea.romecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import toldea.romecraft.managers.CreativeTabsManager;

public class RomeCraftBlockStairs extends BlockStairs {

	public RomeCraftBlockStairs(int par1, Block par2Block, int par3) {
		super(par1, par2Block, par3);
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

}