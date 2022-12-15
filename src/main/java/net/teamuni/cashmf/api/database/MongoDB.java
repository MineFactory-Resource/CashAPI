package net.teamuni.cashmf.api.database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.teamuni.cashmf.Cash;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import static net.teamuni.cashmf.CashMF.getConf;

public class MongoDB implements Database {
    private final MongoClientURI uri;

    public MongoDB() {
        String url = "mongodb://" + getConf().database.get("username") + ":"
                + getConf().database.get("password") + "@"
                + getConf().database.get("address") + ":"
                + getConf().database.get("port") + "/?authSource="
                + getConf().database.get("database_name");
        uri = new MongoClientURI(url);

        load();
    }

    // 콜렉션에서 데이터 불러오기
    @Override
    public void load() {
        try (MongoClient client = new MongoClient(uri)) {
            MongoDatabase db = client.getDatabase(getConf().database.get("database_name"));
            MongoCollection<Document> collection = db.getCollection(getConf().database.get("table"));

            // Cash 데이터 초기화
            Cash.cashes = new HashMap<>();

            try (MongoCursor<Document> cursor = collection.find().cursor()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    if (doc.containsKey("uuid") && doc.containsKey("cash") && Pattern.matches(Cash.UUID_PATTERN, doc.getString("uuid"))) {
                        new Cash(UUID.fromString(doc.getString("uuid")), doc.getInteger("cash"));
                    }
                }
            }
        }
    }

    // 콜렉션에 데이터 저장
    @Override
    public void save() {
        try (MongoClient client = new MongoClient(uri)) {
            MongoDatabase db = client.getDatabase(getConf().database.get("database_name"));
            MongoCollection<Document> collection = db.getCollection(getConf().database.get("table"));

            for (Cash cash : Cash.cashes.values()) {
                BasicDBObject keyObject = new BasicDBObject("uuid", cash.getUUID().toString());
                BasicDBObject cashObject = new BasicDBObject("cash", cash.getCash());
                BasicDBObject updateObject = new BasicDBObject("$set", cashObject);

                collection.updateOne(keyObject, updateObject, new UpdateOptions().upsert(true));
            }
        }

    }
}
