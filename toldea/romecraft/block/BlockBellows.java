package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class BlockBellows extends RomeCraftBlockContainer {
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;

	public BlockBellows(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBellows();
	}

	// This will tell minecraft not to render any side of our cube.
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	// And this tell it that you can see through this block, and neighbor blocks should be rendered.
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking()) {
			return false;
		}

		// Check if the player is holding a bellows. If so, rotate the bellows instead of 'pushing'/using it.
		ItemStack item = player.getCurrentEquippedItem();
		if (item != null && item.itemID == BlockManager.blockBellows.blockID) {
			this.rotateClockwise(world, x, y, z);
			return true;
		}

		if (world.isRemote) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileEntityBellows) {
				((TileEntityBellows) te).pushBellows();
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		boolean foundAdjacentBloomery = false;

		if (!entity.isSneaking()) {
			// Try and let the bellows face any adjacent valid bloomery multiblock structure.
			TileEntityBellows bellows = (TileEntityBellows) world.getBlockTileEntity(x, y, z);
			TileEntityBloomery bloomery = bellows.faceAdjacentBloomery();
			if (bloomery != null && bloomery.getIsMaster()) {
				foundAdjacentBloomery = true;
			}
		}

		// If we haven't found any adjacent bloomery, face the direction the user faced.
		if (!foundAdjacentBloomery) {
			int metadata = 0;
			int facing = META_DIR_WEST;

			int dir = MathHelper.floor_double((double) (entity.rotationYaw * 4f / 360f) + 0.5) & 3;
			if (dir == 0) {
				facing = META_DIR_NORTH;
			}
			if (dir == 1) {
				facing = META_DIR_EAST;
			}
			if (dir == 2) {
				facing = META_DIR_SOUTH;
			}
			if (dir == 3) {
				facing = META_DIR_WEST;
			}

			metadata |= facing;
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
		}
	}

	public static byte getOppositeDirectionByteForInt(int direction) {
		switch (direction) {
		case 0:
			return META_DIR_WEST;
		case 1:
			return META_DIR_NORTH;
		case 2:
			return META_DIR_SOUTH;
		case 3:
			return META_DIR_EAST;
		default:
			return 0;
		}
	}

	private void rotateClockwise(World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null) {
			return;
		}
		int dir = (te.getBlockMetadata() & BlockBloomery.MASK_DIR);
		switch (dir) {
		case 0:
			world.setBlockMetadataWithNotify(x, y, z, META_DIR_NORTH, 2); // West -> North
			break;
		case 1:
			world.setBlockMetadataWithNotify(x, y, z, META_DIR_EAST, 2); // North -> East
			break;
		case 2:
			world.setBlockMetadataWithNotify(x, y, z, META_DIR_WEST, 2); // South -> West
			break;
		case 3:
			world.setBlockMetadataWithNotify(x, y, z, META_DIR_SOUTH, 2); // East -> South
			break;
		}
	}
}
