package pl.hycom.training.reservation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents row level with basic data like: identifier and parent spaces.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class RowDTO {
    
    /**
     * Default constructor
     */
    public RowDTO() {
        
    }

	/**
	 * Row identifier
	 */
    private Long id;

	/**
	 * Parking spaces list
	 */
    private List<ParkingSpaceDTO> parkingSpaces;

	public RowDTO(Long id, List<ParkingSpaceDTO> parkingSpaces) {
		this.id = id;
		this.parkingSpaces = parkingSpaces;
	}

	public Long getId() {
		return id;
	}
	public List<ParkingSpaceDTO> getParkingSpaces() {
	    
	    if (parkingSpaces == null) {
	        parkingSpaces = new ArrayList<ParkingSpaceDTO>();
	    }
	    
		return parkingSpaces;
	}

	public void setParkingSpaces(List<ParkingSpaceDTO> parkingSpaces) {
		this.parkingSpaces = parkingSpaces;
	}

}
