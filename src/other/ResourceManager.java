package other;

import helper.XmlHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Font;
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

    private ResourceManager() {
        this.fontFactory = FontFactory.getInstance();
        this.fonts = new HashMap<String, String>();
        Document document = null;
        try {
            document = XmlHelper.getDocument(new File(Game.CONTENT_PATH + "resources.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NodeList nodeList = document.getElementsByTagName("resource");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NamedNodeMap nodeMap = node.getAttributes();
            switch (nodeMap.getNamedItem("type").getNodeValue()) {
                case "font":
                    this.fonts.put(nodeMap.getNamedItem("id").getNodeValue(), Game.CONTENT_PATH
                            + node.getTextContent());
                    break;
            }
        }
    }

    public static ResourceManager getInstance() {
        if (ResourceManager.resourceManager == null) {
            ResourceManager.resourceManager = new ResourceManager();
        }
        return ResourceManager.resourceManager;
    }

    public Font getFont(String type, int size, Effect effect) throws SlickException {
        return this.fontFactory.getFont(this.fonts.get(type), type, size, effect);
    }
}
