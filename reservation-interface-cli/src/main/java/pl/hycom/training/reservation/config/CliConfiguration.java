package pl.hycom.training.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.service.impl.ReservationServiceCLIImpl;

/**
 * Spring basic configuration
 * 
 * @author Hubert Bieńkowski (hubert.bienkowski@hycom.pl) on 28/06/2016.
 */
@Configuration
@ComponentScan(basePackages = "pl.hycom.training.reservation")
public class CliConfiguration {

    /**
     * Bean provides instance of {@link ReservationServiceCLIImpl} object
     * 
     * @return spring bean
     */
    @Bean(name = "reservationService")
    IReservationService reservationService() {
        return new ReservationServiceCLIImpl();
    }
}
