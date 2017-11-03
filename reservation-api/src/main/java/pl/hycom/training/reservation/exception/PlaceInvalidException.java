package pl.hycom.training.reservation.exception;

/**
 * Exception thrown then we are trying to execute some action on parking place, which does not exist.
 *
 * @author Adam Badura (adam.badura@hycom.pl), HYCOM S.A.
 */
public class PlaceInvalidException extends Exception {
    public PlaceInvalidException(String message) {
        super(message);
    }
}
