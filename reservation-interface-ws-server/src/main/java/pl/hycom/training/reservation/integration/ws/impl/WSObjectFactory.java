package pl.hycom.training.reservation.integration.ws.impl;

import pl.hycom.training.reservation.integration.ws.api.Error;
import pl.hycom.training.reservation.integration.ws.api.ErrorStatus;
import pl.hycom.training.reservation.integration.ws.api.Level;
import pl.hycom.training.reservation.integration.ws.api.Parking;
import pl.hycom.training.reservation.integration.ws.api.Row;
import pl.hycom.training.reservation.integration.ws.api.Space;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * Class provides static methods to create list of defined WS objects.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
class WSObjectFactory {

    /**
     * Static constants OK message for success operatoin result
     */
    private static final String ERROR_MESSAGE_OK = "OK";

    /**
     * Method creates bean object based on given parking object taken from database.
     * 
     * @param parking parking object taken from database
     * @return parking bean
     */
    static Parking createParking(ParkingDTO parking) {
        Parking p = new Parking();
        p.setId(parking.getId().intValue());
        p.setDescription(parking.getDescription());
        p.setName(parking.getName());
        return p;
    }

    /**
     * Method creates bean object based on given level object taken from database.
     * 
     * @param level object taken from database
     * @param order level order (0, 1, 2, etc.)
     * @return level bean
     */
    static Level createLevel(LevelDTO level, int order) {
        Level l = new Level();
        l.setId(level.getId().intValue());
        l.setOrder(order);
        return l;
    }

    /**
     * Method creates bean object based on given row object taken from database.
     * 
     * @param row object taken from database
     * @return row bean
     */
    static Row createRow(RowDTO row) {
        Row r = new Row();
        r.setId(row.getId().intValue());
        return r;
    }

    /**
     * Method creates bean object based on given parking place object taken from database.
     * 
     * @param parkingSpace object taken from database
     * @return parking place bean
     */
    static Space createSpace(ParkingSpaceDTO parkingSpace) {
        Space s = new Space();
        s.setForDisable(parkingSpace.isForDisable());
        s.setId(parkingSpace.getId().intValue());
        s.setNumber(parkingSpace.getPlaceNumber());
        s.setTaken(parkingSpace.isTaken());
        return s;
    }

    /**
     * Method creates error bean object dedicated for operation with success result.
     * 
     * @return error bean
     */
    static Error createSuccess() {
        Error err = new Error();
        err.setStatus(ErrorStatus.SUCCESS);
        err.setMessage(ERROR_MESSAGE_OK);
        err.setMessageKey(ERROR_MESSAGE_OK);
        return err;
    }

    /**
     * Method creates error bean object dedicated for operation with error result.
     * 
     * @param errMsg message for error
     * @param errMsgKey key for bundle message for error
     * @param errCode error code
     * @return error bean
     */
    static Error createError(String errMsg, String errMsgKey, int errCode) {
        return createErrorCommon(errMsg, errMsgKey, errCode, ErrorStatus.ERROR);
    }

    /**
     * Method creates error bean object dedicated for operation with warning result.
     * 
     * @param errMsg message for error
     * @param errMsgKey key for bundle message for error
     * @param errCode error code
     * @return error bean
     */
    static Error createWarning(String errMsg, String errMsgKey, int errCode) {
        return createErrorCommon(errMsg, errMsgKey, errCode, ErrorStatus.WARNING);
    }

    /**
     * Method creates error bean object dedicated for operation given as a parameter.
     * 
     * @param errMsg error message
     * @param errMsgKey key for bundle message for error
     * @param errCode error code
     * @param errStatus error status
     * @return error bean
     */
    private static Error createErrorCommon(String errMsg, String errMsgKey, int errCode, ErrorStatus errStatus) {
        Error err = new Error();
        err.setStatus(errStatus);
        err.setMessage(errMsg);
        err.setMessageKey(errMsgKey);
        err.setCode(errCode);
        return err;
    }
}
