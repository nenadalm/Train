package app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Helper.XmlHelper;

public class Configuration {

    private static Configuration configuration;
    private Map<String, String> properties;

    private Configuration() {
        try {
            Document document = XmlHelper.getDocument(new File(
                    Game.CONTENT_PATH + "config.xml"));
            NodeList nodeList = document.getElementsByTagName("property");
            this.properties = new HashMap<String, String>(nodeList.getLength());
            System.out.println(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println("start");
                Node node = nodeList.item(i);
                System.out.println(node.getNodeName());
                String key = node.getAttributes().getNamedItem("name")
                        .getNodeValue();
                String value = node.getTextContent();
                System.out.println("start");
                this.properties.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
