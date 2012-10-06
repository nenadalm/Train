package factory;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.Effect;

public class FontFactory {

    private static FontFactory factory;
    Map<String, Map<Integer, Map<Effect, Font>>> type_size_effectFont;

    private FontFactory() {
        this.type_size_effectFont = new HashMap<String, Map<Integer, Map<Effect, Font>>>();
    }

    public static FontFactory getInstance() {
        if (FontFactory.factory == null) {
            FontFactory.factory = new FontFactory();
        }
        return FontFactory.factory;
    }

    public Font getFont(String path, String type, int size, Effect effect) throws SlickException {
        if (this.type_size_effectFont.containsKey(type)
                && this.type_size_effectFont.get(type).containsKey(size)
                && this.type_size_effectFont.get(type).get(size).containsKey(effect)) {
            return this.type_size_effectFont.get(type).get(size).get(effect);
        }
        UnicodeFont font = new UnicodeFont(path, size, false, false);
        font.addGlyphs(32, 800);
        font.getEffects().add(effect);
        font.loadGlyphs();
        return font;
    }
}
