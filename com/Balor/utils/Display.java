/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.Balor.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Antoine
 */
public class Display {
   public static void sendHelp(CommandSender sender)
    {
        sender.sendMessage(ChatColor.AQUA+"Gift Post \n"
                + "---------\n"
                +ChatColor.GOLD +"/gp chest "+
                ChatColor.WHITE+": to open your chest");
    }

}
