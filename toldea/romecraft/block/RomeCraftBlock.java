package toldea.romecraft.block;

import toldea.romecraft.managers.CreativeTabsManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class RomeCraftBlock extends Block {

	public RomeCraftBlock(int id, Material material) {
		super(id, material);
		setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}
}
