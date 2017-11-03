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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.model.LevelDTO;

/**
 * Controller used to get data corresponding to given level
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Controller
@RequestMapping({ "/car-park" })
public class LevelController {
    private static final Logger LOG = LogManager.getLogger(LevelController.class);
    
    @Autowired
    @Qualifier("wsClientService")
    IReservationService reservationService;
    
    /**
     * Method responsible for serves parking level details page
     * @param model {@link Model} object for UI
     * @return name of JSP view
     */
    @RequestMapping(method = RequestMethod.GET, path = "{id}/level/{levelId}")
    public String oneLevel(@PathVariable("id") String id, @PathVariable("levelId") String levelId, Model model, RedirectAttributes attr) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting parking level with ID=" + levelId + ", parking ID=" + id);
        }

        try {

            LevelDTO level = reservationService.findLevel(Integer.valueOf(id), Integer.valueOf(levelId));

            if (level != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Parking level with ID=" + levelId);
                }
                if (Long.valueOf(id).equals(level.getParking().getId())) {
                    model.addAttribute("level", level);
                } else {
                    LOG.warn("Parking with ID (" + id + ") from URL is not equals parking ID received from backend (" + level.getParking().getId() + ")");
                }

            } else {
                LOG.warn("Level with ID=" + levelId + " NOT found");
            }
        } catch (ParkingInvalidArgumentException e) {
            attr.addFlashAttribute("operationMessage", e.getMessage());
            return "redirect:/car-park";
        }

        return "oneLevel";
    }
}
