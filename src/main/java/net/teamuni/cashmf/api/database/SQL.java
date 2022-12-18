package net.teamuni.cashmf.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.data.Cash;
import net.teamuni.cashmf.data.CashInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SQL implements Database {
    private final HikariDataSource ds;
    private final CashMF main;

    public SQL(CashMF instance) {
        this.main = instance;
        HikariConfig config = new HikariConfig();

        String url = "jdbc:" + instance.getConf().getDbInfoMap().get("type") + "://"
                + instance.getConf().getDbInfoMap().get("address") + ":"
                + instance.getConf().getDbInfoMap().get("port") + "/"
                + instance.getConf().getDbInfoMap().get("database_name") + "?characterEncoding=utf8&useSSL=false";

        config.setJdbcUrl(url);
        config.setUsername(instance.getConf().getDbInfoMap().get("username"));
        config.setPassword(instance.getConf().getDbInfoMap().get("password"));

        ds = new HikariDataSource(config);

        try {
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테이블이 존재하지 않을 경우 생성
    private void initTable() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS " + main.getConf().getDbInfoMap().get("table")
                    + "(uuid VARCHAR(36) PRIMARY KEY, cash MEDIUMINT, cumulative_cash MEDIUMINT);";
            stmt.execute(query);
        }
    }

    // 테이블에서 데이터 불러오기
    @Override
    public Cash load(UUID uuid) {
        try {
            if (hasAccount(uuid)) {
                try (Connection connection = this.ds.getConnection()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("SELECT cash, cumulative_cash FROM ")
                            .append(main.getConf().getDbInfoMap().get("table"))
                            .append(" WHERE uuid = '")
                            .append(uuid)
                            .append("'");

                    try (Statement statement = connection.createStatement()) {
                        ResultSet result = statement.executeQuery(builder.toString());
                        if (result.next()) {
                            return new Cash(uuid, new CashInfo(result.getLong(1), result.getLong(2)));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Cash(uuid, new CashInfo(0, 0));
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        try {
            try (Connection connection = this.ds.getConnection()) {
                StringBuilder builder = new StringBuilder();
                builder.append("SELECT uuid FROM ")
                        .append(main.getConf().getDbInfoMap().get("table"))
                        .append(" WHERE uuid = '")
                        .append(uuid)
                        .append("'");

                try (Statement statement = connection.createStatement()) {
                    ResultSet result = statement.executeQuery(builder.toString());
                    return result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 테이블에 데이터 저장
    @Override
    public void save(Cash data) {
        try {
            try (Connection connection = this.ds.getConnection()) {
                StringBuilder builder = new StringBuilder();
                builder.append("INSERT INTO ")
                        .append(main.getConf().getDbInfoMap().get("table"))
                        .append(" (uuid, cash, cumulative_cash) VALUE ('")
                        .append(data.getUuid())
                        .append("', ")
                        .append(data.getInfo().cash())
                        .append(", ")
                        .append(data.getInfo().cumulativeCash())
                        .append(") ON DUPLICATE KEY UPDATE ")
                        .append("cash = ")
                        .append(data.getInfo().cash())
                        .append(", cumulative_cash = ")
                        .append(data.getInfo().cumulativeCash());

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(builder.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
