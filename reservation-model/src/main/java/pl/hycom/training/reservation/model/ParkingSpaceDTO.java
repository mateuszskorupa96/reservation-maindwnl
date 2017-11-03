package pl.hycom.training.reservation.model;

/**
 * Class represents parking space with basic data like: identifier, place number, etc.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class ParkingSpaceDTO {
    
    /**
     * Default constructor
     */
    public ParkingSpaceDTO() {
        
    }

    /**
     * parking space identifier
     */
    private Long id;

    /**
     * Parking space number
     */
    private String placeNumber;

    /**
     * Is it for disable?
     */
    private boolean forDisable;

    /**
     * Is it already taken?
     */
    private boolean taken;

    public ParkingSpaceDTO(Long id, String placeNumber, boolean forDisable, boolean taken) {
        this.id = id;
        this.placeNumber = placeNumber;
        this.forDisable = forDisable;
        this.taken = taken;
    }

    public Long getId() {
        return id;
    }

    public boolean isForDisable() {
        return forDisable;
    }

    public boolean isTaken() {
        return taken;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

}
