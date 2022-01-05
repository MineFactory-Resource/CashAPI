package net.teamuni.cashmf.config;

import static net.teamuni.cashmf.CashMF.getInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Frame {
    public File configFile;
    public FileConfiguration config;

    private final String name;
    public Frame(String name) {
        this.name = name;
        load();
    }

    // CashMF 폴더에서 *.yml 불러오기
    public void load() {
        // 만약 CashMF 폴더가 존재하지 않을 경우 생성
        if (!getInstance().getDataFolder().exists()) {
            getInstance().getDataFolder().mkdirs();
        }

        configFile = new File(getInstance().getDataFolder(), name + ".yml");
        // *.yml 파일이 존재하지 않을 경우 새로 생성
        if (!configFile.exists())
            getInstance().saveResource(name + ".yml", false);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

}
