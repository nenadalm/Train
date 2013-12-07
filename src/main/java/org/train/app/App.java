package org.train.app;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.picocontainer.PicoContainer;
import org.train.factory.PicoContainerFactory;

public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {
        App app = new App();
        PicoContainer container = new PicoContainerFactory().create();

        Configuration configuration = container.getComponent(Configuration.class);

        boolean isFullscreen = Boolean.parseBoolean(configuration.get("fullscreen"));
        Dimension displaySize = app.getDisplaySize(configuration);

        AppGameContainer gameContainer = container.getComponent(AppGameContainer.class);
        gameContainer.setShowFPS(false);
        gameContainer.setTargetFrameRate(60);

        try {
            gameContainer.setDisplayMode(displaySize.width, displaySize.height, isFullscreen);
            gameContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dimension getDisplaySize(Configuration configuration) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = environment.getDefaultScreenDevice();
        DisplayMode actual = gd.getDisplayMode();

        int width = Integer.parseInt(configuration.get("width"));
        int height = Integer.parseInt(configuration.get("height"));

        boolean matchFound = false;
        if (width > 0 || height > 0) {
            DisplayMode modes[] = gd.getDisplayModes();
            for (DisplayMode displayMode : modes) {
                if (displayMode.getWidth() == width && displayMode.getHeight() == height) {
                    matchFound = true;
                    break;
                }
            }
        }
        if (width <= 0 || height <= 0 || !matchFound) {
            configuration.set("width", String.valueOf(actual.getWidth()));
            configuration.set("height", String.valueOf(actual.getHeight()));
            configuration.saveChanges();
            width = actual.getWidth();
            height = actual.getHeight();
        }

        return new Dimension(width, height);
    }
}
