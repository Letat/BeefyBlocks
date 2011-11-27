package org.chryson.bukkit.beefyblocks;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Status {
	public enum MessageType { SUCCESS, INFO, WARNING, ERROR }
	
	private static Logger log = Logger.getLogger("Minecraft");
	private static BeefyBlocks plugin;
    
	public Status(BeefyBlocks instance) {
		plugin = instance;
	}
	
	public static String consoleName() {
		return "[" + plugin.getDescription().getName() + 
			   " (" + plugin.getDescription().getVersion() + ")]";
	}
	
	public static String ingameName() {
		return "[" + plugin.getDescription().getName() + "]";
	}
	
	public static void consoleMsg(MessageType type, String msg) {
		switch (type) {
		case SUCCESS: log.info(consoleName() + " " + msg); break;
		case INFO:    log.info(consoleName() + " " + msg); break;
		case WARNING: log.warning(consoleName() + " " + msg); break;
		case ERROR:   log.severe(consoleName() + " " + msg); break;
		}
	}
	
	public static void broadcastMsg(MessageType type, String msg) {
		Server s = plugin.getServer();
		switch (type) {
		case SUCCESS: s.broadcastMessage(ChatColor.GREEN + ingameName() + " " + msg);
		              break;
		case INFO:    s.broadcastMessage(ChatColor.BLUE + ingameName() + " " + msg);
        			  break;
		case WARNING: s.broadcastMessage(ChatColor.YELLOW + ingameName() + " " + msg);
        			  break;
		case ERROR:   s.broadcastMessage(ChatColor.RED + ingameName() + " " + msg);
        			  break;
		}
	}
	
	public static void privateMsg(Player player, MessageType type, String msg) {
		switch (type) {
		case SUCCESS: player.sendMessage(ChatColor.GREEN + ingameName() + " " + msg);
        			  break;
		case INFO:    player.sendMessage(ChatColor.BLUE + ingameName() + " " + msg);
		  			  break;
		case WARNING: player.sendMessage(ChatColor.YELLOW + ingameName() + " " + msg);
		  			  break;
		case ERROR:   player.sendMessage(ChatColor.RED + ingameName() + " " + msg);
		  			  break;
		}
	}
}
