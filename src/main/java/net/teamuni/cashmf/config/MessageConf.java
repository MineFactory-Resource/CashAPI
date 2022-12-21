package net.teamuni.cashmf.config;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConf extends Frame {

    public MessageConf() {
        super("messages");
    }

    @Override
    protected void load() {
        super.load();
        getMessages();
    }

    public Map<String, List<String>> getMessages() {
        Map<String, List<String>> messageListMap = new HashMap<>();
        for (String key : config.getKeys(false)) {
            List<String> messages = config.getStringList(key);
            messageListMap.put(key, messages);
        }
        return messageListMap;
    }

    public void sendTranslatedMessage(Player player, List<String> messages) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            List<String> msgList = PlaceholderAPI.setPlaceholders(player, messages);
            msgList.forEach(s -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', s)));
        } else {
            messages.forEach(s -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', s)));
        }
    }

    public List<String> translate(Player player, List<String> messages, String target, String replacement) {
        List<String> msgList = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                ? PlaceholderAPI.setPlaceholders(player, messages) : new ArrayList<>(messages);
        msgList.replaceAll(s -> s.contains(target) ? s.replace(target, replacement) : s);
        msgList.forEach(s -> ChatColor.translateAlternateColorCodes('&', s));
        return msgList;
    }

    public List<String> translate(Player player, List<String> messages, String target, String replacement, String target2, String replacement2) {
        List<String> msgList = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                ? PlaceholderAPI.setPlaceholders(player, messages) : new ArrayList<>(messages);
        msgList.replaceAll(s -> s.contains(target) ? s.replace(target, replacement) : s);
        msgList.replaceAll(s -> s.contains(target2) ? s.replace(target2, replacement2) : s);
        msgList.forEach(s -> ChatColor.translateAlternateColorCodes('&', s));
        return msgList;
    }
}
