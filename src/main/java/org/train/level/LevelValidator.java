package org.train.level;

import java.util.LinkedList;

import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.model.Coordinate;

public class LevelValidator {
    public static LevelValidationError validateLevelItems(LevelItem[][] levelItems) {
        if (LevelItemUtil.findItemCoordinates(levelItems, Item.TRAIN) == null) {
            return LevelValidationError.MISSING_TRAIN;
        }

        if (LevelItemUtil.findItemCoordinates(levelItems, Item.GATE) == null) {
            return LevelValidationError.MISSING_GATE;
        }

        if (!LevelValidator.pathExistsFromTrainToGate(levelItems)) {
            return LevelValidationError.IMPASSABLE;
        }

        return null;
    }

    private static boolean pathExistsFromTrainToGate(LevelItem[][] levelItems) {
        Coordinate trainCoordinates = LevelItemUtil.findItemCoordinates(levelItems, Item.TRAIN);
        Coordinate gateCoordinates = LevelItemUtil.findItemCoordinates(levelItems, Item.GATE);

        boolean[][] path = new boolean[levelItems.length][levelItems[0].length];
        path[trainCoordinates.x][trainCoordinates.y] = true;

        LinkedList<Coordinate> trainPositions = new LinkedList<Coordinate>();
        trainPositions.add(trainCoordinates);

        while (true) {
            LevelValidator.moveTrain(trainPositions, levelItems, path);

            if (trainPositions.size() == 0) {
                return false;
            }

            for (Coordinate trainPosition : trainPositions) {
                if (trainPosition.equals(gateCoordinates)) {
                    return true;
                }
            }
        }
    }

    private static void moveTrain(LinkedList<Coordinate> trainPositions, LevelItem[][] levelItems, boolean[][] path) {
        Coordinate trainCoordinates = trainPositions.poll();
        if (trainCoordinates == null) {
            return;
        }

        addCoordinate(trainPositions, levelItems, path, trainCoordinates.add(Coordinate.UP));
        addCoordinate(trainPositions, levelItems, path, trainCoordinates.add(Coordinate.DOWN));
        addCoordinate(trainPositions, levelItems, path, trainCoordinates.add(Coordinate.LEFT));
        addCoordinate(trainPositions, levelItems, path, trainCoordinates.add(Coordinate.RIGHT));
    }

    private static void addCoordinate(LinkedList<Coordinate> trainPositions, LevelItem[][] levelItems, boolean[][] path,
            Coordinate movedInto) {
        if (!movedInto.isValidFor(levelItems)) {
            return;
        }

        if (path[movedInto.x][movedInto.y] == true) {
            return; // already visited
        }

        LevelItem item = levelItems[movedInto.x][movedInto.y];

        switch (item.getType()) {
        case EMPTY:
        case ITEM:
        case GATE:
            path[movedInto.x][movedInto.y] = true;
            trainPositions.add(movedInto);
            break;
        default:
            return;
        }
    }
}
