package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ProxyServer;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Yari on 2/28/2015.
 */

public class SocketConnector implements Runnable
{
    public SocketConnector() {
        this.config = new Configuration();
        this.config.setHostname("localhost");
        this.config.setPort(NodeBBIntegration.config.getInt("APIPORT"));
    }

    public static Configuration config;
    public static SocketIOServer server;

    @Override
    public void run()
    {
        server = new SocketIOServer(config);
        server.addListeners(this);
        server.start();
    }

    @OnEvent("someevent")
    public void onSomeEventHandler(SocketIOClient client, Object data, AckRequest ackRequest) {
        System.out.println("Received someevent");
        //char str[] = {'a', 'b', 'c'};
        //client.sendEvent("someevent", new String(str));
        //System.out.println( ((ProxiedPlayer)ProxyServer.getInstance().getPlayers().toArray()[0]).getName() );
        //server.getBroadcastOperations().sendEvent("someevent", new String(str));
    }

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
        System.out.println("Connected to forum");
    }

    @OnDisconnect
    public void onDisconnectHandler(SocketIOClient client) {
        System.out.println("Disconnected");
    }
}
