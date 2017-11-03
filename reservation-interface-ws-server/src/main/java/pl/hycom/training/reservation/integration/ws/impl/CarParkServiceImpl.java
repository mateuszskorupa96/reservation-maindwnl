package pl.hycom.training.reservation.integration.ws.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.integration.ws.api.BookPlace;
import pl.hycom.training.reservation.integration.ws.api.BookPlaceResponse;
import pl.hycom.training.reservation.integration.ws.api.GetLevelDetails;
import pl.hycom.training.reservation.integration.ws.api.GetLevelDetailsResponse;
import pl.hycom.training.reservation.integration.ws.api.GetParkingDetails;
import pl.hycom.training.reservation.integration.ws.api.GetParkingDetailsResponse;
import pl.hycom.training.reservation.integration.ws.api.GetParkingList;
import pl.hycom.training.reservation.integration.ws.api.GetParkingListResponse;
import pl.hycom.training.reservation.integration.ws.api.ICarParkWS;
import pl.hycom.training.reservation.integration.ws.api.ReleasePlace;
import pl.hycom.training.reservation.integration.ws.api.ReleasePlaceResponse;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * Service implements methods from interface {@link ICarParkWS}. All data are provided from database.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Service
@Transactional
public class CarParkServiceImpl implements ICarParkWS {
    
    /**
     * Service responsible for providing data from database.
     */
    @Autowired
    @Qualifier("carParkServiceDao")
    private IReservationService service;

    /**
     * Method responsible for getting list of all parkings from repository and prepare response object based on current list
     * 
     * @param getParkingList empty request object
     * @return {@link GetParkingListResponse} object containing list of all found parkings
     */
    public GetParkingListResponse getParkingList(GetParkingList getParkingList) {

        List<ParkingDTO> list = service.getAllParkings();
        
        GetParkingListResponse response = new GetParkingListResponse();
        
        for(ParkingDTO p : list) {
            response.getParkingList().add(WSObjectFactory.createParking(p));
        }
        response.setError(WSObjectFactory.createSuccess());
        return response;
    }

    /**
     * Method responsible for release parking place given by a place identifier, which belongs to given row, level and parking.
     * If place is not found, specified error is returned, otherwise - success message is returned.
     * 
     * @param releasePlace {@link ReleasePlace} request object
     * @return {@link ReleasePlaceResponse} response object
     */
    public ReleasePlaceResponse releasePlace(ReleasePlace releasePlace) {

        ReleasePlaceResponse response = new ReleasePlaceResponse();
        
        try {
            service.release(releasePlace.getParkingId(), releasePlace.getLevelId(), releasePlace.getRowId(), releasePlace.getSpaceId());
            response.setError(WSObjectFactory.createSuccess());
        } catch(PlaceInvalidException e) {
            response.setError(WSObjectFactory.createError("Place with ID=" + releasePlace.getSpaceId() + " does not exist", "ws.error.place.not.exist", 1));
        }
        return response;
    }

    /**
     * Method returns information about parking (with given identifier) with parking levels. If parking not found, specified error message is returned.
     * 
     * @param getParkingDetails {@link GetParkingDetails} request object
     * @return {@link GetParkingDetailsResponse} response object with information about parking and its levels
     */
    public GetParkingDetailsResponse getParkingDetails(GetParkingDetails getParkingDetails) {
        
        GetParkingDetailsResponse response = new GetParkingDetailsResponse();
        
        ParkingDTO p = service.findParking(getParkingDetails.getParkingId());
        
        if (p != null) {
            pl.hycom.training.reservation.integration.ws.api.Parking parking = WSObjectFactory.createParking(p);
            
            int i = 0;
            for(LevelDTO l : p.getLevels()) {
                parking.getLevels().add(WSObjectFactory.createLevel(l, i));
                i++;
            }
            response.setParking(parking);
            response.setError(WSObjectFactory.createSuccess());
        } else {
            response.setError(WSObjectFactory.createError("Parking with ID " + getParkingDetails.getParkingId() + "does not exist! ", "ws.error.parking.not.exists", 1));
        }
        
        return response;
    }

    /**
     * Method responsible for booking parking place with given identifier (belonged to specified row, level and parking).
     * If place is already booked, not exists - specified errors are returned.
     * 
     * @param bookPlace {@link BookPlace} request object
     * @return {@link BookPlaceResponse} response object
     */
    public BookPlaceResponse bookPlace(BookPlace bookPlace) {

        BookPlaceResponse response = new BookPlaceResponse();
        
        try {
            service.book(bookPlace.getParkingId(), bookPlace.getLevelId(), bookPlace.getRowId(), bookPlace.getSpaceId());
            response.setError(WSObjectFactory.createSuccess());
        } catch(PlaceNotAvailableException e) {
            response.setError(WSObjectFactory.createError("Place with ID=" + bookPlace.getSpaceId() + " is not available (already booked)", "ws.error.place.already.booked", 2));
        } catch(PlaceInvalidException e) {
            response.setError(WSObjectFactory.createError("Place with ID=" + bookPlace.getSpaceId() + " does not exist", "ws.error.place.not.exist", 1));
        }

        return response;
    }

    /**
     * Method returns information about parking level details for specified level identifier (belonged to specified parking).
     * If given level does not exist, error message is returned.
     * 
     * @param getLevelDetails {@link GetLevelDetails} request object
     * @return {@link GetLevelDetailsResponse} response object
     */
    public GetLevelDetailsResponse getLevelDetails(GetLevelDetails getLevelDetails) {
        GetLevelDetailsResponse response = new GetLevelDetailsResponse();

        try {

            LevelDTO l = service.findLevel(getLevelDetails.getParkingId(), getLevelDetails.getLevelId());
            
            if (l != null) {
                int order = 0;
                
                for(LevelDTO level : l.getParking().getLevels()) {
                    if (level.getId() == l.getId()) {
                        break;
                    }
                    order++;
                }
                
                pl.hycom.training.reservation.integration.ws.api.Level level = WSObjectFactory.createLevel(l, order);
                
                for(RowDTO r : l.getRows()) {
                    pl.hycom.training.reservation.integration.ws.api.Row row = WSObjectFactory.createRow(r);
                    
                    for(ParkingSpaceDTO ps : r.getParkingSpaces()) {
                        row.getSpaces().add(WSObjectFactory.createSpace(ps));
                    }
                    
                    level.getRows().add(row);
                }
                response.setParking(WSObjectFactory.createParking(l.getParking()));
                response.setLevel(level);
                response.setError(WSObjectFactory.createSuccess());
            } else {
                response.setError(WSObjectFactory.createWarning("Level with ID=" + getLevelDetails.getLevelId() + " and parkingId=" + getLevelDetails.getParkingId() + " does not exist! ", "ws.error.level.not.exists", 1));
            }
            

        } catch (ParkingInvalidArgumentException e) {
            response.setError(WSObjectFactory.createError("Level with ID=" + getLevelDetails.getLevelId() + " has no parking with ID="
                                    + getLevelDetails.getParkingId() + " does not exist! ", "ws.error.level.wrong.parking", 1));
        }

        return response;
    }
}
