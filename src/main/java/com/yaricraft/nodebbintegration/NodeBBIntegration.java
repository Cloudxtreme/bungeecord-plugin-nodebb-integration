package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class NodeBBIntegration extends Plugin {
    private enum defaults {
        URL("https://community.example.com/register/mc"),
        FORUMNAME("https://community.example.com/"),
        KEY("SECRETPASSWORD");

        public String value;

        defaults(String value) {
            this.value = value;
        }
    }

    private static File dataFolder;
    public static Configuration config;
    public static ProxyServer proxy;

    @Override
    public void onEnable() {
        proxy = getProxy();
        proxy.getPluginManager().registerListener(this, new NodeBBIntegrationListener());
        proxy.getPluginManager().registerCommand(this, new CommandRegister("register", this));
        proxy.getPluginManager().registerCommand(this, new CommandNodeBB("nodebb"));
        NodeBBIntegration.dataFolder = getDataFolder();
        loadConfig();
        initSocketConnector(new SocketConnector("http://www.yaricraft.com/"));
    }

    public static void loadConfig() {
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
        if (!configFile.exists()) {
            try
            {
                config = new Configuration();
                config.set("URL", defaults.URL.value);
                config.set("FORUMNAME", defaults.FORUMNAME.value);
                config.set("KEY", defaults.KEY.value);
                configFile.createNewFile();
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
                System.out.println("Created new config.yml for NodeBBIntegration.");
            } catch (IOException e)
            {
                System.out.println("Could not create config file:");
                e.printStackTrace();
            }
        }else{
            try
            {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(dataFolder, "config.yml"));
                System.out.println("Loaded config.yml for NodeBBIntegration.");
            } catch (IOException e)
            {
                System.out.println("Could not find config file:");
                e.printStackTrace();
            }
        }
    }

    public void scheduleRegisterTask(RegisterTask task) {
        getProxy().getScheduler().runAsync(this, task);
    }

    public void initSocketConnector(SocketConnector task) {
        getProxy().getScheduler().runAsync(this, task);
    }
}
