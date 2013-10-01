package toldea.romecraft.managers;

import toldea.romecraft.client.renderer.RenderScutum;
import toldea.romecraft.entity.ai.SquadManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.world.WorldEvent;

public class EventManager {
	public static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new EventManager());		
	}
	
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties("SquadManagerProperties", SquadManager.instance);
		}
	}
	
	@ForgeSubscribe
	public void onWorldUnload(WorldEvent.Unload event) {
		SquadManager.onWorldUnload();
	}
}