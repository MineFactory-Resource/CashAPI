package net.teamuni.cashmf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoin implements Listener {
    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {
        // 만약 플레이어의 캐시 정보가 존재하지 않을 경우 새로 생성
        if (!Cash.cashes.containsKey(e.getPlayer().getUniqueId()))
            new Cash(e.getPlayer().getUniqueId());
    }
}
