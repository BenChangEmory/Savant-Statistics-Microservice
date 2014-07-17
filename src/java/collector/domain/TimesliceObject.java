package collector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * Created by benjamin on 7/17/14.
 */
public class TimesliceObject {

    @Id
    @JsonProperty("_id")
    @XmlElement
    private ObjectId id;
    private int count;
    private Map <String, String> group;
    private String size;
    private long slice;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<String, String> getGroup() {
        return group;
    }

    public void setGroup(Map<String, String> group) {
        this.group = group;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getSlice() {
        return slice;
    }

    public void setSlice(long slice) {
        this.slice = slice;
    }
}
