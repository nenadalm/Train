package org.train.app;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Caching;
import org.train.entity.MessageBox;
import org.train.other.Translator;

public class App {

    private DefaultPicoContainer container;

    /**
     * @param args
     */
    public static void main(String[] args) {
        App app = new App();
        app.initContainer();
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

            PicoContainer container = app.getContainer();
            AppGameContainer gameContainer = container.getComponent(AppGameContainer.class);
            gameContainer.setShowFPS(false);
            gameContainer.setTargetFrameRate(60);
            gameContainer.setDisplayMode(width, height, isFullscreen);
            gameContainer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PicoContainer getContainer() {
        return this.container;
    }

    public void initContainer() {
        this.container = new DefaultPicoContainer(new Caching());
        this.container.addComponent(new Game("Train", this.container));
        this.container.addComponent(AppGameContainer.class);
        this.container.addComponent(Translator.class);
        this.container.addComponent(MessageBox.class);
    }
}
