package pl.hycom.training.reservation.servlet;

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
    
    public static ParkingStructureHelper getInstance() {
        
        if (INSTANCE == null) {
            INSTANCE = new ParkingStructureHelper();
        }

        return INSTANCE;
    }

    /**
     * Method returns a logo CarPark string representation
     */
    public String printLogo() {
        
        StringBuilder sb = new StringBuilder("\n");

        sb.append(
                "          ,ad8888ba,                           88888888ba                           88         ").append("\n");
        sb.append(
                "         d8\"'    `\"8b                          88      \"8b                          88         ").append("\n");
        sb.append(
                "        d8'                                    88      ,8P                          88         ").append("\n");
        sb.append(
                "        88             ,adPPYYba,  8b,dPPYba,  88aaaaaa8P'  ,adPPYYba,  8b,dPPYba,  88   ,d8   ").append("\n");
        sb.append(
                "        88             \"\"     `Y8  88P'   \"Y8  88\"\"\"\"\"\"'    \"\"     `Y8  88P'   \"Y8  88 ,a8\"    ").append("\n");
        sb.append(
                "        Y8,            ,adPPPPP88  88          88           ,adPPPPP88  88          8888[      ").append("\n");
        sb.append(
                "         Y8a.    .a8P  88,    ,88  88          88           88,    ,88  88          88`\"Yba,   ").append("\n");
        sb.append(
                "          `\"Y8888Y\"'   `\"8bbdP\"Y8  88          88           `\"8bbdP\"Y8  88          88   `Y8a  ").append("\n");
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Method returns parking structure (given as a parameter) as a string representation.
     * 
     * @param parking parking object which structure will be displayed
     *        
     */
    public String printInfo(ParkingDTO parking) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Parking [" + parking.getId() + "]: " + parking.getName()).append("\n");
        sb.append("Description: " + parking.getDescription()).append("\n");
        int levelCounter = 0;
        for (LevelDTO level : parking.getLevels()) {
            sb.append("Level [" + level.getId() + "]: " + levelCounter++).append("\n");
            for (RowDTO r : level.getRows()) {
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
            }
        }
        return sb.append("\n").toString();
    }
}
