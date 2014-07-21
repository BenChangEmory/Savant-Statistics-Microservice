package collector.domain;


import java.util.List;

/**
 * Created by benjamin on 7/18/14.
 */
public class PhaseCount {

    private String status;
    private int profiles;
    private int count;
    private List<ProfilesAndDispatches> list;


    public PhaseCount(String status, int profiles, int count, List<ProfilesAndDispatches> list) {
        this.status = status;
        this.profiles = profiles;
        this.count = count;
        this.list = list;
    }

    public List<ProfilesAndDispatches> getList() {
        return list;
    }

    public void setList(List<ProfilesAndDispatches> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProfiles() {
        return profiles;
    }

    public void setProfiles(int profiles) {
        this.profiles = profiles;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
