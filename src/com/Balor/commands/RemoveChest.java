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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class RemoveChest implements GPCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker
	 * , org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if (args.length > 1) {
			VirtualChest v;
			if ((v = gpw.getChest(p.getName(), args[1])) != null) {
				if (gpw.removeChest(p, v))
					p.sendMessage(chestKeeper() + "Chest destroyed with success");
				else
					p.sendMessage(chestKeeper()
							+ "There were a problem when I tried to destroy the chest");
			} else
				p.sendMessage(chestKeeper() + ChatColor.RED
						+ "You don't have this chest. To buy one type " + ChatColor.GOLD
						+ "/gp buy (large|normal) " + args[1].toLowerCase());

		} else {
			VirtualChest v;
			if ((v = gpw.getDefaultChest(p.getName())) != null)
				if (gpw.removeChest(p, v))
					p.sendMessage(chestKeeper() + "Chest destroyed with success");
				else
					p.sendMessage(chestKeeper()
							+ "There were a problem when I tried to destroy the chest");
			else
				p.sendMessage(chestKeeper() + ChatColor.RED
						+ "You don't have a chest. To buy one type " + ChatColor.GOLD
						+ "/gp buy (large|normal) ");
		}

		sender.sendMessage(chestKeeper() + ChatColor.GREEN + "Chest emptied succefuly");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.
	 * GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return ((gpw.hasFlag(args, "rm") || gpw.hasFlag(args, "remove")))
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getHelp()
	 */
	@Override
	public String getHelp() {
		return ChatColor.GOLD + "/gp rm (ChestName)" + ChatColor.WHITE
				+ ": to remove the chest, if no ChestName remove your default chest.\n";
	}

}
