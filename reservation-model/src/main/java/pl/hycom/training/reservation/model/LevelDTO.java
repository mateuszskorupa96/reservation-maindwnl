package pl.hycom.training.reservation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents parking level with basic data like: identifier, rows list and parent parking.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class LevelDTO {
    
    /**
     * Default constructor
     */
    public LevelDTO() {
        
    }

	/**
	 * Level identifier
	 */
    private Long id;

	/**
	 * Level rows
	 */
    private List<RowDTO> rows;
	
	/**
	 * Parent parking 
	 */
    private ParkingDTO parking;
	
	/**
	 * Level order
	 */
    private int order;

	public LevelDTO(Long id, List<RowDTO> rows, ParkingDTO parking, int order) {
		this.parking = parking;
		this.id = id;
		this.rows = rows;
		this.order = order;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<RowDTO> getRows() {
	    
	    if (rows == null) {
	        rows = new ArrayList<RowDTO>();
	    }
	    
		return rows;
	}

	public void setRows(List<RowDTO> rows) {
		this.rows = rows;
	}

	public ParkingDTO getParking() {
		return parking;
	}

	public void setParking(ParkingDTO parking) {
		this.parking = parking;
	}

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    /**
     * Method calculates maximum number of places based on all rows in current level. 
     * 
     * @return maximum parking spaces number
     */
    public int getMaxPlacesFromAll() {
        
        int max = 0;
        
        for(RowDTO r : rows) {
            if (r.getParkingSpaces() != null && r.getParkingSpaces().size() > max) {
                max = r.getParkingSpaces().size();
            }
        }
        
        return max;
    }
}
