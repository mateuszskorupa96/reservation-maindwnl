package pl.hycom.training.reservation.dao.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class describes parking object, with its name, description and levels.
 * 
 * @author <a href="mailto:bartosz.iskierka@hycom.pl">Bartosz Iskierka, HyCOM, SA</a>
 */
@Entity
@Table(name = "parking")
public class Parking {

    @Id
    private Long id;

    /** Name of a carPark. */
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    /** Business description of a carPark. */
    @Column(name = "description")
    private String description;

    /**
     * Collection of parking levels objects.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "parking_id")
    private List<Level> parkingLevels = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Level> getParkingLevels() {
        return parkingLevels;
    }

    public void setParkingLevels(List<Level> parkingLevels) {
        this.parkingLevels = parkingLevels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
