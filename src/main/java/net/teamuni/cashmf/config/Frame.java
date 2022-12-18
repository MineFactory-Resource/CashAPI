package net.teamuni.cashmf.config;

import net.teamuni.cashmf.CashMF;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Frame {
    private final String name;
    protected File configFile;
    protected FileConfiguration config;

    public Frame(String name) {
        this.name = name;
        load();
    }

    // CashMF 폴더에서 *.yml 불러오기
    protected void load() {
        CashMF main = CashMF.getPlugin(CashMF.class);
        // 만약 CashMF 폴더가 존재하지 않을 경우 생성
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdirs();
        }

        configFile = new File(main.getDataFolder(), name + ".yml");
        // *.yml 파일이 존재하지 않을 경우 새로 생성
        if (!configFile.exists())
            main.saveResource(name + ".yml", false);
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
