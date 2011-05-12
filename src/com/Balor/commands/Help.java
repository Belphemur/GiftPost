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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.Balor.bukkit.GiftPost.GiftPostWorker;

import static com.Balor.utils.Display.chestKeeper;
import static com.Balor.utils.Display.sendHelp;
/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
public class Help implements GPCommand {

	/* (non-Javadoc)
	 * @see com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(getHelp());
			return;
		}
		Integer page;
		try {
			page = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			sender.sendMessage(chestKeeper() + ChatColor.RED + args[2] + " is not a number.");
			return;
		}
		sendHelp(sender, page);
	}

	/* (non-Javadoc)
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return (gpw.hasFlag(args, "?") || gpw.hasFlag(args, "help") || gpw.hasFlag(args, "h"));
	}

	/* (non-Javadoc)
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	@Override
	public String getPermName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.Balor.commands.GPCommand#getHelp()
	 */
	@Override
	public String getHelp() {
		return ChatColor.GOLD + "/gp ? (1,2 or 3)" + ChatColor.WHITE
		+ ": to see the help's pages (1, 2 or 3).\n";
	}

}
