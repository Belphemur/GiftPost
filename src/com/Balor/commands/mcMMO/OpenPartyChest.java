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
package com.Balor.commands.mcMMO;

import static com.Balor.utils.Display.chestKeeper;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.Balor.commands.GPCommand;
import com.Balor.utils.Stacker;
import com.aranai.virtualchest.VirtualChest;
import com.gmail.nossr50.mcMMO;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class OpenPartyChest implements GPCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker
	 * , org.bukkit.command.CommandSender, java.lang.String[])
	 */
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (GiftPostWorker.getmcMMO() != null) {
			if (mcMMO.inParty(player)) {
				if (gpw.getParties().containsKey(mcMMO.getPartyName(player))) {
					VirtualChest v= gpw.getParties().get(mcMMO.getPartyName(player));
					if (gpw.getConfig().getString("auto-sort", "true").matches("true"))
						Stacker.sortChest(v);
					if (gpw.getConfig().getString("auto-stack", "true").matches("true"))
						Stacker.stackChest(v);
					v.openChest(player);
				} else
					sender.sendMessage(chestKeeper() + ChatColor.RED + "Your party don't have a chest.");
			} else
				sender.sendMessage(chestKeeper() + ChatColor.DARK_RED
						+ "You must be in a party to buy a party chest");
		} else
			sender.sendMessage(chestKeeper() + ChatColor.DARK_RED + "You don't have mcMMO installed !");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.
	 * GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return (GiftPostWorker.getmcMMO() != null && (gpw.hasFlag(args, "open") || gpw.hasFlag(args, "o")))
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	public String getPermName() {
		return "mcmmo.commands.party";
	}

}
