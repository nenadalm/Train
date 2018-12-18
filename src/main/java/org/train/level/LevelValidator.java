package org.train.level;

import java.util.LinkedList;
import java.util.List;

import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.model.Coordinate;

public class LevelValidator {
    public static LevelValidationError validateLevelItems(LevelItem[][] levelItems) {
        if (LevelItemUtil.findItemCoordinates(levelItems, Item.TRAIN) == null) {
            return LevelValidationError.MISSING_TRAIN;
        }

        Coordinate gateCoordinates = LevelItemUtil.findItemCoordinates(levelItems, Item.GATE);
        if (gateCoordinates == null) {
            return LevelValidationError.MISSING_GATE;
        }

        if (!LevelValidator.pathExistsFromTrainToCoordinates(levelItems, gateCoordinates)) {
            return LevelValidationError.IMPASSABLE;
        }

        List<Coordinate> itemCoordinates = LevelItemUtil.findItemCoordinatesList(levelItems, Item.ITEM);
        for (Coordinate c : itemCoordinates) {
            if (!LevelValidator.pathExistsFromTrainToCoordinates(levelItems, c)) {
                return LevelValidationError.UNREACHABLE_CONSUMABLE;
            }

            if (LevelValidator.itemIsInDeadEnd(levelItems, c)) {
                return LevelValidationError.DEAD_END_CONSUMABLE;
            }
        }

        return null;
    }

    private static boolean itemIsInDeadEnd(LevelItem[][] levelItems, Coordinate item) {
        Coordinate[] surroundingItems = { item.add(Coordinate.LEFT), item.add(Coordinate.UP),
                item.add(Coordinate.RIGHT), item.add(Coordinate.DOWN), };

        int obstacles = 0;
        for (int i = 0; i < surroundingItems.length; i++) {
            if (!surroundingItems[i].isValidFor(levelItems)) {
                obstacles++;
                continue;
            }

            Coordinate itemC = surroundingItems[i];
            if (levelItems[itemC.x][itemC.y].getType() == Item.WALL) {
                obstacles++;
            }
        }

        return obstacles > 2;
    }

    private static boolean pathExistsFromTrainToCoordinates(LevelItem[][] levelItems, Coordinate target) {
        Coordinate trainCoordinates = LevelItemUtil.findItemCoordinates(levelItems, Item.TRAIN);

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
                if (trainPosition.equals(target)) {
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
