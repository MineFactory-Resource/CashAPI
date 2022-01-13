package net.teamuni.cashmf;

import net.teamuni.cashmf.command.CashExecutor;
import net.teamuni.cashmf.command.CashTabCompleter;
import net.teamuni.cashmf.config.MessageConf;
import net.teamuni.cashmf.config.PlayerConf;
import org.bukkit.plugin.java.JavaPlugin;

public final class CashMF extends JavaPlugin {
    private static CashMF instance;
    private static PlayerConf playerConf;
    private static MessageConf messageConf;

    @Override
    public void onEnable() {
        instance = this;

        // *.yml 파일 셋업
        playerConf = new PlayerConf();
        messageConf = new MessageConf();


        getServer().getPluginManager().registerEvents(new FirstJoin(), this);
        this.getCommand("캐시").setExecutor(new CashExecutor());
        this.getCommand("캐시").setTabCompleter(new CashTabCompleter());

    }

    @Override
    public void onDisable() {
        Cash.save();
    }

    public static CashMF getInstance() {
        return instance;
    }

    public static PlayerConf getPlayerConf() {
        return playerConf;
    }

    public static MessageConf getMessageConf() {
        return messageConf;
    }
}
