package pl.hycom.training.reservation.dao.api;

import java.util.List;

import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;

/**
 * Interface provides the methods for operation on parking and its child elements.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public interface ICarParkDao {

    /**
     * Method returns list of all available car-parks.
     * 
     * @return car-parks list
     */
    public List<Parking> findParkings();
    
    /**
     * Initializes list of car-parks.
     */
    public void init();
    
    /**
     * Returns parking with ID specified as a parameter.
     * 
     * @param id parking identifier
     * @return parking with given ID
     */
    public Parking findParkingById(int id);

	/**
	 * Returns level with ID specified as a parameter.
	 * 
	 * @param id level identifier
	 * @return level with given ID
	 */
    public Level findLevelById(int id);
    
    /**
     * Create a {@link Parking} object in database.
     * 
     * @param parking {@link Parking} object
     */
    public void createParking(Parking parking);
    
    /**
     * Removes parking with given ID.
     * 
     * @param id parking ID to remove
     */
    public void removeParking(int id);
    
    /**
     * Removes all available car-parks.
     */
    public void removeParkings();
   
    /**
     * Returns parking space with ID given as a parameter.
     * 
     * @param id parking place identifier
     * @return found {@link ParkingSpace} object
     */
    public ParkingSpace findParkingSpaceById(int id);
}
