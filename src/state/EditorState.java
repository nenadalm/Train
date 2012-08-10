package state;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import app.Game;
import entity.Level;
import entity.Level.Item;

public class EditorState extends BasicGameState {

    private int stateId;
    // game images
    private Image train;
    private Image gate;
    private Image tree;
    private Image wall;
    // menu images
    private Image itemMenu;
    private Image menuItems[] = null;
    // editor images
    private Image active;
    private Item activeItem;
    // helping fields
    private int itemSize;
    private boolean showMenu = true;
    private Point fieldPosition;
    private Level level;
    // states
    private boolean wasLeftButtonDown = false;

    private Point gatePosition = null;
    private Point trainPosition = null;

    public EditorState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        String path = Game.CONTENT_PATH + "graphics/";

        this.fieldPosition = new Point();

        this.train = new Image(path + "train.png");
        this.gate = new Image(path + "gate.png");
        this.tree = new Image(path + "tree.png");
        this.wall = new Image(path + "wall.png");

        this.itemMenu = new Image(path + "itemMenu.png");
        this.active = new Image(path + "active.png");
        this.activeItem = Item.WALL;
        this.itemSize = this.active.getWidth();

        this.level = new Level(container.getWidth() / this.itemSize, container.getHeight()
                / this.itemSize);
        System.out.println(this.level.getWidth() + "; " + this.level.getHeight());

        this.menuItems = new Image[4];

        this.menuItems[0] = this.train;
        this.menuItems[1] = this.gate;
        this.menuItems[2] = this.tree;
        this.menuItems[3] = this.wall;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

        this.level.render(container, game, g);

        if (this.showMenu) {
            this.itemMenu.draw(0, 0, container.getWidth(), this.itemMenu.getHeight());
            int width = this.menuItems[0].getWidth();
            for (int i = 0; i < this.menuItems.length; i++) {
                this.menuItems[i].draw(width + i * width, 0);
            }
        } else {
            this.itemMenu.draw(0, -this.itemSize, container.getWidth(), this.itemMenu.getHeight());
        }

        this.active.draw(this.fieldPosition.x, this.fieldPosition.y);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        this.fieldPosition.setLocation((mouseX / this.itemSize) * this.itemSize,
                (mouseY / this.itemSize) * this.itemSize);
        Point gridPosition = new Point(mouseX / this.itemSize, mouseY / this.itemSize);

        if (this.showMenu) {
            if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && this.wasLeftButtonDown) {
                if (mouseY < this.itemSize) {
                    int index = this.fieldPosition.x / this.itemSize - 1;
                    if (index >= 0 && index < this.menuItems.length) {
                        this.showMenu = false;
                        Image image = this.menuItems[index];
                        if (image == this.train) {
                            this.activeItem = Item.TRAIN;
                        } else if (image == this.wall) {
                            this.activeItem = Item.WALL;
                        } else if (image == this.gate) {
                            this.activeItem = Item.GATE;
                        } else if (image == this.tree) {
                            this.activeItem = Item.TREE;
                        }
                    }
                }
            }
        } else {
            if (mouseY < 10) {
                this.showMenu = !this.showMenu;
            }
            if (input.isKeyPressed(Keyboard.KEY_E)) {
                this.showMenu = !this.showMenu;
            }
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
                    && mouseY < this.itemSize * this.level.getHeight()
                    && mouseX < this.itemSize * this.level.getWidth()) {
                // remove position
                Item item = this.level.toArray()[gridPosition.x][gridPosition.y];
                switch (item) {
                    case TRAIN:
                        this.trainPosition = null;
                        break;
                    case GATE:
                        this.gatePosition = null;
                    default:
                }
                // load position
                switch (this.activeItem) {
                    case TRAIN:
                        if (this.trainPosition != null) {
                            this.level.setItem(this.trainPosition, Item.EMPTY);
                        }
                        this.trainPosition = gridPosition;
                        break;
                    case GATE:
                        if (this.gatePosition != null) {
                            this.level.setItem(this.gatePosition, Item.EMPTY);
                        }
                        this.gatePosition = gridPosition;
                        break;
                    default:
                        break;
                }
                this.level.setItem(gridPosition, this.activeItem);
            } else if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                this.level.setItem(gridPosition, Item.EMPTY);
            }
        }
        this.wasLeftButtonDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
