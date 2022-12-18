package net.teamuni.cashmf.config;

import net.teamuni.cashmf.data.Cash;
import net.teamuni.cashmf.api.database.Database;
import net.teamuni.cashmf.data.CashInfo;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerConf extends Frame implements Database {

    public PlayerConf() {
        super("players");
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public Cash load(UUID uuid) {
        if (hasAccount(uuid)) {
            long cash = config.getLong("cash");
            long cumulativeCash = config.getLong("cumulative_cash");
            return new Cash(uuid, new CashInfo(cash, cumulativeCash));
        }
        return new Cash(uuid, new CashInfo(0, 0));
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return config.isSet(uuid.toString());
    }

    // 플레이어의 캐시 정보를 player.conf 파일에 저장
    @Override
    public void save(Cash data) {
        ConfigurationSection section = config.isSet(data.getUuid().toString()) ?
                config.getConfigurationSection(data.getUuid().toString()) : config.createSection(data.getUuid().toString());
        assert section != null;
        section.set("cash", data.getInfo().cash());
        section.set("cumulative_cash", data.getInfo().cumulativeCash());

        // player.conf 파일에 저장
        if (configFile != null && config != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "players.yml를 저장할 수 없습니다. " + configFile, e);
            }
        }
    }
}
