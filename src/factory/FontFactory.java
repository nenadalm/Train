package factory;

import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.Effect;

public class FontFactory {

    private static FontFactory factory;

    private FontFactory() {

    }

    public static FontFactory getInstance() {
        if (FontFactory.factory == null) {
            FontFactory.factory = new FontFactory();
        }
        return FontFactory.factory;
    }

    public Font getFont(String path, String type, int size, Effect effect) throws SlickException {
        UnicodeFont font = new UnicodeFont(path, size, false, false);
        font.addGlyphs(32, 800);
        font.getEffects().add(effect);
        font.loadGlyphs();
        return font;
    }
}
