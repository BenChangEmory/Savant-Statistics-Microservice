package collector.domain;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by benjamin on 6/11/14.
 */
public class Dispatch {

    private String id;
    private DispatchStatus status;
    private DateTime createdTime;
    private List<DispatchStatus> statusHistory;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public void setStatus(DispatchStatus status) {
        this.status = status;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
        this.createdTime = createdTime;
    }


    public List<DispatchStatus> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<DispatchStatus> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void addToStatusHistory(DispatchStatus dispatchStatus){

        this.statusHistory.add(dispatchStatus);


    }
}
