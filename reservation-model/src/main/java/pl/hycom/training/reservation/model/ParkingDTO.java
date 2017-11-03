package pl.hycom.training.reservation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents parking with basic data like: identifier, name, description, etc.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */

public class ParkingDTO {
    
    /**
     * Default constructor
     */
    public ParkingDTO() {
        
    }

    /**
     * Parking identifier
     */
    private Long id;
    
    /**
     * Parking name
     */
    private String name;
    
    /**
     * Parking description
     */
    private String description;
    
    /**
     * Parking levels
     */
    private List<LevelDTO> levels = new ArrayList<LevelDTO>();

    public ParkingDTO(Long id, String name, String descirption) {
        this.id = id;
        this.name = name;
        this.description = descirption;
    }

    public Long getId() {
        return id;
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

    public List<LevelDTO> getLevels() {
        
        if (levels == null) {
            levels = new ArrayList<LevelDTO>();
        }
        
        return levels;
    }

    public void setLevels(List<LevelDTO> levels) {
        this.levels = levels;
    }
}
