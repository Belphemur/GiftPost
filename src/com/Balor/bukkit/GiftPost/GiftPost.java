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
package com.Balor.bukkit.GiftPost;

import com.Balor.Listeners.GPPlayerListener;
import com.Balor.Listeners.PluginListener;
import com.Balor.commands.*;
import com.Balor.utils.AutoSaveThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import static com.Balor.utils.Display.sendHelp;
/**
 * 
 * @author Balor
 */
public class GiftPost extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	private GiftPostWorker gpw;
	private GPPlayerListener pListener;
	private AutoSaveThread autoSave;
	private static Server server = null;
	private static PluginListener pluginListener = null;

	private void registerCommand(Class<?> clazz) {
		try {
			GPCommand command = (GPCommand) clazz.newInstance();
			gpw.getCommands().add(command);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void registerCommands() {
		registerCommand(Chest.class);
		registerCommand(Send.class);
		registerCommand(EmptyChest.class);
	}

	private void setupConfigFiles() {
		if (!new File(getDataFolder().toString()).exists()) {
			new File(getDataFolder().toString()).mkdir();
		}
		File yml = new File(getDataFolder() + "/config.yml");
		if (!yml.exists()) {
			new File(getDataFolder().toString()).mkdir();
			try {
				yml.createNewFile();
			} catch (IOException ex) {
				System.out.println("cannot create file " + yml.getPath());
			}

			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(yml,
						true));
				out.write("use-max-range: 'true'");
				out.newLine();
				out.write("max-range: 100");
				out.newLine();
				out.write("allow-offline: 'true'");
				out.newLine();
				out.write("chest-type: 'normal'");
				out.newLine();
				out.write("auto-save-time: 10");
				out.newLine();
				out.write("world-check: 'true'");
				out.newLine();

				// Close the output stream
				out.close();
			} catch (Exception e) {
				System.out.println("cannot write config file: " + e);
			}
		}

	}

	private void setupListener() {
		pListener = new GPPlayerListener(gpw);
		pluginListener = new PluginListener();
		registerCommands();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, pListener, Priority.Normal,
				this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pListener, Priority.Normal,
				this);
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener,
				Priority.Monitor, this);
	}

	public static Server getBukkitServer() {
		return server;
	}

	@Override
	public void onEnable() {
		server = getServer();
		setupConfigFiles();
		log.info("[" + this.getDescription().getName() + "]" + " (version "
				+ this.getDescription().getVersion() + ")");
		gpw = new GiftPostWorker(getConfiguration(), getDataFolder().toString());
		setupListener();
		gpw.load();
		log.info("[" + this.getDescription().getName() + "] Chests loaded !");
		autoSave = new AutoSaveThread(gpw);
		autoSave.start();

	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		autoSave.stopIt();
		log.info("[" + pdfFile.getName() + "]" + " Plugin Disabled. (version"
				+ pdfFile.getVersion() + ")");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You have to be a player!");
			return true;
		} else {
			if (args.length == 0) {
				sendHelp(sender);
				return true;
			}
			int i = gpw.getCommands().size();
			for (GPCommand cmd : gpw.getCommands()) {
				if (!cmd.validate(gpw, sender, args)) {
					i--;
					continue;
				}
				try {
					cmd.execute(gpw, sender, args);
				} catch (Exception e) {
					log.info("A GiftPost command threw an exception!");
					e.printStackTrace();
				}

				return true;
			}
			if (i == 0)
				sendHelp(sender);
		}
		return true;
	}
}
