package org.chryson.bukkit.beefyblocks;

import javax.persistence.PersistenceException;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.chryson.bukkit.beefyblocks.db.DisplayPreference;
import org.chryson.bukkit.beefyblocks.db.PlacedBlock;
import org.chryson.bukkit.beefyblocks.db.PlayerSettings;

public class PersistenceManager {
	private static BeefyBlocks plugin;
    
	public PersistenceManager(BeefyBlocks instance) {
		plugin = instance;
	}
    
	public static PlacedBlock createPlacedBlock(Player player, Block block, boolean perm) {
		return createPlacedBlock(player.getName(), block, perm);
	}
	
	public static PlacedBlock createPlacedBlock(String playerName, Block block, boolean perm) {
		PlacedBlock pBlock = new PlacedBlock(playerName, block, perm);
		plugin.getDatabase().save(pBlock);
		return pBlock;
	}
	
	public static void destroyPlacedBlock(PlacedBlock pBlock) {
		plugin.getDatabase().delete(pBlock);
	}
	
	public static void destroyPlacedBlockAt(Location loc) {
		PlacedBlock pBlock = getPlacedBlockAt(loc, false);
		if (pBlock == null)
			return;
		destroyPlacedBlock(pBlock);
	}
	
    public static PlacedBlock getPlacedBlockAt(Location loc, boolean readOnly) {
    	return plugin.getDatabase()
    			.find(PlacedBlock.class)
    			.setReadOnly(readOnly)
    			.where()
    			.eq("world", loc.getWorld().getName())
    			.eq("x", loc.getBlockX())
    			.eq("y", loc.getBlockY())
    			.eq("z", loc.getBlockZ())
    			.findUnique();
    }
    
    public static boolean isPlacedBlockAt(Location loc) {
    	return (getPlacedBlockAt(loc, true) != null);
    }
    
    public static DisplayPreference getDisplayPref(Player p) {
    	PlayerSettings settings = plugin.getDatabase()
    			.find(PlayerSettings.class)
    			.where()
    			.eq("player_name", p.getName())
    			.findUnique();
    	if (settings != null)
    		return settings.getDisplay();
    	return DisplayPreference.ALL;
    }
    
    public static PlayerSettings getPlayerSettings(String playerName, boolean readOnly) {
    	return plugin.getDatabase()
    			.find(PlayerSettings.class)
    			.setReadOnly(readOnly)
    			.where()
    			.eq("player_name", playerName)
    			.findUnique();
    }
}
