package net.teamuni.cashmf;

import net.teamuni.cashmf.data.Cash;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinAndQuit implements Listener {
    private final CashMF main;

    public JoinAndQuit(CashMF instance) {
        this.main = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Cash data = main.getCashManager().getCache(e.getPlayer().getUniqueId());
            if (!main.getDatabase().hasAccount(e.getPlayer().getUniqueId())) {
                main.getDatabase().save(data);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        main.getCashManager().remove(e.getPlayer());
    }
}
