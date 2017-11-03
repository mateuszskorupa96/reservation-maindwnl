package pl.hycom.training.reservation.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.model.ParkingDTO;


/**
 * Servlet displays on WWW page list of all car-parks.
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@SuppressWarnings("serial")
public class CarParksServlet extends HttpServlet {
    
    private static final Logger LOG = LogManager.getLogger(CarParksServlet.class);

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

        List<ParkingDTO> list = reservationService.getAllParkings();
        
        PrintWriter pr = resp.getWriter();
        
        if (list != null) {
            String logo = ParkingStructureHelper.getInstance().printLogo();
            
            LOG.info(logo);
            pr.write(logo);
            
            for(ParkingDTO p : list) {
                String pStrucure = ParkingStructureHelper.getInstance().printInfo(p);
                
                LOG.info(pStrucure);
                pr.write(pStrucure);
            }
        } else {
            LOG.warn("No parking found!");
            pr.write("No parking found!");            
        }
        
        pr.flush();
        pr.close();
    }
}
