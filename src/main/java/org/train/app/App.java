package org.train.app;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;

public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Configuration configuration = Configuration.getInstance();
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = environment.getDefaultScreenDevice();
            DisplayMode actual = gd.getDisplayMode();

            int width = Integer.parseInt(configuration.get("width"));
            int height = Integer.parseInt(configuration.get("height"));
            boolean isFullscreen = Boolean.parseBoolean(configuration.get("fullscreen"));

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

            AppGameContainer container = new AppGameContainer(new Game("Train"));
            container.setShowFPS(false);
            container.setTargetFrameRate(60);
            container.setDisplayMode(width, height, isFullscreen);
            container.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
