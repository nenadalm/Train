package org.train.helper;

import java.awt.Dimension;

import org.newdawn.slick.GameContainer;
import org.train.app.Configuration;

public class LevelHelper {

    Configuration config;

    public LevelHelper(Configuration config) {
        this.config = config;
    }

    public float computeScale(GameContainer container, int originalImageSize, Dimension level) {
        int itemSize = originalImageSize;
        float optimalScale = 50 / (float) itemSize * Float.valueOf(config.get("scale"));
        float scale = optimalScale;
        float scaleWidth = container.getWidth() / ((float) level.width * itemSize);
        float scaleHeight = container.getHeight() / ((float) level.height * itemSize);

        if (Boolean.valueOf(config.get("autoscale"))) {
            return (scaleWidth < scaleHeight) ? scaleWidth : scaleHeight;
        }

        if (scaleWidth < optimalScale && scaleHeight < optimalScale) {
            scale = (scaleWidth < scaleHeight) ? scaleWidth : scaleHeight;
        } else if (scaleWidth < optimalScale || scaleHeight < optimalScale) {
            scale = (scaleWidth < optimalScale) ? scaleWidth : scaleHeight;
        }
        return scale;
    }
}
