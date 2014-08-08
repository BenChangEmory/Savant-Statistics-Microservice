package collector.configuration;

/**
 * Created by benjamin on 7/31/14.
 */
public class DestinationDBConfiguration {

    int port ;
    String host ;
    String database ;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
