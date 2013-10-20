package toldea.romecraft;

import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.ConfigManager;
import toldea.romecraft.managers.CreativeTabsManager;
import toldea.romecraft.managers.EntityManager;
import toldea.romecraft.managers.EventManager;
import toldea.romecraft.managers.GuiManager;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.managers.PacketManager;
import toldea.romecraft.managers.RecipeManager;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.managers.TileEntityManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "RomeCraftID", name = "RomeCraft", version = "0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { PacketManager.CHANNEL }, packetHandler = PacketManager.class)
public class RomeCraft {
	// The instance of your mod that Forge uses.
	@Instance(value = "RomeCraftID")
	public static RomeCraft instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "toldea.romecraft.client.ClientProxy", serverSide = "toldea.romecraft.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigManager.loadConfig(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		CreativeTabsManager.registerCreativeTabs();

		ItemManager.registerItems();
		BlockManager.registerBlocks();

		EntityManager.registerEntities();
		TileEntityManager.registerTileEntities();

		RecipeManager.registerCraftingRecipes();

		EventManager.registerEvents();
		TickManager.registerTickHandler();

		GuiManager.registerGuiManager();

		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}