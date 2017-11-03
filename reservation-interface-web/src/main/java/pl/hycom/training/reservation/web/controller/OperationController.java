package pl.hycom.training.reservation.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;

/**
 * Controller used to manage parking structure (book/release)
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Controller
@RequestMapping({ "/car-park" })
public class OperationController {
    private static final Logger LOG = LogManager.getLogger(OperationController.class);
    
    @Autowired
    @Qualifier("wsClientService")
    IReservationService reservationService;
    
    /**
     * Method responsible for managing POST request taken from level details form page and book/release operation.
     * After this action, page is reloaded and the same view with specified message is presented
     * 
     * @param model {@link Model} object for UI
     * @return name of JSP view
     */
    @RequestMapping(method = RequestMethod.POST, path = "{id}/level/{levelId}")
    public String bookRelease(@PathVariable("id") String id, @PathVariable("levelId") String levelId, 
            @RequestParam String rowId, @RequestParam String placeId, @RequestParam String book, @RequestParam String placeNumber, RedirectAttributes attr) {
      
        boolean booked = Boolean.valueOf(book);
        
        LOG.info("Parking space ID: " + placeId + ", row ID: " + rowId);
        
        String message = "";

        try {

            if (booked) {
                reservationService.book(Integer.valueOf(id), Integer.valueOf(levelId), Integer.valueOf(rowId), Integer.valueOf(placeId));

                message = "parking.operation.place.book.success";
                
                if (LOG.isDebugEnabled()) {
                    LOG.info("Parking space with ID=" + placeId + ", rowId=" + rowId + ", levelId=" + levelId + ", parkingId=" + id + " booked");
                }
            } else {
                reservationService.release(Integer.valueOf(id), Integer.valueOf(levelId), Integer.valueOf(rowId), Integer.valueOf(placeId));
                message = "parking.operation.place.release.success";
                
                if (LOG.isDebugEnabled()) {
                    LOG.info("Parking space with ID=" + placeId + ", rowId=" + rowId + ", levelId=" + levelId + ", parkingId=" + id + " released");
                }
            }
            
            attr.addFlashAttribute("operationMessage", message);
            attr.addFlashAttribute("operationMessageArgs", String.valueOf(placeNumber));
            
        } catch (NumberFormatException e) {
            LOG.error(e);
        } catch (PlaceNotAvailableException | PlaceInvalidException e) {
            LOG.error(e);
             attr.addFlashAttribute("operationMessageErr", e.getMessage());
             attr.addFlashAttribute("operationMessageArgs", String.valueOf(placeNumber));
        }

        // here redirect after form submit, fix for page refreshing with F5
        return "redirect:/car-park/{id}/level/{levelId}";
    }
}
