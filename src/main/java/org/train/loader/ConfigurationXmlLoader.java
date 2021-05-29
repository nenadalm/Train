package org.train.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.train.app.ConfigurationProperty;
import org.train.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigurationXmlLoader implements ConfigurationLoader {

    private File file;

    public ConfigurationXmlLoader(File file) {
        this.file = file;
    }

    @Override
    public Map<String, ConfigurationProperty> load() {
        Map<String, ConfigurationProperty> properties = new HashMap<String, ConfigurationProperty>();
        try {
            Document document = XmlHelper.getDocument(this.file);
            NodeList nodeList = document.getElementsByTagName("property");
            properties = new HashMap<String, ConfigurationProperty>(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String key = node.getAttributes().getNamedItem("name").getNodeValue();
                ConfigurationProperty property = new ConfigurationProperty(node.getTextContent());
                properties.put(key, property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
