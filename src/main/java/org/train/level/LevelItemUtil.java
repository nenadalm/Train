package org.train.level;

import java.util.ArrayList;
import java.util.List;

import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.model.Coordinate;

public class LevelItemUtil {
    public static int getConsumableItemsCount(LevelItem[][] levelItems) {
        int counter = 0;
        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                if (levelItems[i][j].getType() == Item.ITEM) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public static List<Coordinate> findItemCoordinatesList(LevelItem[][] levelItems, Item item) {
        List<Coordinate> items = new ArrayList<Coordinate>();

        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                if (levelItems[i][j].getType() == item) {
                    items.add(new Coordinate(i, j));
                }
            }
        }

        return items;
    }

    public static Coordinate findItemCoordinates(LevelItem[][] levelItems, Item item) {
        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                if (levelItems[i][j].getType() == item) {
                    return new Coordinate(i, j);
                }
            }
        }

        return null;
    }

    public static LevelItem findLevelItem(LevelItem[][] levelItems, Item item) {
        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                if (levelItems[i][j].getType() == item) {
                    return levelItems[i][j];
                }
            }
        }

        return null;
    }
}
