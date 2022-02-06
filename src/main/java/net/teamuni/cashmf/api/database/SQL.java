package net.teamuni.cashmf.api.database;

import static net.teamuni.cashmf.CashMF.getConf;
import static net.teamuni.cashmf.CashMF.getInstance;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.teamuni.cashmf.Cash;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class SQL implements Database {
    private final HikariDataSource ds;

    private final String CREATE;
    private final String SELECT;
    private final String UPSERT;

    public SQL() {
        HikariConfig config = new HikariConfig();

        String url = "jdbc:" + getConf().database.get("type") + "://"
                + getConf().database.get("address") + ":"
                + getConf().database.get("port") + "/"
                + getConf().database.get("database_name") + "?characterEncoding=utf8&useSSL=false";

        config.setJdbcUrl(url);
        config.setUsername(getConf().database.get("username"));
        config.setPassword(getConf().database.get("password"));

        ds = new HikariDataSource(config);

        CREATE = "CREATE TABLE IF NOT EXISTS `" + getConf().database.get("table") + "` ("
                + "`uuid` VARCHAR(36) PRIMARY KEY,"
                + "`cash` MEDIUMINT UNSIGNED NOT NULL"
                + ");";
        SELECT = "SELECT * FROM `" + getConf().database.get("table") + "`;";
        UPSERT = "INSERT INTO `" + getConf().database.get("table") + "` (`uuid`, `cash`) VALUES ('%1$s', '%2$s') ON DUPLICATE KEY UPDATE `cash` = '%2$s';";

        load();
    }

    // 테이블이 존재하지 않을 경우 생성
    private void checkTable() {
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
    @Override
    public void load() {
        // Cash 데이터 초기화
        Cash.cashes = new HashMap<>();

        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT)) {
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
    @Override
    public void save() {
        checkTable();

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();) {

            for (Cash cash : Cash.cashes.values()) {
                    stmt.execute(String.format(UPSERT, cash.getUUID(), cash.getCash()));
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
