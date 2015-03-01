package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yari on 2/28/2015.
 */
public class RegisterTask implements Runnable
{
    public RegisterTask(CommandSender commandSender, String[] strings) {
        this.commandSender = commandSender;
        this.strings = strings;
    }

    CommandSender commandSender;
    String[] strings;

    @Override
    public void run()
    {
        commandSender.sendMessage(new ComponentBuilder("Registering you on " + NodeBBIntegration.config.getString("FORUMNAME") + "...").color(ChatColor.GREEN).create());

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(NodeBBIntegration.config.getString("URL"));
        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("key", NodeBBIntegration.config.getString("KEY")));
            nameValuePairs.add(new BasicNameValuePair("uuid", ProxyServer.getInstance().getPlayer(commandSender.getName()).getUniqueId().toString()));
            nameValuePairs.add(new BasicNameValuePair("username", commandSender.getName()));
            nameValuePairs.add(new BasicNameValuePair("password", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[0]));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            //while ((line = rd.readLine()) != null)
            line = rd.readLine();
            if (line != null) {
                switch (line) {
                    case "SUCCESS":
                        commandSender.sendMessage(new ComponentBuilder("Successfully created a new account.").color(ChatColor.GREEN).create());
                        break;
                    case "RECREATED":
                        commandSender.sendMessage(new ComponentBuilder("Account recreated.").color(ChatColor.GREEN).create());
                        break;
                    case "FAILPASS":
                        commandSender.sendMessage(new ComponentBuilder("Your password was too short.").color(ChatColor.GREEN).create());
                        break;
                    case "EMAILTAKEN":
                        commandSender.sendMessage(new ComponentBuilder("That email was already taken.").color(ChatColor.GREEN).create());
                        commandSender.sendMessage(new ComponentBuilder("Inform an administrator if you believe this is an error.").color(ChatColor.GREEN).create());
                        break;
                    case "FAILEMAIL":
                        commandSender.sendMessage(new ComponentBuilder("Your email was invalid.").color(ChatColor.GREEN).create());
                        commandSender.sendMessage(new ComponentBuilder("Inform an administrator if you believe this is an error.").color(ChatColor.GREEN).create());
                        break;
                    case "FAILKEY":
                        commandSender.sendMessage(new ComponentBuilder("Registration Pass Key was invalid, please inform an administrator.").color(ChatColor.GREEN).create());
                        System.out.println("NodeBB failed the configured pass key: " + NodeBBIntegration.config.getString("KEY"));
                        break;
                    default:
                        line = rd.readLine();
                        if (line != null) commandSender.sendMessage(new ComponentBuilder(line).color(ChatColor.GREEN).create());
                        break;
                }
            }

        } catch (IOException e)
        {
            commandSender.sendMessage(new ComponentBuilder("Could not connect to NodeBB, please inform an administrator.").color(ChatColor.GREEN).create());
            System.out.println("Failed to connect to NodeBB forum:");
            e.printStackTrace();
        }
    }
}
