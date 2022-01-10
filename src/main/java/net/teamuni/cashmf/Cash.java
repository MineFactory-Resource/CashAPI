package net.teamuni.cashmf;

import static net.teamuni.cashmf.CashMF.getPlayerConf;
import java.util.*;

public class Cash {
    public static HashMap<UUID, Cash> cashes = new HashMap<>();

    private final UUID uuid;
    private int cash;

    public Cash(UUID uuid) {
        this(uuid, 0);
    }

    public Cash(UUID uuid, int cash) {
        this.uuid = uuid;
        this.cash = cash;

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
    }

    // 해당 uuid의 Cash 정보 가져오기
    public static Cash getCash(UUID uuid) {
        if (Cash.contains(uuid))
            return cashes.get(uuid);

        return null;
    }

    // 해당 uuid가 존재하는지 확인
    public static boolean contains(UUID uuid) {
        return cashes.containsKey(uuid);
    }

    // uuid 정보들을 저장
    public static void save() {
        getPlayerConf().save();
    }
}
