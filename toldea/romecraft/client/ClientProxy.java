package toldea.romecraft.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import toldea.romecraft.CommonProxy;
import toldea.romecraft.ItemManager;
import toldea.romecraft.client.model.ModelLegionaryBiped;
import toldea.romecraft.client.renderer.CustomItemRenderer;
import toldea.romecraft.client.renderer.RenderEntityLegionary;
import toldea.romecraft.client.renderer.RenderEntityPilum;
import toldea.romecraft.client.renderer.RenderItemPilum;
import toldea.romecraft.client.renderer.TileEntityBloomeryRenderer;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityPilum;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBloomery.class, new TileEntityBloomeryRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLegionary.class, new RenderEntityLegionary(new ModelLegionaryBiped(), .03f));
		RenderingRegistry.registerEntityRenderingHandler(EntityPilum.class, new RenderEntityPilum());
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}