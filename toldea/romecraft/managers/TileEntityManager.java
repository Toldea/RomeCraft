package toldea.romecraft.managers;

import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityManager {
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRomanVillageForum.class, "tileEntityRomanVillageForum");
		GameRegistry.registerTileEntity(TileEntityBloomery.class, "tileEntityBloomery");
		GameRegistry.registerTileEntity(TileEntityBellows.class, "tileEntityBellows");
	}
}
