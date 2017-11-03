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
 * Class describes one level of a carpark. Defines its size, holds a table with rows.
 * 
 * @author <a href="mailto:bartosz.iskierka@hycom.pl">Bartosz Iskierka, HyCOM, SA</a>
 */
@Entity
@Table(name = "parking_level")
public class Level {

    /**
     * Level identifier
     */
    @Id
    private Long id;

    /**
     * Rows belonging to current level
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "level_id")
    private List<Row> rows = new ArrayList<>();

    /**
     * Parent parking for current level
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_id")
    private Parking parking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}
