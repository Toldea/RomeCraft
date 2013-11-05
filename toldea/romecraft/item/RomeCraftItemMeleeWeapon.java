package toldea.romecraft.item;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import toldea.romecraft.managers.CreativeTabsManager;

public class RomeCraftItemMeleeWeapon extends ItemSword implements IRomeCraftItem {
	private ResourceLocation resourceLocation = null;
	
	public RomeCraftItemMeleeWeapon(int par1, EnumToolMaterial toolMaterial) {
		super(par1, toolMaterial);
		this.setCreativeTab(CreativeTabsManager.tabRomeCraft);
	}

	public ResourceLocation getResourceLocation() {
		return resourceLocation;
	}
	
	@Override
	public Item setTextureName(String textureName) {
		if (resourceLocation == null) {
			String[] textureNameParts = textureName.split(":");
			if (textureNameParts.length == 2) {
				resourceLocation = new ResourceLocation(textureNameParts[0], "textures/items/" + textureNameParts[1] + ".png");
			}
		}
		return super.setTextureName(textureName);
    }
}
