package toldea.romecraft.managers;

import toldea.romecraft.tileentity.TestTileEntityBloomery;
import toldea.romecraft.tileentity.TestTileEntityGag;
import toldea.romecraft.tileentity.TestTileEntityMultiFurnaceCore;
import toldea.romecraft.tileentity.TestTileEntityMultiFurnaceDummy;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityManager {
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRomanVillageForum.class, "tileEntityRomanVillageForum");
		GameRegistry.registerTileEntity(TileEntityBloomery.class, "tileEntityBloomery");
		
		GameRegistry.registerTileEntity(TestTileEntityBloomery.class, "testTileEntityBloomery");
		GameRegistry.registerTileEntity(TestTileEntityGag.class, "testTileEntityGag");
		GameRegistry.registerTileEntity(TestTileEntityMultiFurnaceCore.class, "testTileEntityMultiFurnaceCore");
		GameRegistry.registerTileEntity(TestTileEntityMultiFurnaceDummy.class, "testTileEntityMultiFurnaceDummy");
	}
}
