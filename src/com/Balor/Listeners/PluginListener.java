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
import com.nijiko.coelho.iConomy.iConomy;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.plugin.Plugin;

/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
public class PluginListener extends ServerListener {
   
    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if(GiftPostWorker.getPermission() == null) {
            Plugin Permissions = GiftPost.getBukkitServer().getPluginManager().getPlugin("Permissions");
            if (Permissions != null) {
                if(Permissions.isEnabled()) {
                	GiftPostWorker.setPermission(((Permissions) Permissions).getHandler());
                    System.out.println("[VirtualChest] Successfully linked with Permissions.");
                }
            }
        }
        if(GiftPostWorker.getiConomy() == null) {
            Plugin iConomy = GiftPost.getBukkitServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if(iConomy.isEnabled()) {
                	GiftPostWorker.setiConomy((iConomy)iConomy);
                    System.out.println("[VirtualChest] Successfully linked with iConomy.");
                }
            }
        }
       /* if(GiftPostWorker.getmcMMO() == null) {
            Plugin mcMMOPlugin = GiftPost.getBukkitServer().getPluginManager().getPlugin("mcMMO");
            if (mcMMOPlugin != null) {
                if(mcMMOPlugin.isEnabled()) {
                	GiftPostWorker.setmcMMO((mcMMO)mcMMOPlugin);
                    System.out.println("[VirtualChest] Successfully linked with mcMMO.");
                }
            }
        }*/
    }
}
