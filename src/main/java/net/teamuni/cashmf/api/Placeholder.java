package net.teamuni.cashmf.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.teamuni.cashmf.Cash;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "CashMF";
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
        if(params.equalsIgnoreCase("cash")){
            Cash cash = Cash.getCash(player.getUniqueId());
            return String.valueOf(cash.getCash());
        }

        return null;
    }
}
