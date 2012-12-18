package org.chryson.bukkit.beefyblocks;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.chryson.bukkit.beefyblocks.Status.MessageType;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PermissionsManager {
	private static PermissionHandler permissionHandler;
	private static Logger log = Logger.getLogger("Minecraft");
	
    public static void setupPermissions(Plugin plugin) {
        Plugin permissionsPlugin = plugin.getServer().getPluginManager().getPlugin("Permissions");

        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
                permissionHandler = ((Permissions) permissionsPlugin).getHandler();
            } else {
            	Status.consoleMsg(MessageType.INFO, "Permissions plugin not detected, defaulting to bukkit permissions");
            }
        }
    }
    
    public static boolean usingPermissionsPlugin() {
    	return (permissionHandler != null);
    }
    
    public static boolean hasPermission(Player player, String cmd) {
		if (usingPermissionsPlugin()) {
		    if (permissionHandler.has(player, cmd))
		        return true;
		    return false;
		} else if (player.hasPermission(cmd))
		    return true;
		return false;
    }
}
