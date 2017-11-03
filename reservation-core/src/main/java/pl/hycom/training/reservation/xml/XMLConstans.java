package pl.hycom.training.reservation.xml;
/**
 * Class contains constants used for XML file parse
 * 
 * @author Paweł Szewczyk (pawel.szewczyk@hycom.pl) HYCOM S.A.
 */
public class XMLConstans {

	public static final String CAR_PARK_NAME_TAG = "carPark";

	public class Parking{
		public static final String ID = "id";
		public static final String TAG_PARKING = "parking";
		public static final String NAME = "name"; 
		public static final String DESCRIPTION = "description";
	}

	public class Level {
		public static final String ID = "id";
		public static final String TAG_LEVEL= "level";
		public static final String ROW = "row";
	}

	public class Row {
		public static final String TAG_ROW= "row";
		public static final String ID = "id";
	}

	public class ParkingSpace {
		public static final String ID = "id";
		public static final String TAG_PARKING_SPACE= "parkingSpace";
		public static final String PLACE_NUMBER = "placeNumber";
		public static final String FOR_DISABLE = "forDisable";
		public static final String TAKEN = "taken";
	}
}
