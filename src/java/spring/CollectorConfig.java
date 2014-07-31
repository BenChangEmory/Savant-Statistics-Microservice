package spring;

import collector.configuration.DestinationDBConfiguration;
import collector.configuration.SourceDBConfiguration;
import collector.configuration.TargettedDBConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import realdoc.rest.cors.CorsConfigurable;
import realdoc.rest.cors.CorsConfiguration;
import realdoc.swagger.SwaggerConfigurable;
import realdoc.swagger.SwaggerConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by benjamin on 6/11/14.
 */
public class CollectorConfig extends Configuration implements SwaggerConfigurable, CorsConfigurable {


    @Valid
    @NotNull
    @JsonProperty
    private SwaggerConfiguration swagger = new SwaggerConfiguration();
    @Valid
    @NotNull
    @JsonProperty
    private CorsConfiguration cors = new CorsConfiguration();

    private DestinationDBConfiguration mongoDestination = new DestinationDBConfiguration();

    private SourceDBConfiguration mongoSource = new SourceDBConfiguration();

    private TargettedDBConfiguration targettedDb = new TargettedDBConfiguration();

    public TargettedDBConfiguration getTargettedDb() {
        return targettedDb;
    }

    public void setTargettedDb(TargettedDBConfiguration targettedDb) {
        this.targettedDb = targettedDb;
    }

    public DestinationDBConfiguration getMongoDestination() {
        return mongoDestination;
    }

    public void setMongoDestination(DestinationDBConfiguration mongoDestination) {
        this.mongoDestination = mongoDestination;
    }

    public SourceDBConfiguration getMongoSource() {
        return mongoSource;
    }

    public void setMongoSource(SourceDBConfiguration mongoSource) {
        this.mongoSource = mongoSource;
    }

    public SwaggerConfiguration getSwagger() {
        return swagger;
    }

    public void setSwagger(SwaggerConfiguration swagger) {
        this.swagger = swagger;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public void setCors(CorsConfiguration cors) {
        this.cors = cors;
    }




}