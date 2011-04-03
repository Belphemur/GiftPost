/*
 * VirtualChest is a proof of concept for virtual chests in Bukkit
 * VC creates a single large global chest, accessible by all players via the /chest command
 * 
 */
package com.aranai.virtualchest;

import net.minecraft.server.EntityPlayer;
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

    protected TileEntityVirtualChest chest;
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
        chest.setName(player.getName());
        //lc = new InventoryLargeChest(owner.getName(), chest, chest2);

    }

    /**
     * Check if the chest is belong to the player
     * @param player
     * @return
     */
    public boolean isBelongTo(Player player)
    {
        return chest.getName().equals(player.getName());
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
        } else
            p.sendMessage("You can't open this chest, it's not yours.");
    }
    /**
     * adding a ItemStack to the chest
     * @param is
     * @return
     */
    public boolean addItemStack(ItemStack is)
    {
        return chest.addItemStack(is);
    }
    /**
     * Empty chest
     */
    public void emptyChest()
    {
        chest.emptyChest();
    }

    /**
     * get all the itemStacks that compose the chest
     * @return
     */
    public ItemStack[] getContents()
    {
        return chest.getContents();
    }
}
