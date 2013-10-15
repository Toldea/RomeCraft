package toldea.romecraft.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketManager implements IPacketHandler {
	public static final String CHANNEL = "ToldeaRC";

	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);

		EntityPlayer entityPlayer = (EntityPlayer) player;

		byte packetId = reader.readByte();
		
		int x, y, z;
		TileEntity te;

		switch (packetId) {
		case 0:
			x = reader.readInt();
			y = reader.readInt();
			z = reader.readInt();

			te = entityPlayer.worldObj.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityBellows) {
				TileEntityBellows bellows = (TileEntityBellows) te;
				bellows.pushBellows();
			}
			
			break;
		case 1:
			x = reader.readInt();
			y = reader.readInt();
			z = reader.readInt();

			te = entityPlayer.worldObj.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityBloomery) {
				TileEntityBloomery bloomery = (TileEntityBloomery) te;
				bloomery.applyBellowsBoost(entityPlayer.worldObj);
			}
			
			break;
		default:
			break;
		}
	}
	
	public static void sendPushBellowsPacketToAllPlayers(TileEntityBellows bellows) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		try {
			dataStream.writeByte((byte) 0);
			dataStream.writeInt(bellows.xCoord);
			dataStream.writeInt(bellows.yCoord);
			dataStream.writeInt(bellows.zCoord);

			PacketDispatcher.sendPacketToAllPlayers(PacketDispatcher.getPacket(CHANNEL, byteStream.toByteArray()));
		} catch (IOException ex) {
			System.err.append("Failed to send PushBellows packet");
		}
	}

	public static void sendApplyBellowsBoostPacketToServer(TileEntityBloomery bloomery) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		try {
			dataStream.writeByte((byte) 1);
			dataStream.writeInt(bloomery.xCoord);
			dataStream.writeInt(bloomery.yCoord);
			dataStream.writeInt(bloomery.zCoord);

			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(CHANNEL, byteStream.toByteArray()));
		} catch (IOException ex) {
			System.err.append("Failed to send ApplyBellowsBoost packet");
		}
	}
}
