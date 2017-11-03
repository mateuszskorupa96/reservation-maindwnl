package pl.hycom.training.reservation.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.hycom.training.reservation.servlet.CarParksServlet;
import pl.hycom.training.reservation.servlet.OneCarParkServlet;

/**
 * Main class initializes web-application context
 * 
 * @author Hubert Bieńkowski (hubert.bienkowski@hycom.pl) on 24/06/2016.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SpringRootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic cxfServlet = servletContext.addServlet("CXFServlet", new CXFServlet());
        cxfServlet.setLoadOnStartup(1);
        cxfServlet.addMapping("/ws/api/*");
        
        ServletRegistration.Dynamic carParks = servletContext.addServlet("carParksServlet", CarParksServlet.class);
        carParks.setLoadOnStartup(1);
        carParks.addMapping("/carParks");
        
        Dynamic oneCarpark = servletContext.addServlet("oneCarParkServlet", OneCarParkServlet.class);
        oneCarpark.setLoadOnStartup(1);
        oneCarpark.addMapping("/carPark/*");
        
        super.onStartup(servletContext);
    }
}
