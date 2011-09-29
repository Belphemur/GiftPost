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
package com.Balor.Listeners;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldSaveEvent;

import com.Balor.bukkit.GiftPost.GiftPostWorker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class WorldGPListener extends WorldListener {
	@Override
	public void onWorldSave(WorldSaveEvent event) {
		if (!GiftPostWorker.isDisable()) {
			GiftPostWorker.getInstance().save();
			if (GiftPostWorker.getmcMMO() != null)
				GiftPostWorker.getInstance().saveParties();
		}
	}
}