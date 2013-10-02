package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class BlockBloomery extends RomeCraftBlockContainer {
	public BlockBloomery(int id, Material material) {
		super(id, material);
	}

	// This will tell minecraft not to render any side of our cube.
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	// And this tell it that you can see through this block, and neighbor blocks should be rendered.
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBloomery();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking()) {
			return false;
		}

		TileEntityBloomery tileEntity = (TileEntityBloomery) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					if (world.isRemote) {
						player.sendChatToPlayer(ChatMessageComponent.createFromText("Bloomery Created!"));
					}
				}
			}

			if (tileEntity.getIsValid()) {
				if (world.isRemote) {
					player.sendChatToPlayer(ChatMessageComponent.createFromText("I am the Bloomery " + (tileEntity.getIsMaster() ? " MASTER!" : " slave :(")));
				}
				// player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileEntityBloomery tileEntity = (TileEntityBloomery) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					if (world.isRemote) {
						((EntityPlayer) entity).sendChatToPlayer(ChatMessageComponent.createFromText("Bloomery Created on block placement :O!"));
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityBloomery tileEntity = (TileEntityBloomery) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			tileEntity.invalidateMultiblock();
		}

		// dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}
}
