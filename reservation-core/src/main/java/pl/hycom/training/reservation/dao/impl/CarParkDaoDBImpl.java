package pl.hycom.training.reservation.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;

/**
 * Service implements {@link ICarParkDao} interface. Used JPA/Hibernate implementation
 * to get data from database.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Repository
@Transactional
@Component(value = "carParkDaoDB")
public class CarParkDaoDBImpl implements ICarParkDao {

    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    @Qualifier("carParkDaoXml")
    ICarParkDao carParkDaoXml;

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkings()
     */
    @SuppressWarnings("unchecked")
    public List<Parking> findParkings() {
        Query query = entityManager.createQuery("select p from Parking p");
        return (List<Parking>)query.getResultList();
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingById(int)
     */
    public Parking findParkingById(int id) {
        return entityManager.find(Parking.class, Long.valueOf(id));
    }
    
	/*
	 * (non-Javadoc)
	 * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findLevelById(int)
	 */
    public Level findLevelById(int id) {
		return entityManager.find(Level.class, Long.valueOf(id));
	}

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#createParking(pl.hycom.training.reservation.dao.model.Parking)
     */
    public void createParking(Parking parking) {
        entityManager.merge(parking);
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParking(int)
     */
    public void removeParking(int id) {
        Parking p = findParkingById(id);
        entityManager.remove(p);
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParkings()
     */
    public void removeParkings() {
        List<Parking> list = findParkings();
        
        for(Parking p : list) {
            entityManager.remove(p);
        }
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#init()
     */
    public void init() {
        List<Parking> pList = carParkDaoXml.findParkings();

        for (Parking p : pList) {
            entityManager.merge(p);
        }
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingSpaceById(int)
     */
    public ParkingSpace findParkingSpaceById(int id) {
        return entityManager.find(ParkingSpace.class, Long.valueOf(id));
    }
}
