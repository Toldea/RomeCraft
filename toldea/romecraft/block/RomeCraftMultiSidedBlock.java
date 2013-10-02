package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RomeCraftMultiSidedBlock extends RomeCraftBlock {
	@SideOnly(Side.CLIENT)
	private Icon block_side;
	@SideOnly(Side.CLIENT)
	private Icon block_top;
	@SideOnly(Side.CLIENT)
	private Icon block_bottom;

	public RomeCraftMultiSidedBlock(int id, Material material) {
		super(id, material);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int par1, int par2) {
		switch (par1) {
		case 0:
			return block_bottom;
		case 1:
			return block_top;
		default:
			return block_side;
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		this.block_side = par1IconRegister.registerIcon(this.getTextureName() + "_" + "side");
		this.block_top = par1IconRegister.registerIcon(this.getTextureName() + "_" + "top");
		this.block_bottom = par1IconRegister.registerIcon(this.getTextureName() + "_" + "bottom");
	}
}
