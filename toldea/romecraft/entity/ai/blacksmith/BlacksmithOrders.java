package toldea.romecraft.entity.ai.blacksmith;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import toldea.romecraft.item.crafting.RomanAnvilRecipes;
import toldea.romecraft.item.crafting.RomanAnvilRecipes.AnvilRecipe;

public class BlacksmithOrders {
	private final List<BlacksmithOrder> blacksmithOrdersList;
	private int totalOrderQuantity;
	
	public class BlacksmithOrder {
		private final int itemId;
		private final AnvilRecipe anvilRecipe;
		private int quantity;
		private BlacksmithOrder(AnvilRecipe anvilRecipe, int quantity) {
			this.anvilRecipe = anvilRecipe;
			this.itemId = anvilRecipe.craftedItem.itemID;
			this.quantity = quantity;
		}
		public int getItemId() {
			return itemId;
		}
		public AnvilRecipe getAnvilRecipe() {
			return anvilRecipe;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public void adjustQuantity(int adjustment) {
			quantity += adjustment;
			if (quantity < 0) {
				System.out.println("Warning: RomeCraft.BlacksmithOrder.adjustQuantity adjusted item with id" + itemId + " to below zero!");
				quantity = 0;
			}
		}
	}

	public BlacksmithOrders() {
		blacksmithOrdersList = new ArrayList<BlacksmithOrder>();
		totalOrderQuantity = 0;
		
		List<AnvilRecipe> anvilRecipes = RomanAnvilRecipes.instance().getRecipeList();
		for (int i = 0; i < anvilRecipes.size(); i++) {
			AnvilRecipe recipe = anvilRecipes.get(i);
			blacksmithOrdersList.add(new BlacksmithOrder(recipe, 0));
		}
	}
	
	public int getOrderQuantityForItemId(int itemId) {
		for (int i = 0; i < blacksmithOrdersList.size(); i++) {
			if (blacksmithOrdersList.get(i).getItemId() == itemId) {
				return blacksmithOrdersList.get(i).getQuantity();
			}
		}
		return -1;
	}
	
	public int[] getAllQuantities() {
		int[] quantitiesArray = new int[blacksmithOrdersList.size()];
		for (int i = 0; i < quantitiesArray.length; i++) {
			quantitiesArray[i] = blacksmithOrdersList.get(i).getQuantity();
		}
		return quantitiesArray;
	}
	
	public void setQuantities(int[] quantities) {
		totalOrderQuantity = 0;
		for (int i = 0; i < quantities.length; i++) {
			blacksmithOrdersList.get(i).setQuantity(quantities[i]);
			totalOrderQuantity += quantities[i];
		}
	}
	
	public void adjustOrderQuantityForItemId(int itemId, int adjustment) {
		for (int i = 0; i < blacksmithOrdersList.size(); i++) {
			if (blacksmithOrdersList.get(i).getItemId() == itemId) {
				blacksmithOrdersList.get(i).adjustQuantity(adjustment);
				totalOrderQuantity += adjustment;
				return;
			}
		}
		System.out.println("Error: RomeCraft.BlacksmithOrders.adjustOrderQuantityForItemId called for unknown itemId:" + itemId);
	}
	
	public boolean hasOrder() {
		return totalOrderQuantity > 0;
	}
	
	public BlacksmithOrder getNextOrder() {
		for (int i = 0; i < blacksmithOrdersList.size(); i++) {
			if (blacksmithOrdersList.get(i).getQuantity() > 0) {
				return blacksmithOrdersList.get(i);
			}
		}
		return null;
	}
	
	public void writeOrdersToNBT(NBTTagCompound compound) {
		NBTTagCompound ordersCompound = new NBTTagCompound();
		
		int[] itemIdsArray = new int[blacksmithOrdersList.size()];
		int[] quantitiesArray = new int[blacksmithOrdersList.size()];
		
		for (int i = 0; i < itemIdsArray.length; i++) {
			BlacksmithOrder order = blacksmithOrdersList.get(i);
			itemIdsArray[i] = order.getItemId();
			quantitiesArray[i] = order.getQuantity();
		}
	    
	    ordersCompound.setIntArray("itemIds", itemIdsArray);
		ordersCompound.setIntArray("quantities", quantitiesArray);
	    
	    compound.setCompoundTag("blacksmithOrders", ordersCompound);
	}
	public void readOrdersFromNBT(NBTTagCompound compound) {
		NBTTagCompound ordersCompound = compound.getCompoundTag("blacksmithOrders");
		if (ordersCompound != null) {
			int[] itemIdsArray = ordersCompound.getIntArray("itemIds");
			int[] quantitiesArray = ordersCompound.getIntArray("quantities");
			totalOrderQuantity = 0;
			blacksmithOrdersList.clear();
			for (int i = 0; i < itemIdsArray.length; i++) {
				blacksmithOrdersList.add(new BlacksmithOrder(RomanAnvilRecipes.instance().getRecipeForRecipeResultItemId(itemIdsArray[i]), quantitiesArray[i]));
				totalOrderQuantity += quantitiesArray[i];
			}
		}
	}
}
