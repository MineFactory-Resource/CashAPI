package net.teamuni.cashmf.api.database;

import static net.teamuni.cashmf.CashMF.getConf;
import static net.teamuni.cashmf.CashMF.getInstance;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.cashmf.Cash;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class SQL {
    private final HikariDataSource ds;

    private final String CREATE;
    private final String SELECT;
    private final String INSERT;
    private final String UPDATE;

    public SQL() {
        HikariConfig config = new HikariConfig();

        String url = "jdbc:" + getConf().database.get("type") + "://"
                + getConf().database.get("host") + "/"
                + getConf().database.get("database_name") + "?characterEncoding=utf8&useSSL=false";

        config.setJdbcUrl(url);
        config.setUsername(getConf().database.get("username"));
        config.setPassword(getConf().database.get("password"));

        ds = new HikariDataSource(config);

        CREATE = "CREATE TABLE IF NOT EXISTS `" + getConf().database.get("table") + "` ("
                + "`uuid` VARCHAR(36) PRIMARY KEY,"
                + "`cash` MEDIUMINT UNSIGNED NOT NULL"
                + ");";
        SELECT = "SELECT %s FROM `" + getConf().database.get("table") + "`;";
        INSERT = "INSERT INTO `" + getConf().database.get("table") + "` (`uuid`, `cash`) VALUES ('%1$s', '%2$s');";
        UPDATE = "UPDATE `" + getConf().database.get("table") + "` SET `cash` = %2$s WHERE `uuid` = '%1$s';";

        load();
    }

    // 테이블이 존재하지 않을 경우 생성
    public void checkTable() {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE);

        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테이블에서 데이터 불러오기
    public void load() {
        // Cash 데이터 초기화
        Cash.cashes = new HashMap<>();

        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(String.format(SELECT, "*"))) {
            while (rs.next()) {
                // 잘못된 uuid 형식일 경우 무시
                if (!Pattern.matches(Cash.UUID_PATTERN, rs.getString("uuid")))
                    continue;

                new Cash(UUID.fromString(rs.getString("uuid")), rs.getInt("cash"));
            }
        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테이블에 데이터 저장
    public void save() {
        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(String.format(SELECT, "uuid"))) {
            List<UUID> uuids = new ArrayList<>();
            while (rs.next()) {
                // 잘못된 uuid 형식일 경우 무시
                if (!Pattern.matches(Cash.UUID_PATTERN, rs.getString("uuid")))
                    continue;

                uuids.add(UUID.fromString(rs.getString("uuid")));
            }

            for (Cash cash : Cash.cashes.values()) {
                // 플레이어의 데이터가 테이블에 존재하는 경우 업데이트
                if (uuids.contains(cash.getUUID())) {
                    stmt.execute(String.format(UPDATE, cash.getUUID(), cash.getCash()));

                // 플레이어의 데이터가 테이블에 존재하지 않을 경우 추가
                } else {
                    stmt.execute(String.format(INSERT, cash.getUUID(), cash.getCash()));
                }
            }
        } catch (SQLSyntaxErrorException e) {
            getInstance().getLogger().warning("존재하지 않는 데이터베이스입니다.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            getInstance().getLogger().warning("데이터베이스 로그인에 실패했습니다.");

        } catch (SQLNonTransientConnectionException e) {
            getInstance().getLogger().warning("아이피나 포트가 잘못되었습니다.");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
