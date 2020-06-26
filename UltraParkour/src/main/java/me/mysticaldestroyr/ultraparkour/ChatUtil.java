package me.mysticaldestroyr.ultraparkour;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String translateString(String input, UltraParkour plugin) {
        if (plugin.getConfig().getBoolean("prefix-enabled")) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + input);
        } else {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}
