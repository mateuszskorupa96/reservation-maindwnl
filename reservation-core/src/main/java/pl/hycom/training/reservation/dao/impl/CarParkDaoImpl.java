package pl.hycom.training.reservation.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;
import pl.hycom.training.reservation.dao.model.Row;

/**
 * Class implements {@link ICarParkDao} interface and provides the data initialized in JAVA beans.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Component(value = "carParkDao")
public class CarParkDaoImpl implements ICarParkDao {
    
    /**
     * Car-parks list
     */
    private List<Parking> parkings = new ArrayList<Parking>();
    
    /**
     * Default constructor
     */
    public CarParkDaoImpl() {
        init();
    }

    /* (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkings()
     */
    public List<Parking> findParkings() {
        return parkings;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#init()
     */
    public void init() {
        parkings.clear();

        Parking parking1 = new Parking();
        parking1.setId(1L);
        parking1.setName("Parking1");
        parking1.setDescription("Parking Test nr 1");
        Level level1 = new Level();
        level1.setId(1L);
        Row r1 = new Row();
        r1.setId(1L);
        r1.getParkingSpaces().add(new  ParkingSpace(1L, "A1", false, true));
        r1.getParkingSpaces().add(new  ParkingSpace(2L, "A2", false, false));
        r1.getParkingSpaces().add(new  ParkingSpace(3L, "A3", true, false));
        
        Row r2 = new Row();
        r2.setId(2L);
        r2.getParkingSpaces().add(new  ParkingSpace(1L, "B1", true, true));
        r2.getParkingSpaces().add(new  ParkingSpace(2L, "B2", true, true));
        r2.getParkingSpaces().add(new  ParkingSpace(3L, "B3", false, false));
        r2.getParkingSpaces().add(new  ParkingSpace(4L, "B4", true, false));
        
        level1.getRows().add(r1);
        level1.getRows().add(r2);
        
        parking1.getParkingLevels().add(level1);
        
        Level level2 = new Level();
        level2.setId(2L);
        Row r3 = new Row();
        r3.setId(3L);
        r3.getParkingSpaces().add(new  ParkingSpace(1L, "C1", true, true));
        r3.getParkingSpaces().add(new  ParkingSpace(2L, "C2", false, true));
        r3.getParkingSpaces().add(new  ParkingSpace(3L, "C3", true, false));
        r3.getParkingSpaces().add(new  ParkingSpace(4L, "C4", false, true));
        
        level2.getRows().add(r3);
        
        parking1.getParkingLevels().add(level2);
        parkings.add(parking1);
        
        Parking parking2 = new Parking();
        parking2.setId(2L);
        parking2.setName("Parking2");
        parking2.setDescription("Parking Test nr 2");
        
        Level level3 = new Level();
        level3.setId(3L);
        
        Row r4 = new Row();
        r4.setId(4L);
        
        r4.getParkingSpaces().add(new  ParkingSpace(5L, "C1", false, true));
        r4.getParkingSpaces().add(new  ParkingSpace(6L, "C2", true, true));
        r4.getParkingSpaces().add(new  ParkingSpace(6L, "C3", true, false));
        level3.getRows().add(r4);
        parking2.getParkingLevels().add(level3);
        parkings.add(parking2);
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingById(int)
     */
    public Parking findParkingById(int id) {
        
        for(Parking p : parkings) {
            if (p.getId().intValue() == id) {
                return p;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findLevelById(int)
     */
    public Level findLevelById(int id) {
        for(Parking p : parkings) {
            for(Level l : p.getParkingLevels()) {
                if (l.getId().intValue() == id) {
                    return l;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#createParking(pl.hycom.training.reservation.dao.model.Parking)
     */
    public void createParking(Parking parking) {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParking(int)
     */
    public void removeParking(int id) {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParkings()
     */
    public void removeParkings() {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingSpaceById(int)
     */
    public ParkingSpace findParkingSpaceById(int id) {
        throw new NotImplementedException("Method not implemented");
    }
}
