package net.teamuni.cashmf.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import java.text.DecimalFormat;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.api.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class CashManager {
    private final CashMF main;
    private final LoadingCache<UUID, Cash> cache;


    public CashManager(CashMF instance) {
        this.main = instance;
        this.cache = CacheBuilder.newBuilder().removalListener(
                RemovalListeners.asynchronous((RemovalListener<UUID, Cash>) notify -> {
                    Cash data = notify.getValue();
                    Database database = instance.getDatabase();
                    if (data == null | database == null) {
                        return;
                    }
                    database.save(data);
                }, Executors.newFixedThreadPool(5)))
            .build(new CacheLoader<>() {

                @Override
                public @NotNull Cash load(@NotNull UUID uuid) {
                    Database database = instance.getDatabase();
                    if (database == null) {
                        return new Cash(uuid, new CashInfo(0, 0));
                    }
                    return database.load(uuid);
                }
            });
    }

    public void remove(Player player) {
        this.cache.invalidate(player.getUniqueId());
        this.cache.cleanUp();
    }

    public void removeAll() {
        this.cache.invalidateAll();
        this.cache.cleanUp();
    }

    @NotNull
    public Cash getCache(@NotNull UUID uuid) {
        return this.cache.getUnchecked(uuid);
    }

    @Nullable
    public Cash getCacheIfPresent(@NotNull UUID uuid) {
        return this.cache.getIfPresent(uuid);
    }

    public void look(Player player, OfflinePlayer target, boolean isCumul) {
        if (isInvalidPlayer(player)) {
            return;
        }

        DecimalFormat df = new DecimalFormat("###,###");
        Map<String, List<String>> messageMap = main.getMessageConf().getMessages();
        Cash cash = main.getCashManager().getCache(target.getUniqueId());
        String msgKey = isCumul ? "look_cumul_cash" : "look_cash";
        long point = isCumul ? cash.getInfo().cumulativeCash() : cash.getInfo().cash();

        main.getMessageConf().translate(
                (Player) target,
                messageMap.get(msgKey),
                "%player%", target.getName(),
                "%cash%", df.format(point))
            .forEach(player::sendMessage);
    }

    public void edit(OfflinePlayer target, EditType type, long amount) {
        if (isInvalidPlayer(target)) {
            return;
        }

        Cash data = main.getCashManager().getCache(target.getUniqueId());
        long oldCash = data.getInfo().cash();
        long newCash;

        switch (type) {
            case ADD -> newCash = oldCash + amount;
            case SUB -> newCash = Math.max(0, oldCash - amount);
            default -> {
                return;
            }
        }

        updateData(data, newCash);

        if (!target.isOnline()) {
            saveDataAsync(data);
        }
    }

    public void updateCumul(OfflinePlayer player, long amount) {
        Cash data = main.getCashManager().getCache(player.getUniqueId());
        long cash = data.getInfo().cash();
        long cumulativeCash = data.getInfo().cumulativeCash();
        data.update(new CashInfo(cash, cumulativeCash + amount));

        if (!player.isOnline()) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                main.getDatabase().save(data);
            });
        }
    }

    private boolean isInvalidPlayer(OfflinePlayer player) {
        return player == null || !main.getDatabase().hasAccount(player.getUniqueId());
    }

    private void updateData(Cash data, long newCash) {
        CashInfo updatedInfo = new CashInfo(newCash, data.getInfo().cumulativeCash());
        data.update(updatedInfo);
    }

    private void saveDataAsync(Cash data) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> main.getDatabase().save(data));
    }

    public enum EditType {
        ADD, SUB
    }
}
