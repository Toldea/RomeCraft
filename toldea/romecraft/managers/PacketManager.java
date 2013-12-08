package toldea.romecraft.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

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
		
		int x, y, z, entityId, itemId, adjustment;
		TileEntity te;
		EntityPleb blacksmithPleb;

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
		case 2:
			entityId = reader.readInt();
			itemId = reader.readInt();
			adjustment = reader.readInt();
			blacksmithPleb = (EntityPleb) entityPlayer.worldObj.getEntityByID(entityId);
			blacksmithPleb.getBlacksmithOrders().adjustOrderQuantityForItemId(itemId, adjustment);
			break;
		case 3:
			x = reader.readInt();
			y = reader.readInt();
			z = reader.readInt();

			te = entityPlayer.worldObj.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityRomanAnvil) {
				TileEntityRomanAnvil anvil = (TileEntityRomanAnvil) te;
				anvil.hammerIron();
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
			System.err.append("RomeCraft: Failed to send PushBellows packet!");
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
			System.err.append("RomeCraft: Failed to send ApplyBellowsBoost packet!");
		}
	}
	
	/**
	 * Sends a packet to adjust a blacksmith's order quantity by a specific amount. If sendToServer is true this packet is sent to the server, else it is sent to all players.
	 */
	public static void sendAdjustBlacksmithOrderQuantityPacketToSide(int entityId, int itemId, int quantityAdjustment, boolean sendToServer) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		try {
			dataStream.writeByte((byte) 2);
			dataStream.writeInt(entityId);
			dataStream.writeInt(itemId);
			dataStream.writeInt(quantityAdjustment);

			if (sendToServer) {
				PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(CHANNEL, byteStream.toByteArray()));
			} else {
				PacketDispatcher.sendPacketToAllPlayers(PacketDispatcher.getPacket(CHANNEL, byteStream.toByteArray()));
			}
		} catch (IOException ex) {
			System.err.append("RomeCraft: Failed to send AdjustBlacksmithOrderQuantity packet!");
		}
	}
	
	public static void sendHammerAnvilPacketToAllPlayers(TileEntityRomanAnvil anvil) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		try {
			dataStream.writeByte((byte) 3);
			dataStream.writeInt(anvil.xCoord);
			dataStream.writeInt(anvil.yCoord);
			dataStream.writeInt(anvil.zCoord);
			
			PacketDispatcher.sendPacketToAllPlayers(PacketDispatcher.getPacket(CHANNEL, byteStream.toByteArray()));
		} catch (IOException ex) {
			System.err.append("RomeCraft: Failed to send HammerAnvil packet!");
		}
	}
}
