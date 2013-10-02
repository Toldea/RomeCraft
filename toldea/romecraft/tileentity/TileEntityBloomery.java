package toldea.romecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import toldea.romecraft.managers.BlockManager;

public class TileEntityBloomery extends TileEntity {
	private boolean isValidBloomeryMultiblock = false;
	private boolean isMaster = false;

	public boolean getIsValid() {
		return isValidBloomeryMultiblock;
	}

	public boolean getIsMaster() {
		return isMaster;
	}

	public void invalidateMultiblock() {
		isValidBloomeryMultiblock = false;

		// int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		// metadata = metadata & TestBlockMultiFurnaceCore.MASK_DIR;
		// worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
		// worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);

		// furnaceBurnTime = 0;
		// currentItemBurnTime = 0;
		// furnaceCookTime = 0;

		updateValidityOther(false);
	}

	public boolean checkIfProperlyFormed() {
		boolean isSlave = false;

		for (int height = -2; height < 3; height++) {
			if (height == 0) {
				continue;
			}
			int x = xCoord;
			int y = yCoord + height;
			int z = zCoord;

			int blockId = worldObj.getBlockId(x, y, z);
			boolean blockIsAir = (blockId == 0);

			if (height == -2) {
				// Make sure there isn't a bloomery block 2 blocks under us, to prevent 'bloomery looping'.
				if (!blockIsAir && blockId == BlockManager.blockBloomery.blockID) {
					return false;
				}
			} else if (height == -1) {
				// Check if there is another Bloomery block underneath us. If so we act like the 'slave' and will check if there is an air space above us.
				if (!blockIsAir && blockId == BlockManager.blockBloomery.blockID) {
					isSlave = true;
				}
			} else if (height == 1) {
				if (!isSlave) {
					// If we are not a slave, check if we have another Bloomery block above us.
					if (blockIsAir || blockId != BlockManager.blockBloomery.blockID) {
						return false;
					}
				} else {
					// Else if we are a slave, make sure there is air above us.
					if (!blockIsAir) {
						return false;
					}
				}
			} else if (height == 2 && !isSlave) {
				// Finally on the top level, if we are not the slave make sure there is an air block above us.
				if (!blockIsAir) {
					return false;
				}
			}
		}

		// If we survived all these checks, we are properly formed.
		isValidBloomeryMultiblock = true;
		isMaster = !isSlave;
		updateValidityOther(true);
		return true;
	}

	public void setIsProperlyFormed(boolean isProperlyFormed, boolean isMaster) {
		this.isValidBloomeryMultiblock = isProperlyFormed;
		this.isMaster = isMaster;
		// Notify this block so it knows if it needs to start or stop rendering.
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void updateValidityOther(boolean isValid) {
		TileEntityBloomery otherTileEntity = (TileEntityBloomery) worldObj.getBlockTileEntity(xCoord, yCoord + (isMaster ? 1 : -1), zCoord);
		if (otherTileEntity != null) {
			otherTileEntity.setIsProperlyFormed(isValid, !isMaster);
		}
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		par1.setBoolean("isValid", isValidBloomeryMultiblock);
		par1.setBoolean("isMaster", isMaster);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		isValidBloomeryMultiblock = par1.getBoolean("isValid");
		isMaster = par1.getBoolean("isMaster");
	}
}
