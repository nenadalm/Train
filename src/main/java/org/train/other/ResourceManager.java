package org.train.other;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.Effect;
import org.train.app.Configuration;
import org.train.factory.FontFactory;

public class ResourceManager {

    private Map<String, String> fonts;
    private FontFactory fontFactory;
    private Map<String, Image> images;
    private Configuration config;

    public ResourceManager(Configuration config, FontFactory fontFactory) {
        this.config = config;
        this.images = new HashMap<String, Image>();

        this.fontFactory = fontFactory;
        this.fonts = new HashMap<String, String>();
        this.loadResources();
    }

    private void loadResources() {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(config.get("contentPath") + "resources.xml"));

            for (Object el : document.selectNodes("/resources/resource[@type='image']")) {
                Element imageEl = (Element) el;
                this.images.put(imageEl.attributeValue("id"), new Image(config.get("contentPath")
                        + imageEl.getText()));
            }

            for (Object el : document.selectNodes("/resources/resource[@type='font']")) {
                Element fontEl = (Element) el;
                this.fonts.put(fontEl.attributeValue("id"),
                        config.get("contentPath") + fontEl.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Font getFont(String type, int size, Effect effect) throws SlickException {
        return this.fontFactory.getFont(type, size, effect);
    }

    public Image getImage(String name) {
        return this.images.get(name);
    }
}
