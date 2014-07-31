package spring;

import collector.domain.OplogDataCollector;
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
    public OplogDataCollector oplogDataCollectorService(){
        return new OplogDataCollector();
    }


  @Bean
  public OplogDataCollector tailableService(){
      return new OplogDataCollector();}

}

