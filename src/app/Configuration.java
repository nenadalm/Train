package app;

import helper.XmlHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

    private static Configuration configuration;
    private Map<String, String> properties;

    private Configuration() {
        try {
            Document document = this.getDocument();
            NodeList nodeList = document.getElementsByTagName("property");
            this.properties = new HashMap<String, String>(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                String value = node.getTextContent();
                this.properties.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String configName, String configValue) {

        try {
            if (!this.properties.containsKey(configName)) {
                throw new Exception("Config '" + configName + "' does not exist.");
            }

            Document document = this.getDocument();
            NodeList nodeList = document.getElementsByTagName("property");
            int i = 0;
            while (i < nodeList.getLength()) {
                if (nodeList.item(i).hasAttributes()
                        && nodeList.item(i).getAttributes().getNamedItem("name") != null) {

                    if (nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue()
                            .equals(configName)) {
                        nodeList.item(i).setTextContent(configValue);
                        this.properties.put(configName, configValue);
                        break;
                    }
                }
                i++;
            }
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            DOMSource dom = new DOMSource(document);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(dom, sr);
            String string = sw.toString();
            FileWriter fw = new FileWriter(new File(Game.CONTENT_PATH + "config.xml"));
            fw.write(string);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument() throws Exception {
        return XmlHelper.getDocument(new File(Game.CONTENT_PATH + "config.xml"));
    }

    public String get(String configName) {
        return this.properties.get(configName);
    }

    public static Configuration getInstance() {
        if (Configuration.configuration == null) {
            Configuration.configuration = new Configuration();
        }

        return Configuration.configuration;
    }
}
