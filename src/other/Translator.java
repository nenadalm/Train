package other;

import helper.XmlHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.Configuration;

public class Translator {

    private static Translator translator;
    private String languageCode;
    private Map<String, Map<String, String>> translations;

    private Translator() {
        this.languageCode = Configuration.getInstance().get("language");
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
        Configuration config = Configuration.getInstance();
        Map<String, String> translation = new HashMap<String, String>();
        File file = new File(config.get("contentPath") + "translations/" + this.languageCode + "/"
                + fileName + ".xml");

        Document document = XmlHelper.getDocument(file);

        NodeList nodes = document.getElementsByTagName("translation");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String key = node.getAttributes().getNamedItem("id").getNodeValue();
            String value = node.getTextContent();
            translation.put(key, value);
        }

        this.translations.put(fileName, translation);
    }

    public static Translator getInstance() {
        if (Translator.translator == null) {
            Translator.translator = new Translator();
        }

        return Translator.translator;
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
        if (this.translations.containsKey(fileName)
                && this.translations.get(fileName).containsKey(text)) {
            return this.translations.get(fileName).get(text);
        }

        return text;
    }
}
