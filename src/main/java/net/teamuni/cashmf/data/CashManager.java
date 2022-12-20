package net.teamuni.cashmf.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.api.database.Database;
import net.teamuni.cashmf.config.MessageConf;
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
                            if (data == null | database == null) return;
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

    public void look(Player player, OfflinePlayer target) {
        if (target == null || !main.getDatabase().hasAccount(target.getUniqueId())) return;
        MessageConf messageConf = main.getMessageConf();
        Map<String, List<String>> messageMap = messageConf.getMessages();
        Cash cash = main.getCashManager().getCache(target.getUniqueId());
        long point = cash.getInfo().cash();

        messageConf.translate((Player) target, messageMap.get("look_cash")
                        , "%player%", target.getName()
                        , "%cash%", String.valueOf(point))
                .forEach(player::sendMessage);
    }

    public void edit(OfflinePlayer target, EditType type, long amount) {
        if (target == null || !main.getDatabase().hasAccount(target.getUniqueId())) return;
        Cash data = main.getCashManager().getCache(target.getUniqueId());
        long oldCash = data.getInfo().cash();

        if (type == EditType.ADD) {
            long newCash = oldCash + amount;
            data.update(new CashInfo(newCash, data.getInfo().cumulativeCash()));

        } else if (type == EditType.SUB) {
            long newCash = oldCash - amount;
            if (newCash < 0) {
                newCash = 0;
            }
            data.update(new CashInfo(newCash, data.getInfo().cumulativeCash()));
        }

        if (!target.isOnline()) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                main.getDatabase().save(data);
            });
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

    public enum EditType {
        ADD, SUB
    }
}
