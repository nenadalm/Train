package org.train.app;

import java.awt.Dimension;
import java.lang.reflect.Field;

import org.newdawn.slick.AppGameContainer;
import org.picocontainer.PicoContainer;
import org.train.factory.PicoContainerFactory;
import org.train.other.Display;

public class App {

    public static void main(String[] args) {

        App.addNativesToLibraryPath();

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

    private static void addNativesToLibraryPath() {
        try {
            String originalLibraryPath = System.getProperty("java.library.path");
            System.setProperty("java.library.path", String
                    .format("%s:%s", originalLibraryPath, "natives/"));
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
