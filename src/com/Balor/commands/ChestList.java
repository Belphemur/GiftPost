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
package com.Balor.commands;

import static com.Balor.utils.Display.chestKeeper;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;

/**
 * @author Antoine
 * 
 */
public class ChestList implements GPCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker
	 * , org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		Player player = (Player) sender;
		HashMap<String, VirtualChest> chestList = gpw.listOfChest(player);
		if (chestList == null)
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] " + ChatColor.RED
					+ "You don't have any chest.");
		else {
			String msg = chestKeeper() + ChatColor.GREEN + "List of all your chest (" + chestList.size()
					+ "): " + ChatColor.DARK_RED;
			for (String chestName : chestList.keySet())
				msg += chestName + ", ";
			msg = msg.substring(0, msg.length() - 2);
			sender.sendMessage(msg);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.
	 * GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return (gpw.hasFlag(args, "list") || gpw.hasFlag(args, "l"))
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	@Override
	public String getPermName() {
		return "giftpost.chest.open";
	}

}
