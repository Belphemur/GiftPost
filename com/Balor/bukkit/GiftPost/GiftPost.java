/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Balor.bukkit.GiftPost;

import com.aranai.virtualchest.VirtualChest;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
//Permissions imports
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Antoine
 */
public class GiftPost extends JavaPlugin
{

    private HashMap<Player, VirtualChest> chests;
    public static final Logger log = Logger.getLogger("Minecraft");
    /**
     * Permission plugin
     */
    private static PermissionHandler Permissions = null;

    /**
     * Checks that Permissions is installed.
     */
    private void setupPermissions()
    {

        Plugin perm_plugin = this.getServer().getPluginManager().getPlugin("Permissions");
        PluginDescriptionFile pdfFile = this.getDescription();

        if (Permissions == null)
            if (perm_plugin != null)
            {
                //Permissions found, enable it now
                this.getServer().getPluginManager().enablePlugin(perm_plugin);
                Permissions = ((Permissions) perm_plugin).getHandler();
                log.info("[" + pdfFile.getName() + "]" + " (version " + pdfFile.getVersion() + ") Enabled with Permissions.");
            } else
            {
                log.info("[" + pdfFile.getName() + "]" + " (version " + pdfFile.getVersion() + ") Enables without Permissions.");
                log.info("[" + pdfFile.getName() + "]" + " Instead of Permissions, check if the user is OP.");
            }
    }

    /**
     * Check the permissions
     * @param player
     * @param perm
     * @return boolean
     */
    private boolean hasPerm(Player player, String perm)
    {
        if (Permissions == null)
            return true;
        else
            return Permissions.has(player, perm);
    }

    @Override
    public void onEnable()
    {
        setupPermissions();
        chests = new HashMap<Player, VirtualChest>();
    }

    @Override
    public void onDisable()
    {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "]" + " Plugin Disabled. (version" + pdfFile.getVersion() + ")");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You have to be a player!");
            return true;
        } else
        {
            Player player = (Player) sender;

            String cmd = command.getName();


        }
        return false;
    }
}
