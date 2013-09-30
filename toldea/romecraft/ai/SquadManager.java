package toldea.romecraft.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import toldea.romecraft.RomeCraft;
import toldea.romecraft.entity.EntityLegionary;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SquadManager implements IExtendedEntityProperties {
	private static HashMap<Integer, Contubernium> contuberniumMap = new HashMap<Integer, Contubernium>();

	public static SquadManager instance = new SquadManager();

	private SquadManager() {
	}

	// Get the Contubernium from it's id. Create it if it doesn't exist yet.
	public static Contubernium getContubernium(int contuberniumId) {
		if (contuberniumMap.get(contuberniumId) == null) {
			contuberniumMap.put(contuberniumId, new Contubernium());
		}
		return contuberniumMap.get(contuberniumId);
	}
	private static void removeContubernium(int contuberniumId) {
		if (contuberniumMap.get(contuberniumId) != null) {
			contuberniumMap.remove(contuberniumId);
		}
	}
	
	public static void registerToContubernium(int contuberniumId, EntityLegionary squadMember) {
		getContubernium(contuberniumId).registerSquadMember(squadMember);
	}

	public static void removeFromContubernium(int contuberniumId, EntityLegionary squadMember) {
		Contubernium contubernium = getContubernium(contuberniumId);
		contubernium.removeSquadMember(squadMember);
		// Remove the Contubernium if the last squad member got removed.
		if (contubernium.getSquadSize() <= 0) {
			removeContubernium(contuberniumId);
		}
	}

	public static void giveMovementOrder(int contuberniumId, Vec3 targetLocation) {
		Contubernium contubernium = getContubernium(contuberniumId);
		contubernium.setTargetLocation(targetLocation);
		contubernium.setTargetEntity(null);
		contubernium.setShouldFollowPlayer(targetLocation == null);
	}

	public static void giveAttackOrder(int contuberniumId, Entity entity) {
		if (!(entity instanceof EntityLivingBase)) {
			return;
		}
		System.out.println("Attack order received for Contubernium: " + contuberniumId + ", Target Entity: " + entity);
		Contubernium contubernium = getContubernium(contuberniumId);
		//contubernium.setTargetLocation(null);
		contubernium.setTargetEntity((EntityLivingBase)entity);
		contubernium.setShouldFollowPlayer(false);
	}
	
	public static float getFormationOffsetForContubernium(Contubernium contubernium) {
		int squadSize = contubernium.getSquadSize();
		int firstFileSize = (squadSize > Contubernium.files) ? Contubernium.files : squadSize;
		return ((firstFileSize - 1f) / 2f);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		int[] contuberniumIdsArray = new int[contuberniumMap.size()];
		int index = 0;

		// Loop over all Contubernium entries.
		for (Map.Entry<Integer, Contubernium> entry : contuberniumMap.entrySet()) {
			Integer contuberniumId = entry.getKey();
			Contubernium contubernium = entry.getValue();

			// Keep a list of all registered Contubernium Ids.
			contuberniumIdsArray[index++] = contuberniumId;

			// Each Contubernium gets its own NBT compound to save data into.
			NBTTagCompound contuberniumCompound = new NBTTagCompound();
			contubernium.saveNBTData(contuberniumCompound);

			// Save the newly created compound to the SquadManager's NBT compound.
			compound.setCompoundTag(new String("contubernium_" + contuberniumId), contuberniumCompound);
		}

		// Save the array of Contubernium id's.
		compound.setIntArray("contuberniumIdsArray", contuberniumIdsArray);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// Get the registered contubernium ids array, then get or create their respective Contubernium objects and let them load their NBT compound.
		int[] contuberniumIdsArray = compound.getIntArray("contuberniumIdsArray");
		for (int i = 0; i < contuberniumIdsArray.length; i++) {
			int contuberniumId = contuberniumIdsArray[i];
			NBTTagCompound contuberniumCompound = compound.getCompoundTag(new String("contubernium_" + contuberniumId));
			if (contuberniumCompound != null) {
				Contubernium contubernium = getContubernium(contuberniumId);
				contubernium.loadNBTData(contuberniumCompound);
			}
		}
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public static void onWorldUnload() {
		contuberniumMap.clear();
	}
}
