package toldea.romecraft.command;

import toldea.romecraft.entity.EntityLegionary;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

public class EntitySelectorLegionary implements IEntitySelector {

	@Override
	public boolean isEntityApplicable(Entity entity) {
		return (entity instanceof IMob && !(entity instanceof EntityLegionary));
	}
}
