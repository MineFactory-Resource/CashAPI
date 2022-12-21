package net.teamuni.cashmf.api.database;

import net.teamuni.cashmf.data.Cash;

import java.util.UUID;

public interface Database {
    Cash load(UUID uuid);
    boolean hasAccount(UUID uuid);
    void save(Cash data);
}
