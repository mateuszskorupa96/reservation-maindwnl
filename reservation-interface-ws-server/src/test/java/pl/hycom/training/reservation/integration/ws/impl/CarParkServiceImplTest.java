package pl.hycom.training.reservation.integration.ws.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.dao.impl.CarParkDaoDBImpl;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.integration.ws.api.BookPlace;
import pl.hycom.training.reservation.integration.ws.api.BookPlaceResponse;
import pl.hycom.training.reservation.integration.ws.api.ErrorStatus;
import pl.hycom.training.reservation.integration.ws.api.GetLevelDetails;
import pl.hycom.training.reservation.integration.ws.api.GetLevelDetailsResponse;
import pl.hycom.training.reservation.integration.ws.api.GetParkingDetails;
import pl.hycom.training.reservation.integration.ws.api.GetParkingDetailsResponse;
import pl.hycom.training.reservation.integration.ws.api.GetParkingList;
import pl.hycom.training.reservation.integration.ws.api.GetParkingListResponse;
import pl.hycom.training.reservation.integration.ws.api.ICarParkWS;
import pl.hycom.training.reservation.integration.ws.api.ReleasePlace;
import pl.hycom.training.reservation.integration.ws.api.ReleasePlaceResponse;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * JUnits for {@link CarParkDaoDBImpl} implementation
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class CarParkServiceImplTest {

    /**
     * Repository service provides mocked data
     */
    @Mock
    private IReservationService service;
    
    /**
     * Tested service implementation
     */
    @InjectMocks
    private ICarParkWS carParkWS = new CarParkServiceImpl();
    
    /**
     * Method responsible for preparing initial set of data for each JUnit
     */
    @Before
    public void init() throws ParkingInvalidArgumentException, PlaceNotAvailableException, PlaceInvalidException {
        
        MockitoAnnotations.initMocks(this);

        ParkingDTO p1 = new ParkingDTO(1L, "p1 name", "p1 desc");

        LevelDTO l1 = new LevelDTO(1L, null, p1, 0);
        
        ParkingSpaceDTO ps1 = new ParkingSpaceDTO(2L, "2B", false, false);
        
        ParkingSpaceDTO ps2 = new ParkingSpaceDTO(1L, "2A", true, true);

        List<RowDTO> rows1 = new ArrayList<>();

        RowDTO r1 = new RowDTO(1L, new ArrayList<ParkingSpaceDTO>());
        r1.getParkingSpaces().add(ps1);
        r1.getParkingSpaces().add(ps2);
        rows1.add(r1);
        l1.setRows(rows1);

        LevelDTO l2 = new LevelDTO(2L, null, p1, 1);

        LevelDTO l3 = new LevelDTO(3L, null, p1, 2);

        List<LevelDTO> levels1 = new ArrayList<>();
        levels1.add(l1);
        levels1.add(l2);
        levels1.add(l3);
        p1.setLevels(levels1);


        ParkingDTO p2 = new ParkingDTO(2L, "p2 name", "p2 desc");
        
        ParkingDTO p3 = new ParkingDTO(3L, "p3 name", "p3 desc");
        
        List<ParkingDTO> parkings = new ArrayList<>();
        parkings.add(p1);
        parkings.add(p2);
        parkings.add(p3);
        
        when(service.getAllParkings()).thenReturn(parkings);
        when(service.findParking(1)).thenReturn(p1);
        when(service.findParking(2)).thenReturn(null);

        when(service.findLevel(1, 1)).thenThrow(new ParkingInvalidArgumentException("No parking found"));
        when(service.findLevel(1, 2)).thenReturn(null);
        when(service.findLevel(0, 1)).thenReturn(l1);

        doThrow(new PlaceInvalidException("Place invalid")).when(service).book(0, 0, 0, 2);
        doThrow(new PlaceNotAvailableException("Place not availiable")).when(service).book(0, 0, 0, 1);
        doThrow(new PlaceInvalidException("Place invalid")).when(service).release(0, 0, 0, 2);
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getParkingList(GetParkingList)} method
     */
    @Test
    public void testGetParkingList() {
        GetParkingList getParkingList = new GetParkingList();
        GetParkingListResponse response = carParkWS.getParkingList(getParkingList);
        Assert.assertNotNull(response);
        Assert.assertEquals(3, response.getParkingList().size());
        Assert.assertEquals("p1 name", response.getParkingList().get(0).getName());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getParkingDetails(GetParkingDetails)} method with success result
     */
    @Test
    public void testGetParkingDetailsSuccess() {
        GetParkingDetails getParkingDetails = new GetParkingDetails();
        getParkingDetails.setParkingId(1);
        GetParkingDetailsResponse response = carParkWS.getParkingDetails(getParkingDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.SUCCESS, response.getError().getStatus());
        Assert.assertNotNull(response.getParking());
        Assert.assertEquals("p1 name", response.getParking().getName());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getParkingDetails(GetParkingDetails)} method with error result
     */
    @Test
    public void testGetParkingDetailsError() {
        GetParkingDetails getParkingDetails = new GetParkingDetails();
        getParkingDetails.setParkingId(2);
        GetParkingDetailsResponse response = carParkWS.getParkingDetails(getParkingDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.ERROR, response.getError().getStatus());
        Assert.assertNull(response.getParking());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getLevelDetails(GetLevelDetails)} method with success result
     */
    @Test
    public void testGetLevelDetailsSuccess() {
        GetLevelDetails getLevelDetails = new GetLevelDetails();
        getLevelDetails.setLevelId(1);
        getLevelDetails.setParkingId(0);
        GetLevelDetailsResponse response = carParkWS.getLevelDetails(getLevelDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.SUCCESS, response.getError().getStatus());
        Assert.assertNotNull(response.getParking());
        Assert.assertEquals("p1 name", response.getParking().getName());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getLevelDetails(GetLevelDetails)} method with error result
     */
    @Test
    public void testGetLevelDetailsError() {
        GetLevelDetails getLevelDetails = new GetLevelDetails();
        getLevelDetails.setLevelId(1);
        getLevelDetails.setParkingId(1);
        GetLevelDetailsResponse response = carParkWS.getLevelDetails(getLevelDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.ERROR, response.getError().getStatus());
        Assert.assertNull(response.getLevel());
        Assert.assertNull(response.getParking());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#getLevelDetails(GetLevelDetails)} method with warning result
     */
    @Test
    public void testGetLevelDetailsWarning() {
        GetLevelDetails getLevelDetails = new GetLevelDetails();
        getLevelDetails.setLevelId(2);
        getLevelDetails.setParkingId(2);
        GetLevelDetailsResponse response = carParkWS.getLevelDetails(getLevelDetails);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.WARNING, response.getError().getStatus());
        Assert.assertNull(response.getLevel());
        Assert.assertNull(response.getParking());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#bookPlace(BookPlace)} method with success result
     */
    @Test
    public void testBookSuccess() {
        BookPlace request = new BookPlace();
        request.setParkingId(0);
        request.setLevelId(0);
        request.setRowId(0);
        request.setSpaceId(0);
        BookPlaceResponse response = carParkWS.bookPlace(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.SUCCESS, response.getError().getStatus());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#bookPlace(BookPlace)} method with error result (place is already taken)
     */
    @Test
    public void testBookErrorTaken() {
        BookPlace request = new BookPlace();
        request.setParkingId(0);
        request.setLevelId(0);
        request.setRowId(0);
        request.setSpaceId(1);
        BookPlaceResponse response = carParkWS.bookPlace(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.ERROR, response.getError().getStatus());
        Assert.assertEquals(2, response.getError().getCode());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#bookPlace(BookPlace)} method with error result (place is not available)
     */
    @Test
    public void testBookErrorNotExist() {
        BookPlace request = new BookPlace();
        request.setParkingId(0);
        request.setLevelId(0);
        request.setRowId(0);
        request.setSpaceId(2);
        BookPlaceResponse response = carParkWS.bookPlace(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.ERROR, response.getError().getStatus());
        Assert.assertEquals(1, response.getError().getCode());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#releasePlace(ReleasePlace)} method with success result
     */
    @Test
    public void testBookReleaseSuccess() {
        ReleasePlace request = new ReleasePlace();
        request.setParkingId(0);
        request.setLevelId(0);
        request.setRowId(0);
        request.setSpaceId(1);
        ReleasePlaceResponse response = carParkWS.releasePlace(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.SUCCESS, response.getError().getStatus());
    }
    
    /**
     * Method tests implementation of {@link CarParkServiceImpl#releasePlace(ReleasePlace)} method with error result
     */
    @Test
    public void testBookReleaseError() {
        ReleasePlace request = new ReleasePlace();
        request.setParkingId(0);
        request.setLevelId(0);
        request.setRowId(0);
        request.setSpaceId(2);
        ReleasePlaceResponse response = carParkWS.releasePlace(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(ErrorStatus.ERROR, response.getError().getStatus());
    }
}
