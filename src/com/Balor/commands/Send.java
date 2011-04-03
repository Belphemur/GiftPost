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
package com.Balor.commands;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Balor
 */
public class Send implements GPCommand
{

    @Override
    public void execute(GiftPostWorker gpw, CommandSender sender, String[] args)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args)
    {
        return (gpw.hasFlag(args, "s") || gpw.hasFlag(args, "send")) && gpw.hasPerm((Player) sender, getPermName());
    }

    @Override
    public String getPermName()
    {
        return "giftpost.chest.send";
    }

}
