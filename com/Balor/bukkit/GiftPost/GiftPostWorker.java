/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Balor.bukkit.GiftPost;

import com.Balor.commands.GPCommand;
import com.aranai.virtualchest.VirtualChest;
import com.nijiko.permissions.PermissionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author Antoine
 */
public class GiftPostWorker
{

    private HashMap<Player, VirtualChest> chests;
    private PermissionHandler Perm;
    private List<GPCommand> commands;

    public GiftPostWorker(PermissionHandler Perm)
    {
        chests = new HashMap<Player, VirtualChest>();
        this.Perm = Perm;
        commands = new ArrayList<GPCommand>();
    }

    public VirtualChest getChest(Player p)
    {
        if (chests.containsKey(p))
            return chests.get(p);
        else
        {
            VirtualChest tmp = new VirtualChest(p);
            chests.put(p, tmp);
            return tmp;
        }
    }

    /**
     * Get a command represented by a specific class
     *
     * @param clazz
     * @return
     */
    public GPCommand getCommand(Class<?> clazz)
    {
        for (GPCommand command : commands)
            if (command.getClass() == clazz)
                return command;

        return null;
    }

    /**
     * @return the list of commands
     */
    public List<GPCommand> getCommands()
    {
        return commands;
    }

    public void registerCommand(Class<?> clazz)
    {
        try
        {
            GPCommand command = (GPCommand) clazz.newInstance();
            this.getCommands().add(command);
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check the permissions
     * @param player
     * @param perm
     * @return boolean
     */
    public boolean hasPerm(Player player, String perm)
    {
        if (Perm == null)
            return true;
        else if (Perm.has(player, perm))
            return true;
        else
        {
            player.sendMessage("You don't have the perm to do that");
            return false;
        }

    }

    /**
     * Check if the command contain the flag
     * @param args
     * @param checkFlag
     * @return
     */
    public boolean hasFlag(String[] args, String checkFlag)
    {
        String flag = args[0].toLowerCase();
        return flag.equals(checkFlag) || flag.equals("-" + checkFlag);
    }
}
