package pl.hycom.training.reservation.dao.impl;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.hycom.training.reservation.config.TestConfig;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Parking;

/**
 * JUnits for {@link CarParkDaoImpl} class
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class CarParkDaoXMLImplTest {

    /**
     * Injected tested object
     */
    @Autowired
    @Qualifier(value = "carParkDaoXml")
    private ICarParkDao carParkDao;
   
    /**
     * Method tests implementation of method {@link CarParkDaoImpl#findParkings()}
     */
    @Test
    public void testFindAll() {
        List<Parking> list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoImpl#findParkingById(int)}
     */
    @Test
    public void testFindParkingById() {
        List<Parking> list = carParkDao.findParkings();
        Parking p = carParkDao.findParkingById(list.get(0).getId().intValue());
        Assert.assertNotNull(p);
        Assert.assertEquals(list.get(0).getId(), p.getId());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoImpl#removeParkings()}
     */
    @Test(expected = NotImplementedException.class)
    public void testRemoveAll() {
        carParkDao.removeParkings();
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoImpl#removeParking(int)}
     */
    @Test(expected = NotImplementedException.class)
    public void testRemove() {
        carParkDao.removeParking(1);
    }
}
