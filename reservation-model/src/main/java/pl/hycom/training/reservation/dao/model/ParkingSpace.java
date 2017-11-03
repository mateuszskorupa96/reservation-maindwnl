package pl.hycom.training.reservation.dao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class describes parking place object, with its number, is is booked or released, parent row, etc.
 * 
 * @author <a href="mailto:bartosz.iskierka@hycom.pl">Bartosz Iskierka, HyCOM, SA</a>
 */
@Entity
@Table(name = "parking_space")
public class ParkingSpace {

    /**
     * Parking place identifier
     */
    @Id
    private Long id;

    /**
     * Parking place number
     */
    @Column(name = "place_number")
    private String placeNumber;

    /**
     * Is it for disable?
     */
    @Column(name = "for_disable")
    private boolean forDisable;

    /**
     * Is it taken?
     */
    @Column(name = "taken")
    private boolean taken;

    /**
     * Parent row object
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id")
    private Row row;

    /**
     * Default empty constructor
     */
    public ParkingSpace() {

    }

    /**
     * Constructor with specified parameters
     * 
     * @param id parking place identifier
     * @param placeNumber place number
     * @param forDisable is it for disabled?
     * @param taken is it already taken?
     */
    public ParkingSpace(Long id, String placeNumber, boolean forDisable, boolean taken) {
        this.id = id;
        this.placeNumber = placeNumber;
        this.forDisable = forDisable;
        this.taken = taken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public boolean isForDisable() {
        return forDisable;
    }

    public void setForDisable(boolean forDisable) {
        this.forDisable = forDisable;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }
}
