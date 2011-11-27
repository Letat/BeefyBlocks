package org.chryson.bukkit.beefyblocks.blocks;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.chryson.bukkit.beefyblocks.BeefyBlocks;
import org.chryson.bukkit.beefyblocks.PersistenceManager;
import org.chryson.bukkit.beefyblocks.db.BlockLocation;
import org.chryson.bukkit.beefyblocks.db.DisplayPreference;
import org.chryson.bukkit.beefyblocks.db.ItemStackSerializable;
import org.chryson.bukkit.beefyblocks.db.PlacedBlock;

public class BeefyBlock extends BaseBlock {
	private static int hardenTime;
	private static Set permanentPlacers;
	
	public BeefyBlock(Block b) {
		super(b);
	}
	
	// TODO: Remove?
	public static void updateLocation(BeefyBlock block, Location newLocation) {
		updateLocation(block.block.getLocation(), newLocation);
	}
	
	public static void updateLocation(Location oldLocation, Location newLocation) {
		PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(oldLocation, false);
		if (pBlock == null)
			return;
		pBlock.setLocation(new BlockLocation(newLocation));
	}
	
	public void harden() {
		if (isNotHardened()) {
	    	PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), false);
	    	if (pBlock == null)
	    		return;
			Timestamp now = new Timestamp(Calendar.getInstance().getTime().getTime());
			long newPlacedTime = now.getTime() - (hardenTime*60000);
			pBlock.setTimestamp(new Timestamp(newPlacedTime));
		}
	}
	
	public static void setHardenTime(int time) {
		hardenTime = time;
	}
	
	public static int getHardenTime() {
		return hardenTime;
	}
	
	public void strengthen(short amount) {
		setReinforcement((short)(getReinforcement() + amount));
	}
	
	public void weaken(short amount) {
		setReinforcement((short)(getReinforcement() - amount));
	}
	
	public void makePlaced(String placerName) {
		boolean permanent = isPermanentPlacer(placerName);
		makePlaced(placerName, permanent);
	}
	
	public void makePlaced(String placerName, boolean permanent) {
    	makeNatural();
    	if (isMultiple()) {
    		Block[] blocks = getMultiple();
    		for (Block b : blocks)
    	    	PersistenceManager.createPlacedBlock(placerName, b, permanent);
    	} else {
    		PersistenceManager.createPlacedBlock(placerName, block, permanent);
    	}
	}
	
    public void makePlaced(Player placer) {
    	makePlaced(placer.getName());
    }
    
	public void makePlaced(Player placer, boolean permanent) {
		makePlaced(placer.getName(), permanent);
	}
    
    public void makeNatural() {
    	if (isPlaced()) {
    		if (isMultiple()) {
    			Block[] blocks = getMultiple();
    			for (Block b : blocks) {
    	    		PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(b.getLocation(), false);
    	    		if (pBlock != null)
    	    			PersistenceManager.destroyPlacedBlock(pBlock);
    			}
    		} else {
	    		PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), false);
	    		if (pBlock != null)
	    			PersistenceManager.destroyPlacedBlock(pBlock);
    		}
    	}
    }
    
    public void trackHealth() {
    	BlockHealth.blockHealths.put(block.getLocation().toString(),
    							   new BlockHealth(this));
    }
    
    public void untrackHealth() {
        BlockHealth.blockHealths.remove(block.getLocation().toString());
    }
    
    public BlockHealth getHealth() {
    	String locStr = block.getLocation().toString();
    	return (BlockHealth)BlockHealth.blockHealths.get(locStr);
    }
    
    public void setHealth(BlockHealth health) {
    	String locStr = block.getLocation().toString();
    	BlockHealth.blockHealths.put(locStr, health);
    }
    
    public void setReinforcement(short reinforcement) {
		if (isPlaced()) {
			PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), false);
			if (pBlock == null)
				return;
			if (reinforcement < 0)
				reinforcement = 0;
			else if (reinforcement > 32640)
				reinforcement = 32640;
			pBlock.setReinforcement(reinforcement);
		}
    }
    
    public short getReinforcement() {
		if (isPlaced()) {
			PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), true);
			if (pBlock == null)
				return 0;
			return (short) pBlock.getReinforcement();
		}
		return 0;
    }
    
    public BlockHealth loseHealth(int amount) {
    	BlockHealth health = getHealth();
    	if (health != null) {
    		health.lose((short)amount);
    		setHealth(health);
    	}
    	return health;
    }
    
    /* Unused */
    public BlockHealth loseHealth(ItemStack itemInHand) {
    	return loseHealth(damageFromItem(itemInHand));
    }
    
    public BlockHealth gainHealth(int amount) {
    	BlockHealth health = getHealth();
    	if (health != null) {
    		health.gain((short)amount);
    		setHealth(health);
    	}
    	return health;
    }
    
    public boolean isNatural() {
    	return !isPlaced();
    }
    
    public boolean isPlaced() {
        return PersistenceManager.isPlacedBlockAt(block.getLocation());
    }
    
    public String getPlacer() {
    	PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), true);
    	if (pBlock == null)
    		return null;
    	return pBlock.getPlayerName();
    }
    
    public boolean isPlacedBy(Player player) {
    	String placer = getPlacer();
    	if (placer != null && placer.equals(player.getName()))
    		return true;
    	return false;
    }
    
    public boolean isHealthTracked() {
    	return BlockHealth.blockHealths.containsKey(block.getLocation().toString());
    }
   
    public boolean isHardened() {
    	return !isNotHardened();
    }
    
    public boolean isNotHardened() {
    	PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), true);
    	if (pBlock == null)
    		return false;
    	Timestamp now = new Timestamp(Calendar.getInstance().getTime().getTime());
    	long periodEnd =  pBlock.getTimestamp().getTime() + (hardenTime*60000);
    	return now.before(new Timestamp(periodEnd));
    }
    
    public boolean isPermanent() {
    	PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), true);
    	if (pBlock == null)
    		return false;
    	return pBlock.isPermanent();
    }
    
    public static void addPermanentPlacer(Player player) {
    	addPermanentPlacer(player.getName());
    }
    
    public static void removePermanentPlacer(Player player) {
    	removePermanentPlacer(player.getName());
    }
    
    public static boolean isPermanentPlacer(Player player) {
    	return isPermanentPlacer(player.getName());
    }
    
    public static void addPermanentPlacer(String playerName) {
    	permanentPlacers.add(playerName);
    }
    
    public static void removePermanentPlacer(String playerName) {
    	permanentPlacers.remove(playerName);
    }
    
    public static boolean isPermanentPlacer(String playerName) {
    	return permanentPlacers.contains(playerName);
    }
    
    public boolean changedType() {
    	PlacedBlock pBlock = PersistenceManager.getPlacedBlockAt(block.getLocation(), true);
    	if (pBlock == null)
    		return false;
    	return (block.getType() != pBlock.getMaterial());
    }

    /* Unused */
    public short damageFromItem(ItemStack item) {
    	if (isPlaced())
    		return 1;
    	switch (item.getType()) {
    	case STONE_SPADE: if (isDiggable()) return 2; break;
    	case STONE_PICKAXE: if (isMineable()) return 2; break;
    	case STONE_AXE: if (isChoppable()) return 2; break;
    	case IRON_SPADE: if (isDiggable()) return 3; break;
    	case IRON_PICKAXE: if (isMineable()) return 3; break;
    	case IRON_AXE: if (isChoppable()) return 3; break;
    	case DIAMOND_SPADE: if (isDiggable()) return 5; break;
    	case DIAMOND_PICKAXE: if (isMineable()) return 5; break;
    	case DIAMOND_AXE: if (isChoppable()) return 5; break;
    	}
    	return 1;
    }
}
