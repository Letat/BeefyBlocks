package org.chryson.bukkit.beefyblocks;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.chryson.bukkit.beefyblocks.Status.MessageType;

public class Commands {
    private static boolean canIssue(CommandSender sender, String cmd, boolean ingameOnly) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (PermissionsManager.hasPermission(player, cmd)) {
				return true;
			} else {
				Status.privateMsg(player, MessageType.ERROR, "You do not have permission to use that command.");
				return false;
			}
		} else {
			if (ingameOnly) {
				Status.consoleMsg(MessageType.WARNING, "You must be in-game to issue that command.");
				return false;
			} else {
				return true;
			}
		}
    }
    
	public static void beefup(CommandSender sender, String[] args) {
		if (!Commands.canIssue(sender, "beefyblocks.beefup", false))
			return;
		
		if (args.length == 3) {
			try {
    			int block = Material.valueOf(args[1].toUpperCase()).getId();
    			int lives = Integer.parseInt(args[2]);
    			parent.baseLives[block] = (byte) lives;
    			parent.getConfiguration().setProperty("blocks.base." + args[1].toUpperCase(), lives);
    			parent.getConfiguration().save();
    			p.sendMessage(ChatColor.GREEN + "[Beefy] Block durability updated.");
			} catch (NumberFormatException e) {
    			p.sendMessage(ChatColor.RED + "[Beefy] Invalid amount of breaks");
			} catch (IllegalArgumentException e) {
				p.sendMessage(ChatColor.RED + "[Beefy] Invalid block type");
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "[Beefy] Unable to save changes.");
			}
		} else if (args.length == 4) {
			try {
    			int block = Material.valueOf(args[2].toUpperCase()).getId();
    			int lives = Integer.parseInt(args[3]);
    			parent.placedLives[block] = (byte) lives;
    			parent.getConfiguration().setProperty("blocks.placed." + args[2].toUpperCase(), lives);
    			parent.getConfiguration().save();
    			p.sendMessage(ChatColor.GREEN + "[Beefy] Block durability updated.");
			} catch (NumberFormatException e) {
    			p.sendMessage(ChatColor.RED + "[Beefy] Invalid amount of breaks");
			} catch (IllegalArgumentException e) {
				p.sendMessage(ChatColor.RED + "[Beefy] Invalid block type");
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "[Beefy] Unable to save changes.");
			}
		} else {
			p.sendMessage(ChatColor.YELLOW + "[Beefy] Insufficient amount of parameters.");
			p.sendMessage(ChatColor.YELLOW + "[Beefy] Usage: /beefup [p] dirt 3");
		}
	}
	
	public static void beefyperm(CommandSender sender, String[] args) {
		if (!Commands.canIssue(sender, "beefyblocks.beefyperm", true))
			return;
	}
	
	public static void beefyshow(CommandSender sender, String[] args) {
		if (!Commands.canIssue(sender, "beefyblocks.beefyshow", true))
			return;
	}
}
