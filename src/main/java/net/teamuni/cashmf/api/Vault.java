package net.teamuni.cashmf.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.teamuni.cashmf.Cash;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class Vault implements Economy {

    // 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 플레이어 잔액 조회
    @Override
    public double getBalance(String playerName) {
        Cash cash = Cash.getCash(playerName);
        if (cash != null)
            return cash.getCash();

        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash != null)
            return cash.getCash();

        return 0;
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    // 플레이어 잔액 추가
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Cash cash = Cash.getCash(playerName);
        if (cash != null) {
            cash.addCash((int) amount);
            return new EconomyResponse(amount, cash.getCash(), EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash != null) {
            cash.addCash((int) amount);
            return new EconomyResponse(amount, cash.getCash(), EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    // 플레이어 잔액 차감
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Cash cash = Cash.getCash(playerName);
        if (cash != null) {
            cash.addCash((int) -amount);
            return new EconomyResponse(amount, cash.getCash(), EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash != null) {
            cash.addCash((int) -amount);
            return new EconomyResponse(amount, cash.getCash(), EconomyResponse.ResponseType.SUCCESS, "");
        }

        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    // 플러그인 이름
    @Override
    public String getName() {
        return "CashMF";
    }

    // 사용하지 않는 기능들
    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
