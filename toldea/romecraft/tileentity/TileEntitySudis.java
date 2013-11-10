package toldea.romecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySudis extends TileEntity {
	private int numberOfSudes = 1;

	public final boolean getHasMultipleSudes() {
		return (numberOfSudes > 1);
	}

	public final int getNumberOfSudes() {
		return numberOfSudes;
	}

	public void setNumberOfSudes(int numberOfSudes) {
		this.numberOfSudes = numberOfSudes;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		numberOfSudes = compound.getInteger("numberOfSudes");
	}

	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("numberOfSudes", numberOfSudes);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
}
