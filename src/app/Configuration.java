package app;

import helper.XmlHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

    private static Configuration configuration;
    private Map<String, Property> properties;

    private Configuration() {
        try {
            Document document = this.getDocument();
            NodeList nodeList = document.getElementsByTagName("property");
            this.properties = new HashMap<String, Property>(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                Property property = new Property(node.getTextContent());
                this.properties.put(key, property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChanges() {
        try {
            Document document = this.getDocument();
            NodeList nodeList = document.getElementsByTagName("property");

            for (String key : this.properties.keySet()) {
                Property property = this.properties.get(key);
                if (property.isDirty()) {
                    this.putIntoNodeList(nodeList, key, property);
                }
            }

            XmlHelper.saveDocument(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putIntoNodeList(NodeList nodeList, String configName, Property property) {
        int i = 0;
        while (i < nodeList.getLength()) {
            if (nodeList.item(i).hasAttributes()
                    && nodeList.item(i).getAttributes().getNamedItem("name") != null) {

                if (nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue()
                        .equals(configName)) {
                    nodeList.item(i).setTextContent(property.getValue());
                    break;
                }
            }
            i++;
        }
    }

    public void set(String configName, String configValue) {
        if (!this.properties.containsKey(configName)) {
            this.properties.put(configName, new Property(configValue));
            return;
        }

        this.properties.get(configName).setValue(configValue);
    }

    private Document getDocument() throws Exception {
        File file = new File(Game.CONTENT_PATH + "config.xml");
        if (!file.exists()) {
            file.createNewFile();
            String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<configuration>\n    <properties>";
            s += "<property name=\"language\">en</property>\n";
            s += "<property name=\"width\">0</property>\n";
            s += "<property name=\"height\">0</property>\n";
            s += "<property name=\"fullscreen\">true</property>\n";
            s += "<property name=\"autoscale\">false</property>\n";
            s += "<property name=\"scale\">1</property>\n";
            s += "<property name=\"refreshSpeed\">500</property>\n";
            s += "\n    </properties>\n</configuration>";
            byte[] buffer = s.getBytes("UTF-8");
            OutputStream stream = new FileOutputStream(file);
            stream.write(buffer);
            stream.close();
        }
        return XmlHelper.getDocument(file);
    }

    public String get(String configName) {
        return this.properties.get(configName).getValue();
    }

    public static Configuration getInstance() {
        if (Configuration.configuration == null) {
            Configuration.configuration = new Configuration();
        }

        return Configuration.configuration;
    }

    private class Property {

        private String value;
        private boolean isDirty = false;

        public Property(String value) {
            this.value = value;
            this.isDirty = true;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
            this.isDirty = true;
        }

        public boolean isDirty() {
            return this.isDirty;
        }
    }
}
