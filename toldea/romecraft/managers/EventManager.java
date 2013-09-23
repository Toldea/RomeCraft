package toldea.romecraft.managers;

import toldea.romecraft.ai.SquadManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

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
}