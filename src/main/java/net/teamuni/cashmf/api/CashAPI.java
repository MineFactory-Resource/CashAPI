package net.teamuni.cashmf.api;

import net.teamuni.cashmf.Cash;
import org.bukkit.OfflinePlayer;

public class CashAPI {
    // 금액 확인
    public static int balance(String playerName) {
        Cash cash = Cash.getCash(playerName);

        return balance(cash);
    }

    public static int balance(OfflinePlayer player) {
        Cash cash = Cash.getCash(player.getUniqueId());

        return balance(cash);
    }

    private static int balance(Cash cash) {
        if (cash == null)
            return -1;

        return cash.getCash();
    }

    // 금액 추가
    public static boolean add(String playerName, int amount) {
        Cash cash = Cash.getCash(playerName);

        return add(cash, amount);
    }

    public static boolean add(OfflinePlayer player, int amount) {
        Cash cash = Cash.getCash(player.getUniqueId());

        return add(cash, amount);
    }

    private static boolean add(Cash cash, int amount) {
        if (cash == null)
            return false;

        cash.addCash(amount);
        return true;
    }

    // 금액 차감
    public static boolean sub(String playerName, int amount) {
        Cash cash = Cash.getCash(playerName);

        return sub(cash, amount);
    }

    public static boolean sub(OfflinePlayer player, int amount) {
        Cash cash = Cash.getCash(player.getUniqueId());

        return sub(cash, amount);
    }

    private static boolean sub(Cash cash, int amount) {
        if (cash == null)
            return false;

        cash.addCash(-amount);
        return true;
    }

    // 금액 설정
    public static boolean set(String playerName, int amount) {
        Cash cash = Cash.getCash(playerName);

        return set(cash, amount);
    }

    public static boolean set(OfflinePlayer player, int amount) {
        Cash cash = Cash.getCash(player.getUniqueId());

        return set(cash, amount);
    }

    private static boolean set(Cash cash, int amount) {
        if (cash == null)
            return false;

        int balance = cash.getCash();
        cash.addCash(amount-balance);
        return true;
    }

    // 금액 초기화
    public static boolean reset(String playerName) {
        Cash cash = Cash.getCash(playerName);

        return reset(cash);
    }

    public static boolean reset(OfflinePlayer player) {
        Cash cash = Cash.getCash(player.getUniqueId());

        return reset(cash);
    }

    private static boolean reset(Cash cash) {
        if (cash == null)
            return false;

        int balance = cash.getCash();
        cash.addCash(-balance);
        return true;
    }
}
