package pl.hycom.training.reservation.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

/**
 * Class provides method to parse XML file
 * 
 * @author Paweł Szewczyk (pawel.szewczyk@hycom.pl) HYCOM S.A.
 */
@Component
public class XMLParser {

	/**
	 * Method parses XML file from given URL
	 * 
	 * @param url url to XML file
	 * @return {@link Document} object already parsed from XML
	 * @throws DocumentException
	 */
	public Document parse(String url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}
}
