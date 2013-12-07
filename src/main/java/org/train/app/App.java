package org.train.app;

import java.awt.Dimension;

import org.newdawn.slick.AppGameContainer;
import org.picocontainer.PicoContainer;
import org.train.factory.PicoContainerFactory;
import org.train.other.Display;

public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {
        PicoContainer container = new PicoContainerFactory().create();

        Configuration configuration = container.getComponent(Configuration.class);

        AppGameContainer gameContainer = container.getComponent(AppGameContainer.class);
        gameContainer.setShowFPS(false);
        gameContainer.setTargetFrameRate(60);

        try {
            Dimension displaySize = container.getComponent(Display.class).getOptimalDisplaySize();
            boolean isFullscreen = Boolean.parseBoolean(configuration.get("fullscreen"));
            gameContainer.setDisplayMode(displaySize.width, displaySize.height, isFullscreen);
            gameContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
