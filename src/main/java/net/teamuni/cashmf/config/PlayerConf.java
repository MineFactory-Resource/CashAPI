package net.teamuni.cashmf.config;

import net.teamuni.cashmf.Cash;
import net.teamuni.cashmf.api.database.Database;

import static net.teamuni.cashmf.CashMF.getInstance;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class PlayerConf extends Frame implements Database {
    public PlayerConf() {
        super("players");
    }

    @Override
    public void load() {
        super.load();

        getPlayers();
    }

    // 플레이어의 캐시 정보를 player.conf 파일에 저장
    @Override
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
    private void getPlayers() {
        // Cash 데이터 초기화
        Cash.cashes = new HashMap<>();

        for (String s : config.getKeys(false)) {
            // 잘못된 uuid 형식일 경우 무시
            if (!Pattern.matches(Cash.UUID_PATTERN, s))
                continue;

            new Cash(UUID.fromString(s), config.getInt(s));
        }
    }
}
