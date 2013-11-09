package toldea.romecraft.managers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;


public class RecipeManager {
	private static final CraftingManager craftingManager = CraftingManager.getInstance();
	
	public static void registerCraftingRecipes() {
		craftingManager.addShapelessRecipe(new ItemStack(ItemManager.itemLegionaryEquipment, 1), new Object[] {
			ItemManager.itemGalea,
			ItemManager.itemLoricaSegmentata,
			ItemManager.itemCingulum,
			ItemManager.itemCaligae,
			ItemManager.itemGladius,
			ItemManager.itemScutum,
			ItemManager.itemPilum,
			ItemManager.itemSarcina,
			BlockManager.blockSudis
			});
		
		craftingManager.addRecipe(new ItemStack(BlockManager.blockBloomery, 2), "AAA", "A A", "AAA", 'A', Block.hardenedClay);
		
		craftingManager.addRecipe(new ItemStack(BlockManager.blockBellows, 1), "AA ", "BBC", "AA ", 'A', Block.planks, 'B', Item.leather, 'C', Item.ingotIron);
	}
}
