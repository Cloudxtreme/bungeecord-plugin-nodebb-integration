package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Yari on 2/24/2015.
 */
public class CommandRegister extends Command
{
    NodeBBIntegration node;

    public CommandRegister(String name, NodeBBIntegration node)
    {
        super(name);
        this.node = node;
    }

    public enum params
    {
        EMAIL,
        PASSWORD;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 2) {
            String msg = "";
            for (int i = 0; i < CommandRegister.params.values().length; i++) {
                msg += " " + CommandRegister.params.values()[i].toString();
            }
            commandSender.sendMessage(new ComponentBuilder("Please use /register" + msg).color(ChatColor.GREEN).create());
        }else{
            node.scheduleRegisterTask(new RegisterTask(commandSender, strings));
        }
    }
}
