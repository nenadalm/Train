package entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import app.Game;

public class Level extends Entity {

    public static final String LEVELS_PATH = Game.CONTENT_PATH + "levels/";
    private static final String GRAPHICS = Game.CONTENT_PATH + "graphics/";

    private Map<Item, Image> images;
    private Item[][] level;
    private int interval = 500;
    private float time = 0;
    private Train train;
    private boolean isGameOver = false;
    private boolean isGameWon = false;
    private List<Truck> trucks;
    private int itemsToWin = 0;
    private int imageSize;
    private Point trainDirectionPrepared = new Point();

    public Level(int width, int height) {
        this.levelInit(width, height);
        this.train = new Train();
        this.trucks = new ArrayList<Truck>();
        this.loadImages();
    }

    public Map<Item, Image> getImages() {
        return this.images;
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

    public int getWidth() {
        return this.level.length;
    }

    public void setItem(Point position, Item item) {
        this.level[position.x][position.y] = item;
    }

    public int getHeight() {
        return this.level[0].length;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        this.imageSize *= scale;
    }

    private void loadImages() {
        this.images = new HashMap<Item, Image>(5);
        try {
            this.images.put(Item.WALL, new Image(Level.GRAPHICS + "wall.png"));
            this.images.put(Item.GATE, new Image(Level.GRAPHICS + "gate.png"));
            this.images.put(Item.TREE, new Image(Level.GRAPHICS + "tree.png"));
            this.images.put(Item.TRAIN, new Image(Level.GRAPHICS + "train.png"));
            this.images.put(Item.EMPTY, new Image(Level.GRAPHICS + "empty.png"));
            this.images.put(Item.TRUCK, new Image(Level.GRAPHICS + "treeTruck.png"));
            this.imageSize = this.images.get(Item.WALL).getWidth();
        } catch (SlickException e) {
            e.printStackTrace();
        }
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
        Image image = null;
        for (int i = 0; i < this.level.length; i++) {
            for (int j = 0; j < this.level[0].length; j++) {
                image = this.images.get(this.level[i][j]);
                if (this.level[i][j] == Item.TRAIN) {
                    switch ((int) this.train.getRotation()) {
                        case 180:
                            image = image.getFlippedCopy(false, true);
                            break;
                    }
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
                    switch ((int) truck.getRotation()) {
                        case 180:
                            image = image.getFlippedCopy(false, true);
                            break;
                    }
                    int origin = this.imageSize / 2;
                    image.setCenterOfRotation(origin, origin);
                    image.setRotation(truck.getRotation());
                }
                image.draw(i * this.imageSize, j * this.imageSize, this.getScale());
            }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        Input input = gc.getInput();

        if (!this.isGameOver && !this.isGameWon) {
            if (input.isKeyDown(Keyboard.KEY_UP)) {
                this.trainDirectionPrepared = new Point(0, -1);
            } else if (input.isKeyDown(Keyboard.KEY_DOWN)) {
                this.trainDirectionPrepared = new Point(0, 1);
            } else if (input.isKeyDown(Keyboard.KEY_LEFT)) {
                this.trainDirectionPrepared = new Point(-1, 0);
            } else if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
                this.trainDirectionPrepared = new Point(1, 0);
            }

            this.time += delta;
            if (this.time >= this.interval) {
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
                        if (this.itemsToWin == 0) {
                            try {
                                this.images.put(Item.GATE, new Image(Level.GRAPHICS
                                        + "gateOpen.png"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (this.itemsToWin == 0 && this.level[newPoint.x][newPoint.y] == Item.GATE) {
                            this.isGameWon = true;
                        }
                        this.moveTrucks(lastPoint, this.train.getRotation());

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

    private void moveTrucks(Point moveToPosition, float applyRotation) {
        Point positionTemp = null;
        Truck truck = null;
        float rotationTemp;
        for (int i = this.trucks.size() - 1; i >= 0; i--) {
            truck = this.trucks.get(i);
            positionTemp = truck.getPosition();
            rotationTemp = truck.getRotation();
            truck.setPosition(moveToPosition);
            truck.setRotation(applyRotation);
            this.level[moveToPosition.x][moveToPosition.y] = Item.TRUCK;
            moveToPosition = positionTemp;
            applyRotation = rotationTemp;
        }
        if (this.trucks.size() != 0) {
            this.level[positionTemp.x][positionTemp.y] = Item.EMPTY;
        }
    }

    private void addTruck(Point position) {
        Truck t = new Truck();
        t.setPosition(position);
        t.setRotation(this.train.getRotation());
        this.level[position.x][position.y] = Item.TRUCK;
        this.trucks.add(t);
    }

    private void doCrash() {
        try {
            this.images.put(Item.TRAIN, new Image(Level.GRAPHICS + "trainCrash.png"));
            this.isGameOver = true;
        } catch (SlickException e) {
            e.printStackTrace();
        }
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

    private class Truck {
        private Point position;
        private float rotation;

        public Point getPosition() {
            return this.position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public float getRotation() {
            return this.rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }
    }
}
