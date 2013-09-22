package toldea.romecraft.block;

import toldea.romecraft.tileentity.TileEntityMultiFurnaceCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockMultiFurnaceCore extends BlockContainer {
	public static final int META_ISACTIVE = 0x00000008;
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;

	public BlockMultiFurnaceCore(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMultiFurnaceCore();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking())
			return false;

		TileEntityMultiFurnaceCore tileEntity = (TileEntityMultiFurnaceCore) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			// Determine if the Multiblock is currently known to be valid
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					tileEntity.convertDummies();
					if (world.isRemote) {
						player.sendChatToPlayer(ChatMessageComponent.createFromText("Multi-Block Furnace Created!"));
					}
				}
			}

			// Check if the multi-block structure has been formed.
			if (tileEntity.getIsValid()) {
				if (world.isRemote) {
					player.sendChatToPlayer(ChatMessageComponent.createFromText("Herp Derp I'm a furnace :D"));
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
		
		TileEntityMultiFurnaceCore tileEntity = (TileEntityMultiFurnaceCore) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null) {
			// Determine if the Multiblock is currently known to be valid
			if (!tileEntity.getIsValid()) {
				if (tileEntity.checkIfProperlyFormed()) {
					tileEntity.convertDummies();
					if (world.isRemote) {
					}
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityMultiFurnaceCore tileEntity = (TileEntityMultiFurnaceCore) world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
			tileEntity.invalidateMultiblock();

		//dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}
}
