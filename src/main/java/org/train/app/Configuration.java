package org.train.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.train.helper.XmlHelper;
import org.train.loader.ConfigurationLoader;
import org.train.loader.ConfigurationLoaderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Configuration {

    private ConfigurationLoaderFactory configurationLoaderFactory;
    private Map<String, ConfigurationProperty> properties;

    public Configuration(ConfigurationLoaderFactory configurationLoaderFactory) {
        this.configurationLoaderFactory = configurationLoaderFactory;
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        ConfigurationLoader configLoader = this.configurationLoaderFactory.getLoader();
        this.properties = configLoader.load();
        this.refillDefaultProperties();
    }

    public void saveChanges() {
        try {
            File file = new File("config.xml");
            if (!file.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document document = docBuilder.newDocument();

                Element configuration = document.createElement("configuration");
                Element properties = document.createElement("properties");
                configuration.appendChild(properties);
                document.appendChild(configuration);
                XmlHelper.saveDocument(document, "config.xml");
            }
            Document document = XmlHelper.getDocument(file);
            NodeList properties = document.getElementsByTagName("property");

            for (String key : this.properties.keySet()) {
                ConfigurationProperty property = this.properties.get(key);
                if (property.isDirty()) {
                    this.updateNodeList(document, properties, key, property);
                }
            }

            XmlHelper.saveDocument(document, "config.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNodeList(Document document, NodeList nodeList, String configName,
            ConfigurationProperty property) {
        int i = 0;
        boolean updated = false;
        while (i < nodeList.getLength()) {
            if (nodeList.item(i).hasAttributes()
                    && nodeList.item(i).getAttributes().getNamedItem("name") != null) {

                if (nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue()
                        .equals(configName)) {
                    nodeList.item(i).setTextContent(property.getValue());
                    updated = true;
                    break;
                }
            }
            i++;
        }

        if (!updated) {
            Element newProperty = document.createElement("property");
            newProperty.setAttribute("name", configName);
            newProperty.setTextContent(property.getValue());
            document.getElementsByTagName("properties").item(0).appendChild(newProperty);
        }
    }

    public void set(String configName, String configValue) {
        if (!this.properties.containsKey(configName)) {
            this.properties.put(configName, new ConfigurationProperty(configValue));
            return;
        }

        this.properties.get(configName).setValue(configValue);
    }

    private void refillDefaultProperties() {
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

        for (String key : properties.keySet()) {
            if (!this.properties.containsKey(key)) {
                this.properties.put(key, new ConfigurationProperty(properties.get(key)));
            }
        }
    }

    public String get(String configName) {
        ConfigurationProperty property = this.properties.get(configName);
        return property.getValue();
    }
}
