package net.teamuni.cashmf.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.api.database.Database;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.Executors;

public class CashManager {
    private final LoadingCache<UUID, Cash> cache;

    public CashManager(CashMF instance) {
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
}
