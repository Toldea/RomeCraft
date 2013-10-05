package toldea.romecraft.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import toldea.romecraft.CommonProxy;
import toldea.romecraft.client.model.ModelLegionaryBiped;
import toldea.romecraft.client.renderer.RenderEntityLegionary;
import toldea.romecraft.client.renderer.RenderEntityPilum;
import toldea.romecraft.client.renderer.RenderEntityPleb;
import toldea.romecraft.client.renderer.RenderScutum;
import toldea.romecraft.client.renderer.RenderTileEntityBellows;
import toldea.romecraft.client.renderer.RenderTileEntityBloomery;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityPilum;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBloomery.class, new RenderTileEntityBloomery());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBellows.class, new RenderTileEntityBellows());
		
		MinecraftForgeClient.registerItemRenderer(ItemManager.itemScutum.itemID, new RenderScutum());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLegionary.class, new RenderEntityLegionary(new ModelLegionaryBiped(), .03f));
		RenderingRegistry.registerEntityRenderingHandler(EntityPilum.class, new RenderEntityPilum());
		RenderingRegistry.registerEntityRenderingHandler(EntityPleb.class, new RenderEntityPleb(new ModelBiped(), .03f));
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}