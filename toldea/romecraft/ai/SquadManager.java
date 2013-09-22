package toldea.romecraft.ai;

import java.util.HashMap;

import toldea.romecraft.RomeCraft;
import toldea.romecraft.entity.EntityLegionary;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SquadManager implements IExtendedEntityProperties {
	private static HashMap<Integer, Contubernium> contuberniumMap = new HashMap<Integer, Contubernium>();
	
	public static SquadManager instance = new SquadManager();
	
	private SquadManager() {}
	
	public static void registerToContubernium(int contuberniumId, EntityLegionary squadMember) {
		if (contuberniumMap.get(contuberniumId) == null) {
			contuberniumMap.put(contuberniumId, new Contubernium());
		} 
		contuberniumMap.get(contuberniumId).registerSquadMember(squadMember);
	}
	
	public static Contubernium getContubernium(int contuberniumId) {
		if (contuberniumMap.get(contuberniumId) != null) {
			return contuberniumMap.get(contuberniumId);
		} else return null;
	}
	
	public static void removeFromContubernium(int contuberniumId, EntityLegionary squadMember) {
		if (contuberniumMap.get(contuberniumId) != null) {
			contuberniumMap.get(contuberniumId).removeSquadMember(squadMember);
		} 
	}
	
	public static void giveMovementOrder(int contuberniumId, Vec3 targetLocation) {
		if (contuberniumMap.get(contuberniumId) != null) {
			contuberniumMap.get(contuberniumId).setTargetLocation(targetLocation);
		} 
	}
	
	public static float getFormationOffset(Contubernium contubernium) {
		int squadSize = contubernium.getSquadSize();
		int firstFileSize = squadSize - (squadSize % Contubernium.files);
		return ((firstFileSize - 1f) / 2f);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		//compound.setInteger("HerpDerp", 1337);
		System.out.println("saveNBTData");
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		//System.out.println("HerpDerp: " + compound.getInteger("HerpDerp"));
		System.out.println("loadNBTData");
	}

	@Override
	public void init(Entity entity, World world) {}
}
