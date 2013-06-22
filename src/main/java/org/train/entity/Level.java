package org.train.entity;

import java.awt.Point;
import java.lang.reflect.Array;
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
import org.train.other.ResourceManager;

public class Level extends Entity implements Cloneable {

    private Map<Item, Image> images;
    private Item[][] level;
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
        this.interval = refreshSpeed;
        this.resourceManager = resourceManager;
        this.levelInit(width, height);
        this.train = new Train();
        this.trucks = new ArrayList<Truck>();
        this.loadImages();
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
        return this.level.length;
    }

    public void setItem(Point position, Item item) {
        this.level[position.x][position.y] = item;
    }

    @Override
    public int getHeight() {
        return this.level[0].length;
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
    }

    @Override
    public Level clone() {
        Level level = new Level(this.getWidth(), this.getHeight(), this.interval,
                this.resourceManager);
        Item[][] items = new Item[this.getWidth()][];
        for (int i = 0; i < this.getWidth(); i++) {
            items[i] = (Item[]) Array.newInstance(this.level[i].getClass().getComponentType(),
                    this.level[i].length);
            System.arraycopy(this.level[i], 0, items[i], 0, this.level[i].length);
        }
        level.setArray(items);
        return level;
    }

    private void loadImages() {
        this.images = new HashMap<Item, Image>(6);
        this.images.put(Item.WALL, this.resourceManager.getImage("wall"));
        this.images.put(Item.GATE, this.resourceManager.getImage("gate"));
        this.images.put(Item.TREE, this.resourceManager.getImage("tree"));
        this.images.put(Item.TRAIN, this.resourceManager.getImage("train"));
        this.images.put(Item.EMPTY, this.resourceManager.getImage("empty"));
        this.images.put(Item.TRUCK, this.resourceManager.getImage("treeTruck"));
        this.imageSize = this.images.get(Item.WALL).getWidth();
        this.originalImageSize = this.imageSize;
    }

    private void levelInit(int width, int height) {
        this.level = new Item[width][height];
        for (int i = 0; i < this.level.length; i++) {
            for (int j = 0; j < this.level[0].length; j++) {
                this.level[i][j] = Item.EMPTY;
            }
        }
    }

    public enum Item {
        WALL('W'), GATE('G'), TREE('T'), TRAIN('V'), EMPTY('E'), TRUCK('R');

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
        gr.drawRect(this.getMarginLeft(), this.getMarginTop(), this.level.length * this.imageSize
                - gr.getLineWidth(), this.level[0].length * this.imageSize - gr.getLineWidth());
        Image image = null;
        for (int i = 0; i < this.level.length; i++) {
            for (int j = 0; j < this.level[0].length; j++) {
                image = this.images.get(this.level[i][j]);
                if (this.level[i][j] == Item.TRAIN) {
                    image = image.getFlippedCopy(this.train.isFlippedHorizontal(),
                            this.train.isFlippedVertical());
                    int origin = this.imageSize / 2;
                    image.setCenterOfRotation(origin, origin);
                    image.setRotation(this.train.getRotation());
                } else if (this.level[i][j] == Item.TRUCK) {
                    Truck truck = null;
                    for (Truck t : this.trucks) {
                        if (t.getPosition().x == i && t.getPosition().y == j) {
                            truck = t;
                        }
                    }
                    image = image.getFlippedCopy(truck.isFlippedHorizontal(),
                            truck.isFlippedVertical());
                    int origin = this.imageSize / 2;
                    image.setCenterOfRotation(origin, origin);
                    image.setRotation(truck.getRotation());
                }
                image.draw(this.getMarginLeft() + i * this.imageSize, this.getMarginTop() + j
                        * this.imageSize, this.getScale());
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
                this.images.put(Item.GATE, this.resourceManager.getImage("gateOpen"));
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
                Point lastPoint = (Point) this.train.getPosition().clone();
                this.train.update(gc, sb, delta);
                Point newPoint = this.train.getPosition();

                if (this.trainCrashed()) {
                    this.doCrash();
                } else {
                    if (this.level[newPoint.x][newPoint.y] == Item.TREE) {
                        this.addTruck(lastPoint);
                        this.itemsToWin--;
                    } else {
                        if (this.itemsToWin == 0 && this.level[newPoint.x][newPoint.y] == Item.GATE) {
                            this.isGameWon = true;
                        }
                        this.moveTrucks(lastPoint, this.train.getRotation(),
                                this.train.flippedHorizontal, this.train.flippedVertical);

                        if (this.trucks.size() == 0) {
                            this.level[lastPoint.x][lastPoint.y] = Item.EMPTY;
                        }
                    }
                    this.train.setPosition(newPoint);
                    this.level[newPoint.x][newPoint.y] = Item.TRAIN;
                }

                this.time = 0;
            }
        }
    }

    private boolean trainCrashed() {
        Point newPoint = this.train.getPosition();
        if (newPoint.x < 0 || newPoint.y < 0 || newPoint.x > this.level.length - 1
                || newPoint.y > this.level[0].length - 1) {
            return true;
        }
        if (this.level[newPoint.x][newPoint.y] == Item.WALL
                || this.level[newPoint.x][newPoint.y] == Item.TRUCK) {
            return true;
        }
        if (this.level[newPoint.x][newPoint.y] == Item.GATE && this.itemsToWin != 0) {
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

            truck.setPosition(moveToPosition);
            truck.setRotation(applyRotation);
            truck.setFlippedHorizontal(flippedHorizontal);
            truck.setFlippedVertical(flippedVertical);

            this.level[moveToPosition.x][moveToPosition.y] = Item.TRUCK;

            moveToPosition = positionTemp;
            applyRotation = rotationTemp;
            flippedHorizontal = flippedHorizontalTemp;
            flippedVertical = flippedVerticalTemp;
        }
        if (this.trucks.size() != 0) {
            this.level[positionTemp.x][positionTemp.y] = Item.EMPTY;
        }
    }

    private void addTruck(Point position) {
        Truck t = new Truck();
        t.setPosition(position);
        t.setRotation(this.train.getRotation());
        t.setFlippedHorizontal(train.isFlippedHorizontal());
        t.setFlippedVertical(train.isFlippedVertical());
        this.level[position.x][position.y] = Item.TRUCK;
        this.trucks.add(t);
    }

    private void doCrash() {
        this.images.put(Item.TRAIN, this.resourceManager.getImage("trainCrash"));
        this.isGameOver = true;
    }

    public Train getTrain() {
        return this.train;
    }

    public Item[][] toArray() {
        return this.level;
    }

    public void setArray(Item[][] array) {
        this.level = array;
        try {
            this.train.setPosition(this.findTrainPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.itemsToWin = this.countItemsToWin();
        this.playable = this.isValid();
    }

    private int countItemsToWin() {
        int counter = 0;
        for (int i = 0; i < this.level.length; i++) {
            for (int j = 0; j < this.level[0].length; j++) {
                if (this.level[i][j] == Item.TREE) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public boolean isValid() {
        if (this.findGatePosition() != null && this.findTrainPosition() != null) {
            return true;
        }
        return false;
    }

    public Point findTrainPosition() {
        return this.findItemPosition(Item.TRAIN);
    }

    public Point findGatePosition() {
        return this.findItemPosition(Item.GATE);
    }

    private Point findItemPosition(Item item) {
        for (int i = 0; i < this.level.length; i++) {
            for (int j = 0; j < this.level[0].length; j++) {
                if (this.level[i][j] == item) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }
}
