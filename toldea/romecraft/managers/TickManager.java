package toldea.romecraft.managers;

import java.util.EnumSet;

import toldea.romecraft.romanvillage.RomanVillageCollection;

import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class TickManager implements ITickHandler {
	private static TickManager instance = new TickManager();

	public static RomanVillageCollection romanVillageCollection = null;

	private TickManager() {
	}

	public static void registerTickHandler() {
		TickRegistry.registerTickHandler(instance, Side.SERVER);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.WORLDLOAD))) {
			if (romanVillageCollection == null) {
				World world = null;
				for (int i = 0; i < tickData.length; i++)
					if (tickData[i] instanceof World)
						world = (World) tickData[i];
				if (world == null) {
					return;
				}

				MapStorage storage = world.perWorldStorage;
				romanVillageCollection = (RomanVillageCollection) storage.loadData(RomanVillageCollection.class, RomanVillageCollection.key);
				if (romanVillageCollection == null) {
					romanVillageCollection = new RomanVillageCollection(world);
					storage.setData(RomanVillageCollection.key, romanVillageCollection);
				}
				romanVillageCollection.linkWorld(world);
				
				//romanVillageCollection.markDirty();
				//System.out.println("herp derp roman village collection is dirty :D");
			}
		} else if (type.equals(EnumSet.of(TickType.WORLD))) {
			if (romanVillageCollection != null) {
				romanVillageCollection.tick();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLDLOAD, TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "romecraft_tickmanager";
	}
}
