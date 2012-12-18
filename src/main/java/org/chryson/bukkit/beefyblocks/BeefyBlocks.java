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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
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
    private final BlockListener blockListener;
    private final EntityListener entityListener;
    private final HangingListener hangingListener;
    private final WorldListener worldListener;
    
    public BeefyBlocks() {
    	super();
    	
        log = Logger.getLogger("Minecraft");
        
        blockListener = new BlockListener(this);
        entityListener = new EntityListener(this);
        hangingListener = new HangingListener(this);
        worldListener = new WorldListener(this);
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
    	return persistenceManager.getDatabaseClasses();
    }
    
    public void onEnable() {        
        PluginManager pm = getPluginManager();
        
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(hangingListener, this);
        pm.registerEvents(worldListener, this);
    }
    
    public void onDisable() {
    }
    
	public static String fullName() {
		return "[" + getDescription().getName() + 
			   " (" + getDescription().getVersion() + ")]";
	}
	
	public static String name() {
		return "[" + getDescription().getName() + "]";
	}
	
	public static void log(MessageType type, String msg) {
		switch (type) {
		case SUCCESS: log.info(fullName() + " " + msg); break;
		case INFO:    log.info(fullName() + " " + msg); break;
		case WARNING: log.warning(fullName() + " " + msg); break;
		case ERROR:   log.severe(fullName() + " " + msg); break;
		}
	}
	
	public static void broadcast(MessageType type, String msg) {
		Server s = getServer();
		switch (type) {
		case SUCCESS: s.broadcastMessage(ChatColor.GREEN + name() + " " + msg);
		              break;
		case INFO:    s.broadcastMessage(ChatColor.BLUE + name() + " " + msg);
        			  break;
		case WARNING: s.broadcastMessage(ChatColor.YELLOW + name() + " " + msg);
        			  break;
		case ERROR:   s.broadcastMessage(ChatColor.RED + name() + " " + msg);
        			  break;
		}
	}
	
	public static void sendPM(MessageType type, Player player, String msg) {
		switch (type) {
		case SUCCESS: player.sendMessage(ChatColor.GREEN + name() + " " + msg);
        			  break;
		case INFO:    player.sendMessage(ChatColor.BLUE + name() + " " + msg);
		  			  break;
		case WARNING: player.sendMessage(ChatColor.YELLOW + name() + " " + msg);
		  			  break;
		case ERROR:   player.sendMessage(ChatColor.RED + name() + " " + msg);
		  			  break;
		}
	}
}

