package collector.service;

import com.github.nhuray.dropwizard.spring.SpringBundle;
import com.sun.jersey.api.core.ResourceConfig;
import configuration.CollectorConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import realdoc.rest.cors.CorsBundle;
import realdoc.rest.filter.RelativeLinkHeaderRewritingContainerFilter;
import realdoc.rest.versions.VersionsBundle;
import realdoc.swagger.SwaggerBundle;

/**
 * Created by benjamin on 6/10/14.
 */
public class CollectorApplication extends Application<CollectorConfig> {


    public static void main(final String[] args) throws Exception {
        new CollectorApplication().run(args);
    }


    private ConfigurableApplicationContext applicationContext() throws BeansException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("collector");
        context.scan("configuration");
        return context;
    }

    @Override
    public String getName() {
        return "Savant";
    }

    @Override
    public void initialize(final Bootstrap<CollectorConfig> bootstrap) {
        bootstrap.addBundle(new SpringBundle(applicationContext(), true, true, true));
        bootstrap.addBundle(new SwaggerBundle("2.0"));
        bootstrap.addBundle(new VersionsBundle(this));
        bootstrap.addBundle(new CorsBundle());
    }

    @Override
    public void run(final CollectorConfig configuration,
                    final Environment environment) {
        environment.jersey().property(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, new String[]{
                RelativeLinkHeaderRewritingContainerFilter.class.getCanonicalName()
        });
    }


}

