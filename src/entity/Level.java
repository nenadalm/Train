package entity;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import app.Game;

public class Level extends Entity {

    public static final String LEVELS_PATH = Game.CONTENT_PATH + "levels/";
    private static final String GRAPHICS = Game.CONTENT_PATH + "graphics/";

    private Map<Item, Image> images;
    private Item[][] level;

    private Train train;

    public Level(int width, int height) {
        this.levelInit(width, height);
        this.train = new Train();
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

    private void loadImages() {
        this.images = new HashMap<Item, Image>(5);
        try {
            this.images.put(Item.WALL, new Image(Level.GRAPHICS + "wall.png"));
            this.images.put(Item.GATE, new Image(Level.GRAPHICS + "gate.png"));
            this.images.put(Item.TREE, new Image(Level.GRAPHICS + "tree.png"));
            this.images.put(Item.TRAIN, new Image(Level.GRAPHICS + "train.png"));
            this.images.put(Item.EMPTY, new Image(Level.GRAPHICS + "empty.png"));
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
        WALL('W'), GATE('G'), TREE('T'), TRAIN('V'), EMPTY('E');

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
                gr.drawImage(image, i * image.getWidth(), j * image.getHeight());
            }
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
    }
}
