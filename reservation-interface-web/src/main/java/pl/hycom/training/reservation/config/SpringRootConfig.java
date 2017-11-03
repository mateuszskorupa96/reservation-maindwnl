package pl.hycom.training.reservation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration
 * 
 * @author Hubert Bieńkowski (hubert.bienkowski@hycom.pl) on 24/06/2016.
 */
@Configuration
@ComponentScan(basePackages = { "pl.hycom.training.reservation" })
public class SpringRootConfig {
}
