package net.teamuni.cashmf;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class CashEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerUUID;
    public final int before;
    public final int after;

    public CashEvent(UUID playerUUID, int before, int after) {
        this.playerUUID = playerUUID;
        this.before = before;
        this.after = after;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}


