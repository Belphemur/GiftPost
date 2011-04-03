/*
 * VirtualChest is a proof of concept for virtual chests in Bukkit
 * VC creates a single large global chest, accessible by all players via the /chest command
 * 
 */
package com.aranai.virtualchest;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;

import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

/**
 * VirtualChest for Bukkit
 *
 * @authors Timberjaw and Balor
 * 
 */
public class VirtualChest
{

    private TileEntityVirtualChest chest;
    private TileEntityVirtualChest chest2;
    private InventoryLargeChest lc;
    private String owner;

    /**
     * Constructor
     * @param player
     */
    public VirtualChest(Player player)
    {
        // Large chests are made up of two individual small chests
        // TileEntityVirtualChest extends the TileEntityChest class to remove some bothersome world checks
        // This would NOT work with regular TileEntityChest instances
        chest = new TileEntityVirtualChest();
        //chest2 = new TileEntityVirtualChest();

        // Set up the global chest
        // Note: this is NOT persisted across server restarts
        owner = player.getName();
        //lc = new InventoryLargeChest(owner.getName(), chest, chest2);

    }

    /**
     * Check if the chest is belong to the player
     * @param player
     * @return
     */
    public boolean isBelongTo(Player player)
    {
        return owner.equals(player.getName());
    }

    /**
     * Open the chest for the owner
     */
    public void openChest(Player p)
    {
        if (isBelongTo(p))
        {
            EntityPlayer eh = ((CraftPlayer) p).getHandle();
            eh.a(chest);
        }
        p.sendMessage("You can't open this chest, it's not yours.");
    }

    public ItemStack[] getContents()
    {
        return chest.getContents();
    }
}
