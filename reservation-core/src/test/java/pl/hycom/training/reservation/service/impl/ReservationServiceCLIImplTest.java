package pl.hycom.training.reservation.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;
import pl.hycom.training.reservation.dao.model.Row;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;

/**
 * JUnits for {@link ReservationServiceCLIImpl} class implementation
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class ReservationServiceCLIImplTest {
    
    @Mock
    private ICarParkDao carParkDao;
    
    /**
     * Tested class implementation
     */
    @InjectMocks
    private IReservationService reservationService = new ReservationServiceCLIImpl();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        List<Parking> list = new ArrayList<Parking>();

        Parking p1 = new Parking();
        p1.setId(1L);
        p1.setName("p1name");
        p1.setDescription("p1description");
        
        Parking p2 = new Parking();
        p2.setId(2L);
        p2.setName("p2name");
        p2.setDescription("p2description");
        
        Parking p3 = new Parking();
        p3.setId(3L);
        p3.setName("p3name");
        p3.setDescription("p3description");
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        
        Level l1 = new Level();
        l1.setId(1L);
        l1.setParking(p1);
        
        Level l2 = new Level();
        l2.setId(2L);
        l2.setParking(p1);
        
        
        Row r1 = new Row();
        r1.setId(1L);
        r1.setLevel(l2);
        l2.getRows().add(r1);
        
        ParkingSpace ps1 = new ParkingSpace();
        ps1.setId(1L);
        ps1.setRow(r1);
        r1.getParkingSpaces().add(ps1);
        
        ParkingSpace ps2 = new ParkingSpace();
        ps2.setId(3L);
        ps2.setRow(r1);
        ps2.setTaken(true);
        r1.getParkingSpaces().add(ps2);
        
        Mockito.when(carParkDao.findParkings()).thenReturn(list);
        Mockito.when(carParkDao.findParkingById(1)).thenReturn(p1);
        
        Mockito.when(carParkDao.findLevelById(1)).thenReturn(l1);
        
        Mockito.when(carParkDao.findLevelById(2)).thenReturn(l2);
    }

    /**
     * JUnit for {@link ReservationServiceCLIImpl#getAllParkings()} implementation
     */
    @Test
    public void testGetAllParkings() {
       Assert.assertNotNull(reservationService);
       
       List<ParkingDTO> list = reservationService.getAllParkings();
       
       Assert.assertNotNull(list);
       Assert.assertEquals(3, list.size());
       Assert.assertEquals(1, list.get(0).getId().intValue());
       Assert.assertEquals("p1name", list.get(0).getName());
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#findParking(int)} implementation - found
     */
    @Test
    public void testFindParkingFound() {
        Assert.assertNotNull(reservationService);
        
        ParkingDTO p = reservationService.findParking(1);
        
        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getId().intValue());
        Assert.assertEquals("p1name", p.getName());
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#findLevel(int, int)} implementation - found
     */
    @Test
    public void testFindLevelFound() throws ParkingInvalidArgumentException {
        
        LevelDTO level = reservationService.findLevel(1, 1);

        Assert.assertNotNull(reservationService);
        Assert.assertNotNull(level);
        
        Assert.assertEquals(1, level.getId().intValue());
        Assert.assertNotNull(level.getParking().getId());
        Assert.assertEquals(1, level.getParking().getId().intValue());
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#findLevel(int, int)} implementation - not found
     */
    @Test
    public void testFindLevelNotFound() throws ParkingInvalidArgumentException {
        Assert.assertNotNull(reservationService);
        
        LevelDTO level = reservationService.findLevel(3, 3);
        
        Assert.assertNull(level);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#findLevel(int, int)} implementation - not found - ParkingInvalidArgumentException
     */
    @Test(expected = ParkingInvalidArgumentException.class)
    public void testFindLevelNotFoundException() throws ParkingInvalidArgumentException {
        Assert.assertNotNull(reservationService);
        reservationService.findLevel(2, 2);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#book(int, int, int, int)} implementation - OK
     */
    @Test
    public void testBookOK() throws PlaceInvalidException, PlaceNotAvailableException {
        Assert.assertNotNull(reservationService);
        
        reservationService.book(1, 2, 1, 1);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#book(int, int, int, int)} implementation - PlaceInvalidException
     */
    @Test(expected = PlaceInvalidException.class)
    public void testBookPlaceInvalidException() throws PlaceInvalidException, PlaceNotAvailableException {
        Assert.assertNotNull(reservationService);
        
        reservationService.book(1, 2, 1, 2);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#book(int, int, int, int)} implementation - PlaceNotAvailableException
     */
    @Test(expected = PlaceNotAvailableException.class)
    public void testBookPlaceNotAvailableException() throws PlaceInvalidException, PlaceNotAvailableException {
        Assert.assertNotNull(reservationService);
        
        reservationService.book(1, 2, 1, 3);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#release(int, int, int, int)} implementation - OK
     */
    @Test
    public void testRelease() throws PlaceInvalidException {
        Assert.assertNotNull(reservationService);
        
        reservationService.release(1, 2, 1, 1);
    }
    
    /**
     * JUnit for {@link ReservationServiceCLIImpl#release(int, int, int, int)} implementation - PlaceInvalidException
     */
    @Test(expected = PlaceInvalidException.class)
    public void testReleasePlaceInvalidException() throws PlaceInvalidException {
        Assert.assertNotNull(reservationService);
        
        reservationService.release(1, 2, 1, 2);
    }
}
