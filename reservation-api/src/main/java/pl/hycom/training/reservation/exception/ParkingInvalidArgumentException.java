package pl.hycom.training.reservation.exception;

/**
 * Exception thrown then we are trying to get parking structure with not correct structure,
 * e.g. parking level with wrong parent parking identifier.
 *
 * @author Dominik Kłys (dominik.klys@hycom.pl), HYCOM S.A.
 */
public class ParkingInvalidArgumentException extends Exception {
    
    public ParkingInvalidArgumentException(String message) {
        super(message);
    }
    
}
