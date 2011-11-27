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

package org.chryson.bukkit.beefyblocks.db;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemStackSerializable implements Serializable {
	private static final long serialVersionUID = 1L;
	private int typeId;
	private int amount;
	private short durability;
	private byte data;
	
	public ItemStackSerializable(ItemStack stack) {
		typeId = stack.getTypeId();
		amount = stack.getAmount();
		durability = stack.getDurability();
		MaterialData d = stack.getData();
		if (d != null)
			data = d.getData();
	}
	
	public ItemStack toItemStack() {
		return new ItemStack(typeId, amount, durability, data);
	}
	
	public static ItemStack[] toItemStackArr(ItemStackSerializable[] s) {
		ItemStack[] arr = new ItemStack[s.length];
		for (int i = 0; i < s.length; i++)
			if (s[i] != null)
				arr[i] = s[i].toItemStack();
			else
				arr[i] = null;
		return arr;
	}
	
	public static ItemStackSerializable[] toItemStackSerializableArr(ItemStack[] s) {
		ItemStackSerializable[] arr = new ItemStackSerializable[s.length];
		for (int i = 0; i < s.length; i++)
			if (s[i] != null)
				arr[i] = new ItemStackSerializable(s[i]);
			else
				arr[i] = null;
		return arr;
	}
}
