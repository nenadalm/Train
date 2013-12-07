package org.train.helper;

import java.awt.Dimension;

import org.newdawn.slick.GameContainer;
import org.train.app.Configuration;
import org.train.entity.Level;

public class LevelHelper {

    private Configuration config;

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

    public void adjustLevelToContainer(GameContainer container, Level level) {
        float scale = this.computeScale(container, level.getOriginalImageSize(), new Dimension(
                level.getWidth(), level.getHeight()));
        level.setScale(scale);
        int width = level.getWidth() * (int) (level.getOriginalImageSize() * scale);
        int height = level.getHeight() * (int) (level.getOriginalImageSize() * scale);
        level.setMarginLeft((container.getWidth() - width) / 2);
        level.setMarginTop((container.getHeight() - height) / 2);
    }
}
