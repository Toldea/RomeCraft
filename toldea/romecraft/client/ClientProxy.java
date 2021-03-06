package toldea.romecraft.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import toldea.romecraft.CommonProxy;
import toldea.romecraft.client.model.ModelLegionaryBiped;
import toldea.romecraft.client.renderer.RenderBlockBellows;
import toldea.romecraft.client.renderer.RenderBlockRomanAnvil;
import toldea.romecraft.client.renderer.RenderBlockSudis;
import toldea.romecraft.client.renderer.RenderEntityLegionary;
import toldea.romecraft.client.renderer.RenderEntityPilum;
import toldea.romecraft.client.renderer.RenderEntityPleb;
import toldea.romecraft.client.renderer.RenderItemGladius;
import toldea.romecraft.client.renderer.RenderItemPilum;
import toldea.romecraft.client.renderer.RenderMarblePillar;
import toldea.romecraft.client.renderer.RenderScutum;
import toldea.romecraft.client.renderer.RenderTileEntityBellows;
import toldea.romecraft.client.renderer.RenderTileEntityBloomery;
import toldea.romecraft.client.renderer.RenderTileEntityRomanAnvil;
import toldea.romecraft.client.renderer.RenderTileEntitySudis;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityPilum;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.managers.ItemManager;
import toldea.romecraft.tileentity.TileEntityBellows;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityMarblePillar;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;
import toldea.romecraft.tileentity.TileEntitySudis;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		RenderMarblePillar renderMarblePillar = new RenderMarblePillar();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBloomery.class, new RenderTileEntityBloomery());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBellows.class, new RenderTileEntityBellows());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRomanAnvil.class, new RenderTileEntityRomanAnvil());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySudis.class, new RenderTileEntitySudis());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMarblePillar.class, renderMarblePillar);

		MinecraftForgeClient.registerItemRenderer(ItemManager.itemScutum.itemID, new RenderScutum());
		MinecraftForgeClient.registerItemRenderer(ItemManager.itemGladius.itemID, new RenderItemGladius());
		MinecraftForgeClient.registerItemRenderer(ItemManager.itemPilum.itemID, new RenderItemPilum());

		RenderingRegistry.registerBlockHandler(new RenderBlockBellows());
		RenderingRegistry.registerBlockHandler(new RenderBlockRomanAnvil());
		RenderingRegistry.registerBlockHandler(new RenderBlockSudis());
		RenderingRegistry.registerBlockHandler(renderMarblePillar);

		RenderingRegistry.registerEntityRenderingHandler(EntityLegionary.class, new RenderEntityLegionary(new ModelLegionaryBiped(), .03f));
		RenderingRegistry.registerEntityRenderingHandler(EntityPilum.class, new RenderEntityPilum());
		RenderingRegistry.registerEntityRenderingHandler(EntityPleb.class, new RenderEntityPleb(new ModelBiped(), .03f));
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
}