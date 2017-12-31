package org.train.level;

import org.train.entity.Level;
import org.train.entity.Level.Item;

public class LevelValidator {
    public static LevelValidationError validate(Level level) {
        if (LevelItemUtil.findItemCoordinates(level.getLevelItems(), Item.TRAIN) == null) {
            return LevelValidationError.MISSING_TRAIN;
        }

        if (LevelItemUtil.findItemCoordinates(level.getLevelItems(), Item.GATE) == null) {
            return LevelValidationError.MISSING_GATE;
        }

        return null;
    }
}
