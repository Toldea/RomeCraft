package toldea.romecraft.managers;

import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityGag;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceCore;
import toldea.romecraft.tileentity.TileEntityMultiFurnaceDummy;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityManager {
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRomanVillageForum.class, "tileEntityRomanVillageForum");
		GameRegistry.registerTileEntity(TileEntityBloomery.class, "tileEntityBloomery");
		GameRegistry.registerTileEntity(TileEntityGag.class, "tileEntityGag");
		GameRegistry.registerTileEntity(TileEntityMultiFurnaceCore.class, "tileEntityMultiFurnaceCore");
		GameRegistry.registerTileEntity(TileEntityMultiFurnaceDummy.class, "tileEntityMultiFurnaceDummy");
	}
}
