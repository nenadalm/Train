package org.train.other;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.train.app.Configuration;
import org.train.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Translator {

    private String languageCode;
    private Map<String, Map<String, String>> translations;
    private Configuration config;

    public Translator(Configuration config) {
        this.config = config;
        this.languageCode = this.config.get("language");
        this.init();
    };

    private void init() {
        this.translations = new HashMap<String, Map<String, String>>();
        try {
            this.loadTranslations("global");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTranslations(String fileName) throws Exception {
        Map<String, String> translation = new HashMap<String, String>();
        File file = new File(config.get("contentPath") + "translations/" + this.languageCode + "/"
                + fileName + ".xml");

        Document document = XmlHelper.getDocument(file);

        NodeList nodes = document.getElementsByTagName("trans-unit");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element transUnit = (Element) nodes.item(i);
            String key = transUnit.getElementsByTagName("source").item(0).getTextContent();
            String value = transUnit.getElementsByTagName("target").item(0).getTextContent();
            translation.put(key, value);
        }

        this.translations.put(fileName, translation);
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguage(String code) {
        this.languageCode = code;
        this.init();
    }

    public String translate(String text) {
        return this.translate(text, "global");
    }

    public String translate(String text, String fileName) {
        if (!this.translations.containsKey(fileName)) {
            try {
                this.loadTranslations(fileName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (this.translations.get(fileName).containsKey(text)) {
            return this.translations.get(fileName).get(text);
        }

        return text;
    }
}
