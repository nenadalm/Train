package org.train.storer;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.train.app.ConfigurationProperty;
import org.train.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConfigurationXmlStorer implements ConfigurationStorer {

    private File file;

    public ConfigurationXmlStorer(File file) {
        this.file = file;
    }

    @Override
    public void store(Map<String, ConfigurationProperty> properties) {
        try {
            if (!this.file.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document document = docBuilder.newDocument();

                Element configuration = document.createElement("configuration");
                Element propertiesElement = document.createElement("properties");
                configuration.appendChild(propertiesElement);
                document.appendChild(configuration);
                XmlHelper.saveDocument(document, this.file.getCanonicalPath());
            }
            Document document = XmlHelper.getDocument(file);
            NodeList propertiesElement = document.getElementsByTagName("property");

            for (String key : properties.keySet()) {
                ConfigurationProperty property = properties.get(key);
                if (property.isDirty()) {
                    this.updateNodeList(document, propertiesElement, key, property);
                }
            }

            XmlHelper.saveDocument(document, this.file.getCanonicalPath());

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
}
