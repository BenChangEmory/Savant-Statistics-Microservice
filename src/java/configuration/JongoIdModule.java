package configuration;

import com.fasterxml.jackson.core.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jongo.marshall.jackson.oid.Id;
import realdoc.document.domain.RDFile;
import realdoc.document.domain.RDFileSet;
import realdoc.document.domain.RDPersistable;

import java.io.IOException;

/**
 * @author Ionuț Păduraru
 *
 * TODO remove this once we cleanup the domain model from RealDoc
 *
 */
public class JongoIdModule extends SimpleModule {

    public static class ObjectIdDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Object value = jp.getEmbeddedObject();
            if (value != null) {
                return value.toString();
            }
            return null;
        }
    }

    public static class IdMixin {
        @JsonDeserialize( using=  ObjectIdDeserializer.class )
        @Id
        protected String id;
    }

    public JongoIdModule() {
        super("JongoIdModule");
        setMixInAnnotation(RDPersistable.class, IdMixin.class);
        setMixInAnnotation(RDFile.class, IdMixin.class);
        setMixInAnnotation(RDFileSet.Entry.class, IdMixin.class);
    }
}

