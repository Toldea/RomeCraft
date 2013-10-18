package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRomanVillageForum extends RomeCraftBlockContainer {
	@SideOnly(Side.CLIENT)
	private Icon block_side;
	@SideOnly(Side.CLIENT)
	private Icon block_top;
	@SideOnly(Side.CLIENT)
	private Icon block_bottom;

	public BlockRomanVillageForum(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityRomanVillageForum();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking())
			return false;

		TileEntityRomanVillageForum tileEntity = (TileEntityRomanVillageForum) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					if (world.isRemote) {
						player.sendChatToPlayer(ChatMessageComponent.createFromText("Roman Village Forum MultiBlock Created!"));
					}
				}
			}

			if (tileEntity.getIsValid()) {
				if (world.isRemote) {
					tileEntity.printVillageData(player);
				}
				// player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int metadata = 0;
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
		TileEntityRomanVillageForum tileEntity = (TileEntityRomanVillageForum) world.getBlockTileEntity(x, y, z);
		if (tileEntity != null) {
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					if (world.isRemote) {
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityRomanVillageForum) {
			TileEntityRomanVillageForum village = (TileEntityRomanVillageForum) tileEntity;
			village.invalidateMultiblock();
		}
		// dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
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