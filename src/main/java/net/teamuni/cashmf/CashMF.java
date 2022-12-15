package net.teamuni.cashmf;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.milkbowl.vault.economy.Economy;
import net.teamuni.cashmf.api.Placeholder;
import net.teamuni.cashmf.api.Vault;
import net.teamuni.cashmf.api.database.Database;
import net.teamuni.cashmf.api.database.MongoDB;
import net.teamuni.cashmf.api.database.SQL;
import net.teamuni.cashmf.command.CashExecutor;
import net.teamuni.cashmf.command.CashTabCompleter;
import net.teamuni.cashmf.config.Conf;
import net.teamuni.cashmf.config.MessageConf;
import net.teamuni.cashmf.config.PlayerConf;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class CashMF extends JavaPlugin {
    private static CashMF instance;

    private static Conf conf;
    private static MessageConf messageConf;

    private static Database database;

    private SkriptAddon addon;

    public static CashMF getInstance() {
        return instance;
    }

    public static MessageConf getMessageConf() {
        return messageConf;
    }

    public static Conf getConf() {
        return conf;
    }

    public static Database getDatabase() {
        return database;
    }

    @Override
    public void onEnable() {
        instance = this;

        // *.yml 파일 설정
        messageConf = new MessageConf();
        conf = new Conf();

        // 데이터 저장 방식 설정
        if (conf.database.get("type").equals("yaml")) { // Yaml
            database = new PlayerConf();

        } else if (conf.database.get("type").equals("mongodb")) { // MongoDB
            database = new MongoDB();

        } else { // MariaDB, MySQL, SQLite, PostgreSQL
            database = new SQL();
        }


        // 이벤트 설정
        getServer().getPluginManager().registerEvents(new FirstJoin(), this);

        // 명령어 설정
        this.getCommand("캐시").setExecutor(new CashExecutor());
        this.getCommand("캐시").setTabCompleter(new CashTabCompleter());

        // api 설정
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) { // PlaceholderAPI
            new Placeholder().register();
        } else {
            getLogger().info("PlaceholderAPI 플러그인이 발견되지 않았습니다.");
        }
        if (Bukkit.getPluginManager().getPlugin("Skript") != null) { // Skript
            try {
                addon = Skript.registerAddon(this)
                        .loadClasses("net.teamuni.cashmf.api.skript", "elements");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getLogger().info("Skript 플러그인이 발견되지 않았습니다.");

        }
        if (conf.vault) { // config.yml에서 vault 옵션이 true일 경우
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) { // Vault
                Bukkit.getServicesManager().register(Economy.class, new Vault(), this, ServicePriority.Highest);
            } else {
                getLogger().info("Vault 플러그인이 발견되지 않았습니다.");

            }
        }
    }

    @Override
    public void onDisable() {
        Cash.save();
    }

    public SkriptAddon getAddonInstnace() {
        return addon;
    }
}
