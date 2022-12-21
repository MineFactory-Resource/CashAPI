package net.teamuni.cashmf;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class CashEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerUUID;
    private final long before;
    private final long after;

    public CashEvent(UUID playerUUID, long before, long after) {
        this.playerUUID = playerUUID;
        this.before = before;
        this.after = after;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public long getBefore() {
        return before;
    }

    public long getAfter() {
        return after;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}


