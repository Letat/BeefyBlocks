/*  BeefyBlocks - A Bukkit plugin to beef-up blocks, making them last longer
 *  Copyright (C) 2011 Letat
 *  Copyright (C) 2011 Robert Sargant
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
*/    

package org.chryson.bukkit.beefyblocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;
import org.chryson.bukkit.beefyblocks.Status.MessageType;
import org.chryson.bukkit.beefyblocks.db.BlockLocation;
import org.chryson.bukkit.beefyblocks.db.DisplayPreference;
import org.chryson.bukkit.beefyblocks.db.PlacedBlock;
import org.chryson.bukkit.beefyblocks.db.PlayerSettings;

public class BeefyBlocks extends JavaPlugin {
    protected final Logger log;
    private final BeefyBlocksBlockListener blockListener;
    private final BeefyBlocksPlayerListener playerListener;
    //private PersistenceManager pm = new PersistenceManager(this);
    // temporarily holds attached blocks for respawning after a block is broken
    protected Map attachedBlocks;
    // keeps track of the remaining 'lives' of blocks
    protected Map blockLives;
    // very temporarily stores the inventory of a container block just before it's broken up
    // until just after it is respawned
    protected Map inventories;
    protected Set permBlockPlacers;
    // hold the break counts for each block type
    protected byte[] baseLives;
    protected byte[] placedLives;
    protected int cooldown; // in minutes
    
    public BeefyBlocks() {
        blockListener = new BeefyBlocksBlockListener(this);
        playerListener = new BeefyBlocksPlayerListener(this);
        
    	attachedBlocks = new ConcurrentHashMap();
        blockLives = new ConcurrentHashMap();
        inventories = new ConcurrentHashMap();
        permBlockPlacers = Collections.newSetFromMap(new ConcurrentHashMap());
        
        // should be the size for all block materials
        baseLives = new byte[256];
        placedLives = new byte[256];
        
        log = Logger.getLogger("Minecraft");
        
        new PersistenceManager(this);
        new Status(this);
    }
    
    public void onEnable() {
    	if (!setupConfig()) {
    		disableMessage();
    		return;
    	}
        
    	if (!loadConfig()) {
    		disableMessage();
    		return;
    	}

        if (!setupPersistence()) {
        	disableMessage();
        	return;
        }
        
    	PermissionsManager.setupPermissions(this);
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Lowest, this);

        enableMessage();
    }
    
    public void onDisable() {
    	disableMessage();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	if (commandLabel.equalsIgnoreCase("/beefup")) {
    		Commands.beefup(sender, args);
    		return true;
    	} else if (commandLabel.equalsIgnoreCase("/beefyperm")) {
    		Commands.beefyperm(sender, args);
    		return true;
    	} else if (commandLabel.equalsIgnoreCase("/beefyshow")) {
    		Commands.beefyshow(sender, args);
    		return true;
    	}
    	return false;
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
    	List<Class<?>> list = new ArrayList<Class<?>>();
    	list.add(PlayerSettings.class);
    	list.add(BlockLocation.class);
    	list.add(PlacedBlock.class);
    	return list;
    }
    
    public void enableMessage() {
    	Status.consoleMsg(MessageType.INFO, "enabled");
    }
    
    public void disableMessage() {
    	Status.consoleMsg(MessageType.INFO, "disabled");
    }
    
    private boolean setupPersistence() {
    	try {
    		getDatabase().find(PlayerSettings.class).findRowCount();
    		getDatabase().find(PlacedBlock.class).findRowCount();
    	} catch (PersistenceException ex) {
    		installDDL();
    		Status.consoleMsg(MessageType.SUCCESS, "Installed database");
    	} catch (Exception ex) {
    		Status.consoleMsg(MessageType.ERROR, "Unable to install database");
    		return false;
    	}
    	return true;
    }
    
    public boolean setupConfig() {
        getDataFolder().mkdirs();
        File yml = new File(getDataFolder(), "config.yml");
        if (!yml.exists()) {
        	try {
        		yml.createNewFile();
        		log.info(Utilities.statusMsg(this, "Created empty file: " + 
        											getDataFolder() + File.separator + "config.yml."));
        		log.info(Utilities.statusMsg(this, "Please edit it to change block behavior."));
        		getConfiguration().setProperty("cooldown", 5);
        		getConfiguration().setProperty("blocks", null);
        		getConfiguration().save();
        	} catch (IOException ex){
        		log.severe(Utilities.statusMsg(this, "Could not generate config.yml."));
        		return false;
        	}
        }
        return true;
    }

    public boolean loadConfig() {
        List<String> keys;
        try {
        	keys = getConfiguration().getKeys(null);
        } catch (NullPointerException e) {
        	log.warning(Utilities.statusMsg(this, "Could not find a parent key."));
        	return false;
        }
        
        if (!keys.contains("cooldown")) {
        	log.warning(Utilities.statusMsg(this, "Could not find 'cooldown' key, defaulting to 5 mins"));
        	cooldown = 5;
        } else
        	cooldown = getConfiguration().getInt("cooldown", 5);
        
        if (!keys.contains("blocks")) {
        	log.warning(Utilities.statusMsg(this, "Could not find 'blocks' key."));
        	return false;
        }
        
        keys.clear();
        keys = getConfiguration().getKeys("blocks");
        
        if (keys == null) {
        	log.warning(Utilities.statusMsg(this, "Could not find any block entries."));
        	return false;
        } 
        
        for (int i = 0; i < 256; i++) {
        	baseLives[i] = 1;
        	placedLives[i] = 1;
        }
        
        for (String key : keys) {
        	if (key.equals("base")) {
        		List<String> blockKeys = getConfiguration().getKeys("blocks.base");
        		if (blockKeys != null) {
	        		for (String blockKey : blockKeys) {
	        			int numLives = getConfiguration().getInt("blocks.base." + blockKey, 1);
	        			int block = Material.valueOf(blockKey).getId();
	        			baseLives[block] = (byte)numLives;
	        		}
        		}
        	} else if (key.equals("placed")) {
        		List<String> blockKeys = getConfiguration().getKeys("blocks.placed");
        		if (blockKeys != null) {
	        		for (String blockKey : blockKeys) {
	        			int numLives = getConfiguration().getInt("blocks.placed." + blockKey, 1);
	        			int block = Material.valueOf(blockKey).getId();
	        			placedLives[block] = (byte)numLives;
	        		}
        		}
        	}
        }
        
        return true;
    }
}

