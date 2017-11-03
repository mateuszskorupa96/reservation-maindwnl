package pl.hycom.training.reservation.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.config.TestConfig;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;

/**
 * JUnits for {@link ReservationServiceImpl} class implementation
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
public class ReservationServiceImplTest {

    /**
     * Tested implementation class
     */
    @Autowired
    @Qualifier("carParkServiceDao")
    IReservationService reservationService;
    
    /**
     * DAO service initializes database
     */
    @Autowired
    @Qualifier("carParkDaoDB")
    ICarParkDao carParkDao;
    
    /**
     * Initializing method executed before each JUnit method
     */
    @Before
    public void init() {
        carParkDao.init();
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#getAllParkings()}
     */
    @Test
    public void testGetAllParkings() {
        List<ParkingDTO> list = reservationService.getAllParkings();
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#findParking(int)}
     */
    @Test
    public void testFindParking() {
        ParkingDTO p = reservationService.findParking(5);
        Assert.assertNotNull(p);
        Assert.assertEquals(5, p.getId().intValue());
        Assert.assertFalse(p.getLevels().isEmpty());
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#findLevel(int, int)} - level found
     */
    @Test
    public void testFindLevel_OK() throws ParkingInvalidArgumentException {
        LevelDTO l = reservationService.findLevel(1, 1);
        Assert.assertNotNull(l);
        Assert.assertEquals(1, l.getId().intValue());
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#findLevel(int, int)} - level NOT found
     */
    @Test(expected = ParkingInvalidArgumentException.class)
    public void testFindLevel_Exception() throws ParkingInvalidArgumentException {
        reservationService.findLevel(2, 1);
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#book(int, int, int, int)} - booked with success
     */
    @Test
    public void testBook_OK() throws PlaceInvalidException, PlaceNotAvailableException {
        reservationService.book(1, 1, 2, 7);
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#book(int, int, int, int)} - PlaceInvalidException
     */
    @Test(expected = PlaceInvalidException.class)
    public void testBook_PlaceInvalidException() throws PlaceInvalidException, PlaceNotAvailableException {
        reservationService.book(1, 1, 1, 100);
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#book(int, int, int, int)} - PlaceNotAvailableException
     */
    @Test(expected = PlaceNotAvailableException.class)
    public void testBook_PlaceNotAvailableException() throws PlaceNotAvailableException, PlaceInvalidException {
        reservationService.book(1, 1, 1, 1);
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#release(int, int, int, int)} - released with success
     */
    @Test
    public void testRelease_OK() throws PlaceInvalidException {
        reservationService.release(1, 1, 1, 1);
    }
    
    /**
     * Method implements JUnit for {@link ReservationServiceImpl#release(int, int, int, int)} - PlaceInvalidException
     */
    @Test(expected = PlaceInvalidException.class)
    public void testRelease_PlaceInvalidException() throws PlaceInvalidException {
        reservationService.release(1, 1, 1, 100);
    }
}
