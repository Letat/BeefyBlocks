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

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.bukkit.Location;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Embeddable
public class BlockLocation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Basic
	@NotNull
	@NotEmpty
	private String world;
	@Basic
	@NotNull
	private int x;
	@Basic
	@NotNull
	private int y;
	@Basic
	@NotNull
	private int z;
	
	public BlockLocation() {}
	
	public BlockLocation(Location loc) {
		setWorld(loc.getWorld().getName());
		setX(loc.getBlockX());
		setY(loc.getBlockY());
		setZ(loc.getBlockZ());
	}
	
	public String getWorld() {
		return world;
	}
	
	public void setWorld(String world) {
		this.world = world;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int z) {
		this.z = z;
	}

	public int hashCode() {
		return super.hashCode();
	}
	
	public boolean equals(Object o) {
		if (o instanceof BlockLocation) {
			return (((BlockLocation)o).getWorld().equals(getWorld()) &&
					((BlockLocation)o).getX() == getX() &&
					((BlockLocation)o).getY() == getY() &&
					((BlockLocation)o).getZ() == getZ());
		} else if (o instanceof Location) {
			return (((Location)o).getWorld().getName().equals(getWorld()) &&
					((Location)o).getBlockX() == getX() &&
					((Location)o).getBlockY() == getY() &&
					((Location)o).getBlockZ() == getZ());
		}
		return false;
	}
}
