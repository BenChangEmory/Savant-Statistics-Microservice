package configuration;

//import collector.service.DispatchService;
import collector.service.OplogDataCollector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import jackson.JodaTimeAsInt64Module;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * User: Bogdan Apetrei
 * Date: 29.04.2014
 * Time: 15:10
 */

@Configuration
public class SpringConfig {

    @Autowired
    private CollectorConfig config;

    @Bean
    public DB syncMongo() throws Exception {
        MongoConfig mongoConfig = config.getMongoConfig();
        DB db = new MongoClient(mongoConfig.getHost(), mongoConfig.getPort())
                .getDB(mongoConfig.getDb());
        db.setWriteConcern(WriteConcern.ACKNOWLEDGED);

        return db;
    }

/*
    @Bean(name="rabbitFactory")
    public ConnectionFactory rabbitFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        RabbitConfig rabbitConfig = config.getRabbit();
        factory.setHost(rabbitConfig.getHost());
        factory.setPort(rabbitConfig.getPort());
        factory.setUsername(rabbitConfig.getUsername());
        factory.setPassword(rabbitConfig.getPassword());
        factory.setVirtualHost(rabbitConfig.getVhost());
        return factory;
    }
*/

    @Bean
    public Jongo jongo() throws Exception {
        return new Jongo(syncMongo(), new JacksonMapper.Builder()
                .registerModule(new JodaTimeAsInt64Module())
                .registerModule(new JongoIdModule())
                .registerModule(new DefaultScalaModule())
                .enable(MapperFeature.AUTO_DETECT_GETTERS)
                .build());
    }

    @Bean
    public OplogDataCollector oplogDataCollectorService(){
        return new OplogDataCollector();
    }


  /*  @Bean
    public DispatchService dispatchService(){
        return new DispatchService();
    }*/
  @Bean
  public OplogDataCollector tailableService(){
      return new OplogDataCollector();}

}

