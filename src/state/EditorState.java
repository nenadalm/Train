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

import other.LevelController;
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
    private Image save;
    // menu images
    private Image itemMenu;
    private Image menuItems[] = null;
    // editor images
    private Image active;
    private Item activeItem;
    // helping fields
    private int itemSize;
    private boolean showMenu = true;
    private boolean showActive = true;
    private Point fieldPosition;
    private Level level = null;
    // states
    private boolean wasLeftButtonDown = false;

    private LevelController levelController;
    float scale = 1;

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
        this.save = new Image(path + "save.png");

        this.itemMenu = new Image(path + "itemMenu.png");
        this.active = new Image(path + "active.png");
        this.activeItem = Item.WALL;
        this.itemSize = this.active.getWidth();

        this.levelController = LevelController.getInstance();

        this.menuItems = new Image[5];

        this.menuItems[0] = this.train;
        this.menuItems[1] = this.gate;
        this.menuItems[2] = this.tree;
        this.menuItems[3] = this.wall;
        this.menuItems[4] = this.save;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

        this.level.render(container, game, g);

        if (this.showMenu) {
            this.itemMenu.draw(0, 0, container.getWidth(), this.itemMenu.getHeight() * this.scale);
            int width = (int) (this.menuItems[0].getWidth() * this.scale);
            for (int i = 0; i < this.menuItems.length; i++) {
                this.menuItems[i].draw(width + i * width, 0, this.scale);
            }
        } else {
            this.itemMenu.draw(0, -this.itemSize, container.getWidth(), this.itemMenu.getHeight()
                    * this.scale);
        }
        if (this.showActive) {
            this.active.draw(this.fieldPosition.x, this.fieldPosition.y, this.active.getWidth()
                    * this.scale, this.active.getHeight() * this.scale);
        }
    }

    public boolean isCursorInLevelArea(int mouseX, int mouseY) {
        int offsetX = this.level.getMargin().x;
        int offsetY = this.level.getMargin().y;
        int levelWidth = this.level.getWidth();
        int levelHeight = this.level.getHeight();
        if (mouseX > offsetX && mouseY > offsetY && mouseX < offsetX + this.itemSize * levelWidth
                && mouseY < offsetY + this.itemSize * levelHeight) {
            return true;
        }
        return false;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {

        if (this.level == null) {
            this.loadLevel(container);
        }
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        int offsetX = this.level.getMargin().x;
        int offsetY = this.level.getMargin().y;
        int indexX;
        int indexY;
        if (this.showMenu) {
            indexX = mouseX / this.itemSize;
            indexY = mouseY / this.itemSize;
            offsetX = 0;
            offsetY = 0;
        } else {
            indexX = (mouseX - offsetX) / this.itemSize;
            indexY = (mouseY - offsetY) / this.itemSize;
        }
        int width = indexX * this.itemSize;
        int height = indexY * this.itemSize;
        this.fieldPosition.setLocation(offsetX + width, offsetY + height);
        Point gridPosition = new Point(indexX, indexY);

        if (this.showMenu) {
            this.updateMenu(game, input);
        } else {
            this.updateEditor(gridPosition, input);
        }
        this.wasLeftButtonDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
    }

    private void loadLevel(GameContainer container) {
        this.level = this.levelController.getCurrentLevel();
        this.trainPosition = this.level.findTrainPosition();
        this.gatePosition = this.level.findGatePosition();
        float scale = 1;
        float scaleWidth = container.getWidth() / ((float) this.level.getWidth() * this.itemSize);
        float scaleHeight = container.getHeight()
                / ((float) this.itemSize * this.level.getHeight());
        if (scaleWidth < 1 && scaleHeight < 1) {
            scale = (scaleWidth < scaleHeight) ? scaleWidth : scaleHeight;
        } else if (scaleWidth < 1 || scaleHeight < 1) {
            scale = (scaleWidth < 1) ? scaleWidth : scaleHeight;
        }
        this.level.setScale(scale);
        this.itemSize *= scale;
        this.scale = scale;
        int width = this.level.getWidth() * (this.itemSize);
        int height = this.level.getHeight() * (this.itemSize);
        Point margin = new Point((container.getWidth() - width) / 2,
                (container.getHeight() - height) / 2);
        this.level.setMargin(margin);
    }

    private void setItemPosition(Point gridPosition) {
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
    }

    private void updateEditor(Point gridPosition, Input input) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        if (mouseY < 10) {
            this.showMenu = !this.showMenu;
        }
        if (input.isKeyPressed(Keyboard.KEY_E)) {
            this.showMenu = !this.showMenu;
        }
        if (this.isCursorInLevelArea(mouseX, mouseY)) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
        if (this.isCursorInLevelArea(mouseX, mouseY)) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                this.setItemPosition(gridPosition);
            } else if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                this.level.setItem(gridPosition, Item.EMPTY);
            }
        }
    }

    private void updateMenu(StateBasedGame game, Input input) {
        int mouseY = input.getMouseY();
        int index = this.fieldPosition.x / this.itemSize - 1;
        if (mouseY < this.itemSize && index >= 0 && index < this.menuItems.length) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
        if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && this.wasLeftButtonDown) {
            if (mouseY < this.itemSize) {
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
                    } else if (image == this.save) {
                        this.levelController.saveCurrentLevel();
                        game.enterState(Game.MENU_STATE);
                    }
                }
            }
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
