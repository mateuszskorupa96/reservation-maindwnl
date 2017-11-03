package pl.hycom.training.reservation.api.service;

import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;

import java.util.List;

/**
 * Interface defines method for basic operations on parking.
 *
 * @author Adam Badura (adam.badura@hycom.pl), HYCOM S.A.
 * @author Dominik Kłys (dominik.klys@hycom.pl), HYCOM S.A.
 */
public interface IReservationService {

    /**
     * Method responsible for book parking place with given place, row, level identifier.
     *
     * @param parkingId parking identifier
     * @param levelId level identifier
     * @param rowId row identifier
     * @param placeId parking place identifier
     * @throws PlaceNotAvailableException if chosen place is already booked
     * @throws PlaceInvalidException if chosen place does not exist
     */
    void book(int parkingId, int levelId, int rowId, int placeId) throws PlaceNotAvailableException, PlaceInvalidException;

    /**
     * Method responsible for release parking place with given place, row, level identifier.
     *
     * @param parkingId parking identifier
     * @param levelId level identifier
     * @param rowId row identifier
     * @param placeId parking place identifier
     * @throws PlaceInvalidException if chosen place does not exist
     */
    void release(int parkingId, int levelId, int rowId, int placeId) throws PlaceInvalidException;
    
    /**
     * Method responsible for preparing full list of all parking. If no parking found, empty list is returned.
     * 
     * @return list of {@link ParkingDTO} object
     */
    List<ParkingDTO> getAllParkings();
    
    /**
     * Method responsible for find {@link ParkingDTO} object with given identifier.
     * 
     * @param id specified parking identifier
     * @return {@link ParkingDTO} object represented found parking
     */
    ParkingDTO findParking(int id);
    
    /**
     * Method responsible for find {@link LevelDTO} object with given identifier.
     * 
     * @param parkingId specified parking identifier
     * @param levelId specified level identifier
     * @return {@link LevelDTO} object represented found parking level
     * @throws ParkingInvalidArgumentException if parking level found, but has incorrect parking identifier
     */
    LevelDTO findLevel(int parkingId, int levelId) throws ParkingInvalidArgumentException;
}
