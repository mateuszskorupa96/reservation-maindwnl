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
 * Class implements {@link IReservationService} interface. Implementation is based on provided data taken from database.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Service("carParkServiceDao")
@Transactional
public class ReservationServiceImpl implements IReservationService {
    
    private static final Logger LOG = LogManager.getLogger(ReservationServiceImpl.class);
    
    /**
     * Service responsible for providing data from database.
     */
    @Autowired
    @Qualifier(value = "carParkDaoDB")
    private ICarParkDao serviceDao;

    /**
     * Method responsible for booking parking space with given identifier. If parking place is not found or is found, but has no correct
     * row, level or parking identifier, {@link PlaceInvalidException} is thrown. If place is found, but is already taken - {@link PlaceNotAvailableException} is thrown.
     * If place found and its relations to row, level and parking are correct, parking place is marked as booked (taken).
     * 
     * @param parkingId parking identifier
     * @param levelId level identifier
     * @param rowId row identifier
     * @param placeId parking space identifier
     * @throws PlaceNotAvailableException - if parking space exists, but is already taken
     * @throws PlaceInvalidException - if parking space not found or found, but row, level or parking are incorrect
     */
    public void book(int parkingId, int levelId, int rowId, int placeId) throws PlaceNotAvailableException, PlaceInvalidException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] will be booked");
        }

        ParkingSpace pS = serviceDao.findParkingSpaceById(placeId);
        
        if (pS != null && pS.getRow() != null && pS.getRow().getId() == rowId 
                && pS.getRow().getLevel() != null && pS.getRow().getLevel().getId() == levelId
                && pS.getRow().getLevel().getParking() != null && pS.getRow().getLevel().getParking().getId() == parkingId) {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] found");
            }
            
            if (pS.isTaken()) {
                throw new PlaceNotAvailableException("Place with ID=" + placeId + " is not available (already booked)");
            } else {
                pS.setTaken(true);
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] marked as taken");
                }
            }
        } else {
            throw new PlaceInvalidException("Parking space with [ID=" + placeId + ", ROWID= " + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] not found");
        }
    }

    /**
     * Method responsible for release parking space with given identifier. If parking place is not found or is found, but has no correct
     * row, level or parking identifier, {@link PlaceInvalidException} is thrown. If place found and its relations to row, level and parking are correct, parking place is marked as released (not taken).
     * 
     * @param parkingId parking identifier
     * @param levelId level identifier
     * @param rowId row identifier
     * @param placeId parking space identifier
     * @throws PlaceInvalidException - if parking space not found or found, but row, level or parking are incorrect
     */
    public void release(int parkingId, int levelId, int rowId, int placeId) throws PlaceInvalidException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] will be released");
        }
        
        ParkingSpace pS = serviceDao.findParkingSpaceById(placeId);
        
        if (pS != null && pS.getRow() != null && pS.getRow().getId() == rowId
                && pS.getRow().getLevel() != null && pS.getRow().getLevel().getId() == levelId
                && pS.getRow().getLevel().getParking() != null && pS.getRow().getLevel().getParking().getId() == parkingId) {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] found");
            }
            
            pS.setTaken(false);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking place with [ID=" + placeId + ", ROWID=" + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] marked as released");
            }
            
        } else {
            throw new PlaceInvalidException("Parking space with [ID=" + placeId + ", ROWID= " + rowId + ", LEVELID=" + levelId + ", PARKINGID=" + parkingId + "] not found");
        }
    }

    /**
     * Method responsible for preparing list of all found in database parkings. If no parking found, empty list is returned.
     * 
     * @return list of {@link ParkingDTO} object
     */
    public List<ParkingDTO> getAllParkings() {
        
        List<ParkingDTO> resullt = new ArrayList<ParkingDTO>();
        
        List<Parking> list = serviceDao.findParkings();
        
        if (list.isEmpty()) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking list is empty. Will be initialized by getting data from XML");
            }

            serviceDao.init();
            list = serviceDao.findParkings();
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking list initialized");
            }
        }
        
        for(Parking p : list) {
            resullt.add(new ParkingDTO(Long.valueOf(p.getId()), p.getName(), p.getDescription()));
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking list size: " + resullt.size());
        }

        return resullt;
    }

    /**
     * Method responsible for find parking with given identifier. If parking not found, <code>NULL</code> will be returned.
     * 
     * @return found {@link ParkingDTO} with given identifier or <code>NULL</code> - if not found
     */
    public ParkingDTO findParking(int id) {
        
        Parking p = serviceDao.findParkingById(id);
        
        if (p != null) {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking with ID=" + id + " found");
            }
            
            ParkingDTO pDTO = new ParkingDTO(p.getId(), p.getName(), p.getDescription());
            
            int i = 0;
            for(Level l : p.getParkingLevels()) {
                pDTO.getLevels().add(new LevelDTO(l.getId(), null, null, i++));
            }
            return pDTO;

        } else {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking with ID=" + id + " NOT found");
            }
            
            return null;
        }
    }
    
    /**
     * Method responsible for find parking level with given identifier. If level not found, <code>NULL</code> will be returned.
     * 
     * @return found {@link LevelDTO} with given identifier or <code>NULL</code> - if not found
     */
    public LevelDTO findLevel(int parkingId, int levelId) throws ParkingInvalidArgumentException {

        Level l = serviceDao.findLevelById(levelId);
        
        if (l != null) {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parking level with ID=" + levelId + " found");
            }
            
            if (l.getParking() == null || l.getParking().getId() != parkingId) {
                throw new ParkingInvalidArgumentException("Parking level with ID=" + levelId + " has invalid parking identifier");
            }
            
            int order = 0;
            
            for(Level level : l.getParking().getParkingLevels()) {
                if (level.getId() == l.getId()) {
                    break;
                }
                order++;
            }
            
            LevelDTO lDTO = new LevelDTO(l.getId(), null, new ParkingDTO(l.getParking().getId(), l.getParking().getName(), l.getParking().getDescription()), order);
            
            List<RowDTO> rows = new ArrayList<RowDTO>();
            
            for(Row r : l.getRows()) {
                RowDTO rDTO = new RowDTO(r.getId(), null);
                
                for(ParkingSpace ps : r.getParkingSpaces()) {
                    rDTO.getParkingSpaces().add(new ParkingSpaceDTO(ps.getId(), ps.getPlaceNumber(), ps.isForDisable(), ps.isTaken()));
                }
                rows.add(rDTO);
            }
            lDTO.setRows(rows);
            return lDTO;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parking level with ID=" + levelId + " NOT found");
        }
        
        return null;
    }
}
