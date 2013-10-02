package toldea.romecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TestTileEntityMultiFurnaceDummy extends TileEntity {
	TestTileEntityMultiFurnaceCore tileEntityCore;
	int coreX;
	int coreY;
	int coreZ;

	public TestTileEntityMultiFurnaceDummy() {
	}

	public void setCore(TestTileEntityMultiFurnaceCore core) {
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileEntityCore = core;
	}

	public TestTileEntityMultiFurnaceCore getCore() {
		if (tileEntityCore == null)
			tileEntityCore = (TestTileEntityMultiFurnaceCore) worldObj.getBlockTileEntity(coreX, coreY, coreZ);

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
