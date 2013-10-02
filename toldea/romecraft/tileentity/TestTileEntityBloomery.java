package toldea.romecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TestTileEntityBloomery extends TileEntity {
	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
	}
	
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
	
	/*
	@Override
    public Packet getDescriptionPacket() {
        return PacketHandler.getPacket((TileEntity)this);
    }*/
    //public void handlePacketData() {}
}