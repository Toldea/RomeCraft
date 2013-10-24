package toldea.romecraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import toldea.romecraft.managers.CreativeTabsManager;

public abstract class RomeCraftBlockContainer extends BlockContainer {
	public RomeCraftBlockContainer(int blockId, Material material) {
		super(blockId, material);
		setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world);
}
