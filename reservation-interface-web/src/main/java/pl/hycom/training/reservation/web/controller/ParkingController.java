package pl.hycom.training.reservation.web.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.model.ParkingDTO;

/**
 * Main controller used to manage whole navigation flow over the parking structure
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Controller
@RequestMapping({ "/car-park" })
public class ParkingController {
    private static final Logger LOG = LogManager.getLogger(ParkingController.class);
    
    @Autowired
    @Qualifier("wsClientService")
    IReservationService reservationService;
    
    /**
     * Method responsible for serves list of parking list
     * @param model {@link Model} object for UI
     * @return name of JSP view
     */
    @RequestMapping(method=RequestMethod.GET)
    public String parkingList(Model model) {
        List<ParkingDTO> list = reservationService.getAllParkings();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting parking list...");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received parking list size: " + list.size());
        }
        
        model.addAttribute("parkingList", list);
        
        return "list";
    }
    
    /**
     * Method responsible for parking details page
     * @param model {@link Model} object for UI
     * @return name of JSP view
     */
    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public String oneParking(@PathVariable String id, Model model) {

        ParkingDTO parking = reservationService.findParking(Integer.valueOf(id));

        if (parking != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found parking with ID=" + id);
            }
            
            model.addAttribute("parking", parking);
            
        } else {
            LOG.warn("Parkingu with ID=" + id + " NOT found");
        }
        
        return "oneParking";
    }
}
