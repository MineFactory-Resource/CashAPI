package net.teamuni.cashmf.api;

import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.data.Cash;
import net.teamuni.cashmf.data.CashInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CashAPI {
    private static CashMF getInstance() {
        return CashMF.getPlugin(CashMF.class);
    }

    // 금액 확인
    public static long balance(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) return 0;
        if (!getInstance().getDatabase().hasAccount(player.getUniqueId())) return 0;
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return balance(cash);
    }

    public static long balance(OfflinePlayer player) {
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return balance(cash);
    }

    private static long balance(Cash cash) {
        if (cash == null) return -1;

        return cash.getInfo().cash();
    }

    // 금액 추가
    public static boolean add(String playerName, int amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) return false;
        if (!getInstance().getDatabase().hasAccount(player.getUniqueId())) return false;
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return add(cash, amount);
    }

    public static boolean add(OfflinePlayer player, int amount) {
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return add(cash, amount);
    }

    private static boolean add(Cash cash, long amount) {
        if (cash == null) return false;
        long oldCash = cash.getInfo().cash();
        long newCash = oldCash + amount;
        long cumulativeCash = cash.getInfo().cumulativeCash();

        cash.update(new CashInfo(newCash, cumulativeCash + amount));

        return true;
    }

    // 금액 차감
    public static boolean sub(String playerName, int amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) return false;
        if (!getInstance().getDatabase().hasAccount(player.getUniqueId())) return false;
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return sub(cash, amount);
    }

    public static boolean sub(OfflinePlayer player, int amount) {
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return sub(cash, amount);
    }

    private static boolean sub(Cash cash, long amount) {
        if (cash == null) return false;
        long oldCash = cash.getInfo().cash();
        long newCash = oldCash - amount;
        long cumulativeCash = cash.getInfo().cumulativeCash();

        cash.update(new CashInfo(newCash, cumulativeCash));

        return true;
    }

    // 금액 설정
    public static boolean set(String playerName, int amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) return false;
        if (!getInstance().getDatabase().hasAccount(player.getUniqueId())) return false;
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return set(cash, amount);
    }

    public static boolean set(OfflinePlayer player, int amount) {
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return set(cash, amount);
    }

    private static boolean set(Cash cash, long amount) {
        if (cash == null) return false;
        long oldCash = cash.getInfo().cash();
        long cumulativeCash;

        if (amount > oldCash) {
            cumulativeCash = cash.getInfo().cumulativeCash() + (amount - oldCash);
        } else {
            cumulativeCash = cash.getInfo().cumulativeCash();
        }
        cash.update(new CashInfo(amount, cumulativeCash));
        return true;
    }

    // 금액 초기화
    public static boolean reset(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) return false;
        if (!getInstance().getDatabase().hasAccount(player.getUniqueId())) return false;
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return reset(cash);
    }

    public static boolean reset(OfflinePlayer player) {
        Cash cash = getInstance().getCashManager().getCache(player.getUniqueId());

        return reset(cash);
    }

    private static boolean reset(Cash cash) {
        if (cash == null) return false;
        cash.update(new CashInfo(0, 0));

        return true;
    }
}
