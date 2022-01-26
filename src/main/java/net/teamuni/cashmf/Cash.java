package net.teamuni.cashmf;

import java.util.*;

import static net.teamuni.cashmf.CashMF.*;

public class Cash {
    public static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";
    public static HashMap<UUID, Cash> cashes;

    private final UUID uuid;
    private int cash;

    public Cash(UUID uuid) {
        this(uuid, 0);
    }

    public Cash(UUID uuid, int cash) {
        this.uuid = uuid;
        this.cash = cash;

        if (uuid != null)
            cashes.put(this.uuid, this);
    }

    // uuid 값 가져오기
    public UUID getUUID() {
        return uuid;
    }

    // cash 값 가져오기
    public int getCash() {
        return cash;
    }

    // cash 값 더하기
    public void addCash(int add) {
        cash += add;

        // 플레이어의 캐시가 0보다 적어질 경우 0으로 설정
        if (cash < 0)
            cash = 0;
    }

    // 해당 uuid의 Cash 정보 가져오기
    public static Cash getCash(UUID uuid) {
        if (cashes.containsKey(uuid))
            return cashes.get(uuid);

        return null;
    }

    // uuid 정보들을 저장
    public static void save() {
        if (getConf().database.get("type").equals("yaml")) {
            getPlayerConf().save();

        } else if (getConf().database.get("type").equals("mongodb")) {
            getMongoDB().save();

        } else {
            getSQL().save();
        }
    }
}
