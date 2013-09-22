package toldea.romecraft.command;

import toldea.romecraft.entity.EntityLegionary;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

final class MTWFilterIMob implements IEntitySelector {
	public boolean isEntityApplicable(Entity par1Entity) {
		return (par1Entity instanceof IMob && !(par1Entity instanceof EntityLegionary));
	}
}
