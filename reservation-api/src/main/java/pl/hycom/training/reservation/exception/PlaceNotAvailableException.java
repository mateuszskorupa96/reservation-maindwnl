package pl.hycom.training.reservation.exception;

/**
 * Exception thrown then we are trying to book an already booked parking place.
 *
 * @author Adam Badura (adam.badura@hycom.pl), HYCOM S.A.
 */
public class PlaceNotAvailableException extends Exception {
    public PlaceNotAvailableException(String message) {
        super(message);
    }
}
