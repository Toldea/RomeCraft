package toldea.romecraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import toldea.romecraft.managers.CreativeTabsManager;

public abstract class RomeCraftBlockContainer extends BlockContainer {

	protected RomeCraftBlockContainer(int par1, Material par2Material) {
		super(par1, par2Material);
		setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world);
}
