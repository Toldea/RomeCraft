package toldea.romecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
		/*
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		/*
		World world = RomeCraft.proxy.getClientWorld();
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityBloomery) {
			TileEntityBloomery bloomery = (TileEntityBloomery) te;
			bloomery.handlePacketData();
		}*/
	}
	/*
	public static Packet getPacket(TileEntity tileEntity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
		} catch (IOException e) {
			// UNPOSSIBLE?
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "ToldeaRC";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
	*/
}
