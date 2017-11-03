package pl.hycom.training.reservation.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.hycom.training.reservation.model.LevelDTO;
import pl.hycom.training.reservation.model.ParkingDTO;
import pl.hycom.training.reservation.model.ParkingSpaceDTO;
import pl.hycom.training.reservation.model.RowDTO;

/**
 * Class provides the methods to display parking structure
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
public class ParkingStructureHelper {
    
    private static ParkingStructureHelper INSTANCE;
    
    private static final Logger LOG = LogManager.getLogger(ParkingStructureHelper.class);
    
    public static ParkingStructureHelper getInstance() {
        
        if (INSTANCE == null) {
            INSTANCE = new ParkingStructureHelper();
        }

        return INSTANCE;
    }

    /**
     * Method displays a program menu with possible choices.
     */
    public void printHelp() {

        LOG.info(
                "          ,ad8888ba,                           88888888ba                           88         ");
        LOG.info(
                "         d8\"'    `\"8b                          88      \"8b                          88         ");
        LOG.info(
                "        d8'                                    88      ,8P                          88         ");
        LOG.info(
                "        88             ,adPPYYba,  8b,dPPYba,  88aaaaaa8P'  ,adPPYYba,  8b,dPPYba,  88   ,d8   ");
        LOG.info(
                "        88             \"\"     `Y8  88P'   \"Y8  88\"\"\"\"\"\"'    \"\"     `Y8  88P'   \"Y8  88 ,a8\"    ");
        LOG.info(
                "        Y8,            ,adPPPPP88  88          88           ,adPPPPP88  88          8888[      ");
        LOG.info(
                "         Y8a.    .a8P  88,    ,88  88          88           88,    ,88  88          88`\"Yba,   ");
        LOG.info(
                "          `\"Y8888Y\"'   `\"8bbdP\"Y8  88          88           `\"8bbdP\"Y8  88          88   `Y8a  ");
        LOG.info("\n");
        LOG.info("Parknig reservation system for HyPark");
        LOG.info("1. Display information about all parkings");
        LOG.info("2. Display information about chosen parking");
        LOG.info("0. Exit");
        LOG.info("Your choice:");
    }

    /**
     * Method displays parking structure (given as a parameter).
     * 
     * @param parking parking object which structure will be displayed
     *        
     */
    public void printInfo(ParkingDTO parking) {
        LOG.info("Parking [" + parking.getId() + "]: " + parking.getName());
        LOG.info("Description: " + parking.getDescription());
        int levelCounter = 0;
        for (LevelDTO level : parking.getLevels()) {
            LOG.info("Level [" + level.getId() + "]: " + levelCounter++);
            for (RowDTO r : level.getRows()) {
                StringBuilder sb = new StringBuilder("\n");
                for (ParkingSpaceDTO ps : r.getParkingSpaces()) {
                    sb.append("| " + ps.getPlaceNumber() + "|\t");
                }
                sb.append("\n");
                for (ParkingSpaceDTO ps : r.getParkingSpaces()) {
                    if (ps.isTaken()) {
                        sb.append("| x |\t");
                    } else {
                        sb.append("|   |\t");
                    }
                }
                sb.append("\n");
                LOG.info(sb.toString());
            }
        }
    }
}
