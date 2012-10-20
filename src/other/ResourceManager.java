package other;

import helper.XmlHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.Effect;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.Game;
import factory.FontFactory;

public class ResourceManager {

    private static ResourceManager resourceManager;
    private Map<String, String> fonts;
    private FontFactory fontFactory;
    private Map<String, Image> images;

    private ResourceManager() {
        this.images = new HashMap<String, Image>();
        this.fontFactory = FontFactory.getInstance();
        this.fonts = new HashMap<String, String>();
        this.loadResources();
    }

    private void loadResources() {
        try {
            Document document = null;
            document = XmlHelper.getDocument(new File(Game.CONTENT_PATH + "resources.xml"));

            NodeList nodeList = document.getElementsByTagName("resource");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap nodeMap = node.getAttributes();
                switch (nodeMap.getNamedItem("type").getNodeValue()) {
                    case "image":
                        this.images.put(nodeMap.getNamedItem("id").getNodeValue(), new Image(
                                Game.CONTENT_PATH + node.getTextContent()));
                        break;
                    case "font":
                        this.fonts.put(nodeMap.getNamedItem("id").getNodeValue(), Game.CONTENT_PATH
                                + node.getTextContent());
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ResourceManager getInstance() {
        if (ResourceManager.resourceManager == null) {
            ResourceManager.resourceManager = new ResourceManager();
        }
        return ResourceManager.resourceManager;
    }

    public Font getFont(String type, int size, Effect effect) throws SlickException {
        return this.fontFactory.getFont(type, size, effect);
    }

    public Image getImage(String name) {
        return this.images.get(name);
    }
}
