package toldea.romecraft.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import toldea.romecraft.client.renderer.RenderBlockSudis;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntitySudis;

public class BlockSudis extends RomeCraftBlockContainer {
	private static final float MODEL_SUDIS_HEIGHT = 1.75f;

	public BlockSudis(int blockId, Material material) {
		super(blockId, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySudis();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockSudis.renderID;
	}

	/*
	 * public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
	 * par5Entity.attackEntityFrom(DamageSource.cactus, 1.0F); }
	 */

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntitySudis tileEntitySudis = (TileEntitySudis) world.getBlockTileEntity(x, y, z);
		if (tileEntitySudis != null) {
			// Calculate the current and maximum number of sudes that can be placed inside this block.
			int currentNumSudes = tileEntitySudis.getNumberOfSudes();
			int maxNumSudes = 0;
			if (this.canConnectSudisTo(world, x, y, z - 1)) {
				maxNumSudes++;
			}
			if (this.canConnectSudisTo(world, x, y, z + 1)) {
				maxNumSudes++;
			}
			if (this.canConnectSudisTo(world, x - 1, y, z)) {
				maxNumSudes++;
			}
			if (this.canConnectSudisTo(world, x + 1, y, z)) {
				maxNumSudes++;
			}
			if (maxNumSudes <= 1) {
				maxNumSudes = 2;
			}

			if (currentNumSudes < maxNumSudes) {
				// If the sudis isn't a double sudes yet, check if the player is trying to 'activate' this sudis with another sudis.
				ItemStack itemstack = player.getCurrentEquippedItem();
				if (itemstack != null) {
					int id = itemstack.itemID;
					if (id == BlockManager.blockSudis.blockID) {
						tileEntitySudis.setNumberOfSudes(currentNumSudes + 1);
						if (!player.capabilities.isCreativeMode) {
							--itemstack.stackSize;
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.setBlock(x, y + 1, z, BlockManager.blockGhostBlock.blockID);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		// Remove the ghost block.
		world.setBlockToAir(x, y + 1, z);

		// Spawn the required number of extra sudes.
		TileEntitySudis tileEntitySudis = (TileEntitySudis) world.getBlockTileEntity(x, y, z);
		if (tileEntitySudis != null && tileEntitySudis.getHasMultipleSudes()) {
			for (int i = 0; i < tileEntitySudis.getNumberOfSudes() - 1; i++) {
				float spawnX = x + world.rand.nextFloat();
				float spawnY = y + world.rand.nextFloat();
				float spawnZ = z + world.rand.nextFloat();

				EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, new ItemStack(BlockManager.blockSudis, 1));

				float mult = 0.05F;

				droppedItem.motionX = (-0.5F + world.rand.nextFloat()) * mult;
				droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
				droppedItem.motionZ = (-0.5F + world.rand.nextFloat()) * mult;

				world.spawnEntityInWorld(droppedItem);
			}
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the mask.) Parameters: World, X, Y, Z, mask,
	 * list, colliding entity
	 */
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		TileEntitySudis tileEntitySudis = (TileEntitySudis) world.getBlockTileEntity(x, y, z);
		boolean flag = false, flag1 = false, flag2 = false, flag3 = false;
		if (tileEntitySudis != null && tileEntitySudis.getHasMultipleSudes()) {
			flag = canConnectSudisTo(world, x, y, z - 1);
			flag1 = canConnectSudisTo(world, x, y, z + 1);
			flag2 = canConnectSudisTo(world, x - 1, y, z);
			flag3 = canConnectSudisTo(world, x + 1, y, z);
		}
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag) {
			f2 = 0.0F;
		}
		if (flag1) {
			f3 = 1.0F;
		}
		if (flag || flag1) {
			setBlockBounds(f, 0, f2, f1, MODEL_SUDIS_HEIGHT, f3);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;

		if (flag2) {
			f = 0.0F;
		}
		if (flag3) {
			f1 = 1.0F;
		}
		if (flag2 || flag3 || !flag && !flag1) {
			setBlockBounds(f, 0, f2, f1, MODEL_SUDIS_HEIGHT, f3);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity);
		}
		if (flag) {
			f2 = 0.0F;
		}
		if (flag1) {
			f3 = 1.0F;
		}

		setBlockBounds(f, 0, f2, f1, MODEL_SUDIS_HEIGHT, f3);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		setSudisBlockBoundsBasedOnState(this, blockAccess, x, y, z, 0);
	}

	public static void setSudisBlockBoundsBasedOnState(Block block, IBlockAccess blockAccess, int x, int y, int z, int yOffset) {
		TileEntitySudis tileEntitySudis = (TileEntitySudis) blockAccess.getBlockTileEntity(x, y, z);
		boolean flag = false, flag1 = false, flag2 = false, flag3 = false;
		if (tileEntitySudis != null && tileEntitySudis.getHasMultipleSudes()) {
			flag = canConnectSudisTo(blockAccess, x, y, z - 1);
			flag1 = canConnectSudisTo(blockAccess, x, y, z + 1);
			flag2 = canConnectSudisTo(blockAccess, x - 1, y, z);
			flag3 = canConnectSudisTo(blockAccess, x + 1, y, z);
		}
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag) {
			f2 = 0.0F;
		}
		if (flag1) {
			f3 = 1.0F;
		}
		if (flag2) {
			f = 0.0F;
		}
		if (flag3) {
			f1 = 1.0F;
		}

		block.setBlockBounds(f, yOffset, f2, f1, MODEL_SUDIS_HEIGHT + yOffset, f3);
	}

	public static boolean canConnectSudisTo(IBlockAccess par1IBlockAccess, int x, int y, int z) {
		int l = par1IBlockAccess.getBlockId(x, y, z);

		if (l != BlockManager.blockSudis.blockID && l != BlockManager.blockSudis.blockID) {
			Block block = Block.blocksList[l];
			return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
		} else {
			return true;
		}
	}
}
