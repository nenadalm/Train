package org.train.other;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.train.app.Configuration;

public class Display {
    private Configuration configuration;

    private GraphicsDevice graphicsDevice;

    public Display(Configuration configuration) {
        this.configuration = configuration;
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public Dimension getOptimalDisplaySize() {
        DisplayMode actual = this.graphicsDevice.getDisplayMode();

        int width = Integer.parseInt(this.configuration.get("width"));
        int height = Integer.parseInt(this.configuration.get("height"));

        if (width <= 0 || height <= 0 || !this.isDisplaySizeSupported(width, height)) {
            this.configuration.set("width", String.valueOf(actual.getWidth()));
            this.configuration.set("height", String.valueOf(actual.getHeight()));
            this.configuration.saveChanges();
            width = actual.getWidth();
            height = actual.getHeight();
        }

        return new Dimension(width, height);
    }

    private boolean isDisplaySizeSupported(int width, int height) {
        for (DisplayMode displayMode : this.graphicsDevice.getDisplayModes()) {
            if (displayMode.getWidth() == width && displayMode.getHeight() == height) {
                return true;
            }
        }

        return false;
    }
}
