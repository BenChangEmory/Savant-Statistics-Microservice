package collector.domain;

import org.joda.time.DateTime;

/**
 * Created by benjamin on 6/11/14.
 */
public class DispatchStatus {

    private String code;
    private DateTime time;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }
}
