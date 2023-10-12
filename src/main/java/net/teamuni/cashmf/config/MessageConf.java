package net.teamuni.cashmf.config;

import java.util.ArrayList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConf extends Frame {
    private Map<String, List<String>> messageListMap;

    public MessageConf() {
        super("messages");
    }

    @Override
    protected void load() {
        super.load();
        this.messageListMap = new HashMap<>();
        loadMessages();
    }

    public void loadMessages() {
        for (String key : config.getKeys(false)) {
            this.messageListMap.put(key, config.getStringList(key));
        }
    }

    public Map<String, List<String>> getMessages() {
        return this.messageListMap;
    }

    @Override
    public void reload() {
        super.reload();
        this.messageListMap.clear();
        loadMessages();
    }

    public void sendTranslatedMessage(Player player, List<String> messages) {
        List<String> msgList = applyPlaceholdersIfAvailable(player, messages);

        msgList.forEach(s ->
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s)));
    }

    private List<String> applyPlaceholdersIfAvailable(Player player, List<String> messages) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, messages);
        }
        return new ArrayList<>(messages);
    }

    public List<String> translate(Player player, List<String> messages, String target, String replacement) {
        List<String> msgList = applyPlaceholdersIfAvailable(player, messages);

        msgList.replaceAll(s -> s.contains(target) ? s.replace(target, replacement) : s);
        msgList.forEach(s -> ChatColor.translateAlternateColorCodes('&', s));

        return msgList;
    }

    public List<String> translate(Player player, List<String> messages, String target, String replacement, String target2, String replacement2) {
        List<String> msgList = applyPlaceholdersIfAvailable(player, messages);

        msgList.replaceAll(s -> s.contains(target) ? s.replace(target, replacement) : s);
        msgList.replaceAll(s -> s.contains(target2) ? s.replace(target2, replacement2) : s);
        msgList.forEach(s -> ChatColor.translateAlternateColorCodes('&', s));

        return msgList;
    }
}