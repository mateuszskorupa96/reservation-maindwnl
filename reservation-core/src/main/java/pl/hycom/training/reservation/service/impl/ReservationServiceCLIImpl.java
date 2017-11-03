package pl.hycom.training.reservation.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;
import pl.hycom.training.reservation.dao.model.Row;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * Basic implementation of interface {@link IReservationService}.
 *
 * @author Adam Badura (adam.badura@hycom.pl), HYCOM S.A.
 */
@Service("carParkServiceXML")
@Transactional
public class ReservationServiceCLIImpl implements IReservationService {

    private static final Logger LOG = LogManager.getLogger(ReservationServiceCLIImpl.class);

    @Autowired
    @Qualifier(value = "carParkDaoXml")
    private ICarParkDao carParkDao;

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.ReservationManager#getAllParkings()
     */
    public List<ParkingDTO> getAllParkings() {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("All car-parks will be returned");
        }

        List<ParkingDTO> result = new ArrayList<ParkingDTO>();

        List<Parking> list = carParkDao.findParkings();

        for (Parking p : list) {
            ParkingDTO pDTO = new ParkingDTO(p.getId(), p.getName(), p.getDescription());
            
            int i = 0;
            for(Level l : p.getParkingLevels()) {
                
                LevelDTO lDTO = new LevelDTO(l.getId(), null, pDTO, i++);
                
                for(Row r : l.getRows()) {
                    RowDTO rDTO = new RowDTO(r.getId(), null);
                    
                    for(ParkingSpace ps : r.getParkingSpaces()) {
                        ParkingSpaceDTO psDTO = new ParkingSpaceDTO(ps.getId(), ps.getPlaceNumber(), ps.isForDisable(), ps.isTaken());
                        rDTO.getParkingSpaces().add(psDTO);
                    }
                    
                    lDTO.getRows().add(rDTO);
                }
                
                pDTO.getLevels().add(lDTO);
            }
            
            result.add(pDTO);
        }
        
        if (result.isEmpty()) {
            LOG.info("No car-parks found");            
        } else {
            LOG.info("Found: " + result.size() + " car-parks");    
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.ReservationManager#findParking(int)
     */
    public ParkingDTO findParking(int id) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Car-park with ID=" + id + " will be returned");
        }
        
        Parking p = carParkDao.findParkingById(id);

        if (p != null) {
            ParkingDTO pDTO = new ParkingDTO(p.getId(), p.getName(), p.getDescription());
            for (Level l : p.getParkingLevels()) {
                
                int order = 0;
                
                for(Level level : p.getParkingLevels()) {
                    if (level.getId() == l.getId()) {
                        break;
                    }
                    order++;
                }
                
                LevelDTO lDTO = new LevelDTO(l.getId(), new ArrayList<RowDTO>(), pDTO, order);
                for (Row r : l.getRows()) {
                    RowDTO rDTO = new RowDTO(r.getId(), new ArrayList<ParkingSpaceDTO>());
                    for (ParkingSpace ps : r.getParkingSpaces()) {
                        rDTO.getParkingSpaces().add(
                                new ParkingSpaceDTO(ps.getId(), ps.getPlaceNumber(), ps.isForDisable(), ps.isTaken()));
                    }
                    lDTO.getRows().add(rDTO);
                }
                pDTO.getLevels().add(lDTO);
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking with ID=" + id + " found");
            }
            
            return pDTO;
        }
        
        LOG.warn("Parking with ID=" + id + " NOT found!");

        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.ReservationManager#findLevel(int, int)
     */
    public LevelDTO findLevel(int parkingId, int levelId) throws ParkingInvalidArgumentException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Level with ID=" + levelId + " and parkingId=" + parkingId + " will be returned");
        }
        
        Level l = carParkDao.findLevelById(levelId);

        if (l != null) {

            if (l.getParking() == null || l.getParking().getId() != parkingId) {
                LOG.error("Level with ID=" + levelId + " has no parent parking with ID=" + parkingId);

                throw new ParkingInvalidArgumentException("parking.structure.level.wrong.parking");
            }

            ParkingDTO pDTO = new ParkingDTO(l.getParking().getId(), l.getParking().getName(),
                    l.getParking().getDescription());
            
            int order = 0;
            
            for(Level level : l.getParking().getParkingLevels()) {
                if (level.getId() == l.getId()) {
                    break;
                }
                order++;
            }
            
            LevelDTO lDTO = new LevelDTO(l.getId(), new ArrayList<RowDTO>(), pDTO, order);
            for (Row r : l.getRows()) {
                RowDTO rDTO = new RowDTO(r.getId(), new ArrayList<ParkingSpaceDTO>());
                for (ParkingSpace ps : r.getParkingSpaces()) {
                    rDTO.getParkingSpaces()
                            .add(new ParkingSpaceDTO(ps.getId(), ps.getPlaceNumber(), ps.isForDisable(), ps.isTaken()));
                }
                lDTO.getRows().add(rDTO);
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Level with ID=" + levelId + " and parkingId=" + parkingId + " found");
            }
            
            return lDTO;
        }
        
        LOG.warn("Level with ID=" + levelId + " and parkingId=" + parkingId + " NOt found!");
        
        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.ReservationManager#book(int, int, int, int)
     */
    public void book(int parkingId, int levelId, int rowId, int placeId)
            throws PlaceNotAvailableException, PlaceInvalidException {
        Level l = carParkDao.findLevelById(levelId);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking place with ID=" + placeId + " will be booked");
        }

        if (l != null) {
            if (l.getParking() == null || l.getParking().getId() != parkingId) {
                LOG.error("Level with ID=" + levelId + " has no parent parking with ID=" + parkingId);
                throw new PlaceInvalidException("parking.operation.place.book.error");
            }
            for (Row r : l.getRows()) {

                if (r.getId() == rowId) {

                    for (ParkingSpace ps : r.getParkingSpaces()) {

                        if (ps.getId() == placeId) {

                            if (!ps.isTaken()) {
                                ps.setTaken(true);
                                return;
                            } else {
                                LOG.error("Place with ID=" + placeId + " has been already booked.");
                                throw new PlaceNotAvailableException("parking.operation.place.book.error");
                            }
                        }
                    }
                }
            }
        } else {
            LOG.error("Level with given ID does not exist");
        }
        throw new PlaceInvalidException("parking.operation.place.book.error");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.ReservationManager#release(int, int, int, int)
     */
    public void release(int parkingId, int levelId, int rowId, int placeId) throws PlaceInvalidException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking place with ID=" + placeId + " will be released");
        }
        
        Level l = carParkDao.findLevelById(levelId);

        if (l != null) {
            if (l.getParking() == null || l.getParking().getId() != parkingId) {
                LOG.error("Level with ID=" + levelId + " has no parent parking with ID=" + parkingId);
                throw new PlaceInvalidException("parking.operation.place.release.error");
            }
            for (Row r : l.getRows()) {

                if (r.getId() == rowId) {

                    for (ParkingSpace ps : r.getParkingSpaces()) {

                        if (ps.getId() == placeId) {

                            ps.setTaken(false);
                            return;
                        }
                    }
                }
            }
        } else {
            LOG.error("Level with given ID does not exist.");
        }
        throw new PlaceInvalidException("parking.operation.place.release.error");
    }
}
