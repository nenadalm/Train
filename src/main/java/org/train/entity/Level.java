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
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.train.collection.LevelItemsStorage;
import org.train.other.ResourceManager;

public class Level extends Entity implements Cloneable {

    private int levelIndex;
    private int packageIndex;
    private String levelName;
    private String packageName;
    private Map<Item, Image> images;
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
    private Sound winSound;
    private Sound moveSound;

    public Level(int width, int height, int refreshSpeed, ResourceManager resourceManager) {
        this.winSound = resourceManager.getSound("win");
        this.moveSound = resourceManager.getSound("ingame");
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
        return this.levelItemsStorage.getLevelItems().length;
    }

    public void setLevelItem(LevelItem levelItem, Point position) {
        LevelItem clonedItem = (LevelItem) levelItem.clone();
        clonedItem.setPosition(this.getItemPosition(position));
        clonedItem.setScale(this.getScale());
        this.levelItemsStorage.getLevelItems()[position.x][position.y] = clonedItem;
    }

    @Override
    public int getHeight() {
        return this.levelItemsStorage.getLevelItems()[0].length;
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
        this.setLevelItems(this.getLevelItems());
    }

    @Override
    public Level clone() {
        LevelItem[][] levelItems = new LevelItem[this.levelItemsStorage.getLevelItems().length][this.levelItemsStorage
                .getLevelItems()[0].length];
        for (int i = 0; i < this.levelItemsStorage.getLevelItems().length; i++) {
            for (int j = 0; j < this.levelItemsStorage.getLevelItems()[0].length; j++) {
                levelItems[i][j] = (LevelItem) this.levelItemsStorage.getLevelItems()[i][j].clone();
            }
        }

        Level level = new Level(this.getWidth(), this.getHeight(), this.interval,
                this.resourceManager);
        level.setLevelIndex(this.levelIndex);
        level.setPackageIndex(this.packageIndex);
        level.setLevelName(this.levelName);
        level.setPackageName(this.packageName);
        level.setLevelItems(levelItems);

        return level;
    }

    private void loadImages() {
        this.images = new HashMap<Item, Image>(6);
        this.images.put(Item.WALL, this.resourceManager.getImage("wall"));
        this.images.put(Item.GATE, this.resourceManager.getImage("gate"));
        this.images.put(Item.ITEM, this.resourceManager.getImage("tree"));
        this.images.put(Item.TRAIN, this.resourceManager.getImage("train"));
        this.images.put(Item.EMPTY, this.resourceManager.getImage("empty"));
        this.imageSize = this.images.get(Item.WALL).getWidth();
        this.originalImageSize = this.imageSize;
    }

    private void levelInit(int width, int height) {
        LevelItem[][] levelItems = new LevelItem[width][height];
        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                levelItems[i][j] = new LevelItem("empty", this.images.get(Item.EMPTY), Item.EMPTY);
            }
        }

        this.setLevelItems(levelItems);
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
        gr.drawRect(
                this.getMarginLeft(),
                this.getMarginTop(),
                this.levelItemsStorage.getLevelItems().length * this.imageSize - gr.getLineWidth(),
                this.levelItemsStorage.getLevelItems()[0].length * this.imageSize
                        - gr.getLineWidth());

        for (LevelItem[] row : this.levelItemsStorage.getLevelItems()) {
            for (LevelItem col : row) {
                col.render(gc, sb, gr);
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

        this.openGateIfItemsAreJustCollected();

        Input input = gc.getInput();

        if (!this.isGameOver && !this.isGameWon) {
            this.storeMovementKeysInQueue(input);

            this.time += delta;
            if (this.time >= this.interval) {
                this.changeTrainDirectionBasedOnKeysInQueue();

                Point lastPoint = this.levelItemsStorage.findTrainCoordinates();
                Point newPoint = new Point(lastPoint.x + this.trainDirectionPrepared.x, lastPoint.y
                        + this.trainDirectionPrepared.y);

                if (this.trainCrashed(newPoint)) {
                    this.doCrash();
                } else if (!this.isFinished()) {
                    this.moveTrain(lastPoint, newPoint);
                }

                this.time = 0;
            }
        }
    }

    private void moveTrain(Point lastPoint, Point newPoint) {
        if (this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y].getType() == Item.ITEM) {
            this.addTruck(lastPoint, this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y]);
            this.itemsToWin--;
        } else {
            if (this.itemsToWin == 0
                    && this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y].getType() == Item.GATE) {
                this.isGameWon = true;
                this.moveSound.stop();
                this.winSound.play();
            }
            this.moveTrucks(lastPoint, this.train.getRotation(), this.train.flippedHorizontal,
                    this.train.flippedVertical);

            if (this.trucks.size() == 0) {
                LevelItem empty = new LevelItem("empty", this.images.get(Item.EMPTY), Item.EMPTY);
                empty.setPosition(this.getItemPosition(lastPoint));
                empty.setScale(this.getScale());
                this.levelItemsStorage.getLevelItems()[lastPoint.x][lastPoint.y] = empty;
            }
        }
        if (!this.train.getPosition().equals(this.getItemPosition(newPoint))
                && !this.trainDirectionPrepared.equals(new Point()) && !this.isFinished()) {
            this.moveSound.play();
        }
        this.train.setPosition(this.getItemPosition(newPoint));
        this.train.setScale(this.getScale());
        this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y] = this.train;
    }

    private void changeTrainDirectionBasedOnKeysInQueue() {
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
    }

    private void storeMovementKeysInQueue(Input input) {
        if (input.isKeyPressed(Keyboard.KEY_UP)) {
            this.keys.add(Keyboard.KEY_UP);
        } else if (input.isKeyPressed(Keyboard.KEY_DOWN)) {
            this.keys.add(Keyboard.KEY_DOWN);
        } else if (input.isKeyPressed(Keyboard.KEY_LEFT)) {
            this.keys.add(Keyboard.KEY_LEFT);
        } else if (input.isKeyPressed(Keyboard.KEY_RIGHT)) {
            this.keys.add(Keyboard.KEY_RIGHT);
        }
    }

    private void openGateIfItemsAreJustCollected() {
        if (this.itemsToWin == 0 && !this.isGateOpened) {
            try {
                this.levelItemsStorage.findGate().setImage(
                        this.resourceManager.getImage("gateOpen"));
                this.isGateOpened = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean trainCrashed(Point newPoint) {
        if (newPoint.x < 0 || newPoint.y < 0
                || newPoint.x > this.levelItemsStorage.getLevelItems().length - 1
                || newPoint.y > this.levelItemsStorage.getLevelItems()[0].length - 1) {
            return true;
        }
        if (this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y].getType() == Item.WALL
                || this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y].getType() == Item.TRUCK) {
            return true;
        }
        if (this.levelItemsStorage.getLevelItems()[newPoint.x][newPoint.y].getType() == Item.GATE
                && this.itemsToWin != 0) {
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

            this.levelItemsStorage.getLevelItems()[moveToPosition.x][moveToPosition.y] = truck;

            moveToPosition = this.getItemCoordinates(positionTemp);
            applyRotation = rotationTemp;
            flippedHorizontal = flippedHorizontalTemp;
            flippedVertical = flippedVerticalTemp;
        }
        if (this.trucks.size() != 0) {
            LevelItem empty = new LevelItem("", this.images.get(Item.EMPTY), Item.EMPTY);
            empty.setPosition(this.getItemPosition(moveToPosition));
            empty.setScale(this.getScale());
            Point coordinates = this.getItemCoordinates(positionTemp);
            this.levelItemsStorage.getLevelItems()[coordinates.x][coordinates.y] = empty;
        }
    }

    private void addTruck(Point position, LevelItem eatenItem) {
        String resourceId = eatenItem.getName();
        Image item = this.resourceManager.getTruck(resourceId).getTruck();

        Truck t = new Truck(item, Item.TRUCK);
        t.setScale(this.getScale());
        t.setPosition(this.getItemPosition(position));
        t.setRotation(this.train.getRotation());
        t.setFlippedHorizontal(train.isFlippedHorizontal());
        t.setFlippedVertical(train.isFlippedVertical());
        this.levelItemsStorage.getLevelItems()[position.x][position.y] = t;
        this.trucks.add(t);
    }

    private void doCrash() {
        this.train.setImage(this.resourceManager.getImage("trainCrash"));
        this.isGameOver = true;
    }

    public Train getTrain() {
        return this.train;
    }

    public LevelItem[][] getLevelItems() {
        return this.levelItemsStorage.getLevelItems();
    }

    public void setLevelItems(LevelItem[][] levelItems) {
        this.levelItemsStorage.setLevelItems(levelItems);
        for (int i = 0; i < levelItems.length; i++) {
            for (int j = 0; j < levelItems[0].length; j++) {
                this.levelItemsStorage.getLevelItems()[i][j].setPosition(this
                        .getItemPosition(new Point(i, j)));
                this.levelItemsStorage.getLevelItems()[i][j].setScale(this.getScale());
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
        this.setLevelItems(this.getLevelItems());
    }

    @Override
    public void setMarginLeft(int marginLeft) {
        super.setMarginLeft(marginLeft);
        this.setLevelItems(this.getLevelItems());
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

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getPackageIndex() {
        return packageIndex;
    }

    public void setPackageIndex(int packageIndex) {
        this.packageIndex = packageIndex;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
