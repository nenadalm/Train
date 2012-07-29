package Helper;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlHelper {

    public static Document getDocument(File xml)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document document = documentBuilder.parse(xml);
        document.normalizeDocument();

        return document;
    }
}
