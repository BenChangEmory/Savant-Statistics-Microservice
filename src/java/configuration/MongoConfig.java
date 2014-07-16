package configuration;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.apache.commons.lang.UnhandledException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.net.UnknownHostException;

/**
 * Created by benjamin on 6/11/14.
 */

@Configuration
public class MongoConfig {

    private String host = "localhost";
    private String db = "local";
    private Integer port = 27017;
    private String username;
    private String password;

    public String getHost(){
        return host;
    }

    public void setHost(String host){
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    public DB getDb (String myMongoClient, int port, String dbName) throws UnknownHostException{
        MongoClient mongoClient = new MongoClient(myMongoClient, port);
        DB db = mongoClient.getDB(dbName);
        return db;
    }



}

