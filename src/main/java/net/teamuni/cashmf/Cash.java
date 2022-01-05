package net.teamuni.cashmf;

import static net.teamuni.cashmf.CashMF.getPlayerConf;
import java.util.*;

public class Cash {
    public static HashSet<Cash> cashs = new HashSet<>();

    private final UUID uuid;
    private int cash;

    public Cash(UUID uuid) {
        this(uuid, 0);
    }

    public Cash(UUID uuid, int cash) {
        this.uuid = uuid;
        this.cash = cash;

        cashs.add(this);
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
        for (Cash cash : cashs) {
            if (cash.getUUID().equals(uuid))
                return cash;
        }

        return null;
    }

    // 해당 uuid가 존재하는지 확인
    public static boolean contains(UUID uuid) {
        for (Cash cash : cashs) {
            if (cash.getUUID().equals(uuid))
                return true;
        }

        return false;
    }

    // uuid 정보들을 저장
    public static void save() {
        getPlayerConf().save();
    }
}
