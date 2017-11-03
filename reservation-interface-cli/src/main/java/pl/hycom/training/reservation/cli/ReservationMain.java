package pl.hycom.training.reservation.cli;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.model.ParkingDTO;

/**
 * Entry point for CLI interface.
 *
 * @author Adam Badura (adam.badura@hycom.pl), HYCOM S.A.
 */
@Component
public class ReservationMain {
    
    private static final Logger LOG = LogManager.getLogger(ReservationMain.class);
    
    /**
     * Object provides methods for operation on parking
     */
    static IReservationService reservationService;
    
    static ParkingStructureHelper parkingStructureHelper;

	public static void main(String[] args) {
		boolean exit = false;
		Scanner scanner = new Scanner(System.in);

		ApplicationContext context = new AnnotationConfigApplicationContext("pl.hycom.training.reservation");

		reservationService = (IReservationService) context.getBean("reservationService");
		parkingStructureHelper = ParkingStructureHelper.getInstance();

		while (!exit) {
			parkingStructureHelper.printHelp();
			switch (scanner.nextInt()) {
				case 1:
					printInfo(reservationService.getAllParkings());
					break;
				case 2:
					LOG.info("Put parking ID:");
					String parkingId = scanner.next();
					printParkingById(Integer.valueOf(parkingId));
					break;
				case 0:
					exit = true;
					break;
				default:
					LOG.info("Wrong choice. Try again!\n");
					break;
			}
		}
		scanner.close();
		
		((ConfigurableApplicationContext)context).close();
	}

	/**
	 * Method displays parking information with its structure for each parking from the list (given as a parameter)
	 *
	 * @param parkings list of parking
	 */
	private static void printInfo(List<ParkingDTO> parkings) {
		for (ParkingDTO parking : parkings) {
			parkingStructureHelper.printInfo(parking);
		}
		LOG.info("\n");
	}

	/**
	 * Method finds parking with given ID and displays its structure
	 *
	 * @param parkingId parking ID, which we want to display
	 */
	private static void printParkingById(int parkingId) {

		ParkingDTO parking = reservationService.findParking(parkingId);

		if (parking != null) {
			parkingStructureHelper.printInfo(parking);
		} else {
			LOG.info("Parking with given ID does not exist!");
		}
	}
}
