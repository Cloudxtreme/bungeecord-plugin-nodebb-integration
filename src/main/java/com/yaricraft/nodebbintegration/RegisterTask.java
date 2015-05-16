package com.yaricraft.nodebbintegration;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

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

        try
        {
            String urlParameters = "";
            urlParameters += "key=" + NodeBBIntegration.config.getString("KEY") + "&";
            urlParameters += "uuid=" + ProxyServer.getInstance().getPlayer(commandSender.getName()).getUniqueId().toString() + "&";
            urlParameters += "username=" + commandSender.getName() + "&";
            urlParameters += "password=" + strings[1] + "&";
            urlParameters += "email=" + strings[0];

            byte[] postData       = urlParameters.getBytes( Charset.forName("UTF-8"));
            int    postDataLength = postData.length;

            String httpsURL = NodeBBIntegration.config.getString("URL");
            URL url = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setDoOutput( true );
            con.setDoInput ( true );
            con.setInstanceFollowRedirects( false );
            con.setRequestMethod( "POST" );
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty( "charset", "utf-8");
            con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            con.setUseCaches( false );

            try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
                wr.write( postData );
            }

            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                switch (inputLine) {
                    case "SUCCESS":
                        commandSender.sendMessage(new ComponentBuilder("Successfully created a new account.").color(ChatColor.GREEN).create());
                        break;
                    case "RECREATED":
                        commandSender.sendMessage(new ComponentBuilder("Account recreated.").color(ChatColor.GREEN).create());
                        break;
                    case "FAILPASS":
                        commandSender.sendMessage(new ComponentBuilder("Your password was too short.").color(ChatColor.GREEN).create());
                        break;
                    case "[[error:email-taken]]":
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
                        commandSender.sendMessage(new ComponentBuilder(inputLine).color(ChatColor.GREEN).create());
                        break;
                }
            }

            System.out.println(con.getCipherSuite());

            in.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
