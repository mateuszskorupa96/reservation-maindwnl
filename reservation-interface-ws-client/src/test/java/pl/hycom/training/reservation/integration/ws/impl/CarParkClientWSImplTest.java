package pl.hycom.training.reservation.integration.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.integration.ws.api.Error;
import pl.hycom.training.reservation.integration.ws.api.ErrorStatus;
import pl.hycom.training.reservation.integration.ws.api.GetParkingList;
import pl.hycom.training.reservation.integration.ws.api.GetParkingListResponse;
import pl.hycom.training.reservation.integration.ws.api.ICarParkWS;
import pl.hycom.training.reservation.integration.ws.api.Parking;
import pl.hycom.training.reservation.model.ParkingDTO;

/**
 * Class implements JUnits for class {@link CarParkClientWSImpl} implementation
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class CarParkClientWSImplTest {

    /**
     * Injected object with WS implementation
     */
    @Mock
    ICarParkWS serviceWS;
    
    /**
     * Tested service object
     */
    @InjectMocks
    IReservationService service = new CarParkClientWSImpl();
    
    /**
     * Method prepared initial data set for each JUnit
     */
    @Before
    public void init() {
        
        MockitoAnnotations.initMocks(this);
        
        GetParkingListResponse getParkingListResponse1 = new GetParkingListResponse();
        Error success = new Error();

        Parking p1 = new Parking();
        p1.setDescription("p1 desc");
        p1.setName("p1 name");
        p1.setId(1);
        
        Parking p2 = new Parking();
        p2.setDescription("p2 desc");
        p2.setName("p2 name");
        p2.setId(2);
        
        success.setStatus(ErrorStatus.SUCCESS);
        
        getParkingListResponse1.setError(success);
        getParkingListResponse1.getParkingList().add(p1);
        getParkingListResponse1.getParkingList().add(p2);
        
        when(serviceWS.getParkingList(any(GetParkingList.class))).thenReturn(getParkingListResponse1);
    }

    /**
     * JUnit for {@link CarParkClientWSImpl#getAllParkings()} implementation
     */
    @Test
    public void testGetAllParkings() {
        List<ParkingDTO> list = service.getAllParkings();
        
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(1, list.get(0).getId().intValue());
        Assert.assertEquals(2, list.get(1).getId().intValue());
        Assert.assertEquals("p1 name", list.get(0).getName());
    }

    @Test
    public void testFindParkingSuccess() {
        ParkingDTO parkingDTO = service.findParking(1);
    }


    @Test
    public void testFindParkingError() {
        throw new NotImplementedException("Not implemented yet");
    }
    
    @Test
    public void testFindLevelSuccess() throws ParkingInvalidArgumentException {
        throw new NotImplementedException("Not implemented yet");
    }

    @Test(expected=ParkingInvalidArgumentException.class)
    public void testFindLevelError() throws ParkingInvalidArgumentException {
        throw new NotImplementedException("Not implemented yet");
    }

    @Test
    public void testBookSuccess() throws PlaceNotAvailableException, PlaceInvalidException {
        throw new NotImplementedException("Not implemented yet");
    }

    @Test(expected=PlaceNotAvailableException.class)
    public void testBookPlaceNotAvailableException() throws PlaceNotAvailableException, PlaceInvalidException {
        throw new NotImplementedException("Not implemented yet");
    }
    
    @Test(expected=PlaceInvalidException.class)
    public void testBookPlaceInvalidException() throws PlaceNotAvailableException, PlaceInvalidException {
        throw new NotImplementedException("Not implemented yet");
    }
}
