/*This file is part of GiftPost .

    GiftPost is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GiftPost is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.*/
package com.aranai.virtualchest;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemStack;

import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

/**
 * VirtualChest for Bukkit
 * 
 * @authors Timberjaw and Balor
 * 
 */
public class VirtualChest {

	protected TileEntityVirtualChest chest;

	/**
	 * Constructor
	 * 
	 * @param player
	 */
	public VirtualChest(String chestName) {
		chest = new TileEntityVirtualChest();
		chest.setName(chestName);
	}

	public VirtualChest(VirtualChest v) {
		this.chest = v.chest;
	}

	/**
	 * Open the chest for the owner
	 */
	public void openChest(Player p) {

		EntityPlayer eh = ((CraftPlayer) p).getHandle();
		eh.a(chest);
	}

	/**
	 * Add some ItemStack to the chest
	 * 
	 * @param iss
	 */
	public void addItemStack(ItemStack[] iss) {
		for (ItemStack is : iss)
			if (is != null)
				addItemStack(is);
	}

	/**
	 * adding a ItemStack to the chest
	 * 
	 * @param is
	 * @return
	 */
	public boolean addItemStack(ItemStack is) {
		if (isFull())
			return false;
		return chest.addItemStack(is);
	}

	/**
	 * Empty chest
	 */
	public void emptyChest() {
		chest.emptyChest();
	}

	/**
	 * is Chest Full
	 * 
	 * @return
	 */
	public boolean isFull() {
		return chest.isFull();
	}

	/**
	 * is Chest Empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return chest.isEmpty();
	}

	/**
	 * Nb of empty cases left
	 * 
	 * @return
	 */
	public int leftCases() {
		return chest.emptyCasesLeft();
	}

	/**
	 * Nb of used Cases
	 * 
	 * @return
	 */
	public int usedCases() {
		return chest.size() - chest.emptyCasesLeft();
	}

	/**
	 * get all the itemStacks that compose the chest
	 * 
	 * @return
	 */
	public ItemStack[] getContents() {
		return chest.getContents();
	}

	/**
	 * Search for a given itemStack and remove it.
	 * 
	 * @param is
	 */
	public boolean removeItemStack(ItemStack is) {
		for (int i = 0; i < this.getContents().length; i++)
			if (this.getContents()[i].equals(is)) {
				chest.removeItemStack(i);
				return true;
			}
		return false;
	}

	public void removeItemStack(int i) {
		chest.removeItemStack(i);
	}

	public String getName() {
		return this.chest.getName();
	}
}
