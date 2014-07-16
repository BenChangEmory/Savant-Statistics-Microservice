package collector.domain;

import org.bson.types.BSONTimestamp;

import java.util.List;

/**
 * Created by benjamin on 7/1/14.
 */
public class Updates{

    BSONTimestamp ts;
    long h;
    int v;
    String op;
    String ns;
    Object o;

    public BSONTimestamp getTs() {
        return ts;
    }

    public void setTs(BSONTimestamp ts) {
        this.ts = ts;
    }

    private List<Object> entry;

    public List<Object> getEntry() {
        return entry;
    }

    public void setEntry(List<Object> entry) {
        this.entry = entry;
    }

    public void addToList(Object myEntry){
        this.entry.add(myEntry);
    }
}
