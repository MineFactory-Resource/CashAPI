package net.teamuni.cashmf.config;

import net.teamuni.cashmf.Cash;

import static net.teamuni.cashmf.CashMF.getInstance;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerConf extends Frame{
    public PlayerConf() {
        super("players");
    }

    @Override
    public void load() {
        super.load();

        getPlayers();
    }

    // 플레이어의 캐시 정보를 player.conf 파일에 저장
    public void save() {
        for (Cash cash: Cash.cashes.values()) {
            config.set(cash.getUUID().toString(), cash.getCash());
        }

        // player.conf 파일에 저장
        if (configFile != null && config != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                getInstance().getLogger().log(Level.SEVERE, "players.yml를 저장할 수 없습니다. " + configFile, e);
            }
        }
    }

    // 플레이어의 캐시정보를 player.conf 파일에서 불러오기
    public void getPlayers() {
        for (String s : config.getKeys(false)) {
            new Cash(UUID.fromString(s), config.getInt(s));
        }
    }


}
