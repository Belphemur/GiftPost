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
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;

/**
 * 
 * @author Balor
 */
public class Display {
	public static void sendHelp(GiftPostWorker gpw, CommandSender sender) {
		Player player = (Player) sender;
		sender.sendMessage(ChatColor.AQUA + "Virtual Chest (Gift Post) \n");
		sender.sendMessage("--------------------\n");
		if (gpw.hasPerm(player, "giftpost.chest.everywhere",false))
			sender.sendMessage(ChatColor.GOLD + "/gp c (ChestName OR nothing)" + ChatColor.WHITE
					+ ": to open your chest if you don't set a ChestName, open your default chest.\n");
		if (gpw.hasPerm(player, "giftpost.chest.open",false)) {
			sender.sendMessage(ChatColor.GOLD + "/gp b (large OR normal) ChestName"
					+ ChatColor.WHITE + ": to buy a large or normal chest \n");
			sender.sendMessage(ChatColor.GOLD + "/gp u (ChestName OR nothing)" + ChatColor.WHITE
					+ ": if you have a normal chest, upgrade to a large chest.");
			sender.sendMessage(ChatColor.GOLD
					+ "/gp set ChestName (default|send)"
					+ ChatColor.WHITE
					+ ": set the ChestName as your default chest (open when using a chest) or send/receive chest (for gifts)");
			sender.sendMessage(ChatColor.GOLD + "/gp l " + ChatColor.WHITE
					+ ": list all your chests");
			sender.sendMessage(ChatColor.GOLD + "/gp r oldName newName" + ChatColor.WHITE
					+ ": rename the chest.");
		}
		if (gpw.hasPerm(player, "giftpost.chest.send",false))
			sender.sendMessage(ChatColor.GOLD + "/gp s player " + ChatColor.WHITE
					+ ": send the content of your send chest to the player (case sensitive)");
		if (gpw.hasPerm(player, "giftpost.admin.limit",false))
			sender.sendMessage(ChatColor.GOLD + "/gp lim PlayerName limit" + ChatColor.WHITE
					+ ": set the chest's limit for the PlayerName(case sensitive)");

		if (GiftPostWorker.getmcMMO() != null && gpw.hasPerm(player, "mcmmo.commands.party",false)) {
			sender.sendMessage(ChatColor.AQUA + "mcMMO commands ! ");
			sender.sendMessage(ChatColor.GOLD + "/pchest" + ChatColor.WHITE
					+ ": to open your party chest if you are in a party");
			sender.sendMessage(ChatColor.GOLD + "/gp party (large OR normal)" + ChatColor.WHITE
					+ ": to buy a party chest for your party");
		}

	}
	public static String chestKeeper()
	{
		return "[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] ";
	}

}