package toldea.romecraft.managers;

import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityMarblePillar;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import toldea.romecraft.tileentity.TileEntitySudis;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityManager {
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRomanVillageForum.class, "tileEntityRomanVillageForum");
		GameRegistry.registerTileEntity(TileEntityBloomery.class, "tileEntityBloomery");
		GameRegistry.registerTileEntity(TileEntityBellows.class, "tileEntityBellows");
		GameRegistry.registerTileEntity(TileEntityRomanAnvil.class, "tileEntityRomanAnvil");
		GameRegistry.registerTileEntity(TileEntitySudis.class, "tileEntitySudis");
		GameRegistry.registerTileEntity(TileEntityMarblePillar.class, "tileEntityMarblePillar");
	}
}
