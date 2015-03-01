package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Yari on 2/24/2015.
 */
public class CommandNodeBB extends Command
{
    public CommandNodeBB(String name)
    {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings)
    {
        if (commandSender.hasPermission("nodebbintegration.command.reload")) {
            commandSender.sendMessage(new ComponentBuilder("Reloading NodeBB config...").color(ChatColor.GREEN).create());
            NodeBBIntegration.loadConfig();
        }
    }
}
