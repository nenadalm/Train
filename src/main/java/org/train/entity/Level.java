package org.train.entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.train.collection.LevelItemsStorage;
import org.train.other.ResourceManager;

public class Level extends Entity implements Cloneable {

    private Map<Item, Image> images;
    private LevelItem[][] levelItems;
    private LevelItemsStorage levelItemsStorage;
    private int interval = 500;
    private float time = 0;
    private Train train;
    private boolean isGameOver = false;
    private boolean isGameWon = false;
    private boolean isGateOpened = false;
    private List<Truck> trucks;
    private int itemsToWin = 0;
    private int originalImageSize;
    private int imageSize;
    private Point trainDirectionPrepared = new Point();
    private ResourceManager resourceManager;
    private boolean playable = false;
    private Queue<Integer> keys = new LinkedList<Integer>();

    public Level(int width, int height, int refreshSpeed, ResourceManager resourceManager) {
        this.levelItemsStorage = new LevelItemsStorage();
        this.interval = refreshSpeed;
        this.resourceManager = resourceManager;
        this.trucks = new ArrayList<Truck>();
        this.loadImages();
        this.train = new Train(this.images.get(Item.TRAIN), Item.TRAIN);
        this.levelInit(width, height);
    }

    public Map<Item, Image> getImages() {
        return this.images;
    }

    public int getImageSize() {
        return this.imageSize;
    }

    public Item[] getItems() {
        Item[] items = new Item[this.images.size()];
        int i = 0;
        for (Object o : this.images.keySet().toArray()) {
            items[i] = (Item) o;
            i++;
        }
        return items;
    }

    @Override
    public int getWidth() {
        return this.levelItems.length;
    }

    public void setItem(Point position, Item item) {
        LevelItem levelItem = new LevelItem(this.images.get(item), item);
        levelItem.setPosition(this.getItemPosition(position));
        levelItem.setScale(this.getScale());
        this.levelItems[position.x][position.y] = levelItem;
    }

    @Override
    public int getHeight() {
        return this.levelItems[0].length;
    }

    public int getOriginalImageSize() {
        return this.originalImageSize;
    }

    public void setOriginalImageSize(int originalImageSize) {
        this.originalImageSize = originalImageSize;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        this.imageSize = (int) (this.originalImageSize * scale);
        this.setArray(this.toArray());
    }

    @Override
    public Level clone() {
        Level level = new Level(this.getWidth(), this.getHeight(), this.interval,
                this.resourceManager);
        Item[][] items = new Item[this.levelItems.length][this.levelItems[0].length];
        for (int i = 0; i < this.levelItems.length; i++) {
            for (int j = 0; j < this.levelItems[0].length; j++) {
                items[i][j] = this.levelItems[i][j].getType();
            }
        }
        level.setArray(items);
        return level;
    }

    private void loadImages() {
        this.images = new HashMap<Item, Image>(6);
        this.images.put(Item.WALL, this.resourceManager.getImage("wall"));
        this.images.put(Item.GATE, this.resourceManager.getImage("gate"));
        this.images.put(Item.ITEM, this.resourceManager.getImage("tree"));
        this.images.put(Item.TRAIN, this.resourceManager.getImage("train"));
        this.images.put(Item.EMPTY, this.resourceManager.getImage("empty"));
        this.images.put(Item.TRUCK, this.resourceManager.getImage("treeTruck"));
        this.imageSize = this.images.get(Item.WALL).getWidth();
        this.originalImageSize = this.imageSize;
    }

    private void levelInit(int width, int height) {
        Item[][] level = new Item[width][height];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[0].length; j++) {
                level[i][j] = Item.EMPTY;
            }
        }
        this.setArray(level);
    }

    public enum Item {
        WALL('W'), GATE('G'), ITEM('T'), TRAIN('V'), EMPTY('E'), TRUCK('R');

        char c;

        private Item(char c) {
            this.c = c;
        }

        public char getChar() {
            return this.c;
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
        gr.setColor(Color.red);
        gr.drawRect(this.getMarginLeft(), this.getMarginTop(), this.levelItems.length
                * this.imageSize - gr.getLineWidth(), this.levelItems[0].length * this.imageSize
                - gr.getLineWidth());
        for (int i = 0; i < this.levelItems.length; i++) {
            for (int j = 0; j < this.levelItems[0].length; j++) {
                this.levelItems[i][j].render(gc, sb, gr);
            }
        }
    }

    public boolean isFinished() {
        return this.isGameWon;
    }

    public boolean isOver() {
        return this.isGameOver;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        if (!this.playable) {
            return;
        }

        if (this.itemsToWin == 0 && !this.isGateOpened) {
            try {
                this.levelItemsStorage.findGate().setImage(
                        this.resourceManager.getImage("gateOpen"));
                this.isGateOpened = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Input input = gc.getInput();

        if (!this.isGameOver && !this.isGameWon) {
            if (input.isKeyPressed(Keyboard.KEY_UP)) {
                this.keys.add(Keyboard.KEY_UP);
            } else if (input.isKeyPressed(Keyboard.KEY_DOWN)) {
                this.keys.add(Keyboard.KEY_DOWN);
            } else if (input.isKeyPressed(Keyboard.KEY_LEFT)) {
                this.keys.add(Keyboard.KEY_LEFT);
            } else if (input.isKeyPressed(Keyboard.KEY_RIGHT)) {
                this.keys.add(Keyboard.KEY_RIGHT);
            }

            this.time += delta;
            if (this.time >= this.interval) {

                Integer key = this.keys.poll();
                if (key != null) {
                    if (key == Keyboard.KEY_UP) {
                        this.trainDirectionPrepared = new Point(0, -1);
                    } else if (key == Keyboard.KEY_DOWN) {
                        this.trainDirectionPrepared = new Point(0, 1);
                    } else if (key == Keyboard.KEY_LEFT) {
                        this.trainDirectionPrepared = new Point(-1, 0);
                    } else if (key == Keyboard.KEY_RIGHT) {
                        this.trainDirectionPrepared = new Point(1, 0);
                    }
                }

                this.train.setDirection(this.trainDirectionPrepared);
                Point lastPoint = this.levelItemsStorage.findTrainCoordinates();
                Point newPoint = new Point(lastPoint.x + this.trainDirectionPrepared.x, lastPoint.y
                        + this.trainDirectionPrepared.y);

                if (this.trainCrashed(newPoint)) {
                    this.doCrash();
                } else {
                    if (this.levelItems[newPoint.x][newPoint.y].getType() == Item.ITEM) {
                        this.addTruck(lastPoint);
                        this.itemsToWin--;
                    } else {
                        if (this.itemsToWin == 0
                                && this.levelItems[newPoint.x][newPoint.y].getType() == Item.GATE) {
                            this.isGameWon = true;
                        }
                        this.moveTrucks(lastPoint, this.train.getRotation(),
                                this.train.flippedHorizontal, this.train.flippedVertical);

                        if (this.trucks.size() == 0) {
                            LevelItem empty = new LevelItem(this.images.get(Item.EMPTY), Item.EMPTY);
                            empty.setPosition(this.getItemPosition(lastPoint));
                            empty.setScale(this.getScale());
                            this.levelItems[lastPoint.x][lastPoint.y] = empty;
                        }
                    }
                    this.train.setPosition(this.getItemPosition(newPoint));
                    this.train.setScale(this.getScale());
                    this.levelItems[newPoint.x][newPoint.y] = this.train;
                }

                this.time = 0;
            }
        }
    }

    private boolean trainCrashed(Point newPoint) {
        if (newPoint.x < 0 || newPoint.y < 0 || newPoint.x > this.levelItems.length - 1
                || newPoint.y > this.levelItems[0].length - 1) {
            return true;
        }
        if (this.levelItems[newPoint.x][newPoint.y].getType() == Item.WALL
                || this.levelItems[newPoint.x][newPoint.y].getType() == Item.TRUCK) {
            return true;
        }
        if (this.levelItems[newPoint.x][newPoint.y].getType() == Item.GATE && this.itemsToWin != 0) {
            return true;
        }
        return false;
    }

    private void moveTrucks(Point moveToPosition, float applyRotation, boolean flippedHorizontal,
            boolean flippedVertical) {
        Point positionTemp = null;
        Truck truck = null;
        float rotationTemp;
        boolean flippedHorizontalTemp;
        boolean flippedVerticalTemp;
        for (int i = this.trucks.size() - 1; i >= 0; i--) {
            truck = this.trucks.get(i);

            positionTemp = truck.getPosition();
            rotationTemp = truck.getRotation();
            flippedHorizontalTemp = truck.isFlippedHorizontal();
            flippedVerticalTemp = truck.isFlippedVertical();

            truck.setPosition(this.getItemPosition(moveToPosition));
            truck.setRotation(applyRotation);
            truck.setFlippedHorizontal(flippedHorizontal);
            truck.setFlippedVertical(flippedVertical);

            this.levelItems[moveToPosition.x][moveToPosition.y] = truck;

            moveToPosition = this.getItemCoordinates(positionTemp);
            applyRotation = rotationTemp;
            flippedHorizontal = flippedHorizontalTemp;
            flippedVertical = flippedVerticalTemp;
        }
        if (this.trucks.size() != 0) {
            LevelItem empty = new LevelItem(this.images.get(Item.EMPTY), Item.EMPTY);
            empty.setPosition(this.getItemPosition(moveToPosition));
            empty.setScale(this.getScale());
            Point coordinates = this.getItemCoordinates(positionTemp);
            this.levelItems[coordinates.x][coordinates.y] = empty;
        }
    }

    private void addTruck(Point position) {

        Truck t = new Truck(this.images.get(Item.TRUCK), Item.TRUCK);
        t.setScale(this.getScale());
        t.setPosition(this.getItemPosition(position));
        t.setRotation(this.train.getRotation());
        t.setFlippedHorizontal(train.isFlippedHorizontal());
        t.setFlippedVertical(train.isFlippedVertical());
        this.levelItems[position.x][position.y] = t;
        this.trucks.add(t);
    }

    private void doCrash() {
        this.train.setImage(this.resourceManager.getImage("trainCrash"));
        this.isGameOver = true;
    }

    public Train getTrain() {
        return this.train;
    }

    public Item[][] toArray() {
        Item[][] items = new Item[this.levelItems.length][this.levelItems[0].length];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                items[i][j] = this.levelItems[i][j].getType();
            }
        }

        return items;
    }

    public void setArray(Item[][] array) {
        this.levelItems = new LevelItem[array.length][array[0].length];
        this.levelItemsStorage.setLevelItems(this.levelItems);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                this.levelItems[i][j] = new LevelItem(this.images.get(array[i][j]), array[i][j]);
                this.levelItems[i][j].setPosition(this.getItemPosition(new Point(i, j)));
                this.levelItems[i][j].setScale(this.getScale());
            }
        }

        try {
            this.train.setPosition(this.levelItemsStorage.findTrainCoordinates());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.itemsToWin = this.levelItemsStorage.getConsumableItemsCount();
        this.playable = this.isValid();
    }

    private Point getItemCoordinates(Point position) {
        return new Point((position.x - this.getMarginLeft()) / this.imageSize,
                (position.y - this.getMarginTop()) / this.imageSize);
    }

    private Point getItemPosition(Point coordinates) {
        return new Point(new Point(this.getMarginLeft() + coordinates.x * this.imageSize,
                this.getMarginTop() + coordinates.y * this.imageSize));
    }

    @Override
    public void setMarginTop(int marginTop) {
        super.setMarginTop(marginTop);
        this.setArray(this.toArray());
    }

    @Override
    public void setMarginLeft(int marginLeft) {
        super.setMarginLeft(marginLeft);
        this.setArray(this.toArray());
    }

    public boolean isValid() {
        if (this.levelItemsStorage.findGateCoordinates() != null
                && this.levelItemsStorage.findTrainCoordinates() != null) {
            return true;
        }
        return false;
    }

    public Point findTrainPosition() {
        return this.levelItemsStorage.findTrainCoordinates();
    }

    public Point findGatePosition() {
        return this.levelItemsStorage.findGateCoordinates();
    }
}
