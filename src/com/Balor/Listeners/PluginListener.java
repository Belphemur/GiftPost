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

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.Balor.bukkit.GiftPost.GiftPost;
import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.gmail.nossr50.mcMMO;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Methods;

import org.bukkit.plugin.Plugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PluginListener extends ServerListener {
	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if (GiftPostWorker.getPermission() == null) {
			Plugin Permissions = GiftPost.getBukkitServer().getPluginManager()
					.getPlugin("Permissions");
			if (Permissions != null) {
				if (Permissions.isEnabled()) {
					GiftPostWorker.setPermission(((Permissions) Permissions).getHandler());
					System.out.println("[VirtualChest] Successfully linked with Permissions.");
				}
			}
		}
		if (GiftPostWorker.getPayement() == null) {
			Methods methods = new Methods();
			// Check to see if we need a payment method
			if (!methods.hasMethod()) {
				if (methods.setMethod(event.getPlugin())) {
					// You might want to make this a public variable inside your
					// MAIN class public Method Method = null;
					// then reference it through this.plugin.Method so that way
					// you can use it in the rest of your plugin ;)
					GiftPostWorker.setPayementMethod(methods.getMethod());
					System.out.println("[VirtualChest] Payment method found ("
							+ GiftPostWorker.getPayement().getName() + " version: "
							+ GiftPostWorker.getPayement().getVersion() + ")");
				}
			}
		}
		if (GiftPostWorker.getmcMMO() == null) {
			Plugin mcMMOPlugin = GiftPost.getBukkitServer().getPluginManager().getPlugin("mcMMO");
			if (mcMMOPlugin != null) {
				if (mcMMOPlugin.isEnabled()) {
					GiftPostWorker.setmcMMO((mcMMO) mcMMOPlugin);
					System.out.println("[VirtualChest] Successfully linked with mcMMO.");
				}
			}
		}
	}
}
