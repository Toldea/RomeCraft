package toldea.romecraft.ai;

import java.util.HashMap;

import toldea.romecraft.entity.EntityLegionary;

import net.minecraft.util.Vec3;

public class SquadManager {
	private static HashMap<Integer, Contubernium> contuberniumMap = new HashMap<Integer, Contubernium>();
	
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
}
