package toldea.romecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMultiFurnaceDummy extends TileEntity {
	TileEntityMultiFurnaceCore tileEntityCore;
	int coreX;
	int coreY;
	int coreZ;

	public TileEntityMultiFurnaceDummy() {
	}

	public void setCore(TileEntityMultiFurnaceCore core) {
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileEntityCore = core;
	}

	public TileEntityMultiFurnaceCore getCore() {
		if (tileEntityCore == null)
			tileEntityCore = (TileEntityMultiFurnaceCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);

		return tileEntityCore;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		coreX = tagCompound.getInteger("CoreX");
		coreY = tagCompound.getInteger("CoreY");
		coreZ = tagCompound.getInteger("CoreZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
	}
}
