package org.train.loader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.train.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TranslationXmlLoader implements TranslationLoader {

    private File file;

    public TranslationXmlLoader(File file) {
        this.file = file;
    }

    @Override
    public Map<String, String> load() {
        Map<String, String> translations = new HashMap<String, String>();

        Document document = null;
        try {
            document = XmlHelper.getDocument(file);

            NodeList nodes = document.getElementsByTagName("trans-unit");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element transUnit = (Element) nodes.item(i);
                String key = transUnit.getElementsByTagName("source").item(0).getTextContent();
                String value = transUnit.getElementsByTagName("target").item(0).getTextContent();
                translations.put(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return translations;
    }

}
