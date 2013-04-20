package org.train.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.train.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

    private Map<String, Property> properties;

    public Configuration() {
        this.loadConfiguration();
    }

    private void loadConfiguration() {
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

            XmlHelper.saveDocument(document, "config.xml");

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
        File file = new File("config.xml");
        if (!file.exists()) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            Element configuration = document.createElement("configuration");
            Element properties = document.createElement("properties");
            configuration.appendChild(properties);
            for (Entry<String, String> property : this.getDefaultProperties().entrySet()) {
                Element prop = document.createElement("property");
                prop.setAttribute("name", property.getKey());
                prop.setTextContent(property.getValue());
                properties.appendChild(prop);
            }
            document.appendChild(configuration);
            XmlHelper.saveDocument(document, "config.xml");
            return document;
        }
        return XmlHelper.getDocument(file);
    }

    private Map<String, String> getDefaultProperties() {
        HashMap<String, String> properties = new HashMap<String, String>(7);
        properties.put("language", "en");
        properties.put("width", "0");
        properties.put("height", "0");
        properties.put("fullscreen", "true");
        properties.put("autoscale", "false");
        properties.put("scale", "1");
        properties.put("refreshSpeed", "350");
        properties.put("contentPath", "content/");
        properties.put("levelsPath", "content/levels/");
        properties.put("graphicsPath", "content/graphics/");
        return properties;
    }

    public String get(String configName) {
        Property property = this.properties.get(configName);

        if (property == null) {
            try {
                String propertyValue = this.getDefaultProperties().get(configName);
                Document document = this.getDocument();
                NodeList properties = document.getElementsByTagName("properties");

                Element prop = document.createElement("property");
                prop.setAttribute("name", configName);
                prop.setTextContent(propertyValue);

                properties.item(0).appendChild(prop);
                XmlHelper.saveDocument(document, "config.xml");
                this.loadConfiguration();
                property = this.properties.get(configName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return property.getValue();
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
