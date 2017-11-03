package pl.hycom.training.reservation.config;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import pl.hycom.training.reservation.integration.ws.api.ICarParkWS;

/**
 * Class provides configuration for CXF client
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@PropertySource(value = { "classpath:application.properties" })
public class CXFClientConfig {
    
    @Value("${ws.api.url}")
    private String wsServerUrl;

    @Bean(name = "clientWS")
    public ICarParkWS peopleWSService() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ICarParkWS.class);
        factory.setAddress(wsServerUrl);

        LoggingFeature lf = new LoggingFeature();
        lf.setPrettyLogging(true);
        factory.getFeatures().add(lf);

        ICarParkWS client = (ICarParkWS) factory.create();
        return client;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
