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

import java.util.List;
import java.util.TreeMap;

/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
public class PlayerChests {
	public List<String> types;
	public List<String> names;

	PlayerChests() {
		types = null;
		names = null;
	}

	PlayerChests(List<String> t, List<String> n) {
		types = t;
		names = n;
	}

	public boolean haveAChest() {
		return types != null;
	}

	public TreeMap<String, String> concat() {
		TreeMap<String, String> tmp = new TreeMap<String, String>();
		int i = 0;
		for (String s : types) {
			tmp.put((String) names.toArray()[i], s);
			i++;
		}
		return tmp;
	}
}