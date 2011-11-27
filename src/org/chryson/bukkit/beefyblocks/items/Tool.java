package org.chryson.bukkit.beefyblocks.items;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tool {
	private Player player;
	private ItemStack item;
	
	public Tool(Player p, ItemStack i) {
		player = p;
		item = i;
	}
	
    public static boolean isTool(ItemStack item) {
    	switch (item.getType()) {
    	case IRON_SPADE:
    	case IRON_PICKAXE:
    	case IRON_AXE:
    	case IRON_SWORD:
    	case IRON_HOE:
    	case WOOD_SWORD:
    	case WOOD_SPADE:
    	case WOOD_PICKAXE:
    	case WOOD_AXE:
    	case WOOD_HOE:
    	case STONE_SWORD:
    	case STONE_SPADE:
    	case STONE_PICKAXE:
    	case STONE_AXE:
    	case STONE_HOE:
    	case DIAMOND_SWORD:
    	case DIAMOND_SPADE:
    	case DIAMOND_PICKAXE:
    	case DIAMOND_AXE:
    	case DIAMOND_HOE:
    	case GOLD_SWORD:
    	case GOLD_SPADE:
    	case GOLD_PICKAXE:
    	case GOLD_AXE:
    	case GOLD_HOE:
    		return true;
    	}
    	return false;
    }
	
	public void damage(int amount) {
		if (isTool(item)) {
	    	if (item.getDurability() >= item.getType().getMaxDurability())
	    		if (item.getAmount() > 1)
	    			item.setAmount(item.getAmount()-1);
	    		else
	    			player.setItemInHand(null);
	    	else
	    		item.setDurability((short)(item.getDurability()+amount));
		}
	}
	
	public void damageFrom(Block block) {
		// TODO
	}
}
