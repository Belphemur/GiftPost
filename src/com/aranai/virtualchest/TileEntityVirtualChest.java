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
    along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aranai.virtualchest;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;

public class TileEntityVirtualChest extends TileEntityChest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8338831559626786200L;
	protected String name = "Chest";
	protected Queue<Integer> emptyCases;

	TileEntityVirtualChest() {
		super();
		initEmptyCases();
	}

	private void initEmptyCases() {
		emptyCases = new ArrayDeque<Integer>(q_());
		for (int i = 0; i < q_(); i++)
			emptyCases.add(i);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return if the chest is full
	 * 
	 * @return
	 */
	public boolean isFull() {
		return emptyCases.isEmpty();
	}

	/**
	 * Return if the chest is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return emptyCases.size() == q_();
	}

	/**
	 * return the number of emptyCases
	 * 
	 * @return
	 */
	public int emptyCasesLeft() {
		return emptyCases.size();
	}

	/**
	 * Alias to q_()
	 * 
	 * @return
	 */
	public int size() {
		return q_();
	}

	/**
	 * Look for the first empty case in the chest to add the stack.
	 * 
	 * @param itemstack
	 * @return
	 */
	public boolean addItemStack(ItemStack itemstack) {
		Integer i = emptyCases.poll();
		if (i == null)
			return false;
		else {
			super.a(i, itemstack);
			return true;
		}
	}

	@Override
	public void a(int i, ItemStack itemstack) {
		emptyCases.remove(i);
		super.a(i, itemstack);
	}

	public void emptyChest() {
		for (int i = 0; i < this.getContents().length; i++)
			this.getContents()[i] = null;
		initEmptyCases();
	}

	@Override
	public ItemStack a(int i, int j) {
		ItemStack toReturn = super.a(i, j);
		if (toReturn != null) {
			ItemStack afterSuper[] = this.getContents();
			if (afterSuper[i] == null)
				emptyCases.add(i);
		}

		return toReturn;
	}

	public void removeItemStack(int i) {
		if (this.getContents()[i] != null && i >= 0 && i <= q_()) {
			this.getContents()[i] = null;
			emptyCases.add(i);
		}
	}

	@Override
	public String c() {
		return name;
	}

	/**
	 * Alias to c()
	 * 
	 * @return
	 */
	public String getName() {
		return this.c();
	}

	@Override
	public boolean a_(EntityHuman entityhuman) {
		/*
		 * For this proof of concept, we ALWAYS validate the chest. This
		 * behavior has not been thoroughly tested, and may cause unexpected
		 * results depending on the state of the player.
		 * 
		 * Depending on your purposes, you might want to change this. It would
		 * likely be preferable to enforce your business logic outside of this
		 * file instead, however.
		 */
		return true;
	}
}
