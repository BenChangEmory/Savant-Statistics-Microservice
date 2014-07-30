package collector.domain;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

/**
 * Created by benjamin on 7/17/14.
 */
public class DbConfig {
    public MongoCollection useJongo(String Host, int Port, String Db, String Col) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(Host, Port);
        DB db = mongoClient.getDB(Db);
        Jongo jongo = new Jongo(db);
        MongoCollection mongoCollection = jongo.getCollection(Col);
        return mongoCollection;
    }

    public DBCollection useDBCollection(String Host, int Port, String Db, String Col) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(Host, Port);
        DB db = mongoClient.getDB(Db);
        DBCollection dbCollection = db.getCollection(Col);
        return dbCollection;
    }
}
