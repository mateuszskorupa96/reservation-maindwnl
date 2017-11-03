package pl.hycom.training.reservation.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import pl.hycom.training.reservation.integration.ws.api.ICarParkWS;

/**
 * Component contains configuration for CXF web-service
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class CXFConfig {

    @Bean
    public EndpointImpl soapServiceEndpoint(Bus cxfBus, ICarParkWS soapService) {
        
        EndpointImpl endpoint = new EndpointImpl(cxfBus, soapService);
        LoggingFeature lf = new LoggingFeature();
        lf.setPrettyLogging(true);
        
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("schema-validation-enabled", true);
        endpoint.setProperties(properties);
        endpoint.getFeatures().add(lf);
        endpoint.setAddress("/carpark");
        endpoint.publish();
        
        return endpoint;
    }
}