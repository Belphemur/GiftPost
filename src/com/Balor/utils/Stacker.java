/************************************************************************
 * This file is part of GiftPost.									
 *																		
 * GiftPost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * GiftPost is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package com.Balor.utils;

import net.minecraft.server.ItemStack;

import com.aranai.virtualchest.VirtualChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class Stacker {
	public static boolean sortChest(VirtualChest chest) {

		int swapStackI = 0;
		ItemStack swapStack;
		int size = chest.getMcContents().length;
		for (int index = 0; index < size; index++) {
			ItemStack stack = chest.getItemStack(index);
			swapStack = stack;
			swapStackI = index;
			for (int i = index + 1; i < size; i++) {
				ItemStack stack2 = chest.getItemStack(i);
				if ((stack2 != null && stack2.count != 0 && stack2.id != 0)
						&& (swapStack == null || stack2.id < swapStack.id)) {
					swapStackI = i;
					swapStack = stack2;
				}
			}

			if (swapStack != null) {
				if (swapStack != stack) {
					chest.swapItemStack(index, swapStackI);
				}
			} else {
				break;
			}
		}
		return true;
	}

	/**
	 * Stacks the contents of a chest.
	 */
	public static boolean stackChest(VirtualChest chest)  {
		for (int index = 0; index < chest.getMcContents().length; index++) {
			ItemStack stack = chest.getItemStack(index);
			if (stack != null && stack.count != 0 && stack.id != 0) {
				int i = 0;
				for (ItemStack stack2 : chest.getMcContents()) {
					if (stack2 != null && i != index && stack2.count != 0 && stack2.count < stack2.b()
							&& stack2.id == stack.id) {
						stack.count = Math.min(stack2.b(), stack.count + stack2.count);
						chest.setItemStack(index, stack);
						stack2.count = Math.max(0, stack.count + stack2.count - stack2.b());
						if (stack2.count > 0) {
							chest.setItemStack(i, stack2);
							break;
						} else
							chest.removeItemStack(i);
					}
					i++;
				}
			}
			index++;
		}
		return true;
	}
}
