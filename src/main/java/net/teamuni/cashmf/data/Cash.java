package net.teamuni.cashmf.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Cash {
    private final UUID uuid;
    private CashInfo info;

    public void update(CashInfo cashInfo) {
        this.info = cashInfo;
    }
}
