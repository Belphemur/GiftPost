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
package com.aranai.virtualchest;

import java.io.Serializable;

import net.minecraft.server.InventoryLargeChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class InventoryVirtualLargeChest extends InventoryLargeChest implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 218446299025155660L;

	public InventoryVirtualLargeChest(String name, TileEntityVirtualChest c1,
			TileEntityVirtualChest c2) {
		super(name, c1, c2);
	}
}
