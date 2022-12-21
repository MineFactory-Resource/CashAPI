CashAPI
=======
----
플러그인 소개
-----------
캐시 재화 역할을 맡고있는 플러그인입니다.

----
플러그인 적용 방법
-----------

1. [release](https://github.com/MineFactory-Resource/CashAPI/releases) 항목에서 최신 버전의 플러그인 파일을 다운로드
2. 서버 속 plugins 폴더에 다운로드 받은 *.jar 파일을 넣기

----
messages.yml 사용 방법
-----------
플러그인의 메세지를 messages.yml 파일을 통해 변경할 수 있습니다.
예시)

```yaml
cash_command_details:
   - ""
   - "&e[알림] &f자세한 명령어는 &e/캐시 도움말 &f명령어를 통해 확인하실 수 있습니다."
   - ""
```

위와 같이 큰 따옴표 속 메세지를 변경할 경우, 플러그인의 메세지가 변경됩니다.

----
config.yml 사용 방법
-----------
해당 플러그인의 config.yml에서는 데이터베이스 설정을 할 수 있습니다.

- DataBase 설정
    ```yaml
    database:
        type: "yaml"
        address: "localhost"
        port: "3306"
        database_name: ""
        table: "CashAPI"
        username: ""
        password: ""
    ```
   type 항목에서 저장 방식을 변경할 수 있습니다. (기본: yaml - players.yml)  
   `Yaml, MySQL, MariaDB, PostgreSQL, SQLite, MongoDB`
----
PlaceHolderAPI 지원 정보
-----------
CashAPI 플러그인은 PlaceHolderAPI와 연동하여 사용하실 수 있습니다.  
PlaceHolderAPI와 함께 사용 중에 PlaceHolderAPI 플러그인에서 `%CashAPI_cash%` 플레이스홀더를 통해 캐시 정보를 불러올 수 있습니다.

----
CashAPI 라이브러리
-----------
CashAPI 플러그인을 라이브러리로 사용할 경우, plugins.yml에서 depend 설정을 해야합니다.

```yaml
depend:
  - CashAPI
```

----
API 사용 방법
-----------
CashAPI 플러그인을 라이브러리로 사용할 경우, CashAPI 클래스로 간단하게 cash 값을 조절할 수 있습니다.

```java
import net.teamuni.cashmf.api.CashAPI;
import org.bukkit.OfflinePlayer;

public class Example {
    // OfflinePlayer 클래스를 통해 플레이어 값을 가져올 경우
    public void exampleOne(OfflinePlayer offlinePlayer, int val) {
        // 캐시 값 확인
        CashAPI.balance(offlinePlayer);

        // 캐시 추가
        CashAPI.add(offlinePlayer, val);

        // 캐시 차감
        CashAPI.sub(offlinePlayer, val);

        // 캐시 설정
        CashAPI.set(offlinePlayer, val);

        // 캐시 초기화
        CashAPI.reset(offlinePlayer);
    }

    // 문자열로 플레이어 닉네임을 통해 플레이어 값을 가져올 경우
    public void exampleTwo(String playerName, int val) {
        // 캐시 값 확인
        CashAPI.balance(playerName);

        // 캐시 추가
        CashAPI.add(playerName, val);

        // 캐시 차감
        CashAPI.sub(playerName, val);

        // 캐시 설정
        CashAPI.set(playerName, val);

        // 캐시 초기화
        CashAPI.reset(playerName);
    }
}
```

----
CashEvent 사용 방법
-----------
CashAPI 플러그인에는 자체 Event가 존재하며, 캐시가 변동될때, 플레이어의 uuid와 변동 전 값, 변동 후 값이 기록됩니다.  
CashAPI 플러그인을 라이브러리로 사용할 경우, CashEvent 클래스를 통해 값을 가져올 수 있습니다.

```java
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.teamuni.cashmf.CashEvent;

import java.util.UUID;

public class Example implements Listener {
    @EventHandler
    public void onCashEvent(CashEvent e) {
        UUID uuid = e.getPlayerUUID();
        int after = e.after;
        int before = e.before;
    }
}
```