package net.teamuni.cashmf;

import lombok.Getter;
import net.teamuni.cashmf.api.Placeholder;
import net.teamuni.cashmf.api.database.Database;
import net.teamuni.cashmf.api.database.SQL;
import net.teamuni.cashmf.command.CashExecutor;
import net.teamuni.cashmf.command.CashTabCompleter;
import net.teamuni.cashmf.config.Conf;
import net.teamuni.cashmf.config.MessageConf;
import net.teamuni.cashmf.config.PlayerConf;
import net.teamuni.cashmf.data.CashManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CashMF extends JavaPlugin {
    private CashMF instance;
    private Conf conf;
    private MessageConf messageConf;
    private Database database;
    private CashManager cashManager;

    @Override
    public void onEnable() {
        this.instance = this;

        // *.yml 파일 설정
        this.messageConf = new MessageConf();
        this.conf = new Conf();

        // 데이터 저장 방식 설정
        if (this.conf.getDbInfoMap().get("type").equals("yaml")) { // Yaml
            this.database = new PlayerConf();
        } else { // MariaDB, MySQL, SQLite, PostgreSQL
            this.database = new SQL(this);
        }
        this.cashManager = new CashManager(this);

        // 이벤트 설정
        getServer().getPluginManager().registerEvents(new JoinAndQuit(this), this);

        // 명령어 설정
        this.getCommand("캐시").setExecutor(new CashExecutor(this));
        this.getCommand("캐시").setTabCompleter(new CashTabCompleter());

        // api 설정
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) { // PlaceholderAPI
            new Placeholder(this).register();
        } else {
            getLogger().info("PlaceholderAPI 플러그인이 발견되지 않았습니다.");
        }
    }

    @Override
    public void onDisable() {
        this.cashManager.removeAll();
    }
}
