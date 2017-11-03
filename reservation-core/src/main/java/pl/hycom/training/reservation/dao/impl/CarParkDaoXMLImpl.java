package pl.hycom.training.reservation.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.hycom.training.reservation.common.Constants;
import pl.hycom.training.reservation.dao.api.ICarParkDao;
import pl.hycom.training.reservation.dao.model.Level;
import pl.hycom.training.reservation.dao.model.Parking;
import pl.hycom.training.reservation.dao.model.ParkingSpace;
import pl.hycom.training.reservation.dao.model.Row;
import pl.hycom.training.reservation.xml.XMLConstans;
import pl.hycom.training.reservation.xml.XMLParser;

/**
 * Class implements {@link ICarParkDao} interface and provides data taken from external XML file
 * 
 * @author <a href="mailto:dominik.klys@hycom.pl">Dominik Klys, HYCOM</a>
 *
 */
@Component(value = "carParkDaoXml")
public class CarParkDaoXMLImpl implements ICarParkDao {
    
    private static final Logger LOG = LogManager.getLogger(CarParkDaoXMLImpl.class);
    
    private static final String XPATH_PREFIX = "//";
    private final static String XML_PATH = Constants.XML_PATH_PROPERTY_NAME;
    
    /** Car-parks list */
    private List<Parking> parkings = new ArrayList<Parking>();

    /** Component responsible for providing data from XML file */
    private XMLParser xmlParser;
    
    /**
     * Default constructor - initialize data set
     */
    @Autowired
    public CarParkDaoXMLImpl(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
        init();
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkings()
     */
    public List<Parking> findParkings() {
        return parkings;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#init()
     */
    public void init() {

        parkings.clear();
        
        Document doc = readXml();
        
        if (doc == null) {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("XML cannot be read");
            }

            parkings = Collections.emptyList();
            return;
        }

        Element rootElement = doc.getRootElement();
        if (rootElement.getName().equals(XMLConstans.CAR_PARK_NAME_TAG)) {
            Iterator<Element> parkingIterator = rootElement.elementIterator();
            while (parkingIterator.hasNext()) {
                Element parkingElement = parkingIterator.next();
                Parking parking = objectToParking(parkingElement);
                parkings.add(parking);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingById(int)
     */
    public Parking findParkingById(int id) {
        
        for(Parking p : parkings) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findLevelById(int)
     */
    public Level findLevelById(int id) {
        for(Parking p : parkings) {
            for(Level l : p.getParkingLevels()) {
                if (l.getId() == id) {
                    return l;
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#createParking(pl.hycom.training.reservation.dao.model.Parking)
     */
    public void createParking(Parking parking) {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParking(int)
     */
    public void removeParking(int id) {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#removeParkings()
     */
    public void removeParkings() {
        throw new NotImplementedException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.dao.api.ICarParkDao#findParkingSpaceById(int)
     */
    public ParkingSpace findParkingSpaceById(int id) {
        throw new NotImplementedException("Method not implemented");
    }

    /**
     * Method reads XML file from path (put into the configuration file) and returns {@link Document} object
     * 
     * @return {@link Document} object
     */
    private Document readXml() {
        Document document = null;
        Properties properties = new Properties();
        InputStream input = null;
        String xmlPath = null;

        try {
            String filename = Constants.CONFIGURATION_PROPERTIES_PATH;
            input = getClass().getResourceAsStream(filename);

            properties.load(input);
            xmlPath = properties.getProperty(XML_PATH);
            document = xmlParser.parse(xmlPath);
        } catch (DocumentException  | IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }

        return document;
    }

    
    /**
     * Method reads attributes from XML element and put into {@link Parking} object
     * 
     * @param parking {@link Parking} object
     * @param parkingElement XML element for parking
     */
    @SuppressWarnings("unchecked")
    private static void setParkingAttributes(Parking parking, Element parkingElement) {
        Iterator<DefaultAttribute> atributeIterator = parkingElement.attributeIterator();
        while (atributeIterator.hasNext()) {
            DefaultAttribute attribute = (DefaultAttribute) atributeIterator.next();
            if (attribute.getName().equals(XMLConstans.Parking.ID)) {
                parking.setId(Long.valueOf(attribute.getValue()));
            }
            
            if (attribute.getName().equals(XMLConstans.Parking.NAME)) {
                parking.setName(attribute.getValue());
            }
            if (attribute.getName().equals(XMLConstans.Parking.DESCRIPTION)) {
                parking.setDescription(attribute.getValue());
            }
        }
    }

    /**
     * Method reads attributes from XML element and put into a {@link Level} object
     * 
     * @param level {@link Level} object
     * @param levelElement XML element for level
     * @throws NumberFormatException
     */
    @SuppressWarnings("unchecked")
    private static void setLevelAttributes(Level level, Element levelElement) throws NumberFormatException {
        Iterator<DefaultAttribute> attributeIterator = levelElement.attributeIterator();
        while (attributeIterator.hasNext()) {
            DefaultAttribute attribute = (DefaultAttribute) attributeIterator.next();
            if (attribute.getName().equals(XMLConstans.Level.ID)) {
                level.setId(Long.parseLong(attribute.getValue()));
            }
        }
    }
    
    /**
     * Method reads attributes from XML element and put into a {@link Row} object
     * 
     * @param row {@link Row} object
     * @param element XML element for row
     */
    @SuppressWarnings("unchecked")
    private void setRowAttributes(Row row, Element element) {
        Iterator<DefaultAttribute> attributeIterator = element.attributeIterator();
        while (attributeIterator.hasNext()) {
            DefaultAttribute attribute = (DefaultAttribute) attributeIterator.next();

            if (attribute.getName().equals(XMLConstans.Row.ID)) {
                row.setId(Long.valueOf(attribute.getValue()));
            }
        }
    }

    /**
     * Method reads attributes from XML element and put into a {@link ParkingSpace} object
     * 
     * @param parkingSpace {@link ParkingSpace} object
     * @param element XML element for parking space
     */
    @SuppressWarnings("unchecked")
    private void setParkingSpaceAttributes(ParkingSpace parkingSpace, Element element) {
        Iterator<DefaultAttribute> attributeIterator = element.attributeIterator();
        while (attributeIterator.hasNext()) {
            DefaultAttribute attribute = (DefaultAttribute) attributeIterator.next();

            if (attribute.getName().equals(XMLConstans.ParkingSpace.ID)) {
                parkingSpace.setId(Long.valueOf(attribute.getValue()));
            } else if (attribute.getName().equals(XMLConstans.ParkingSpace.FOR_DISABLE)) {
                parkingSpace.setForDisable(Boolean.valueOf((attribute.getValue())));
            } else if (attribute.getName().equals(XMLConstans.ParkingSpace.TAKEN)) {
                parkingSpace.setTaken(Boolean.valueOf((attribute.getValue())));
            } else if (attribute.getName().equals(XMLConstans.ParkingSpace.PLACE_NUMBER)) {
                parkingSpace.setPlaceNumber(attribute.getValue());
            }
        }
    }

    /**
     * Methods converts given object to {@link Level} object
     * 
     * @param object XML object to converse
     * @return {@link Level} object
     */
    @SuppressWarnings("unchecked")
    private Level objectToLevel(Object object) {
        Element element = (Element) object;
        Level level = new Level();
        setLevelAttributes(level, element);

        for (Iterator<Element> elementIterator = element.elementIterator(); elementIterator.hasNext();) {

            Element childElement = elementIterator.next();

            if (childElement.getName().equals(XMLConstans.Row.TAG_ROW)) {
                Row r = objectToRow(childElement);
                level.getRows().add(r);
                r.setLevel(level);
            }
        }
        return level;
    }

    /**
     * Methods converts given object to {@link Parking} object
     * 
     * @param object XML object to converse
     * @return {@link Parking} object
     */
    @SuppressWarnings("unchecked")
    private Parking objectToParking(Object object) {
        Element element = (Element) object;
        Parking parking = new Parking();
        setParkingAttributes(parking, element);

        Iterator<Element> childElementsIterator = element.elementIterator();

        while (childElementsIterator.hasNext()) {
            Element childElement = childElementsIterator.next();
            if (childElement.getName().equals(XMLConstans.Level.TAG_LEVEL)) {
                Level l = objectToLevel(childElement);
                parking.getParkingLevels().add(l);
                l.setParking(parking);
            }
        }
        
        return parking;
    }
    
    /**
     * Methods converts given object to {@link Row} object
     * 
     * @param object XML object to converse
     * @return {@link Row} object
     */
    @SuppressWarnings("unchecked")
    private Row objectToRow(Object object) {
        Element element = (Element) object;
        Row row = new Row();
        setRowAttributes(row, element);
        
        Iterator<Element> childElementsIterator = element.elementIterator();
        
        while (childElementsIterator.hasNext()) {
            Element childElement = childElementsIterator.next();
            if (childElement.getName().equals(XMLConstans.ParkingSpace.TAG_PARKING_SPACE)) {
                ParkingSpace ps = objectToParkingSpace(childElement);
                row.getParkingSpaces().add(ps);
                ps.setRow(row);
            }
        }

        return row;
    }

    /**
     * Methods converts given object to {@link ParkingSpace} object
     * 
     * @param object XML object to converse
     * @return {@link ParkingSpace} object
     */
    private ParkingSpace objectToParkingSpace(Object object) {
        Element element = (Element) object;
        ParkingSpace parkingSpace = new ParkingSpace();
        setParkingSpaceAttributes(parkingSpace, element);
        return parkingSpace;
    }

    /*
     * (non-Javadoc)
     * @see pl.hycom.training.reservation.model.IDataModelService#getParkingByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Parking> findParkingByName(String parkingName) {
        List<Parking> parkingList = new ArrayList<Parking>();
        List<Node> nodes = readXml().selectNodes(XPATH_PREFIX + XMLConstans.Parking.TAG_PARKING);
        for (Node node : nodes) {
            if (node instanceof Element) {
                Element element = (Element) node;
                Parking parking = objectToParking(element);
                if (parking.getName().equals(parkingName)) {
                    parkingList.add(parking);
                }
            }
        }

        return parkingList;
    }
}
