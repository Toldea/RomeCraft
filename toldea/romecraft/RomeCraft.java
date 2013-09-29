package toldea.romecraft;

import toldea.romecraft.ai.Contubernium;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.managers.CreativeTabsManager;
import toldea.romecraft.managers.EntityManager;
import toldea.romecraft.managers.EventManager;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.managers.TickManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler; // used in 1.6.2
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "RomeCraftID", name = "RomeCraft", version = "0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "ToldeaRC" }, packetHandler = PacketHandler.class)
public class RomeCraft {
	// The instance of your mod that Forge uses.
	@Instance(value = "RomeCraftID")
	public static RomeCraft instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "toldea.romecraft.client.ClientProxy", serverSide = "toldea.romecraft.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		CreativeTabsManager.registerCreativeTabs();
		ItemManager.registerItems();
		BlockManager.registerBlocks();
		EntityManager.registerEntities();
		EventManager.registerEvents();
		TickManager.registerTickHandler();
		
		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}