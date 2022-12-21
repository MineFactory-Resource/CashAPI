package net.teamuni.cashmf.config;

import lombok.Getter;

import java.util.HashMap;

public class Conf extends Frame {
    @Getter
    public HashMap<String, String> dbInfoMap;

    public Conf() {
        super("config");
    }

    @Override
    protected void load() {
        super.load();

        getDatabase();
    }

    private void getDatabase() {
        HashMap<String, String> db = new HashMap<>();

        db.put("type", config.getString("database.type").toLowerCase());

        // yaml 방식일 경우 아래 정보가 필요 없으므로 리턴
        if (db.get("type").equals("yaml")) {
            this.dbInfoMap = db;
            return;
        }

        db.put("address", config.getString("database.address"));
        db.put("port", config.getString("database.port"));
        db.put("database_name", config.getString("database.database_name"));
        db.put("table", config.getString("database.table"));
        db.put("username", config.getString("database.username"));
        db.put("password", config.getString("database.password"));

        this.dbInfoMap = db;
    }
}
