package app;

import helper.XmlHelper;

import java.io.File;
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
                if (property.isDirty) {
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
        try {
            if (!this.properties.containsKey(configName)) {
                throw new Exception("Config '" + configName + "' does not exist.");
            }

            this.properties.get(configName).setValue(configValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument() throws Exception {
        return XmlHelper.getDocument(new File(Game.CONTENT_PATH + "config.xml"));
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

        public void setDirty(boolean isDirty) {
            this.isDirty = isDirty;
        }
    }
}
