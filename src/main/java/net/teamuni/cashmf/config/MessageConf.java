package net.teamuni.cashmf.config;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class MessageConf extends Frame{
    private HashMap<String, String> messages;
    public MessageConf() {
        super("messages");
    }

    @Override
    protected void load() {
        super.load();

        getText();
    }

    // 메세지 불러오기
    private void getText() {
        HashMap<String, String> messages = new HashMap<>();
        for (String s : config.getKeys(true)) {
            messages.put(s, ChatColor.translateAlternateColorCodes('&',config.getString(s)));
        }

        this.messages = messages;
    }

    public String getMessage(String tag) {
        if (messages.containsKey(tag))
            return messages.get("prefix") + messages.get(tag);

        return null;
    }
}
