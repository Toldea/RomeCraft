package toldea.romecraft.managers;

import toldea.romecraft.RomeCraft;
import toldea.romecraft.client.renderer.RenderEntityLegionary;
import toldea.romecraft.client.renderer.RenderEntityPilum;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityPilum;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.entity.ai.Contubernium;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityEggInfo;
import net.minecraft.entity.EntityList;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class EntityManager {
	static int nextModEntityId = 0;
	static int nextSpawnEggId = 300;

	public static void registerEntities() {
		// Legionary
		EntityRegistry.registerModEntity(EntityLegionary.class, "entityLegionary", nextModEntityId++, RomeCraft.instance, 32, 5, true);
		registerEntityEgg(EntityLegionary.class, 0xff0000, 0xf0ff00);
		LanguageRegistry.instance().addStringLocalization("entity.entityLegionary.name", "Legionary");

		// Pleb
		EntityRegistry.registerModEntity(EntityPleb.class, "entityPleb", nextModEntityId++, RomeCraft.instance, 32, 5, true);
		registerEntityEgg(EntityPleb.class, 0xce272e, 0x780308);
		LanguageRegistry.instance().addStringLocalization("entity.entityPleb.name", "Pleb");

		// Pilum
		EntityRegistry.registerModEntity(EntityPilum.class, "entityPilum", nextModEntityId++, RomeCraft.instance, 32, 5, true);
	}

	private static void registerEntityEgg(Class<? extends Entity> entity, int primaryColor, int secondaryColor) {
		int id = getUniqueSpawnEggId();
		EntityList.IDtoClassMapping.put(id, entity);
		EntityList.entityEggs.put(id, new EntityEggInfo(id, primaryColor, secondaryColor));
	}

	private static int getUniqueSpawnEggId() {
		do {
			nextSpawnEggId++;
		} while (EntityList.getStringFromID(nextSpawnEggId) != null);
		return nextSpawnEggId++;
	}
}
