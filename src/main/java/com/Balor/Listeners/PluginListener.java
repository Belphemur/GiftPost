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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import VirtualChest.Manager.Permissions.PermissionManager;

import com.Balor.bukkit.GiftPost.GiftPost;
import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.gmail.nossr50.mcMMO;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PluginListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getDescription().getName().equals("PermissionsEx"))
			PermissionManager.setPEX(PermissionsEx.getPermissionManager());

		if (!PermissionManager.isPermissionsExSet()) {
			final Plugin Permissions = GiftPost.getBukkitServer().getPluginManager()
					.getPlugin("PermissionsEx");
			if (Permissions != null) {
				if (Permissions.isEnabled())
					PermissionManager.setPEX(PermissionsEx.getPermissionManager());
			}
		}
		if (!PermissionManager.isYetiPermissionsSet()) {
			final Plugin Permissions = GiftPost.getBukkitServer().getPluginManager()
					.getPlugin("Permissions");
			if (Permissions != null) {
				if (Permissions.isEnabled())
					PermissionManager.setYetiPermissions(((Permissions) Permissions).getHandler());
			}
		}
		if (GiftPostWorker.getmcMMO() == null) {
			final Plugin mcMMOPlugin = GiftPost.getBukkitServer().getPluginManager()
					.getPlugin("mcMMO");
			if (mcMMOPlugin != null) {
				if (mcMMOPlugin.isEnabled()) {
					GiftPostWorker.setmcMMO((mcMMO) mcMMOPlugin);
					System.out.println("[VirtualChest] Successfully linked with mcMMO.");
				}
			}
		}
	}
}
