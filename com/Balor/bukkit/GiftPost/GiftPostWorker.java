/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Balor.bukkit.GiftPost;

import com.aranai.virtualchest.VirtualChest;
import com.nijiko.permissions.PermissionHandler;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 *
 * @author Antoine
 */
public class GiftPostWorker
{

    private HashMap<Player, VirtualChest> chests;
    private PermissionHandler Perm;
    public GiftPostWorker(PermissionHandler Perm)
    {
        chests= new HashMap<Player, VirtualChest>();
        this.Perm=Perm;
    }
        /**
     * Check the permissions
     * @param player
     * @param perm
     * @return boolean
     */
    private boolean hasPerm(Player player, String perm)
    {
        if (Perm == null)
            return true;
        else
            return Perm.has(player, perm);
    }
}
