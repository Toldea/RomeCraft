package toldea.romecraft.managers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import toldea.romecraft.RomeCraft;
import toldea.romecraft.client.gui.GuiBlacksmith;
import toldea.romecraft.client.gui.GuiForum;
import toldea.romecraft.tileentity.TileEntityRomanVillageForum;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiManager implements IGuiHandler {
	public static GuiManager instance = new GuiManager();
	
	public static final int romanVillageForumGuiId = 0;
	public static final int blacksmithGuiId = 1;

	private GuiManager() {}
	
	public static void registerGuiManager() {
		NetworkRegistry.instance().registerGuiHandler(RomeCraft.instance, GuiManager.instance);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case romanVillageForumGuiId:
			return null;
		case blacksmithGuiId:
			return null;
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case romanVillageForumGuiId:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityRomanVillageForum) {
				return new GuiForum((TileEntityRomanVillageForum) tileEntity);
			}
		case blacksmithGuiId:
			return new GuiBlacksmith();
		default:
			return null;
		}
	}

}
