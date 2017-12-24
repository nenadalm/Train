package org.train.factory;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.Effect;
import org.train.app.Configuration;

public class FontFactory {

    Map<String, Map<Integer, Map<Effect, Font>>> type_size_effectFont;
    Configuration config;

    public FontFactory(Configuration config) {
        this.config = config;
        this.type_size_effectFont = new HashMap<String, Map<Integer, Map<Effect, Font>>>();
    }

    @SuppressWarnings("unchecked")
    public Font getFont(String type, int size, Effect effect) throws SlickException {
        if (!this.type_size_effectFont.containsKey(type)) {
            this.type_size_effectFont.put(type, new HashMap<Integer, Map<Effect, Font>>());
        }
        if (!this.type_size_effectFont.get(type).containsKey(size)) {
            this.type_size_effectFont.get(type).put(size, new HashMap<Effect, Font>());
        }
        if (!this.type_size_effectFont.get(type).get(size).containsKey(effect)) {
            UnicodeFont font = new UnicodeFont(String.format("%1$sfonts/%2$s.ttf", config.getPath("contentPath"), type),
                    size, false, false);
            font.addGlyphs(32, 382);
            font.getEffects().add(effect);
            font.loadGlyphs();
            this.type_size_effectFont.get(type).get(size).put(effect, font);
        }
        return this.type_size_effectFont.get(type).get(size).get(effect);
    }
}
