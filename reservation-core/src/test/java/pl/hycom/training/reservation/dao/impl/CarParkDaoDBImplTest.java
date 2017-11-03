package pl.hycom.training.reservation.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.hycom.training.reservation.config.TestConfig;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;

/**
 * JUnits for {@link CarParkDaoDBImpl} class
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class CarParkDaoDBImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * Injected tested object
     */
    @Autowired
    @Qualifier(value = "carParkDaoDB")
    private ICarParkDao carParkDao;
    
    /**
     * Method responsible for preparing initial data for each JUnit
     */
    @Before
    public void init() {
        carParkDao.init();
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findParkings()}
     */
    @Test
    public void testFindParkings() {
        List<Parking> list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(2, list.get(0).getParkingLevels().size());
        Assert.assertEquals(3, list.get(0).getParkingLevels().get(0).getRows().size());
        Assert.assertEquals(5, list.get(0).getParkingLevels().get(0).getRows().get(0).getParkingSpaces().size());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findParkingById(int)} - parking found
     */
    @Test
    public void testFindParkingById_FOUND() {
        List<Parking> list = carParkDao.findParkings();
        Parking p = carParkDao.findParkingById(list.get(0).getId().intValue());
        Assert.assertNotNull(p);
        Assert.assertEquals(list.get(0).getId(), p.getId());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findParkingById(int)} - parking not found
     */
    @Test
    public void testFindParkingById_NOT_FOUND() {
        Parking p = carParkDao.findParkingById(6);
        Assert.assertNull(p);
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findLevelById(int)} - level found
     */
    @Test
    public void testFindLevelById_FOUND() {
        Level l = carParkDao.findLevelById(1);
        Assert.assertNotNull(l);
        Assert.assertEquals(1, l.getId().intValue());
        Assert.assertNotNull(l.getParking());
        Assert.assertEquals(1, l.getParking().getId().intValue());
        Assert.assertEquals(3, l.getRows().size());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findLevelById(int)} - level not found
     */
    @Test
    public void testFindLevelById_NOT_FOUND() {
        Level l = carParkDao.findLevelById(100);
        Assert.assertNull(l);
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findParkingSpaceById(int)} - parking space found
     */
    @Test
    public void testFindParkingSpaceById_FOUND() {
        ParkingSpace ps = carParkDao.findParkingSpaceById(1);
        Assert.assertNotNull(ps);
        Assert.assertEquals(1, ps.getId().intValue());
        Assert.assertFalse(ps.isForDisable());
        Assert.assertTrue(ps.isTaken());
        Assert.assertNotNull(ps.getRow());
        Assert.assertNotNull(ps.getRow().getLevel());
        Assert.assertNotNull(ps.getRow().getLevel().getParking());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#findParkingSpaceById(int)} - parking space not found
     */
    @Test
    public void testFindParkingSpaceById_NOT_FOUND() {
        ParkingSpace ps = carParkDao.findParkingSpaceById(200);
        Assert.assertNull(ps);
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#removeParkings()}
     */
    @Test
    public void testRemoveParkings() {
        List<Parking> list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
        carParkDao.removeParkings();
        list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }
    
    /**
     * Method tests implementation of method {@link CarParkDaoDBImpl#removeParking(int)}
     */
    @Test
    public void testRemoveParking() {
        List<Parking> list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
        carParkDao.removeParking(list.get(0).getId().intValue());
        list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(4, list.size());
        
        list = carParkDao.findParkings();
        carParkDao.removeParking(list.get(0).getId().intValue());
        list = carParkDao.findParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(3, list.size());
    }
}
