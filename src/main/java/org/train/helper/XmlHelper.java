package org.train.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlHelper {

    public static Document getDocument(File xml) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(xml);
        document.normalizeDocument();

        return document;
    }

    public static void saveDocument(Document document, String path)
            throws TransformerConfigurationException, TransformerFactoryConfigurationError,
            TransformerFactoryConfigurationError, TransformerException, IOException {

        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        DOMSource dom = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(dom, sr);

        String string = sw.toString();
        FileWriter fw = new FileWriter(new File(path));
        fw.write(string);
        fw.close();
    }
}
