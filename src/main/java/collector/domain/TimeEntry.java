package collector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by benjamin on 7/15/14.
 */

public class TimeEntry {

    @Id
    @JsonProperty("_id")
    @XmlElement
    private ObjectId id;
    private BSONTimestamp ts;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public BSONTimestamp getTs() {
        return ts;
    }

    public void setTs(BSONTimestamp ts) {
        this.ts = ts;
    }
}
