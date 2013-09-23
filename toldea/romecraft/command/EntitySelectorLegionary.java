package toldea.romecraft.command;

import toldea.romecraft.entity.EntityLegionary;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

public class EntitySelectorLegionary implements IEntitySelector {
	public static EntitySelectorLegionary instance = new EntitySelectorLegionary();
	
	private EntitySelectorLegionary() {}
	
	@Override
	public boolean isEntityApplicable(Entity entity) {
		return (entity instanceof IMob && !(entity instanceof EntityLegionary));
	}
}
