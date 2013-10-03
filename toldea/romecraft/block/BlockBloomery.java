package toldea.romecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class BlockBloomery extends RomeCraftBlockContainer {
	public static final int META_ISACTIVE = 0x00000008;
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;

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
					}
				}
			}

			if (tileEntity.getIsValid()) {
				if (world.isRemote) {
					player.sendChatToPlayer(ChatMessageComponent.createFromText("I am the Bloomery " + (tileEntity.getIsMaster() ? " master." : " slave.")));
				}
				// player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int metadata = 0;
		int facing = META_DIR_WEST;

		int dir = MathHelper.floor_double((double) (entity.rotationYaw * 4f / 360f) + 0.5) & 3;
		if (dir == 0)
			facing = META_DIR_NORTH;
		if (dir == 1)
			facing = META_DIR_EAST;
		if (dir == 2)
			facing = META_DIR_SOUTH;
		if (dir == 3)
			facing = META_DIR_WEST;

		metadata |= facing;
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);

		TileEntityBloomery tileEntity = (TileEntityBloomery) world.getBlockTileEntity(x, y, z);

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
		TileEntityBloomery tileEntity = (TileEntityBloomery) world.getBlockTileEntity(x, y, z);
		System.out.println("breakBlock");
		if (tileEntity != null && tileEntity.getIsValid()) {
			System.out.println("tileEntity and multiblock valid");
			// Get any items in the Bloomery block and drop them.
			for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
				System.out.println("looping through inventory slots..");
				ItemStack stack = tileEntity.getStackInSlotOnClosing(i);

				if (stack != null) {
					System.out.println("preparing to drop stack for slot #" + i);
					float spawnX = x + world.rand.nextFloat();
					float spawnY = y + world.rand.nextFloat();
					float spawnZ = z + world.rand.nextFloat();

					EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);

					float mult = 0.05F;

					droppedItem.motionX = (-0.5F + world.rand.nextFloat()) * mult;
					droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
					droppedItem.motionZ = (-0.5F + world.rand.nextFloat()) * mult;

					world.spawnEntityInWorld(droppedItem);
				}
			}

			tileEntity.invalidateMultiblock();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}
}
