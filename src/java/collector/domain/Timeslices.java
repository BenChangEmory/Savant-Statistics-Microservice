package collector.domain;

/**
 * Created by benjamin on 7/17/14.
 */
public enum Timeslices {
    FIVE_MINUTES(300000l),
    TEN_MINUTES(600000l),
    THIRTY_MINUTES(1800000l),
    ONE_HOUR(3600000l),
    ONE_DAY(86400000l);

    public long value;
    private Timeslices(long value){
        this.value = value;
    }
};
