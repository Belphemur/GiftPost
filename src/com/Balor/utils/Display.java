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

package com.Balor.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * 
 * @author Balor
 */
public class Display {
	public static void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "Gift Post \n");
		sender.sendMessage("-------\n");
		sender.sendMessage(ChatColor.GOLD + "/gp (chest OR c) "
				+ ChatColor.WHITE + ": to open your chest\n");
		sender.sendMessage(ChatColor.GOLD + "/gp (buy OR b) (large OR normal) "
				+ ChatColor.WHITE + ": to buy a large or normal chest \n");
		sender.sendMessage(ChatColor.GOLD + "/gp (upgrade OR u) "
				+ ChatColor.WHITE
				+ ": if you have a normal chest, upgrade to a large chest.");
		sender.sendMessage(ChatColor.GOLD + "/gp (send OR s) player "
				+ ChatColor.WHITE
				+ ": send the content of your chest to the player's chest.");
	}

}