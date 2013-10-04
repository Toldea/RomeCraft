package toldea.romecraft.managers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;


public class RecipeManager {
	private static final CraftingManager craftingManager = CraftingManager.getInstance();
	
	public static void registerCraftingRecipes() {
		//CraftingManager.getInstance().addRecipe(new ItemStack(BlockManager.testMultiFurnaceCore, 1), "XXX", "X X", "XXX", 'X', Block.brick);
		
		craftingManager.addShapelessRecipe(new ItemStack(ItemManager.itemLegionaryEquipment, 1), new Object[] {
			ItemManager.itemGalea,
			ItemManager.itemLoricaSegmentata,
			ItemManager.itemCingulum,
			ItemManager.itemCaligae,
			ItemManager.itemGladius,
			ItemManager.itemScutum,
			ItemManager.itemPilum,
			ItemManager.itemSarcina,
			ItemManager.itemSudis
			});
	}
}
