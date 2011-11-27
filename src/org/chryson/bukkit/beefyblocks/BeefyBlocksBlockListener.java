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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.chryson.bukkit.beefyblocks.Status.MessageType;
import org.chryson.bukkit.beefyblocks.blocks.BeefyBlock;
import org.chryson.bukkit.beefyblocks.blocks.BlockHealth;
import org.chryson.bukkit.beefyblocks.db.DisplayPreference;
import org.chryson.bukkit.beefyblocks.db.ItemStackSerializable;
import org.chryson.bukkit.beefyblocks.db.PlacedBlock;
import org.chryson.bukkit.beefyblocks.items.Tool;

public class BeefyBlocksBlockListener extends BlockListener {
    private BeefyBlocks plugin;

    public BeefyBlocksBlockListener(BeefyBlocks instance) {
        plugin = instance;
    }

    /*
    public void resizeMaps() {
        // To avoid consuming too much memory, reset these
        if (plugin.blockLives.size() > 10000) {
            plugin.blockLives.clear();
        }
    }
    */

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        BeefyBlock block = new BeefyBlock(event.getBlock());
        block.makePlaced(player);
        if (block.isPermanent())
            Status.privateMsg(player, MessageType.INFO, "You place a permanent block.");
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.isPermanent()) {
            if (PermissionsManager.hasPermission(player, "beefyblocks.permremove")) {
                if (PermissionsManager.hasPermission(player, "beefyblocks.instabreak")) {
                    // TODO: make sure instabreak DOES NOT trigger a block_break event
                    //       Also, consider creating a synonymous method for makeNatural
                    //       that just means "remove placed block from database"
                    block.makeNatural();
                    event.setInstaBreak(true);
                }
            } else {
                event.setCancelled(true);
                Status.privateMsg(player, MessageType.WARNING, "This block is permanent.");
            }
        }
    }

    @Override
    public void onBlockFade(BlockFadeEvent event) {
        if (event.isCancelled())
            return;

        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.changedType())
            block.makeNatural();

        if (!block.isHealthTracked())
            block.trackHealth();
        BlockHealth health = block.loseHealth(1);
        if (health.isGone()) {
            block.untrackHealth();
            block.makeNatural();
        } else {
            block.trackAttachedBlocks();
            event.setCancelled(true);
        }
        //resizeMaps();
    }

    @Override
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled())
            return;

        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.changedType())
            block.makeNatural();

        if (!block.isHealthTracked())
            block.trackHealth();
        BlockHealth health = block.loseHealth(1);
        if (health.isGone()) {
            block.untrackHealth();
            block.makeNatural();
        } else {
            block.trackAttachedBlocks();
            event.setCancelled(true);
        }
        //resizeMaps();
    }

    // TODO: should this method even exist?
    @Override
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (event.isCancelled())
            return;

        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.changedType())
            block.makeNatural();

        if (!block.isHealthTracked())
            block.trackHealth();
        BlockHealth health = block.loseHealth(1);
        if (health.isGone()) {
            block.untrackHealth();
            block.makeNatural();
        } else {
            block.trackAttachedBlocks();
            event.setCancelled(true);
        }
        //resizeMaps();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        Tool tool = new Tool(player, player.getItemInHand());
        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.changedType())
            block.makeNatural();

        if (block.isNotHardened()) {
            if (block.isHealthTracked())
                block.untrackHealth();
            block.makeNatural();
        } else {
            if (!block.isHealthTracked())
                block.untrackHealth();
            BlockHealth health = block.loseHealth(1);
            if (health.isGone()) {
                block.untrackHealth();
                block.makeNatural();
            } else {
                block.trackAttachedBlocks(); // TODO: test
                block.cancelEventAndRespawn(event);
                tool.damage(1);
                health.notify(player);
            }
        }
        //resizeMaps();
    }

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled())
            return;

        BeefyBlock block = new BeefyBlock(event.getBlock());
        BlockFace dir = event.getDirection();

        Block ext = event.getBlock().getRelative(dir);
        BeefyBlock bExt = new BeefyBlock(ext);

        if (event.getLength() > 0) {
            if (block.isPlaced()) {
                String placer = block.getPlacer();
                bExt.makePlaced(placer);
            } else {
                bExt.makeNatural();
            }
            for (Block b : event.getBlocks()) {
                BeefyBlock movedBlock = new BeefyBlock(b);
                if (movedBlock.isPlaced()) {
                    String placer = movedBlock.getPlacer();
                    new BeefyBlock(b.getRelative(dir)).makePlaced(placer);
                } else {
                    new BeefyBlock(b.getRelative(dir)).makeNatural();
                }
            }
        } else {
            if (ext.getPistonMoveReaction() == PistonMoveReaction.BREAK) {
                bExt.makeNatural();
            }
        }
    }

    // TODO: Finish this method
    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled())
            return;

        if (event.isSticky()) {
            Block block = event.getRetractLocation().getBlock();
        }
    }

    // TODO: Fix bug where torch on top of a block hops onto side of an adjacent block that is one
    //       level higher
    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.isCancelled())
            return;

        // FIXME: Not yet updated
        /*
        BeefyBlock block = new BeefyBlock(event.getBlock());
        if (block.isAttached())

        Block block = event.getBlock();
        if (isAttached(block)) {
            int attachedToId = (Integer)plugin.attachedBlocks.get(block.getLocation().toString());
            if (attachedToId == event.getChangedTypeId()) {
                untrackAttachedLives(block);
                event.setCancelled(true);
            }
        }
        */
    }
}

