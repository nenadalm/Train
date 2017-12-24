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
import org.train.model.Truck;

public class ResourceManager {

    private Map<String, Truck> trucks;
    private Map<String, String> fonts;
    private FontFactory fontFactory;
    private Map<String, Image> images;
    private Map<String, Sound> sounds;
    private Configuration config;

    public ResourceManager(Configuration config, FontFactory fontFactory) {
        this.config = config;
        this.images = new HashMap<String, Image>();
        this.sounds = new HashMap<String, Sound>();
        this.trucks = new HashMap<String, Truck>();

        this.fontFactory = fontFactory;
        this.fonts = new HashMap<String, String>();
        this.loadResources();
    }

    private void loadResources() {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(config.getPath("contentPath") + "resources.xml"));

            for (Object el : document.selectNodes("/resources/resource[@type='image']")) {
                Element imageEl = (Element) el;
                this.images.put(imageEl.attributeValue("id"),
                        new Image(config.getPath("contentPath") + imageEl.getText()));
            }

            for (Object el : document.selectNodes("/resources/resource[@type='font']")) {
                Element fontEl = (Element) el;
                this.fonts.put(fontEl.attributeValue("id"), config.getPath("contentPath") + fontEl.getText());
            }

            for (Object el : document.selectNodes("/resources/resource[@type='sound']")) {
                Element soundEl = (Element) el;
                String soundPath = config.getPath("contentPath") + soundEl.getText();
                Sound sound = new Sound(soundPath);
                sound.setConfiguration(this.config);
                this.sounds.put(soundEl.attributeValue("id"), sound);
            }

            for (Object el : document.selectNodes("/resources/resource[@type='truck']")) {
                Element truckEl = (Element) el;
                String truckImg = truckEl.element("truck").attributeValue("image");
                String itemImg = truckEl.element("item").attributeValue("image");
                this.trucks.put(truckEl.attributeValue("id"),
                        new Truck(this.getImage(truckImg), this.getImage(itemImg)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, Truck> getTrucks() {
        return trucks;
    }

    public Truck getTruck(String name) {
        return this.trucks.get(name);
    }

    public Font getFont(String type, int size, Effect effect) throws SlickException {
        return this.fontFactory.getFont(type, size, effect);
    }

    public Image getImage(String name) {
        return this.images.get(name);
    }

    public Sound getSound(String name) {
        return this.sounds.get(name);
    }
}
