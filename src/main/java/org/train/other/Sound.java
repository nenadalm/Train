package org.train.other;

import java.io.InputStream;
import java.net.URL;
import org.newdawn.slick.SlickException;
import org.train.app.Configuration;

public class Sound extends org.newdawn.slick.Sound {

    private Configuration configuration;

    public Sound(InputStream in, String ref) throws SlickException {
        super(in, ref);
    }

    public Sound(String ref) throws SlickException {
        super(ref);
    }

    public Sound(URL url) throws SlickException {
        super(url);
    }

    @Override
    public void play() {
        if (this.configuration == null || Boolean.parseBoolean(this.configuration.get("soundEnabled"))) {
            super.play();
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
