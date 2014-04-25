package org.train.collection;

import java.awt.Point;

import org.train.entity.Level.Item;
import org.train.entity.LevelItem;

public class LevelItemsStorage {
    private LevelItem[][] levelItems;

    public void setLevelItems(LevelItem[][] levelItems) {
        this.levelItems = levelItems;
    }

    public LevelItem[][] getLevelItems() {
        return levelItems;
    }

    public int getConsumableItemsCount() {
        int counter = 0;
        for (int i = 0; i < this.levelItems.length; i++) {
            for (int j = 0; j < this.levelItems[0].length; j++) {
                if (this.levelItems[i][j].getType() == Item.ITEM) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public Point findTrainCoordinates() {
        return this.findItemCoordinates(Item.TRAIN);
    }

    public Point findGateCoordinates() {
        return this.findItemCoordinates(Item.GATE);
    }

    public LevelItem findGate() {
        return this.findLevelItem(Item.GATE);
    }

    private LevelItem findLevelItem(Item item) {
        for (int i = 0; i < this.levelItems.length; i++) {
            for (int j = 0; j < this.levelItems[0].length; j++) {
                if (this.levelItems[i][j].getType() == item) {
                    return this.levelItems[i][j];
                }
            }
        }

        return null;
    }

    private Point findItemCoordinates(Item item) {
        for (int i = 0; i < this.levelItems.length; i++) {
            for (int j = 0; j < this.levelItems[0].length; j++) {
                if (this.levelItems[i][j].getType() == item) {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }
}
