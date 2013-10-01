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
	static int startEntityId = 300;
	static int startEntityModId = 0;

	public static void registerEntities() {
		// Legionary
		EntityRegistry.registerGlobalEntityID(EntityLegionary.class, "entityLegionary", EntityRegistry.findGlobalUniqueEntityId());
		registerEntityEgg(EntityLegionary.class, 0xff0000, 0xf0ff00);
		LanguageRegistry.instance().addStringLocalization("entity.entityLegionary.name", "Legionary");
		// Pleb
		EntityRegistry.registerGlobalEntityID(EntityPleb.class, "entityPleb", EntityRegistry.findGlobalUniqueEntityId());
		//EntityRegistry.registerModEntity(EntityPleb.class, "entityPleb", EntityRegistry.findGlobalUniqueEntityId(), RomeCraft.instance, 32, 5, true);
		registerEntityEgg(EntityPleb.class, 0x00ff00, 0x0f00ff);
		LanguageRegistry.instance().addStringLocalization("entity.entityPleb.name", "Pleb");
		// Pilum
		EntityRegistry.registerModEntity(EntityPilum.class, "entityPilum", EntityRegistry.findGlobalUniqueEntityId(), RomeCraft.instance, 32, 5, true);
	}

	public static int getUniqueEntityId() {
		do {
			startEntityId++;
		} while (EntityList.getStringFromID(startEntityId) != null);
		return startEntityId++;
	}

	public static void registerEntityEgg(Class<? extends Entity> entity, int primaryColor, int secondaryColor) {
		int id = getUniqueEntityId();
		EntityList.IDtoClassMapping.put(id, entity);
		EntityList.entityEggs.put(id, new EntityEggInfo(id, primaryColor, secondaryColor));
	}
}
