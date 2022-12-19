package net.teamuni.cashmf.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.data.Cash;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Placeholder extends PlaceholderExpansion {
    private final CashMF main;
    private final DecimalFormat df = new DecimalFormat("###,###");

    public Placeholder(CashMF instance) {
        this.main = instance;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "CashAPI";
    }

    @Override
    public @NotNull String getAuthor() {
        return "All_Lie";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("cash")) {
            Cash cash = main.getCashManager().getCacheIfPresent(player.getUniqueId());
            if (cash == null) return "";
            return df.format(cash.getInfo().cash());
        }
        if (params.equalsIgnoreCase("cumulative_cash")) {
            Cash cash = main.getCashManager().getCacheIfPresent(player.getUniqueId());
            if (cash == null) return "";
            return df.format(cash.getInfo().cumulativeCash());
        }
        return null;
    }
}
