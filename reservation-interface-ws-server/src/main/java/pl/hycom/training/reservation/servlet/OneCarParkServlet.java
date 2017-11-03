package pl.hycom.training.reservation.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.model.ParkingDTO;


/**
 * Servlet displays on WWW page car-park with geiven identifier.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@SuppressWarnings("serial")
public class OneCarParkServlet extends HttpServlet {
    
    private static final Logger LOG = LogManager.getLogger(OneCarParkServlet.class);

    /**
     * Component provides all car-parks
     */
    @Autowired
    @Qualifier("carParkServiceXML")
    private IReservationService reservationService;
    
    /**
     * Initializing Spring context
     * 
     * @param config servlet configuration object
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * Method responsible for manage GET requests
     * 
     * @param req servlet request
     * @param resp servlet response
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String uri = req.getRequestURI();
        
        String[] uriParts = uri.split("/");
        
        PrintWriter pr = resp.getWriter();
        
        if (uriParts != null && uriParts.length > 3) {
            if (!NumberUtils.isDigits(uriParts[3])) {
                String msg = "Parking identifier is not a number!";
                LOG.error(msg);
                pr.write(msg);    
            } else {
                int parkingId = Integer.valueOf(uriParts[3]);
                
                ParkingDTO p = reservationService.findParking(parkingId);
                
                if (p != null) {
                    String logo = ParkingStructureHelper.getInstance().printLogo();
                    LOG.info(logo);
                    
                    pr.write(logo);
                    
                    String pStrucure = ParkingStructureHelper.getInstance().printInfo(p);
                    LOG.info(pStrucure);
                        
                    pr.write(pStrucure);
                } else {
                    String msg = "Parking with ID=" + parkingId + " NOT found!";
                    LOG.warn(msg);
                    pr.write(msg);
                }
            }

        } else {
            String msg = "Wrong uri structure!";
            LOG.error(msg);
            pr.write(msg);
        }

        pr.flush();
        pr.close();
    }
}
