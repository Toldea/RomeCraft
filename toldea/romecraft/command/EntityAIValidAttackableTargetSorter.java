package toldea.romecraft.command;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class EntityAIValidAttackableTargetSorter implements Comparator {
	private Entity theEntity = null;

	public EntityAIValidAttackableTargetSorter() {}

	public int compareDistanceSq(Entity par1Entity, Entity par2Entity) {
		double d0 = this.theEntity.getDistanceSqToEntity(par1Entity);
		double d1 = this.theEntity.getDistanceSqToEntity(par2Entity);
		return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.compareDistanceSq((Entity) par1Obj, (Entity) par2Obj);
	}
}