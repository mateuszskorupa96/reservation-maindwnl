package pl.hycom.training.reservation.integration.ws.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.soap.SoapFaultException;
import org.springframework.stereotype.Service;

import pl.hycom.training.reservation.api.service.IReservationService;
import pl.hycom.training.reservation.dao.model.ParkingSpace;
import pl.hycom.training.reservation.exception.ParkingInvalidArgumentException;
import pl.hycom.training.reservation.exception.PlaceInvalidException;
import pl.hycom.training.reservation.exception.PlaceNotAvailableException;
import pl.hycom.training.reservation.integration.ws.api.*;
import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * Class provides implementation for getting data from WS and prepare proper model, which will be used in client web-application.
 *
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 */
@Service("wsClientService")
public class CarParkClientWSImpl implements IReservationService {

    private static final Logger LOG = LogManager.getLogger(CarParkClientWSImpl.class);

    /**
     * WS client
     */
    @Autowired
    @Qualifier("clientWS")
    private ICarParkWS client;

    /**
     * Method provides list of {@link ParkingDTO} object. Data are provided by web-service.
     *
     * @return parkings list
     */
    public List<ParkingDTO> getAllParkings() {

        List<ParkingDTO> result = new ArrayList<ParkingDTO>();

        try {

            GetParkingList request = new GetParkingList();

            GetParkingListResponse response = client.getParkingList(request);

            if (response != null) {
                if (response.getError().getStatus() != ErrorStatus.ERROR) {
                    for (Parking p : response.getParkingList()) {
                        ParkingDTO parking = new ParkingDTO(Long.valueOf(p.getId()), p.getName(), p.getDescription());
                        result.add(parking);
                    }
                } else {
                    LOG.error(response.getError().getMessage());
                }
            } else {
                LOG.error("WS error. Response is null");
            }
        } catch (SoapFaultException | WebServiceException e) {
            LOG.error("WS error: " + e.getMessage());
        }

        return result;
    }

    public ParkingDTO findParking(int id) {
        ParkingDTO parkingDTO = new ParkingDTO();

        try {

            GetParkingDetails request = new GetParkingDetails();

            GetParkingDetailsResponse response = client.getParkingDetails(request);
            if (response != null) {
                if (response.getError().getStatus() != ErrorStatus.ERROR) {
                    if (response.getParking().getId() == id) {
                        parkingDTO = new ParkingDTO(Long.valueOf(response.getParking().getId()), response.getParking().getName(), response.getParking().getDescription());
                    }
                } else {
                    LOG.debug(response.getError().getMessage());
                }
            } else {
                LOG.debug("Parking with id: {} does not exist", id);
            }
        } catch (SoapFaultException | WebServiceException e) {
            LOG.error("WS error {}", e.getMessage());
        }
        return parkingDTO;
    }

    public LevelDTO findLevel(int parkingId, int levelId) throws ParkingInvalidArgumentException {

        LevelDTO lDTO = new LevelDTO();

        try {
            GetLevelDetails request = new GetLevelDetails();
            GetLevelDetailsResponse response = client.getLevelDetails(request);

            if (response != null) {


                LOG.debug("Parking level with ID= {} found", levelId);


                if (response.getParking() == null || response.getParking().getId() != parkingId) {
                    throw new ParkingInvalidArgumentException("Parking level with ID=" + levelId + " has invalid parking identifier");
                }

                int order = 0;

                for (Level level : response.getParking().getLevels()) {
                    if (level.getId() == response.getLevel().getId()) {
                        break;
                    }
                    order++;
                }

                lDTO = new LevelDTO(Long.valueOf(response.getLevel().getId()), null, new ParkingDTO(Long.valueOf(response.getParking().getId()), response.getParking().getName(), response.getParking().getDescription()), order);

                List<RowDTO> rows = new ArrayList<RowDTO>();

                for (Row r : response.getLevel().getRows()) {
                    RowDTO rDTO = new RowDTO(Long.valueOf(r.getId()), null);

                    for (Space ps : r.getSpaces()) {
                        rDTO.getParkingSpaces().add(new ParkingSpaceDTO(Long.valueOf(ps.getId()), ps.getNumber(), ps.isForDisable(), ps.isTaken()));
                    }
                    rows.add(rDTO);
                }
                lDTO.setRows(rows);

            } else {
                LOG.debug(response.getError().getMessage());
            }

        } catch (SoapFaultException | WebServiceException e) {
            LOG.error("WS error {}", e.getMessage());
        }

        return lDTO;

    }


    public void book(int parkingId, int levelId, int rowId, int placeId)
            throws PlaceNotAvailableException, PlaceInvalidException {

            BookPlace request = new BookPlace();
            BookPlaceResponse response = client.bookPlace(request);
            if (response != null) {
                if (response.getError().getStatus() == ErrorStatus.SUCCESS) {
                    LOG.debug("Booked successfully");
                }
            } else {
                throw new PlaceInvalidException("Place invalid");
            }
    }

    public void release(int parkingId, int levelId, int rowId, int placeId) throws PlaceInvalidException {
        throw new NotImplementedException("Not implemented yet");
    }
}
