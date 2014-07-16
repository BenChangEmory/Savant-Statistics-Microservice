package configuration;

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

    @Valid
    @NotNull
    @JsonProperty
    private MongoConfig mongoConfig = new MongoConfig();

    public MongoConfig getMongoConfig() {
        return mongoConfig;
    }

    public void setMongoConfig(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
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
/*package realdoc.correspondence.mailroom.configuration;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import realdoc.correspondence.deprecated.vega.configuration.VegaConfiguration;
import realdoc.rest.cors.CorsConfigurable;
import realdoc.rest.cors.CorsConfiguration;
import realdoc.swagger.SwaggerConfigurable;
import realdoc.swagger.SwaggerConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class MailroomConfig extends Configuration implements SwaggerConfigurable, CorsConfigurable {

    @Valid
    @NotNull
    @JsonProperty
    private MongoConfig mongo = new MongoConfig();

    @NotNull
    @JsonProperty
    private RabbitConfig rabbit = new RabbitConfig();

    @Valid
    @NotNull
    @JsonProperty
    private SwaggerConfiguration swagger = new SwaggerConfiguration();
    @Valid
    @NotNull
    @JsonProperty
    private CorsConfiguration cors = new CorsConfiguration();

    @Valid
    @NotNull
    @JsonProperty
    private AkkaConfig akka = new AkkaConfig();

    @Valid
    @NotNull
    @JsonProperty
    private MysqlConfig mysql = new MysqlConfig();


    private VegaConfiguration vega = new VegaConfiguration();


    public MongoConfig getMongo() {
        return mongo;
    }

    public void setMongo(MongoConfig mongo) {
        this.mongo = mongo;
    }

    public RabbitConfig getRabbit() {
        return rabbit;
    }

    public void setRabbit(RabbitConfig rabbit) {
        this.rabbit = rabbit;
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

    public AkkaConfig getAkka() {
        return akka;
    }

    public void setAkka(AkkaConfig akka) {
        this.akka = akka;
    }

    public VegaConfiguration getVega() {
        return vega;
    }

    public void setVega(VegaConfiguration vega) {
        this.vega = vega;
    }

    public MysqlConfig getMysql() {
        return mysql;
    }

    public void setMysql(MysqlConfig mysql) {
        this.mysql = mysql;
    }
}
*/