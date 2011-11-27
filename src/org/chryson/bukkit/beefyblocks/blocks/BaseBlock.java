package org.chryson.bukkit.beefyblocks.blocks;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.chryson.bukkit.beefyblocks.BeefyBlocks;
import org.chryson.bukkit.beefyblocks.db.ItemStackSerializable;

public class BaseBlock {
    private static Map inventories;
    private static Map attachedBlocks;

    protected Block block;

    public BaseBlock(Block b) {
        block = b;
    }

    public void cancelEventAndRespawn(Cancellable event) {
        event.setCancelled(true);
    }

    /*
    public void cancelEventAndRespawn(Cancellable event) {
        if (isContainer())
            hideInventory();
        trackAttachedBlocks();
        Material oldType = block.getType();
        int oldId = block.getTypeId();
        byte oldData = block.getData();
        String[] oldLines = null;
        if (oldId == 63)
            oldLines = ((Sign)block.getState()).getLines();
        event.setCancelled(true);
        block.setType(Material.AIR);
        block.setType(oldType);
        block.setData(oldData);
        if (oldLines != null)
            for (byte i = 0; i < oldLines.length; i++)
                ((Sign)block.getState()).setLine(i, oldLines[i]);
        if (isContainer())
            restoreInventory();
    }
    */

    public void trackAttachedBlocks() {
        // We need to find all attached blocks and store them in a map along with
        // the main block's material type so that when they break in the physics
        // event following the main block's break event, they can decide whether
        // or not to cancel the secondary "breakage" event and remove themselves from the map
        int id = block.getTypeId();

        byte[] blocks = new byte[5];
        blocks[0] = countAttachedTop();
        blocks[1] = countAttachedSide(BlockFace.EAST);
        blocks[2] = countAttachedSide(BlockFace.WEST);
        blocks[3] = countAttachedSide(BlockFace.NORTH);
        blocks[4] = countAttachedSide(BlockFace.SOUTH);

        for (byte i = 0; i < blocks[0]; i++)
            attachedBlocks.put(block.getRelative(BlockFace.UP, i+1).getLocation().toString(), id);
        for (byte i = 0; i < blocks[1]; i++)
            attachedBlocks.put(block.getRelative(BlockFace.EAST, i+1).getLocation().toString(), id);
        for (byte i = 0; i < blocks[2]; i++)
            attachedBlocks.put(block.getRelative(BlockFace.WEST, i+1).getLocation().toString(), id);
        for (byte i = 0; i < blocks[3]; i++)
            attachedBlocks.put(block.getRelative(BlockFace.NORTH, i+1).getLocation().toString(), id);
        for (byte i = 0; i < blocks[4]; i++)
            attachedBlocks.put(block.getRelative(BlockFace.SOUTH, i+1).getLocation().toString(), id);
    }

    public void untrackAttachedBlocks() {
        attachedBlocks.remove(block.getLocation().toString());
    }

    public byte countAttachedTop() {
        Material mat = block.getRelative(BlockFace.UP, 1).getType();
        switch (mat) {
        case SAPLING:
        case POWERED_RAIL:
        case DETECTOR_RAIL:
        case LONG_GRASS:
        case DEAD_BUSH:
        case YELLOW_FLOWER:
        case RED_ROSE:
        case BROWN_MUSHROOM:
        case RED_MUSHROOM:
        case TORCH:
        case REDSTONE_WIRE:
        case CROPS:
        case SIGN_POST:
        case RAILS:
        case LEVER:
        case STONE_PLATE:
        case WOOD_PLATE:
        case REDSTONE_TORCH_OFF:
        case REDSTONE_TORCH_ON:
        case SNOW:
        case CAKE_BLOCK:
        case DIODE_BLOCK_OFF:
        case DIODE_BLOCK_ON:
            return 1;
        case WOODEN_DOOR:
        case IRON_DOOR_BLOCK:
        case CACTUS:
        case SUGAR_CANE_BLOCK:
        case PORTAL:
            byte num = 1;
            while (block.getRelative(BlockFace.UP, 1+num).getType() == mat)
                num++;
            return num;
        default: break;
        }
        return 0;
    }

    public byte countAttachedSide(BlockFace dir) {
        Material mat = block.getRelative(dir, 1).getType();
        switch (mat) {
        case TORCH:
        case LADDER:
        case WALL_SIGN:
        case LEVER:
        case REDSTONE_TORCH_OFF:
        case REDSTONE_TORCH_ON:
        case STONE_BUTTON:
        case PORTAL:
        case TRAP_DOOR:
            return 1;
        default: break;
        }
        return 0;
    }

    public boolean isAttached() {
        return attachedBlocks.containsKey(block.getLocation().toString());
    }

    public void hideInventory() {
        boolean hasInventory = false;
        Inventory inventory = null;

        if (block.getType() == Material.CHEST) {
            hasInventory = true;
            inventory = ((Chest)block.getState()).getInventory();
        } else if (block.getType() == Material.DISPENSER) {
            hasInventory = true;
            inventory = ((Dispenser)block.getState()).getInventory();
        } else if (block.getType() == Material.FURNACE) {
            hasInventory = true;
            inventory = ((Furnace)block.getState()).getInventory();
        }

        if (hasInventory) {
            ItemStackSerializable[] sInventory = ItemStackSerializable.toItemStackSerializableArr(
                    inventory.getContents()
            );
            inventories.put(block.getLocation().toString(), sInventory);
            inventory.clear();
        }
    }

    public void restoreInventory() {
        ItemStack[] inventory = ItemStackSerializable.toItemStackArr(
                ((ItemStackSerializable[]) inventories.get(block.getLocation().toString()))
        );
        if (block.getType() == Material.CHEST) {
            ((Chest)block.getState()).getInventory().setContents(inventory);
        } else if (block.getType() == Material.DISPENSER) {
            ((Dispenser)block.getState()).getInventory().setContents(inventory);
        } else if (block.getType() == Material.FURNACE) {
            ((Furnace)block.getState()).getInventory().setContents(inventory);
        }
        inventories.remove(block.getLocation().toString());
    }

    /* Convenience method */
    public Material getType() {
        return block.getType();
    }

    /* Convenience method */
    public int getTypeId() {
        return block.getTypeId();
    }

    public boolean isDiggable() {
        switch (block.getType()) {
        case CLAY:
        case GRASS:
        case GRAVEL:
        case DIRT:
        case SAND:
        case SNOW_BLOCK:
        case SNOW:
            return true;
        }
        return false;
    }

    public boolean isMineable() {
        switch (block.getType()) {
        case OBSIDIAN:
        case IRON_DOOR:
        case DIAMOND_BLOCK:
        case IRON_BLOCK:
        case MOB_SPAWNER:
        case DISPENSER:
        case FURNACE:
        case COAL_ORE:
        case DIAMOND_ORE:
        case GOLD_ORE:
        case IRON_ORE:
        case LAPIS_ORE:
        case REDSTONE_ORE:
        case GOLD_BLOCK:
        case LAPIS_BLOCK:
        case BRICK:
        case COBBLESTONE:
        case MOSSY_COBBLESTONE:
        case STEP:
        case DOUBLE_STEP:
        case COBBLESTONE_STAIRS:
        case STONE:
        case SANDSTONE:
        case ICE:
        case STONE_PLATE:
        case NETHERRACK:
        case GLOWSTONE:
            return true;
        }
        return false;
    }

    public boolean isChoppable() {
        switch (block.getType()) {
        case CHEST:
        case LOG:
        case WOOD:
        case BOOKSHELF:
            return true;
        }
        return false;
    }

    public boolean isContainer() {
        return (block.getType() == Material.CHEST ||
                block.getType() == Material.DISPENSER ||
                block.getType() == Material.FURNACE);
    }

    // TODO: finish
    public boolean isDoor() {
        return (block.)
    }
}
