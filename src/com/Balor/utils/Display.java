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
import com.Balor.commands.*;
import com.Balor.commands.mcMMO.BuyPartyChest;
import com.Balor.commands.mcMMO.OpenPartyChest;

/**
 * 
 * @author Balor
 */
public class Display {
	public static void sendHelp(CommandSender sender, Class<?> command) {
		GPCommand gpCom = GiftPostWorker.getInstance().getCommand(command);
		if (gpCom != null)
			sender.sendMessage(gpCom.getHelp());
	}

	public static void sendHelp(CommandSender sender) {
		Player player = (Player) sender;
		sender.sendMessage(ChatColor.AQUA + "Virtual Chest (Gift Post) \n");
		sender.sendMessage("--------------------\n");
		if (GiftPostWorker.getInstance().hasPerm(player, "giftpost.chest.everywhere", false))
			sendHelp(sender, Chest.class);
		if (GiftPostWorker.getInstance().hasPerm(player, "giftpost.chest.open", false)) {
			sendHelp(sender, Buy.class);
			sendHelp(sender, SetChest.class);
			sendHelp(sender, ChestList.class);
			sendHelp(sender, Upgrade.class);
			sendHelp(sender, Rename.class);
			// sendHelp(sender, EmptyChest.class);
		}
		if (GiftPostWorker.getInstance().hasPerm(player, "giftpost.chest.send", false))
			sendHelp(sender, Send.class);
		if (GiftPostWorker.getInstance().hasPerm(player, "giftpost.admin.limit", false))
			sendHelp(sender, SetChestLimit.class);
		if (GiftPostWorker.getmcMMO() != null
				&& GiftPostWorker.getInstance().hasPerm(player, "mcmmo.commands.party", false)) {
			sender.sendMessage(ChatColor.AQUA + "mcMMO commands ! ");
			sendHelp(sender, OpenPartyChest.class);
			sendHelp(sender, BuyPartyChest.class);
		}

	}

	public static String chestKeeper() {
		return "[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] ";
	}

}