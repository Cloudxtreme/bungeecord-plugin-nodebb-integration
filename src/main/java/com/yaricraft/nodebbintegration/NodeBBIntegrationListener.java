package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class NodeBBIntegrationListener implements Listener {
    @EventHandler
    public void onServerConnected(final ServerConnectedEvent event) {
        // TODO: Get the player's nodebb username and message them if they have not registered yet.
        //event.getPlayer().sendMessage(new ComponentBuilder("Use \"/register EMAIL PASSWORD\" to register on our forums!").color(ChatColor.GREEN).create());
    }



}
