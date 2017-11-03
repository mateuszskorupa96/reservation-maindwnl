package pl.hycom.training.reservation.dao.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class describes row of parking places, with its parent level and list of child parking palces.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Entity
@Table(name = "row")
public class Row {

    /**
     * Row identifier
     */
    @Id
    private Long id;

    /**
     * Parent level object
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "level_id")
    private Level level;

    /**
     * List of parking places
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "row_id")
    private List<ParkingSpace> parkingSpaces = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(List<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
