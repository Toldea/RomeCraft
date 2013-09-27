package toldea.romecraft.tileentity;

import toldea.romecraft.RomeCraft;
import toldea.romecraft.block.BlockRomanVillageForum;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.romanvillage.RomanVillage;
import toldea.romecraft.romanvillage.RomanVillageCollection;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;

public class TileEntityRomanVillageForum extends TileEntity {
	private boolean isValidMultiblock = false;

	public boolean getIsValid() {
		return isValidMultiblock;
	}

	public void invalidateMultiblock() {
		// Check if we were a valid multiblock. If so, try to get the accompanying village and flag it for annihilation by the RomanVillageCollection.
		if (isValidMultiblock) {
			RomanVillage romanVillage = TickManager.romanVillageCollection.getVillageAt(MathHelper.floor_double(xCoord), MathHelper.floor_double(yCoord), MathHelper.floor_double(zCoord));
			if (romanVillage != null) {
				romanVillage.flagForAnnihilation();
			}
		}
		
		isValidMultiblock = false;
		
		/*
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		metadata = metadata & BlockRomanVillageForum.MASK_DIR;
		
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
		*/
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);

		//revertDummies();
	}

	public boolean checkIfProperlyFormed() {
		for (int horiz = -1; horiz <= 1; horiz++) {
			for (int vert = -1; vert <= 0; vert++) {
				for (int depth = -1; depth <= 1; depth++) {
					int x = xCoord + horiz;
					int y = yCoord + vert;
					int z = zCoord + depth;

					int blockId = worldObj.getBlockId(x, y, z);

					// Check if we are level with the RomanVillageForum block.
					if (vert == 0) {
						if (horiz == 0 && depth == 0) {
							// We are looking at ourself, so skip.
							continue;
						} else {
							// Check if the rest of the blocks around us are air, return false if not.
							if (blockId != 0) {
								//System.out.println("Block Isn't Air! BlockId: " + blockId + " (" + x + ", " + y + ", " + z + ")");
								return false;
							} else {
								continue;
							}
						}
					} 
					// On the row below us make sure everything is made out of some sort of quartz block.
					else if (!isBlockIdQuartz(blockId)) {
						//System.out.println("Block Isn't Quartz! BlockId: " + blockId + " (" + x + ", " + y + ", " + z + ")");
						return false;
					}
				}
			}
		}
		if (!worldObj.isRemote) {
			TickManager.romanVillageCollection.createNewVillage(MathHelper.floor_double(xCoord), MathHelper.floor_double(yCoord), MathHelper.floor_double(zCoord));
		}
		
		isValidMultiblock = true;
		
		return true;
	}
	/*
	public void convertDummies() {
		int dir = (getBlockMetadata() & BlockRomanVillageForum.MASK_DIR);

		int depthMultiplier = ((dir == BlockRomanVillageForum.META_DIR_NORTH || dir == BlockRomanVillageForum.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockRomanVillageForum.META_DIR_NORTH) || (dir == BlockRomanVillageForum.META_DIR_SOUTH));

		for (int horiz = -1; horiz <= 1; horiz++) // Horizontal (X or Z)
		{
			for (int vert = -1; vert <= 1; vert++) // Vertical (Y)
			{
				for (int depth = 0; depth <= 2; depth++) // Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == -1 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == 1 && depth == 1)
						continue;

					worldObj.setBlock(x, y, z, BlockManager.multiFurnaceDummy.blockID);
					worldObj.markBlockForUpdate(x, y, z);
					TileEntityMultiFurnaceDummy dummyTE = (TileEntityMultiFurnaceDummy) worldObj.getBlockTileEntity(x, y, z);
					dummyTE.setCore(this);
				}
			}
		}

		isValidMultiblock = true;
	}

	private void revertDummies() {
		int dir = (getBlockMetadata() & BlockRomanVillageForum.MASK_DIR);

		int depthMultiplier = ((dir == BlockRomanVillageForum.META_DIR_NORTH || dir == BlockRomanVillageForum.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockRomanVillageForum.META_DIR_NORTH) || (dir == BlockRomanVillageForum.META_DIR_SOUTH));

		for (int horiz = -1; horiz <= 1; horiz++) { // Horizontal (X or Z)
			for (int vert = -1; vert <= 1; vert++) { // Vertical (Y)
				for (int depth = 0; depth <= 2; depth++) { // Depth (Z or X)
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);

					int blockId = worldObj.getBlockId(x, y, z);

					if (horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == -1 && (depth == 0 || depth == 1))
						continue;
					if (horiz == 0 && vert == 1 && depth == 1)
						continue;

					if (blockId != BlockManager.multiFurnaceDummy.blockID)
						continue;

					worldObj.setBlock(x, y, z, Block.brick.blockID);
					worldObj.markBlockForUpdate(x, y, z);
				}
			}
		}

		isValidMultiblock = false;
	}
*/
	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		par1.setBoolean("isValidMultiblock", isValidMultiblock);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		isValidMultiblock = par1.getBoolean("isValidMultiblock");
	}
	
	private static boolean isBlockIdQuartz(int blockId) {
		return (blockId == Block.blockNetherQuartz.blockID || blockId == Block.stairsNetherQuartz.blockID || blockId == Block.stairsNetherQuartz.blockID);
	}
	
	public void printVillageData(EntityPlayer player) {
		if (!isValidMultiblock) {
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Roman Village forum is not formed properly!"));
		} 
		
		RomanVillage romanVillage = TickManager.romanVillageCollection.getVillageAt(MathHelper.floor_double(xCoord), MathHelper.floor_double(yCoord), MathHelper.floor_double(zCoord));
		
		if (romanVillage == null) {
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Roman Village forum is null!"));
		} else {
			player.sendChatToPlayer(ChatMessageComponent.createFromText("-- Roman Village Data --"));
			player.sendChatToPlayer(ChatMessageComponent.createFromText(new String("Village Forum: (" + romanVillage.getVillageForumLocation().posX + ", " + romanVillage.getVillageForumLocation().posY + ", " + romanVillage.getVillageForumLocation().posZ + ")")));
			//player.sendChatToPlayer(ChatMessageComponent.createFromText(new String("Village Center: (" + romanVillage.getCenter().posX + ", " + romanVillage.getCenter().posY + ", " + romanVillage.getCenter().posZ + ")")));
			player.sendChatToPlayer(ChatMessageComponent.createFromText(new String("Number of doors: " + romanVillage.getNumVillageDoors())));
			player.sendChatToPlayer(ChatMessageComponent.createFromText(new String("Number of villagers: " + romanVillage.getNumVillagers())));
			player.sendChatToPlayer(ChatMessageComponent.createFromText(new String("Village radius: " + romanVillage.getVillageRadius())));
		}
	}
}
