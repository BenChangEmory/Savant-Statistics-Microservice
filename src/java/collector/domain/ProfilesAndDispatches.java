package collector.domain;

/**
 * Created by benjamin on 7/18/14.
 */
public class ProfilesAndDispatches {

    private String profiles;
    private int dispatches;

    public ProfilesAndDispatches(String profiles, int dispatches) {
        this.profiles = profiles;
        this.dispatches = dispatches;
    }

    public int getDispatches() {
        return dispatches;
    }

    public void setDispatches(int dispatches) {
        this.dispatches = dispatches;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }
}
