package collector.domain;

/**
 * Created by benjamin on 8/6/14.
 */
public class GraphRequest {

    private String size;
    private long lowerTime;
    private long upperTime;
    private String[] clients;
    private String[] profiles;


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getLowerTime() {
        return lowerTime;
    }

    public void setLowerTime(long lowerTime) {
        this.lowerTime = lowerTime;
    }

    public long getUpperTime() {
        return upperTime;
    }

    public void setUpperTime(long upperTime) {
        this.upperTime = upperTime;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }
}
